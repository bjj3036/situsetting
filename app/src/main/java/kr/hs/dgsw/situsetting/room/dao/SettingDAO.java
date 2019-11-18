package kr.hs.dgsw.situsetting.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import kr.hs.dgsw.situsetting.room.entity.SettingBean;

@Dao
public interface SettingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSetting(SettingBean settingBean);

    @Query("select * from setting where situation = :situation")
    SettingBean selectSettingById(long situation);
}
