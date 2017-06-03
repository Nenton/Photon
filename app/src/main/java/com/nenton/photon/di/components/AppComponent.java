package com.nenton.photon.di.components;

import android.content.Context;


import com.nenton.photon.di.modules.AppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
