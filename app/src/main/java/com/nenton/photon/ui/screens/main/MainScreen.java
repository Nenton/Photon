package com.nenton.photon.ui.screens.main;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
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
import com.nenton.photon.ui.screens.photocard.PhotocardScreen;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by serge on 04.06.2017.
 */
@Screen(R.layout.screen_main)
public class MainScreen extends AbstractScreen<RootActivity.RootComponent> {
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerMainScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(MainScreen.class)
        MainModel providePhotoModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(MainScreen.class)
        MainPresenter provideMainPresenter() {
            return new MainPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(MainScreen.class)
    public interface Component {
        void inject(MainPresenter presenter);

        void inject(MainView view);

        void inject(MainAdapter adapter);

        Picasso getPicasso();

        RootPresenter getRootPresenter();
    }

    public class MainPresenter extends AbstractPresenter<MainView, MainModel> {

        private static final int AUTH_STATE = 900;
        private static final int UN_AUTH_STATE = 990;
        private int mLoginState;

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setTitle("Фотон")
                    .addAction(new MenuItemHolder("Поиск", R.drawable.ic_custom_search_black_24dp, item -> {
                        Flow.get(getView().getContext()).set(new SearchFiltersScreen());
                        return true;
                    }))
                    .addAction(new MenuItemHolder("Настройки", R.drawable.ic_custom_gear_black_24dp, item -> {
                        getRootView().showSettings();
                        return true;
                    }))
                    .build();

        }

        @Override
        protected void initMenuPopup() {
            if (mLoginState == UN_AUTH_STATE) {
                mRootPresenter.newMenuPopupBuilder()
                        .setIdMenuRes(R.menu.main_auth_settings_menu)
                        .addMenuPopup(new PopupMenuItem(R.id.enter_dial, this::enterAccount))
                        .addMenuPopup(new PopupMenuItem(R.id.registration_dial, this::registration))
                        .build();
            } else {
                mRootPresenter.newMenuPopupBuilder()
                        .setIdMenuRes(R.menu.main_un_auth_settings_menu)
                        .addMenuPopup(new PopupMenuItem(R.id.exit_dial, this::exitUser))
                        .build();
            }
        }

        private void registration() {
            getView().signUp();
        }

        private void enterAccount() {
            getView().signIn();
        }

        private void exitUser() {
            mModel.unAuth();
            changeStateAuth();
            initMenuPopup();
            getRootView().showMessage("Пользователь вышел");
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
            mLoginState = mModel.isSignIn() ? AUTH_STATE : UN_AUTH_STATE;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            mCompSubs.add(subscribeOnProductRealmObs());
        }

        private Subscription subscribeOnProductRealmObs() {
            return mModel.getPhotocardObs()
                    .subscribe(new RealmSubscriber());
        }

        public void signIn(UserLoginReq loginReq) {
            mCompSubs.add(mModel.signIn(loginReq)
                    .subscribe(userLoginRes -> {
                    }, throwable -> getRootView().showMessage("не правильный логин или пароль"), () -> {
                        getRootView().showMessage("Пользователь залогинен");
                        getView().cancelSignIn();
                    }));
        }

        public void signUp(UserCreateReq createReq) {
            mCompSubs.add(mModel.signUp(createReq)
                    .subscribe(userCreateRes -> {
                            }, throwable -> getRootView().showMessage("не правильный логин или пароль"),
                            () -> {
                                getRootView().showMessage("Пользователь успешно создан");
                                getView().cancelSignUp();
                            }));
        }

        private void changeStateAuth() {
            if (mLoginState == AUTH_STATE) {
                mLoginState = UN_AUTH_STATE;
            } else {
                mLoginState = AUTH_STATE;
            }
        }

        public void clickOnPhoto(PhotocardRealm photocard) {
            Flow.get(getView().getContext()).set(new PhotocardScreen(photocard));
        }

        private class RealmSubscriber extends Subscriber<PhotocardRealm> {
            MainAdapter mAdapter = getView().getAdapter();

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getRootView() != null) {
                    getRootView().showError(e);
                }
            }

            @Override
            public void onNext(PhotocardRealm photocardRealm) {
                mAdapter.addPhoto(photocardRealm);
                getRootView().hideLoad();
            }
        }
    }


}
