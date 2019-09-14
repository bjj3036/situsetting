package kr.hs.dgsw.situsetting.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import kr.hs.dgsw.situsetting.services.ApplyService;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingBean;
import kr.hs.dgsw.situsetting.SettingDBHelper;
import kr.hs.dgsw.situsetting.SettingSituation;
import kr.hs.dgsw.situsetting.SettingUtil;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Situsetting";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;

    private SettingDBHelper dbHelper;
    private SharedPreferences sp;
    private SettingBean setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        if (!sp.contains(getString(R.string.notification_state))) {
            firstBoot();
        }else if (sp.getBoolean(getString(R.string.notification_state), false))
            createNotification();

        dbHelper = new SettingDBHelper(this, "settingDB", null, 1);

        requestPermissions();

    }

    private void firstBoot() {
        confirmNotificationable();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean settingsCanWrite = Settings.System.canWrite(this);
            if (!settingsCanWrite) {
                Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                i.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(i, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }

            NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (!n.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, 2);
            }
        }

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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channelId), "Situsetting", importance);
            channel.setDescription("test notification channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onSettingClick(View v) {
        switch (v.getId()) {
            case R.id.setting_home:
                confirmSave(SettingSituation.HOME, getString(R.string.string_home));
                break;
            case R.id.setting_outdoor:
                confirmSave(SettingSituation.OUTDOOR, getString(R.string.string_outdoor));
                break;
            case R.id.setting_night:
                confirmSave(SettingSituation.NIGHT, getString(R.string.string_night));
                break;
            case R.id.setting_alpha:
                confirmSave(SettingSituation.ALPHA, getString(R.string.string_alpha));
                break;
        }
    }

    public void onApplyClick(View v) {
        SettingBean setting = null;
        switch (v.getId()) {
            case R.id.layout_home:
                setting = dbHelper.select(SettingSituation.HOME.getId());
                break;
            case R.id.layout_outdoor:
                setting = dbHelper.select(SettingSituation.OUTDOOR.getId());
                break;
            case R.id.layout_night:
                setting = dbHelper.select(SettingSituation.NIGHT.getId());
                break;
            case R.id.layout_alpha:
                setting = dbHelper.select(SettingSituation.ALPHA.getId());
                break;
        }
        if (setting != null)
            SettingUtil.applySetting(this, setting);
        else
            Toast.makeText(this, "저장된 정보가 없습니다", Toast.LENGTH_SHORT).show();
    }

    private void confirmSave(SettingSituation situation, String type) {
        SettingBean setting;
        try {
            setting = SettingUtil.getSetting(this);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle("설정 저장")
                    .setMessage("현재의 설정을 " + type + "(으)로 저장하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dbHelper.setting(situation, setting);
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .show();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void confirmNotificationable() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        SharedPreferences.Editor editor = sp.edit();
        alertDialogBuilder
                .setTitle("알림창")
                .setMessage("알림창을 활성화 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                editor.putBoolean(getString(R.string.notification_state), true);
                                editor.apply();
                                createNotification();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                editor.putBoolean(getString(R.string.notification_state), false);
                                editor.apply();
                                dialog.cancel();
                            }
                        })
                .show();
    }

    public void onHelpClick(View v) {
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }
}
