package com.nenton.photon.di.modules;

import com.nenton.photon.di.sqopes.RootScope;
import com.nenton.photon.mvp.presenters.RootPresenter;

import dagger.Provides;

@dagger.Module
public class RootModule {
    @Provides
    @RootScope
    public RootPresenter provideRootPresenter() {
        return new RootPresenter();
    }
}
