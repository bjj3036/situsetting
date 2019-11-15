package kr.hs.dgsw.situsetting;

public interface SettingRepository {
    void insertSetting(SettingSituation settingSituation, SettingBean settingBean);
    SettingBean selectSetting(SettingSituation settingSituation);
    SettingBean selectSetting(String settingSituation);
}
