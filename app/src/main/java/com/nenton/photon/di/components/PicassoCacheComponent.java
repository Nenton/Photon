package com.nenton.photon.di.components;

import com.nenton.photon.di.modules.PicassoCacheModule;
import com.nenton.photon.di.sqopes.RootScope;
import com.squareup.picasso.Picasso;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PicassoCacheModule.class)
@RootScope
public interface PicassoCacheComponent {
    Picasso getPicasso();
}
