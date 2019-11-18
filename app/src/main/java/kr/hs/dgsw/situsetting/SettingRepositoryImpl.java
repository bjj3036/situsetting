package kr.hs.dgsw.situsetting;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kr.hs.dgsw.situsetting.SettingRepository;
import kr.hs.dgsw.situsetting.SettingSituation;
import kr.hs.dgsw.situsetting.room.dao.SettingDAO;
import kr.hs.dgsw.situsetting.room.database.SitusettingDatabase;
import kr.hs.dgsw.situsetting.room.entity.SettingBean;

public class SettingRepositoryImpl implements SettingRepository {

    SettingDAO settingDAO;
    List<Disposable> disposables;

    @Inject
    public SettingRepositoryImpl(SettingDAO settingDAO) {
        this.settingDAO = settingDAO;
    }

    @Override
    public void insertSetting(SettingBean settingBean) {
        disposables.add(
                Observable.just(settingDAO)
                        .observeOn(Schedulers.io())
                        .subscribe(dao -> {
                            dao.insertSetting(settingBean);
                        })
        );
    }

    @Override
    public void selectSetting(long settingSituation, Consumer<SettingBean> observer) {
        disposables.add(
                Observable.just(settingDAO)
                        .observeOn(Schedulers.io())
                        .map(dao ->dao.selectSettingById(settingSituation))
                        .subscribe(observer)
        );
    }

    @Override
    public void onCreate() {
        disposables = new ArrayList<>();
    }

    @Override
    public void onDestory() {
        if (disposables != null)
            for (Disposable d : disposables)
                d.dispose();
    }
}
