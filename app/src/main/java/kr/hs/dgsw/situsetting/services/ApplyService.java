package kr.hs.dgsw.situsetting.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import kr.hs.dgsw.situsetting.SettingDBHelper;
import kr.hs.dgsw.situsetting.SettingUtil;

import static kr.hs.dgsw.situsetting.activities.MainActivity.TAG;

public class ApplyService extends Service {

    public static final String EXTRA_SITUATION_ID = "situationId";

    private SettingDBHelper dbHelper;

    public ApplyService() {
        dbHelper = new SettingDBHelper(this, "settingDB", null, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Alpha");
        SettingUtil.applySetting(this, dbHelper.select(intent.getStringExtra(EXTRA_SITUATION_ID)));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
