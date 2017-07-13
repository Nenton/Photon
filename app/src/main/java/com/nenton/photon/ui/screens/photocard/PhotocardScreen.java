package com.nenton.photon.ui.screens.photocard;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.nenton.photon.R;
import com.nenton.photon.data.network.errors.ApiError;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.PhotocardModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.IPhotocardPresenter;
import com.nenton.photon.mvp.presenters.MenuItemHolder;
import com.nenton.photon.mvp.presenters.PopupMenuItem;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.author.AuthorScreen;
import com.nenton.photon.utils.NetworkStatusChecker;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Subscriber;

import static android.content.Context.DOWNLOAD_SERVICE;

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof PhotocardScreen) {
            return super.equals(o) && ((PhotocardScreen) o).mPhotocard.getId().equals(this.mPhotocard.getId());
        } else {
            return super.equals(o);
        }
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(PhotocardScreen.class)
        PhotocardModel providePhotocardModel() {
            return new PhotocardModel();
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

        RootPresenter getRootPresenter();

        Picasso getPicasso();
    }

    public class PhotocardPresenter extends AbstractPresenter<PhotocardView, PhotocardModel> implements IPhotocardPresenter {

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setTitle("Фотокарточка")
                    .setBackArrow(true)
                    .addAction(new MenuItemHolder("Меню", R.drawable.ic_custom_menu_black_24dp, item -> {
                        if (getRootView() != null) {
                            getRootView().showSettings();
                        }
                        return true;
                    }))
                    .build();
        }

        @Override
        protected void initMenuPopup() {
            mModel.isPhotoFromFav(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        mRootPresenter.newMenuPopupBuilder()
                                .setIdMenuRes(R.menu.photocard_settings_menu_unfav)
                                .addMenuPopup(new PopupMenuItem(R.id.unfav_photo_dial, () -> deleteFromFav()))
                                .addMenuPopup(new PopupMenuItem(R.id.share_photo_dial, () -> sharePhotoDialog()))
                                .addMenuPopup(new PopupMenuItem(R.id.download_photo_dial, () -> downloadPhoto()))
                                .build();
                    } else {
                        mRootPresenter.newMenuPopupBuilder()
                                .setIdMenuRes(R.menu.photocard_settings_menu_fav)
                                .addMenuPopup(new PopupMenuItem(R.id.fav_photo_dial, () -> addToFavouriteDialog()))
                                .addMenuPopup(new PopupMenuItem(R.id.share_photo_dial, () -> sharePhotoDialog()))
                                .addMenuPopup(new PopupMenuItem(R.id.download_photo_dial, () -> downloadPhoto()))
                                .build();
                    }
                }
            });
        }

        @Override
        public void deleteFromFav() {
            if (mModel.isSignIn()) {
                mCompSubs.add(mModel.deleteFromFav(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
            }
        }

        @Override
        public void downloadPhoto() {
            if (!mPhotocard.getPhoto().isEmpty() && NetworkStatusChecker.isNetworkAvailible()) {
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse(mPhotocard.getPhoto()));

                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, mPhotocard.getTitle());
                r.allowScanningByMediaScanner();
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager dm = (DownloadManager) ((RootActivity) getRootView()).getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);
            } else {
                getRootView().showError(new ApiError("Интернет недоступен попробуйте позже"));
            }
        }


        private void sharePhotoDialog() {
            if (getView() != null) {
                getView().showDialogSharePhoto();
            }
        }

        @Override
        public void sharePhoto() {
            if (getRootView() != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, mPhotocard.getPhoto());
                shareIntent.setType("image/jpeg");
                RootActivity rootView = (RootActivity) getRootView();
                rootView.startActivity(Intent.createChooser(shareIntent, rootView.getResources().getText(R.string.app_name)));
            }
        }

        private void addToFavouriteDialog() {
            if (mModel.isSignIn() && getView() != null) {
                getView().showDialogAddFav();
            }
        }

        @Override
        public void addTofav() {
            mModel.addToFav(mPhotocard.getId(), () -> {
                if (getRootView() != null) {
                    ((RootActivity) getRootView()).runOnUiThread(() -> getRootView().showMessage("Фотокарточка добавлена в избранное"));
                }
            });
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initPhoto(mPhotocard);
            mCompSubs.add(mModel.getUser(mPhotocard.getOwner()).subscribe(new RealmSubscriber()));
            if (mModel.isSignIn()) {
                mCompSubs.add(mModel.addViewsToPhotocard(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));

                mCompSubs.add(mModel.isPhotoFromFav(mPhotocard.getId()).subscribe(new ViewSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (getView() != null) {
                            getView().showFavIcon(aBoolean);
                        }
                    }
                }));
            }
        }

        @Override
        public void clickOnAuthor() {
            if (getView() != null) {
                Flow.get(getView().getContext()).set(new AuthorScreen(mPhotocard.getOwner()));
            }
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
                if (getView() != null) {
                    getView().initUser(new UserInfoDto(userRealm));
                }
            }
        }
    }
}
