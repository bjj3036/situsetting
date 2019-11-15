package kr.hs.dgsw.situsetting.utils;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.Nullable;

import kr.hs.dgsw.situsetting.SettingBean;

public class SettingUtil {
    private Context context;

    public SettingUtil(Context context) {
        this.context = context;
    }

    public void applySetting(SettingBean setting) {
        if (setting == null)
            return;
        ContentResolver cResolver = context.getContentResolver();
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        NotificationManager n = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean settingsCanWrite = Settings.System.canWrite(context);
            if (settingsCanWrite && n.isNotificationPolicyAccessGranted()) {

                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, setting.getBrightness());

                am.setStreamVolume(AudioManager.STREAM_MUSIC, setting.getMusicVolume(), 0);
                am.setStreamVolume(AudioManager.STREAM_RING, setting.getRingVolume(), 0);
                am.setRingerMode(setting.getRingerMode());
            }
        }
    }

    @Nullable
    public SettingBean getSetting() throws Settings.SettingNotFoundException {
        ContentResolver cResolver = context.getContentResolver();
        SettingBean setting = new SettingBean();
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        NotificationManager n = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean settingsCanWrite = Settings.System.canWrite(context);
            if (settingsCanWrite && n.isNotificationPolicyAccessGranted()) {

                setting.setBrightness(Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS));
                setting.setMusicVolume(am.getStreamVolume(AudioManager.STREAM_MUSIC));
                setting.setRingVolume(am.getStreamVolume(AudioManager.STREAM_RING));
                setting.setRingerMode(am.getRingerMode());
            }
        }

        return setting;
    }
}
