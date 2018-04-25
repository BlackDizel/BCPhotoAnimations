package org.byters.bcphotoanimations.view.ui.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;
import org.byters.bcphotoanimations.view.ui.activity.ActivityMain;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExport;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExportListener;

import javax.inject.Inject;

public class ServiceProjectExport extends Service {
    public static final int PROGRESS_MAX = 100;
    private static final int SERVICE_NOTIFICATION_ID = 1432;
    private static final String SERVICE_NOTIFICATION_CHANNEL_ID = "service_notification";

    private static final String EXTRA_PROJECT_ID = "project_id";
    @Inject
    ICacheProjects cacheProjects;
    @Inject
    ICacheStorage cacheStorage;
    private AsyncTaskProjectExport worker;
    private NotificationCompat.Builder notificationBuilder;
    private ListenerExportTask listenerExportTask;

    public static void start(Context context, String projectId) {
        Intent intent = new Intent(context, ServiceProjectExport.class);

        intent.putExtra(EXTRA_PROJECT_ID, projectId);
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

        String projectId = intent.getExtras().getString(EXTRA_PROJECT_ID);
        String title = cacheProjects.getItemTitleById(projectId);

        initNotificationBuilder(title);

        startForeground(SERVICE_NOTIFICATION_ID, notificationBuilder.build());

        export(projectId);

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

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void export(String projectId) {
        if (worker != null) worker.cancel(true);
        worker = new AsyncTaskProjectExport(projectId,
                cacheProjects,
                cacheStorage,
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
        public void onUpdate(Integer[] values) {
            if (values == null
                    || values.length != 2) return;

            notificationBuilder.setProgress(values[0], values[1], false);
            updateNotification();
        }

        @Override
        public void onComplete() {
            Toast.makeText(ServiceProjectExport.this, R.string.export_project_success, Toast.LENGTH_SHORT).show();
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
