package com.nenton.photon.di.modules;

import android.content.Context;
import android.util.Log;


import com.nenton.photon.data.managers.PreferencesManager;
import com.nenton.photon.data.managers.RealmManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule{

    @Provides
    @Singleton
    PreferencesManager provideAppPrefManager(Context context){
        return new PreferencesManager(context);
    }

    @Provides
    @Singleton
    RealmManager provideRealmManager(Context context){
        return new RealmManager();
    }
}