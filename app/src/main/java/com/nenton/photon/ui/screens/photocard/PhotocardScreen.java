package com.nenton.photon.ui.screens.photocard;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.MenuItemHolder;
import com.nenton.photon.mvp.presenters.PopupMenuItem;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import mortar.MortarScope;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.photocard_screen)
public class PhotocardScreen extends AbstractScreen<RootActivity.RootComponent> {

    private PhotocardRealm mPhotocard;

    public PhotocardScreen(PhotocardRealm photocard) {
        this.mPhotocard = photocard;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerPhotocardScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(PhotocardScreen.class)
        MainModel provideSearchModel(){
            return new MainModel();
        }

        @Provides
        @DaggerScope(PhotocardScreen.class)
        PhotocardPresenter provideSearchPresenter(){
            return new PhotocardPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(PhotocardScreen.class)
    public interface Component{
        void inject(PhotocardPresenter presenter);
        void inject(PhotocardView view);
        void inject(PhotocardAdapter adapter);

        RootPresenter getRootPresenter();
        Picasso getPicasso();
    }

    public class PhotocardPresenter extends AbstractPresenter<PhotocardView, MainModel>{

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setTitle("Фотокарточка")
                    .setBackArrow(true)
                    .addAction(new MenuItemHolder("Меню", R.drawable.ic_custom_menu_black_24dp, item -> {
                        getRootView().showSettings();
                        return true;
                    }))
                    .build();
        }

        @Override
        protected void initMenuPopup() {
            mRootPresenter.newMenuPopupBuilder()
                    .setIdMenuRes(R.menu.photocard_settings_menu)
                    .addMenuPopup(new PopupMenuItem(R.id.fav_photo_dial, this::addToFavourite))
                    .addMenuPopup(new PopupMenuItem(R.id.share_photo_dial, this::sharePhoto))
                    .addMenuPopup(new PopupMenuItem(R.id.download_photo_dial, this::downloadPhoto))
                    .build();
        }

        private void downloadPhoto() {

        }

        private void sharePhoto() {

        }

        private void addToFavourite() {
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
