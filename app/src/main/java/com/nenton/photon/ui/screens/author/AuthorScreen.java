package com.nenton.photon.ui.screens.author;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.album.AlbumScreen;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.screen_author)
public class AuthorScreen extends AbstractScreen<RootActivity.RootComponent> {

    private final String owner;

    public AuthorScreen(String owner) {
        this.owner = owner;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAuthorScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(AuthorScreen.class)
        MainModel providePhotoModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(AuthorScreen.class)
        AuthorPresenter provideAccountPresenter() {
            return new AuthorPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AuthorScreen.class)
    public interface Component {
        void inject(AuthorPresenter presenter);
        void inject(AuthorView view);
        void inject(AuthorAdapter adapter);
        Picasso getPicasso();
        RootPresenter getRootPresenter();
    }

    public class AuthorPresenter extends AbstractPresenter<AuthorView, MainModel> {

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setBackArrow(true)
                    .setTitle("Автор")
                    .build();
        }

        @Override
        protected void initMenuPopup() {
            mRootPresenter.newMenuPopupBuilder()
                    .build();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            mCompSubs.add(mModel.getUser(owner).subscribe(userRealm -> getView().initView(userRealm), throwable -> {
            }));
        }

        public void clickOnAlbum(AlbumRealm album) {
            Flow.get(getView().getContext()).set(new AlbumScreen(album));
        }
    }
}
