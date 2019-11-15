package kr.hs.dgsw.situsetting.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingDBHelper;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.di.PerActivity;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

@Module
public class ActivityModule {

    private Context mContext;

    public ActivityModule(Context context){
        mContext = context;
    }

    @PerActivity
    @Provides
    SettingRepository provideSettingRepository() {
        return new SettingDBHelper(mContext, "settingDB", null, 1);
    }

    @PerActivity
    @Provides
    SharedPreferences provideSharedPreference() {
        return mContext.getSharedPreferences(mContext.getString(R.string.preference_name), Context.MODE_PRIVATE);
    }


    @PerActivity
    @Provides
    SettingUtil provideSettingUtil() {
        return new SettingUtil(mContext);
    }

    @PerActivity
    @Provides
    NotificationUtil provideNotificationUtil() {
        return new NotificationUtil(mContext);
    }
}
