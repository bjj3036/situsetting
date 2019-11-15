package kr.hs.dgsw.situsetting.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingDBHelper;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }




}
