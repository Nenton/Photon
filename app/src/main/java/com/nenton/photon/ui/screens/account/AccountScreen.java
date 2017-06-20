package com.nenton.photon.ui.screens.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.UserEditRes;
import com.nenton.photon.data.storage.dto.ActivityResultDto;
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
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.UriHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import dagger.Provides;
import mortar.MortarScope;
import rx.Observable;
import rx.Subscription;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.screen_account)
public class AccountScreen extends AbstractScreen<RootActivity.RootComponent> {

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAccountScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(AccountScreen.class)
        MainModel providePhotoModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(AccountScreen.class)
        AccountPresenter provideAccountPresenter() {
            return new AccountPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AccountScreen.class)
    public interface Component {
        void inject(AccountPresenter presenter);

        void inject(AccountView view);

        void inject(AccountAdapter adapter);

        Picasso getPicasso();

        RootPresenter getRootPresenter();
    }

    public class AccountPresenter extends AbstractPresenter<AccountView, MainModel> {

        Subscription subscribe;

        @Override
        protected void initActionBar() {
            if (mModel.isSignIn()) {
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
                    .addMenuPopup(new PopupMenuItem(R.id.add_album_dial, this::showAddAlbum))
                    .addMenuPopup(new PopupMenuItem(R.id.edit_user_dial, this::showEditUser))
                    .addMenuPopup(new PopupMenuItem(R.id.upload_avatar_dial, this::uploadAvatar))
                    .addMenuPopup(new PopupMenuItem(R.id.exit_account_dial, this::exitAccount))
                    .build();
        }

        private void showEditUser() {
            getView().showDialogEditUserInfo();
        }

        private void exitAccount() {
            mModel.unAuth();
            initActionBar();
            loadUserInfo();
        }


        public void editUserInfo(String name, String login) {
            mCompSubs.add(mModel.editUserInfoObs(name, login).subscribe(new ViewSubscriber<UserEditRes>() {
                @Override
                public void onNext(UserEditRes userEditRes) {
                    loadUserInfo();
                }
            }));
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            subscribeOnActivityResult();
        }

        @Override
        protected void onExitScope() {
            subscribe.unsubscribe();
            super.onExitScope();
        }

        //region ========================= Gallery =========================

        private void uploadAvatar() {
            if (getRootView() != null) {
                String[] permissions = new String[]{READ_EXTERNAL_STORAGE};
                if (mRootPresenter.checkPermissionsAndRequestIfNotGranted(permissions,
                        ConstantsManager.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)) {
                    takePhotoFromGallery();
                }
            }
        }

        private void takePhotoFromGallery() {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }
            ((RootActivity) getRootView()).startActivityForResult(intent, ConstantsManager.REQUEST_PROFILE_PHOTO_PICTURE);
        }

        private void subscribeOnActivityResult() {
            Observable<ActivityResultDto> filter = mRootPresenter.getActivityResultSubject()
                    .filter(
                            activityResultDto -> activityResultDto.getResultCode() == Activity.RESULT_OK);
            subscribe = subscribe(filter, new ViewSubscriber<ActivityResultDto>() {
                @Override
                public void onNext(ActivityResultDto activityResultDto) {
                    handleActivityResult(activityResultDto);
                }
            });

        }

        private void handleActivityResult(ActivityResultDto activityResultDto) {
            if (activityResultDto.getRequestCode() == ConstantsManager.REQUEST_PROFILE_PHOTO_PICTURE && activityResultDto.getIntent() != null) {
                String mAvatarUri = activityResultDto.getIntent().getData().toString();
                if (mAvatarUri != null) {
                    UriHelper uriHelper = new UriHelper();
                    File file = new File(uriHelper.getPath(getView().getContext(), Uri.parse(mAvatarUri)));
                    mModel.uploadPhotoToNetwork(mAvatarUri, file).subscribe(new ViewSubscriber<String>() {
                        @Override
                        public void onNext(String s) {
                            mModel.editUserInfoAvatarObs(s).subscribe(new ViewSubscriber<UserEditRes>() {
                                @Override
                                public void onNext(UserEditRes userEditRes) {
                                    loadUserInfo();
                                }
                            });
                        }
                    });
                }
            } else {
                getRootView().showMessage("Что-то пошло не так");
            }
        }

        //endregion


        private void showAddAlbum() {
            getView().showDialogAddAlbum();
        }

        void addAlbum(String name, String description) {
            mCompSubs.add(mModel.createAlbumObs(new AlbumCreateReq(name, description)).subscribe(new ViewSubscriber<Album>() {
                @Override
                public void onNext(Album album) {
                    loadUserInfo();
                }
            }));
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            loadUserInfo();
        }

        private void loadUserInfo() {
            if (mModel.isSignIn()) {
                UserInfoDto userInfo = mModel.getUserInfo();
                mCompSubs.add(mModel.getUser(userInfo.getId())
                        .subscribe(new ViewSubscriber<UserRealm>() {
                            @Override
                            public void onNext(UserRealm userRealm) {
                                getView().showAuthState(userRealm);
                            }
                        }));
            } else {
                getView().showUnAuthState();
            }
        }

        void clickOnSignIn() {
            getView().signIn();
        }

        void clickOnSignUp() {
            getView().signUp();
        }

        void signIn(UserLoginReq loginReq) {
            mCompSubs.add(mModel.signIn(loginReq).subscribe(new ViewSubscriber<SignInRes>() {
                @Override
                public void onNext(SignInRes signInRes) {
                    loadUserInfo();
                    getView().cancelSignIn();
                    initActionBar();
                }
            }));
        }

        void signUp(UserCreateReq createReq) {
            mCompSubs.add(mModel.signUp(createReq).subscribe(new ViewSubscriber<SignUpRes>() {
                @Override
                public void onNext(SignUpRes signUpRes) {
                    getView().cancelSignUp();
                }
            }));
        }
    }
}
