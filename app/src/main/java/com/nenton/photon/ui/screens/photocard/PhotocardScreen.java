package com.nenton.photon.ui.screens.photocard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.PhotoIdReq;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
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
import com.nenton.photon.ui.screens.author.AuthorScreen;
import com.nenton.photon.utils.BasicImageDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Subscriber;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.screen_photocard)
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
    public class Module {
        @Provides
        @DaggerScope(PhotocardScreen.class)
        MainModel provideSearchModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(PhotocardScreen.class)
        PhotocardPresenter provideSearchPresenter() {
            return new PhotocardPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(PhotocardScreen.class)
    public interface Component {
        void inject(PhotocardPresenter presenter);

        void inject(PhotocardView view);

        void inject(PhotocardAdapter adapter);

        RootPresenter getRootPresenter();

        Picasso getPicasso();
    }

    public class PhotocardPresenter extends AbstractPresenter<PhotocardView, MainModel> {

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
                    .addMenuPopup(new PopupMenuItem(R.id.fav_photo_delete_dial, this::deleteFromFav))
                    .addMenuPopup(new PopupMenuItem(R.id.download_photo_dial, this::downloadPhoto))
                    .build();
        }

        private void deleteFromFav() {
            if (mModel.isSignIn()) {
                mCompSubs.add(mModel.deleteFromFav(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
            }
        }

        private void downloadPhoto() {
            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                @Override
                public void onError(BasicImageDownloader.ImageError error) {

                }

                @Override
                public void onProgressChange(int percent) {

                }

                @Override
                public void onComplete(Bitmap result) {
                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath() + "/saveImage");
                    dir.mkdirs();
                    File file = new File(dir, mPhotocard.getTitle() + ".png");

                    BasicImageDownloader.writeToDisk(file,
                            result,
                            new BasicImageDownloader.OnBitmapSaveListener() {
                                @Override
                                public void onBitmapSaved() {
                                    getRootView().showMessage("Загрузка завершена");
                                }

                                @Override
                                public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                }
                            }, Bitmap.CompressFormat.PNG, true);
                }
            });
            downloader.download(mPhotocard.getPhoto(), false);
        }

        private void sharePhoto() {
            getRootView().showMessage("Пока не реализовано");
        }

        private void addToFavourite() {
            if (mModel.isSignIn()) {
                mCompSubs.add(mModel.addToFav(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
            }
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            mCompSubs.add(mModel.getUser(mPhotocard.getOwner()).subscribe(new RealmSubscriber()));
            if (mModel.isSignIn()) {
                mCompSubs.add(mModel.addViewsToPhotocard(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
            }
        }

        public void clickOnAuthor() {
            Flow.get(getView().getContext()).set(new AuthorScreen(mPhotocard.getOwner()));
        }

        private class RealmSubscriber extends Subscriber<UserRealm> {

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
            public void onNext(UserRealm userRealm) {
                getView().initView(mPhotocard, new UserInfoDto(userRealm));
            }
        }
    }
}
