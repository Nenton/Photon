package com.nenton.photon.ui.screens.add_photocard;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import mortar.MortarScope;

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
    public class Module{
        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        MainModel providePhotoModel(){
            return new MainModel();
        }

        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        AddPhotocardPresenter provideAccountPresenter(){
            return new AddPhotocardPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AddPhotocardScreen.class)
    public interface Component{
        void inject(AddPhotocardPresenter presenter);
        void inject(AddPhotocardView view);
        void inject(AddPhotocardSelectAlbumAdapter adapter);

        Picasso getPicasso();
        RootPresenter getRootPresenter();
    }

    public class AddPhotocardPresenter extends AbstractPresenter<AddPhotocardView, MainModel>{

        @Override
        protected void initActionBar() {
            
        }

        @Override
        protected void initMenuPopup() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component)scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().showView();
            getView().initView();
//            if (mModel.isSignIn()){
//                UserInfoDto userInfo = mModel.getUserInfo();
//                mCompSubs.add(mModel.getUser(userInfo.getId())
//                        .subscribe(userRealm -> {
//                            getView().showView(userRealm);
//                        }, throwable -> {
//                            getRootView().showMessage("Ошибка");
//                        }));
//            } else {
//                getRootView().showMessage("Не авторизован");
//            }
        }

        public void clickAlbum(int positionOnSelectItem) {
            getView().checkedCurrentAlbum(positionOnSelectItem);
        }
    }
}
