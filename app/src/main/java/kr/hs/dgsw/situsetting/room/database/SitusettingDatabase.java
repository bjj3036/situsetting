package kr.hs.dgsw.situsetting.room.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import kr.hs.dgsw.situsetting.room.dao.SettingDAO;
import kr.hs.dgsw.situsetting.room.entity.SettingBean;

@Database(entities = {SettingBean.class}, version = 1)
public abstract class SitusettingDatabase extends RoomDatabase {
    public abstract SettingDAO settingDAO();
}
