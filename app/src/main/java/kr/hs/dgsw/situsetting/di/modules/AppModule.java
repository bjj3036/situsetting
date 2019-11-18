package kr.hs.dgsw.situsetting.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kr.hs.dgsw.situsetting.R;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.room.dao.SettingDAO;
import kr.hs.dgsw.situsetting.room.database.SitusettingDatabase;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.utils.SettingUtil;

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }



}
