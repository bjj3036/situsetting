package kr.hs.dgsw.situsetting;

import android.app.Application;
import android.content.Context;

import kr.hs.dgsw.situsetting.di.components.AppComponent;
import kr.hs.dgsw.situsetting.di.components.DaggerAppComponent;
import kr.hs.dgsw.situsetting.di.modules.AppModule;

public class InitAppcliation extends Application {

    private AppComponent appComponent;

    public static InitAppcliation get(Context context) {
        return (InitAppcliation) context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public AppComponent getComponent() {
        return appComponent;
    }
}
