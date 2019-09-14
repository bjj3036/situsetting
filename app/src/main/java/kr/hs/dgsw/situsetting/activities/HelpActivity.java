package kr.hs.dgsw.situsetting.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Switch;

import kr.hs.dgsw.situsetting.services.ApplyService;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingSituation;

public class HelpActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch mSwitch;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        sp = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);

        mSwitch = findViewById(R.id.switch1);
        mSwitch.setChecked(sp.getBoolean(getString(R.string.notification_state), false));
        mSwitch.setOnCheckedChangeListener(this);
    }

    public void onInfoClick(View v) {
        startActivity(new Intent(this, InfoActivity.class));
    }

    public void onManualClick(View v) {
        startActivity(new Intent(this, ManualActivity.class));
    }

    public void onRequestPermission(View v) {
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            i.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(i, 1);

            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, 2);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.notification_state), b);
        editor.apply();
        if (b)
            createNotification();
        else
            removeNotification();
    }


    private void createNotification() {
        Intent intentHome = new Intent(this, ApplyService.class);
        intentHome.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.HOME.getId());
        Intent intentOutdoor = new Intent(this, ApplyService.class);
        intentOutdoor.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.OUTDOOR.getId());
        Intent intentNight = new Intent(this, ApplyService.class);
        intentNight.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.NIGHT.getId());
        Intent intentAlpha = new Intent(this, ApplyService.class);
        intentAlpha.putExtra(ApplyService.EXTRA_SITUATION_ID, SettingSituation.ALPHA.getId());
        Intent intentMain = new Intent(this, MainActivity.class);

        PendingIntent pendingIntentHome = PendingIntent.getService(this, 1, intentHome, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentOutdoor = PendingIntent.getService(this, 2, intentOutdoor, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentNight = PendingIntent.getService(this, 3, intentNight, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentAlpha = PendingIntent.getService(this, 4, intentAlpha, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentMain = PendingIntent.getActivity(this, 5, intentMain, PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        notificationLayout.setOnClickPendingIntent(R.id.noti_home, pendingIntentHome);
        notificationLayout.setOnClickPendingIntent(R.id.noti_outdoor, pendingIntentOutdoor);
        notificationLayout.setOnClickPendingIntent(R.id.noti_night, pendingIntentNight);
        notificationLayout.setOnClickPendingIntent(R.id.noti_alpha, pendingIntentAlpha);
        notificationLayout.setOnClickPendingIntent(R.id.noti_setting, pendingIntentMain);

        Notification customNotification = new NotificationCompat.Builder(this, getString(R.string.notification_channelId))
                .setSmallIcon(R.drawable.situsetting_noti_icon)
                .setShowWhen(false)
                .setCustomContentView(notificationLayout)
                .setOnlyAlertOnce(true)
                .build();

        customNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        createNotificationChannel();
        NotificationManagerCompat.from(this).notify(1, customNotification);
    }

    private void removeNotification() {
        NotificationManagerCompat.from(this).cancel(1);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channelId), "Situsetting", importance);
            channel.setDescription("test notification channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
