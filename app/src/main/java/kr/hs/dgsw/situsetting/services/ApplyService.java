package kr.hs.dgsw.situsetting.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import javax.inject.Inject;

import kr.hs.dgsw.situsetting.InitAppcliation;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.di.components.ActivityComponent;
import kr.hs.dgsw.situsetting.di.components.DaggerActivityComponent;
import kr.hs.dgsw.situsetting.di.modules.ActivityModule;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

import static kr.hs.dgsw.situsetting.activities.MainActivity.TAG;

public class ApplyService extends Service {

    public static final String EXTRA_SITUATION_ID = "situationId";

    @Inject
    SettingRepository settingRepository;
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

        settingRepository.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settingRepository.onDestory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Alpha");
        settingRepository.selectSetting(intent.getLongExtra(EXTRA_SITUATION_ID, -1),
                settingBean -> settingUtil.applySetting(settingBean));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
