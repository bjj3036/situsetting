package kr.hs.dgsw.situsetting.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import javax.inject.Inject;

import kr.hs.dgsw.situsetting.InitAppcliation;
import kr.hs.dgsw.situsetting.SettingDBHelper;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.di.components.ActivityComponent;
import kr.hs.dgsw.situsetting.di.components.DaggerActivityComponent;
import kr.hs.dgsw.situsetting.di.modules.ActivityModule;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

import static kr.hs.dgsw.situsetting.activities.MainActivity.TAG;

public class ApplyService extends Service {

    public static final String EXTRA_SITUATION_ID = "situationId";

    @Inject
    SettingRepository dbHelper;
    @Inject
    SettingUtil settingUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityComponent component = DaggerActivityComponent.builder()
                .appComponent(InitAppcliation.get(getApplicationContext()).getComponent())
                .activityModule(new ActivityModule(this))
                .build();

        component.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Alpha");
        settingUtil.applySetting(dbHelper.selectSetting(intent.getStringExtra(EXTRA_SITUATION_ID)));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
