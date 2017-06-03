package com.nenton.photon.di.components;

import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.di.modules.LocalModule;
import com.nenton.photon.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {LocalModule.class, NetworkModule.class})
@Singleton
public interface DataManagerComponent {
    void inject(DataManager dataManager);
}
