package org.byters.bcphotoanimations.view.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.BuildConfig;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.view.ui.service.ServiceProjectExport;

import java.io.File;

import javax.inject.Inject;

public class NotificationExportComplete {


    private final String filepath;
    private final String projectId;

    @Inject
    ICacheProjects cacheProjects;

    public NotificationExportComplete(String filepath, String projectId) {
        ApplicationStopMotion.getComponent().inject(this);
        this.filepath = filepath;
        this.projectId = projectId;
    }

    private static Notification getNotification(Context context, String title, String filepath) {

        File videoFile = new File(filepath);
        Uri fileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", videoFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(context, ServiceProjectExport.SERVICE_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(String.format(context.getString(R.string.notification_video_open_title_format), title))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setContentIntent(pi)
                .build();
    }

    public void show(Context context) {
        String title = getProjectTitle();
        Notification n = getNotification(context, title, filepath);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ServiceProjectExport.SERVICE_NOTIFICATION_ID_OPEN_VIDEO, n);

    }

    private String getProjectTitle() {
        return cacheProjects.getItemTitleById(projectId);
    }
}
