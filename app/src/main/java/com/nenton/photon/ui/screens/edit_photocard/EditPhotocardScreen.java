package com.nenton.photon.ui.screens.edit_photocard;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.model.PhotocardModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.IEditPhotocardPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.account.AccountScreen;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;

/**
 * Created by serge on 07.07.2017.
 */
@Screen(R.layout.screen_edit_photocard)
public class EditPhotocardScreen extends AbstractScreen<RootActivity.RootComponent> {

    private final PhotocardRealm mPhotocardRealm;

    public EditPhotocardScreen(PhotocardRealm photocardRealm) {
        mPhotocardRealm = photocardRealm;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerEditPhotocardScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(EditPhotocardScreen.class)
        PhotocardModel providePhotocardModel() {
            return new PhotocardModel();
        }

        @Provides
        @DaggerScope(EditPhotocardScreen.class)
        EditPhotocardPresenter provideAccountPresenter() {
            return new EditPhotocardPresenter();
        }

    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(EditPhotocardScreen.class)
    public interface Component {
        void inject(EditPhotocardPresenter presenter);
        void inject(EditPhotocardView view);
        void inject(EditPhotocardSelectAlbumAdapter adapter);
        void inject(EditPhotocardSuggestionTagsAdapter adapter);

        Picasso getPicasso();

        RootPresenter getRootPresenter();
    }

    public class EditPhotocardPresenter extends AbstractPresenter<EditPhotocardView, PhotocardModel> implements IEditPhotocardPresenter{

        private List<String> mStrings = new ArrayList<>();

        public void addString(String s) {
            mStrings.add(s);
        }

        public void removeString(String s) {
            mStrings.remove(s);
        }

        public List<String> getStrings() {
            return mStrings;
        }

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setBackArrow(true)
                    .setTitle("Редактирование фотокарточки")
                    .build();
        }

        @Override
        protected void initMenuPopup() {
            mRootPresenter.newMenuPopupBuilder().build();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null){
                getView().initView();
                getView().showView(mPhotocardRealm);
            }
            initPropertyView();
            initAdapterSuggestionTags();
        }

        public void clickOnSuggestTag(String string) {
            if (getView() != null){
                getView().addTag(string);
            }
        }

        public void clickAlbum(int positionOnSelectItem) {
            if (getView() != null){
                getView().checkedCurrentAlbum(positionOnSelectItem);
            }
        }

        @Override
        public void initPropertyView() {
            UserInfoDto userInfo = mModel.getUserInfo();
            mCompSubs.add(mModel.getUser(userInfo.getId()).subscribe(new ViewSubscriber<UserRealm>() {
                @Override
                public void onNext(UserRealm userRealm) {
                    if (getView() != null){
                        getView().initAlbums(userRealm);
                    }
                }
            }));
        }

        private void initAdapterSuggestionTags() {
            mCompSubs.add(mModel.getPhotocardTagsObs().subscribe(new ViewSubscriber<String>() {
                @Override
                public void onNext(String s) {
                    if (getView() != null){
                        getView().getTagsSuggestionAdapter().addTag(s);
                    }
                }
            }));
        }

        @Override
        public void savePhotocard() {
            if (getView() != null){
                FiltersDto filters = getView().getFilters();
                String namePhotocard = getView().getNamePhotocard();
                List<String> tags = mStrings;
                String idAlbum = getView().getIdAlbum();
                if (getRootView() != null){
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
                }

                PhotocardReq photocardReq = new PhotocardReq(namePhotocard, mPhotocardRealm.getPhoto(), idAlbum, tags, filters);

                mModel.editPhotocards(mPhotocardRealm.getId(), idAlbum, photocardReq, () -> {
                    if (getRootView() != null){
                        ((RootActivity) getRootView()).runOnUiThread(() -> Flow.get(getView().getContext()).set(new AccountScreen()));
                    }
                });
            }
        }

        @Override
        public void cancelEdit() {
            if (getRootView() != null){
                ((RootActivity) getRootView()).onBackPressed();
            }
        }

        public void removeTag(String string) {
            if (getView() != null){
                getView().removeTag(string);
            }
        }
    }
}
