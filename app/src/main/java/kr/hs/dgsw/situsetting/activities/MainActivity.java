package kr.hs.dgsw.situsetting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import kr.hs.dgsw.situsetting.InitAppcliation;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.di.components.ActivityComponent;
import kr.hs.dgsw.situsetting.di.components.DaggerActivityComponent;
import kr.hs.dgsw.situsetting.di.modules.ActivityModule;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.room.entity.SettingBean;
import kr.hs.dgsw.situsetting.SettingSituation;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Situsetting";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;

    @Inject
    SettingRepository settingRepository;
    @Inject
    SharedPreferences sp;
    @Inject
    SettingUtil settingUtil;
    @Inject
    NotificationUtil notificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityComponent component = DaggerActivityComponent.builder()
                .appComponent(InitAppcliation.get(getApplicationContext()).getComponent())
                .activityModule(new ActivityModule(this))
                .build();

        component.inject(this);

        if (!sp.contains(getString(R.string.notification_state)))
            firstBoot();
        else if (sp.getBoolean(getString(R.string.notification_state), false))
            notificationUtil.createNotification();

        requestPermissions();

        settingRepository.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingRepository.onDestory();
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
        SettingBean setting;
        Consumer<SettingBean> observer = settingBean -> settingUtil.applySetting(settingBean);
        switch (v.getId()) {
            case R.id.layout_home:
                settingRepository.selectSetting(SettingSituation.HOME.getId(), observer);
                break;
            case R.id.layout_outdoor:
                settingRepository.selectSetting(SettingSituation.OUTDOOR.getId(), observer);
                break;
            case R.id.layout_night:
                settingRepository.selectSetting(SettingSituation.NIGHT.getId(), observer);
                break;
            case R.id.layout_alpha:
                settingRepository.selectSetting(SettingSituation.ALPHA.getId(), observer);
                break;
            default:
                setting = null;
        }
        Toast.makeText(this, "저장된 정보가 없습니다", Toast.LENGTH_SHORT).show();
    }

    private void confirmSave(SettingSituation situation, String type) {
        SettingBean setting;
        try {
            setting = settingUtil.getSetting(situation);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle("설정 저장")
                    .setMessage("현재의 설정을 " + type + "(으)로 저장하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", (dialog, id) -> {
                        settingRepository.insertSetting(setting);
                        dialog.cancel();
                    })
                    .setNegativeButton("취소", (dialog, id) -> {
                        dialog.cancel();
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
                .setPositiveButton("네", (dialog, id) -> {
                    editor.putBoolean(getString(R.string.notification_state), true);
                    editor.apply();
                    notificationUtil.createNotification();
                    dialog.cancel();
                })
                .setNegativeButton("아니요", (dialog, id) -> {
                    editor.putBoolean(getString(R.string.notification_state), false);
                    editor.apply();
                    dialog.cancel();
                })
                .show();
    }

    public void onHelpClick(View v) {
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }
}
