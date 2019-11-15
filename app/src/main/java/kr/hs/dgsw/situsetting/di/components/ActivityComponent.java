package kr.hs.dgsw.situsetting.di.components;


import android.content.BroadcastReceiver;

import dagger.Component;
import kr.hs.dgsw.situsetting.activities.HelpActivity;
import kr.hs.dgsw.situsetting.activities.MainActivity;
import kr.hs.dgsw.situsetting.di.PerActivity;
import kr.hs.dgsw.situsetting.di.modules.ActivityModule;
import kr.hs.dgsw.situsetting.di.modules.AppModule;
import kr.hs.dgsw.situsetting.receivers.BootReceiver;
import kr.hs.dgsw.situsetting.services.ApplyService;

@PerActivity
@Component(modules = {ActivityModule.class}
        , dependencies = {AppComponent.class})
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(HelpActivity helpActivity);

    void inject(ApplyService applyService);

    @Component.Builder
    interface Builder {
        ActivityComponent build();

        Builder activityModule(ActivityModule activityModule);

        Builder appComponent(AppComponent appComponent);
    }
}
