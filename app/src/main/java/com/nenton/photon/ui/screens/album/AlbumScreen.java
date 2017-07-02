package com.nenton.photon.ui.screens.album;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
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
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by serge_000 on 06.06.2017.
 */
@Screen(R.layout.screen_album)
public class AlbumScreen extends AbstractScreen<RootActivity.RootComponent> {

    private AlbumRealm mAlbum;

    public AlbumScreen(AlbumRealm album) {
        this.mAlbum = album;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAlbumScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(AlbumScreen.class)
        MainModel providePhotoModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(AlbumScreen.class)
        AlbumPresenter provideAlbumPresenter() {
            return new AlbumPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AlbumScreen.class)
    public interface Component {
        void inject(AlbumPresenter presenter);

        void inject(AlbumView view);

        void inject(AlbumAdapter adapter);

        Picasso getPicasso();

        RootPresenter getRootPresenter();
    }

    public class AlbumPresenter extends AbstractPresenter<AlbumView, MainModel> {

        private boolean isCurAlbumUser = false;

        @Override
        protected void initActionBar() {
            RootPresenter.ActionBarBuilder builder = mRootPresenter.newActionBarBuilder()
                    .setTitle("Альбом")
                    .setBackArrow(true);
            if (isCurAlbumUser) {
                builder.addAction(new MenuItemHolder("Меню", R.drawable.ic_custom_menu_black_24dp, item -> {
                    getRootView().showSettings();
                    return true;
                }));
            }
            builder.build();
        }

        @Override
        protected void initMenuPopup() {
            if (isCurAlbumUser) {
                mRootPresenter.newMenuPopupBuilder()
                        .setIdMenuRes(R.menu.album_settings_menu)
                        .addMenuPopup(new PopupMenuItem(R.id.edit_album_dial, this::editAlbum))
                        .addMenuPopup(new PopupMenuItem(R.id.delete_album_dial, this::showDeleteAlbum))
                        .addMenuPopup(new PopupMenuItem(R.id.upload_photo_album_dial, this::addPhotoToAlbum))
                        .build();
            } else {
                mRootPresenter.newMenuPopupBuilder().build();
            }
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            mCompSubs = new CompositeSubscription();
            mCompSubs.add(mModel.isAlbumFromUser(mAlbum.getOwner()).subscribe(new ViewSubscriber<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    isCurAlbumUser = aBoolean;
                    initActionBar();
                    initMenuPopup();
                }
            }));
            initView();
            initActionBar();
            initMenuPopup();
        }

        private void initView() {
            mCompSubs.add(mModel.getAlbumFromRealm(mAlbum.getId(), mAlbum.getOwner()).subscribe(new ViewSubscriber<AlbumRealm>() {
                @Override
                public void onNext(AlbumRealm albumRealm) {
                    getView().initView(albumRealm);
                }
            }));
        }

        public void editAlbum() {
            getView().showEditAlbum();
        }

        public void editAlbumObs(String name, String description) {
            mModel.editAlbum(mAlbum.getId(), name, description, () -> {
                ((RootActivity) getRootView()).runOnUiThread(() -> {
                    getView().cancelEditAlbum();
                    initView();
                });
            });
        }

        public void showDeleteAlbum() {
            getView().showDeleteAlbum();
        }

        public void deleteAlbum() {
            mModel.deleteAlbumObs(mAlbum.getId(), () -> {
                ((RootActivity) getRootView()).runOnUiThread(() -> {
                    ((RootActivity) getRootView()).onBackPressed();
                });
            });

        }

        public void addPhotoToAlbum() {

        }

        public void clickOnPhotocard(PhotocardRealm mPhoto) {
            Flow.get(getView().getContext()).set(new PhotocardScreen(mPhoto));
        }
    }
}
