package kr.hs.dgsw.situsetting.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingSituation;
import kr.hs.dgsw.situsetting.activities.MainActivity;
import kr.hs.dgsw.situsetting.services.ApplyService;

import static kr.hs.dgsw.situsetting.activities.MainActivity.TAG;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onBootCompleted");
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.preference_name), Context.MODE_PRIVATE);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (sp.getBoolean(context.getString(R.string.notification_state), false))
                createNotification(context);
        }
    }

    private void createNotification(Context context) {
        Intent intentHome = new Intent(context, ApplyService.class);
        intentHome.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.HOME.getId());
        Intent intentOutdoor = new Intent(context, ApplyService.class);
        intentOutdoor.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.OUTDOOR.getId());
        Intent intentNight = new Intent(context, ApplyService.class);
        intentNight.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.NIGHT.getId());
        Intent intentAlpha = new Intent(context, ApplyService.class);
        intentAlpha.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.ALPHA.getId());
        Intent intentMain = new Intent(context, MainActivity.class);

        PendingIntent pendingIntentHome = PendingIntent.getService(context, 1, intentHome, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentOutdoor = PendingIntent.getService(context, 2, intentOutdoor, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentNight = PendingIntent.getService(context, 3, intentNight, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentAlpha = PendingIntent.getService(context, 4, intentAlpha, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentMain = PendingIntent.getActivity(context, 5, intentMain, PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
        notificationLayout.setOnClickPendingIntent(R.id.noti_home, pendingIntentHome);
        notificationLayout.setOnClickPendingIntent(R.id.noti_outdoor, pendingIntentOutdoor);
        notificationLayout.setOnClickPendingIntent(R.id.noti_night, pendingIntentNight);
        notificationLayout.setOnClickPendingIntent(R.id.noti_alpha, pendingIntentAlpha);
        notificationLayout.setOnClickPendingIntent(R.id.noti_setting, pendingIntentMain);

        Notification customNotification = new NotificationCompat.Builder(context, context.getString(R.string.notification_channelId))
                .setSmallIcon(R.drawable.situsetting_noti_icon)
                .setShowWhen(false)
                .setCustomContentView(notificationLayout)
                .setOnlyAlertOnce(true)
                .build();

        customNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        createNotificationChannel(context);
        NotificationManagerCompat.from(context).notify(1, customNotification);
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
}
