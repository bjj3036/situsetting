package kr.hs.dgsw.situsetting.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingSituation;
import kr.hs.dgsw.situsetting.activities.MainActivity;
import kr.hs.dgsw.situsetting.services.ApplyService;

public class NotificationUtil {
    public static final int NOTIFICATION_ID = 1;
    private Context context;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    public void createNotification() {
        createNotification(this.context);
    }

    private PendingIntent createPendingService(SettingSituation settingSituation) {
        Intent intent = new Intent(context, ApplyService.class);
        intent.putExtra(ApplyService.EXTRA_SITUATION_ID, settingSituation.getId());
        return PendingIntent.getService(context, Integer.parseInt(settingSituation.getId()), intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void createNotification(Context context) {
        Intent intentMain = new Intent(context, MainActivity.class);

        PendingIntent pendingIntentHome = createPendingService(SettingSituation.HOME);
        PendingIntent pendingIntentOutdoor = createPendingService(SettingSituation.OUTDOOR);
        PendingIntent pendingIntentNight = createPendingService(SettingSituation.NIGHT);
        PendingIntent pendingIntentAlpha = createPendingService(SettingSituation.ALPHA);
        PendingIntent pendingIntentMain = PendingIntent.getActivity(context, 5, intentMain, PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
        notificationLayout.setOnClickPendingIntent(R.id.noti_home, pendingIntentHome);
        notificationLayout.setOnClickPendingIntent(R.id.noti_outdoor, pendingIntentOutdoor);
        notificationLayout.setOnClickPendingIntent(R.id.noti_night, pendingIntentNight);
        notificationLayout.setOnClickPendingIntent(R.id.noti_alpha, pendingIntentAlpha);
        notificationLayout.setOnClickPendingIntent(R.id.noti_setting, pendingIntentMain);

        Notification customNotification = new NotificationCompat.Builder(context, context.getString(R.string.notification_channelId))
                .setSmallIcon(R.drawable.ic_situsetting_noti_icon)
                .setShowWhen(false)
                .setCustomContentView(notificationLayout)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setOngoing(true)
                .build();

        customNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT|Notification.FLAG_INSISTENT;
        createNotificationChannel(context);
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, customNotification);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.notification_channelId), "Situsetting", importance);
            channel.setDescription("test notification channel");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void removeNotification() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
    }
}
