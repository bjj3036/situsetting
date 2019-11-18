package kr.hs.dgsw.situsetting;


import io.reactivex.functions.Consumer;
import kr.hs.dgsw.situsetting.room.entity.SettingBean;

public interface SettingRepository {
    void insertSetting(SettingBean settingBean);
    void selectSetting(long settingSituation, Consumer<SettingBean> observer);

    void onCreate();
    void onDestory();
}
