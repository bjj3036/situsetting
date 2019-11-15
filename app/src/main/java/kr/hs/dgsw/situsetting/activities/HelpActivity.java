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

import javax.inject.Inject;

import kr.hs.dgsw.situsetting.InitAppcliation;
import kr.hs.dgsw.situsetting.di.components.ActivityComponent;
import kr.hs.dgsw.situsetting.di.components.DaggerActivityComponent;
import kr.hs.dgsw.situsetting.di.modules.ActivityModule;
import kr.hs.dgsw.situsetting.services.ApplyService;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingSituation;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

public class HelpActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Inject
     SharedPreferences sp;
    @Inject
     NotificationUtil notificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ActivityComponent component = DaggerActivityComponent.builder()
                .appComponent(InitAppcliation.get(getApplicationContext()).getComponent())
                .activityModule(new ActivityModule(this))
                .build();

        component.inject(this);

        Switch mSwitch = findViewById(R.id.switch1);
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
            notificationUtil.createNotification();
        else
            notificationUtil.removeNotification();
    }

}
