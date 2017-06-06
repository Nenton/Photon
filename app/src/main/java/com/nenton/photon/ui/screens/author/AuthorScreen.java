package com.nenton.photon.ui.screens.author;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.PhotoModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.account.AccountAdapter;
import com.nenton.photon.ui.screens.account.AccountView;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import mortar.MortarScope;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.account_screen)
public class AuthorScreen extends AbstractScreen<RootActivity.RootComponent>{
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAuthorScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(AuthorScreen.class)
        PhotoModel providePhotoModel(){
            return new PhotoModel();
        }

        @Provides
        @DaggerScope(AuthorScreen.class)
        AuthorPresenter provideAccountPresenter(){
            return new AuthorPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AuthorScreen.class)
    public interface Component{
        void inject(AuthorPresenter presenter);
        void inject(AuthorView view);
        void inject(AuthorAdapter adapter);

        Picasso getPicasso();
        RootPresenter getRootPresenter();
    }

    public class AuthorPresenter extends AbstractPresenter<AuthorView, PhotoModel>{

        @Override
        protected void initActionBar() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component)scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView();
        }
    }
}
