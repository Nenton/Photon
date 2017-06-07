package com.nenton.photon.ui.screens.main;

import android.os.Bundle;
import android.view.MenuItem;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.PhotoModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.MenuItemHolder;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.DaggerRootActivity_RootComponent;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;

/**
 * Created by serge on 04.06.2017.
 */
@Screen(R.layout.main_screen)
public class MainScreen extends AbstractScreen<RootActivity.RootComponent> {
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerMainScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(MainScreen.class)
        PhotoModel providePhotoModel(){
            return new PhotoModel();
        }

        @Provides
        @DaggerScope(MainScreen.class)
        MainPresenter provideMainPresenter(){
            return new MainPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(MainScreen.class)
    public interface Component{
        void inject(MainPresenter presenter);
        void inject(MainView view);
        void inject(MainAdapter adapter);

        Picasso getPicasso();
        RootPresenter getRootPresenter();
    }

    public class MainPresenter extends AbstractPresenter<MainView, PhotoModel>{

        @Override
        protected void initActionBar() {
            mRootPresenter.new ActionBarBuilder()
                    .setTitle("Фотон")
                    .addAction(new MenuItemHolder("Поиск", R.drawable.ic_custom_search_black_24dp, item -> {
                        Flow.get(getView().getContext()).set(new SearchFiltersScreen());
                        return false;
                    }))
                    .addAction(new MenuItemHolder("Настройки", R.drawable.ic_custom_gear_black_24dp, item -> {
                        // TODO: 07.06.2017 открыть меню
                        return false;
                    }))
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
        }
    }
}
