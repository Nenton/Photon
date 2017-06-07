package com.nenton.photon.ui.screens.photocard;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.PhotoModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.screens.main.MainScreen;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import mortar.MortarScope;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.photocard_screen)
public class PhotocardScreen extends AbstractScreen<MainScreen.Component> {

    private PhotocardRealm mPhotocard;

    public PhotocardScreen(PhotocardRealm photocard) {
        this.mPhotocard = photocard;
    }

    @Override
    public Object createScreenComponent(MainScreen.Component parentComponent) {
        return DaggerPhotocardScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(PhotocardScreen.class)
        PhotoModel provideSearchModel(){
            return new PhotoModel();
        }

        @Provides
        @DaggerScope(PhotocardScreen.class)
        PhotocardPresenter provideSearchPresenter(){
            return new PhotocardPresenter();
        }
    }

    @dagger.Component(dependencies = MainScreen.Component.class, modules = Module.class)
    @DaggerScope(PhotocardScreen.class)
    public interface Component{
        void inject(PhotocardPresenter presenter);
        void inject(PhotocardView view);
        void inject(PhotocardAdapter adapter);

        RootPresenter getRootPresenter();
        Picasso getPicasso();
    }

    public class PhotocardPresenter extends AbstractPresenter<PhotocardView, PhotoModel>{

        @Override
        protected void initActionBar() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView(mPhotocard);
        }
    }
}
