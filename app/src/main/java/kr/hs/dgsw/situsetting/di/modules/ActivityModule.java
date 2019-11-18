package kr.hs.dgsw.situsetting.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.PrimaryKey;
import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.SettingRepositoryImpl;
import kr.hs.dgsw.situsetting.di.PerActivity;
import kr.hs.dgsw.situsetting.room.dao.SettingDAO;
import kr.hs.dgsw.situsetting.room.database.SitusettingDatabase;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

@Module
public class ActivityModule {

    private Context mContext;

    public ActivityModule(Context context) {
        mContext = context;
    }

    @PerActivity
    @Provides
    SettingRepository provideSettingRepository(SettingDAO settingDAO) {
        return new SettingRepositoryImpl(settingDAO);
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


    @PerActivity
    @Provides
    SitusettingDatabase provideSitusettingDatabase(){
        return Room.databaseBuilder(mContext, SitusettingDatabase.class, "SitusettingDB")
                .build();
    }

    @PerActivity
    @Provides
    SettingDAO provideSettingDAO(SitusettingDatabase situsettingDatabase){
        return situsettingDatabase.settingDAO();
    }
}
