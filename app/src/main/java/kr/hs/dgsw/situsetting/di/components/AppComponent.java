package kr.hs.dgsw.situsetting.di.components;

import javax.inject.Singleton;

import dagger.Component;
import kr.hs.dgsw.situsetting.InitAppcliation;
import kr.hs.dgsw.situsetting.di.modules.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(InitAppcliation initAppcliation);

    @Component.Builder
    interface Builder {
        AppComponent build();

        Builder appModule(AppModule appModule);
    }
}
