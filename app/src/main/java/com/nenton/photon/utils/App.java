package com.nenton.photon.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.components.AppComponent;
import com.nenton.photon.di.components.DaggerAppComponent;
import com.nenton.photon.di.modules.AppModule;
import com.nenton.photon.di.modules.PicassoCacheModule;
import com.nenton.photon.di.modules.RootModule;
import com.nenton.photon.mortar.ScreenScoper;
import com.nenton.photon.ui.activities.DaggerRootActivity_RootComponent;
import com.nenton.photon.ui.activities.RootActivity;

import io.realm.Realm;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class App extends Application {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static AppComponent sAppComponent;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
    private MortarScope mMortarScope;
    private MortarScope mRootActivityScope;
    private static RootActivity.RootComponent mRootActivityRootComponent;

    @Override
    public Object getSystemService(String name) {
        // т. к. выполняем инструментальный тест выполняем так иначе не найдет mortarScope
        if (mMortarScope != null) {
            return mMortarScope.hasService(name) ? mMortarScope.getService(name) : super.getSystemService(name);
        } else {
            return super.getSystemService(name);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        sContext = getApplicationContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        createAppComponent();
        createRootActivityComponent();

        mMortarScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, sAppComponent)
                .build("Root");

        mRootActivityScope = mMortarScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, mRootActivityRootComponent)
                .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                .build(RootActivity.class.getName());

        ScreenScoper.registerScope(mMortarScope);
        ScreenScoper.registerScope(mRootActivityScope);

    }

    private void createRootActivityComponent() {
        mRootActivityRootComponent = DaggerRootActivity_RootComponent.builder()
                .appComponent(sAppComponent)
                .rootModule(new RootModule())
                .picassoCacheModule(new PicassoCacheModule())
                .build();
    }

    private void createAppComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    public static Context getContext() {
        return sContext;
    }

    public static RootActivity.RootComponent getRootActivityRootComponent() {
        return mRootActivityRootComponent;
    }
}
