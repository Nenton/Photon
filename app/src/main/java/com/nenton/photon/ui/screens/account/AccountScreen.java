package com.nenton.photon.ui.screens.account;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.UserRealm;
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
@Screen(R.layout.account_screen)
public class AccountScreen extends AbstractScreen<RootActivity.RootComponent>{

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAccountScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(AccountScreen.class)
        MainModel providePhotoModel(){
            return new MainModel();
        }

        @Provides
        @DaggerScope(AccountScreen.class)
        AccountPresenter provideAccountPresenter(){
            return new AccountPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AccountScreen.class)
    public interface Component{
        void inject(AccountPresenter presenter);
        void inject(AccountView view);
        void inject(AccountAdapter adapter);

        Picasso getPicasso();
        RootPresenter getRootPresenter();
    }

    public class AccountPresenter extends AbstractPresenter<AccountView, MainModel>{

        @Override
        protected void initActionBar() {
            if (mModel.isSignIn()){
                mRootPresenter.newActionBarBuilder()
                        .setTitle("Профиль")
                        .setBackArrow(false)
                        .addAction(new MenuItemHolder("Настройки", R.drawable.ic_custom_menu_black_24dp, item -> {
                            getRootView().showSettings();
                            return true;
                        }))
                        .build();
            } else {
                mRootPresenter.newActionBarBuilder()
                        .setTitle("Профиль")
                        .setBackArrow(false)
                        .build();
            }
        }

        @Override
        protected void initMenuPopup() {
            mRootPresenter.newMenuPopupBuilder()
                    .setIdMenuRes(R.menu.account_settings_menu)
                    .addMenuPopup(new PopupMenuItem(R.id.add_album_dial, this::addAlbum))
                    .addMenuPopup(new PopupMenuItem(R.id.edit_user_dial, this::editUserInfo))
                    .addMenuPopup(new PopupMenuItem(R.id.upload_avatar_dial, this::uploadAvatar))
                    .addMenuPopup(new PopupMenuItem(R.id.exit_account_dial, this::exitAccount))
                    .build();
        }

        private void exitAccount() {

        }

        private void uploadAvatar() {

        }

        private void editUserInfo() {

        }

        private void addAlbum() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component)scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            UserInfoDto userInfo = mModel.getUserInfo();
            mCompSubs.add(mModel.getUserRealm(userInfo.getId())
            .subscribe(userRealm -> {
                getView().showAuthState(userRealm);
            }, throwable -> {
                getView().showUnAuthState();
            }));
        }

        public boolean isAuth() {
            return mModel.isSignIn();
        }
    }
}
