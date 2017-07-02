package com.nenton.photon.ui.screens.add_photocard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.ActivityResultDto;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.account.AccountScreen;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.UriHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Observable;
import rx.Subscription;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by serge on 18.06.2017.
 */
@Screen(R.layout.screen_add_photocard)
public class AddPhotocardScreen extends AbstractScreen<RootActivity.RootComponent> {
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAddPhotocardScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        MainModel providePhotoModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        AddPhotocardPresenter provideAccountPresenter() {
            return new AddPhotocardPresenter();
        }

        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        AddPhotocardSelectTagsAdapter provideAddPhotocardSelectTagsAdapter() {
            return new AddPhotocardSelectTagsAdapter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AddPhotocardScreen.class)
    public interface Component {
        void inject(AddPhotocardPresenter presenter);

        void inject(AddPhotocardView view);

        void inject(AddPhotocardSelectAlbumAdapter adapter);

        void inject(AddPhotocardSuggestionTagsAdapter adapter);

        Picasso getPicasso();

        RootPresenter getRootPresenter();
    }

    public class AddPhotocardPresenter extends AbstractPresenter<AddPhotocardView, MainModel> {

        private String mAvatarUri;
        Subscription subscribe;

        //region ========================= LifeCycle =========================

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setTitle("Добавление фотокарточки")
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
            getView().initView();
            getView().showPhotoPanel();
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

        //endregion

        //region ========================= Gallery =========================

        public void chooseGallery() {
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
                mAvatarUri = activityResultDto.getIntent().getData().toString();
                initPropertyView();
                initAdapterSuggestionTags();
                getView().showPropertyPanel();
            } else {
                getRootView().showMessage("Что-то пошло не так");
            }
        }

        //endregion

        private void initPropertyView() {
            UserInfoDto userInfo = mModel.getUserInfo();
            mCompSubs.add(mModel.getUser(userInfo.getId()).subscribe(new ViewSubscriber<UserRealm>() {
                @Override
                public void onNext(UserRealm userRealm) {
                    getView().showView(userRealm);
                    if (!userRealm.getAlbums().isEmpty()) {
                        getView().goneAddAlbum();
                    }
                }
            }));
        }

        private void initAdapterSuggestionTags() {
            mCompSubs.add(mModel.getPhotocardTagsObs().subscribe(new ViewSubscriber<String>() {
                @Override
                public void onNext(String s) {
                    getView().getTagsSuggestionAdapter().addTag(s);
                }
            }));
        }

        public void clickAlbum(int positionOnSelectItem) {
            getView().checkedCurrentAlbum(positionOnSelectItem);
        }

        public void clickOnSuggestTag(String albumRealm) {
            getView().addTag(albumRealm);
        }

        public void savePhotocard() {
            FiltersDto filters = getView().getFilters();
            String namePhotocard = getView().getNamePhotocard();
            List<String> tags = getView().getTags();
            String idAlbum = getView().getIdAlbum();
            if (filters == null) {
                getRootView().showMessage("Не все фильтры выбраны");
                return;
            }
            if (namePhotocard == null) {
                getRootView().showMessage("Не выбрано имя фотокарточки");
                return;
            }
            if (tags == null) {
                getRootView().showMessage("Не выбрано ни одного тэга");
                return;
            }
            if (idAlbum == null) {
                getRootView().showMessage("Не выбран альбом");
                return;
            }

            if (mAvatarUri != null) {
                UriHelper uriHelper = new UriHelper();
                File file = new File(uriHelper.getPath(getView().getContext(), Uri.parse(mAvatarUri)));
                mModel.createPhotocard(idAlbum, namePhotocard, file, mAvatarUri, tags, filters, () -> {
                    ((RootActivity) getRootView()).runOnUiThread(() -> Flow.get(getView().getContext()).set(new AccountScreen()));
                });
            }
        }

        public void cancelCreate() {
            mAvatarUri = null;
            getView().showPhotoPanel();
        }

        public void addAlbum(String name, String description) {
            mModel.createAlbumObs(name, description, () -> {
                ((RootActivity) getRootView()).runOnUiThread(() -> {
                    initPropertyView();
                    getView().goneAddAlbum();
                });
            });
        }
    }
}
