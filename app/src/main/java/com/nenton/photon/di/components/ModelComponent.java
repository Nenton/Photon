package com.nenton.photon.di.components;


import com.nenton.photon.di.modules.ModelModule;
import com.nenton.photon.mvp.models.AbstractModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel abstractModel);
}
