package kr.hs.dgsw.situsetting.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import dagger.android.AndroidInjection;
import kr.hs.dgsw.situsetting.utils.NotificationUtil;
import kr.hs.dgsw.situsetting.R;

import static kr.hs.dgsw.situsetting.activities.MainActivity.TAG;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtil notificationUtil = new NotificationUtil(context);
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.preference_name), Context.MODE_PRIVATE);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (sp.getBoolean(context.getString(R.string.notification_state), false))
                notificationUtil.createNotification();
        }
    }
}
