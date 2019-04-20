package org.byters.bcphotoanimations.view.ui.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.widget.Toast;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheExportAttempts;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;
import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;
import org.byters.bcphotoanimations.view.ui.activity.ActivityMain;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExport;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExportListener;
import org.byters.bcphotoanimations.view.util.AsyncTaskExportMP4_JCodec;
import org.byters.bcphotoanimations.view.util.AsyncTaskExportMP4_MediaCodec;

import javax.inject.Inject;

public class ServiceProjectExport extends Service {
    public static final int PROGRESS_MAX = 100;
    private static final int SERVICE_NOTIFICATION_ID = 1432;
    private static final String SERVICE_NOTIFICATION_CHANNEL_ID = "service_notification";

    private static final String EXTRA_PROJECT_ID = "project_id";
    private static final String EXTRA_EXPORT_TYPE = "export_type";
    private static final int EXPORT_TYPE_UNKNOWN = 0;
    private static final int EXPORT_TYPE_IMAGES = 1;
    private static final int EXPORT_TYPE_MJPEG = 2;
    private static final int EXPORT_TYPE_MEDIACODEC = 3;
    private static final String EXTRA_EXPORT_FPS = "video_fps";
    private static final String EXTRA_EXPORT_FRAME_WIDTH = "video_width";
    private static final String EXTRA_EXPORT_FRAME_HEIGHT = "video_height";

    @Inject
    ICacheProjects cacheProjects;

    @Inject
    ICacheStorage cacheStorage;

    @Inject
    ICacheExportAttempts cacheExportAttempts;

    private AsyncTask<Void, Integer, Boolean> worker;
    private NotificationCompat.Builder notificationBuilder;
    private ListenerExportTask listenerExportTask;

    public static void startExportImages(Context context, String projectId) {
        Intent intent = new Intent(context, ServiceProjectExport.class);

        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        intent.putExtra(EXTRA_EXPORT_TYPE, EXPORT_TYPE_IMAGES);
        context.startService(intent);
    }

    public static void startExportMJPEG(Context context, String projectId, int w, int h, int fps) {
        Intent intent = new Intent(context, ServiceProjectExport.class);

        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        intent.putExtra(EXTRA_EXPORT_TYPE, EXPORT_TYPE_MJPEG);
        intent.putExtra(EXTRA_EXPORT_FPS, fps);
        intent.putExtra(EXTRA_EXPORT_FRAME_WIDTH, w);
        intent.putExtra(EXTRA_EXPORT_FRAME_HEIGHT, h);
        context.startService(intent);
    }

    public static void startExportMediaCodec(ActivityBase context, String projectId, int w, int h, int fps) {
        Intent intent = new Intent(context, ServiceProjectExport.class);

        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        intent.putExtra(EXTRA_EXPORT_TYPE, EXPORT_TYPE_MEDIACODEC);
        intent.putExtra(EXTRA_EXPORT_FPS, fps);
        intent.putExtra(EXTRA_EXPORT_FRAME_WIDTH, w);
        intent.putExtra(EXTRA_EXPORT_FRAME_HEIGHT, h);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationStopMotion.getComponent().inject(this);

        listenerExportTask = new ListenerExportTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String projectId = intent.getExtras() == null ? null : intent.getExtras().getString(EXTRA_PROJECT_ID);

        if (projectId == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        String title = cacheProjects.getItemTitleById(projectId);

        initNotificationBuilder(title);

        if (!cacheExportAttempts.isEnoughAttempts()) {
            stopSelf();
            return START_NOT_STICKY;
        }

        startForeground(SERVICE_NOTIFICATION_ID, notificationBuilder.build());

        int type = intent.getIntExtra(EXTRA_EXPORT_TYPE, EXPORT_TYPE_UNKNOWN);
        if (type == EXPORT_TYPE_IMAGES)
            exportImages(projectId);
        if (type == EXPORT_TYPE_MJPEG)
            exportVideo(projectId,
                    intent.getIntExtra(EXTRA_EXPORT_FPS, 0),
                    intent.getIntExtra(EXTRA_EXPORT_FRAME_WIDTH, 0),
                    intent.getIntExtra(EXTRA_EXPORT_FRAME_HEIGHT, 0));
        if (type == EXPORT_TYPE_MEDIACODEC)
            exportVideoMediaCodec(projectId,
                    intent.getIntExtra(EXTRA_EXPORT_FPS, 0),
                    intent.getIntExtra(EXTRA_EXPORT_FRAME_WIDTH, 0),
                    intent.getIntExtra(EXTRA_EXPORT_FRAME_HEIGHT, 0));

        return START_STICKY;
    }

    private void initNotificationBuilder(String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel();

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), ActivityMain.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new NotificationCompat.Builder(this, SERVICE_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(String.format(getString(R.string.notification_project_export_title_format), title))
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setContentIntent(pi)
                .setProgress(PROGRESS_MAX, 0, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(
                SERVICE_NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_project_export_channel_name),
                NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription(getString(R.string.notification_project_export_channel_description));
        notificationChannel.setSound(null, null);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void exportImages(String projectId) {
        if (worker != null) worker.cancel(true);
        worker = new AsyncTaskProjectExport(projectId,
                cacheProjects,
                cacheStorage,
                listenerExportTask);
        worker.execute();
    }


    private void exportVideo(String projectId, int fps, int w, int h) {
        if (worker != null) worker.cancel(true);

        worker = new AsyncTaskExportMP4_JCodec(projectId, fps, w, h,
                listenerExportTask);
        worker.execute();
    }


    private void exportVideoMediaCodec(String projectId, int fps, int w, int h) {
        if (worker != null) worker.cancel(true);

        worker = new AsyncTaskExportMP4_MediaCodec(projectId, fps, w, h,
                listenerExportTask);
        worker.execute();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ListenerExportTask implements AsyncTaskProjectExportListener {

        @Override
        public void onUpdate(String projectId, Integer[] values) {
            if (values == null
                    || values.length < 2) return;

            notificationBuilder.setProgress(values[0], values[1], false);
            String title = cacheProjects.getItemTitleById(projectId);
            String message = TextUtils.isEmpty(projectId)
                    ? getString(R.string.export_frame_message_error)
                    : values.length == 2
                    ? String.format(getString(R.string.notification_project_export_title_progress_format), title, values[1] + 1)
                    : values.length == 3
                    ? String.format(getString(R.string.notification_project_export_title_progress_format_with_state), title, values[1] + 1, getState(values[2]))
                    : getString(R.string.export_frame_message_error);

            notificationBuilder.setContentText(message);
            updateNotification();
        }

        private String getState(int value) {
            return getString(value == 1 ? R.string.export_frame_state_success : R.string.export_frame_state_error);
        }

        @Override
        public void onComplete(String projectId) {
            String title = cacheProjects.getItemTitleById(projectId);
            if (!TextUtils.isEmpty(title))
                Toast.makeText(ServiceProjectExport.this,
                        String.format(getString(R.string.export_project_success_success), cacheStorage.getProjectOutputFolder(title)),
                        Toast.LENGTH_LONG)
                        .show();
            cacheExportAttempts.decreaseAttempts();
            completeNotification();
            stopService();
        }

        private void updateNotification() {
            NotificationManagerCompat
                    .from(ServiceProjectExport.this)
                    .notify(SERVICE_NOTIFICATION_ID, notificationBuilder.build());
        }

        private void completeNotification() {
            notificationBuilder.setOngoing(false);
            updateNotification();
        }

        private void stopService() {
            ServiceProjectExport.this.stopForeground(false);
            ServiceProjectExport.this.stopSelf();
        }
    }
}
