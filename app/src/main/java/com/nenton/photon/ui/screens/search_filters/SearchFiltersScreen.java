package com.nenton.photon.ui.screens.search_filters;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.SearchModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.screens.main.MainScreen;

import dagger.Provides;
import flow.TreeKey;
import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */
@Screen(R.layout.search_filters_screen)
public class SearchFiltersScreen extends AbstractScreen<MainScreen.Component> implements TreeKey{

    @Override
    public Object createScreenComponent(MainScreen.Component parentComponent) {
        return DaggerSearchFiltersScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @NonNull
    @Override
    public Object getParentKey() {
        return new MainScreen();
    }

    //region ========================= DI =========================

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(SearchFiltersScreen.class)
        SearchFiltersPresenter provideSearchFiltersPresenter(){
            return new SearchFiltersPresenter();
        }

        @Provides
        @DaggerScope(SearchFiltersScreen.class)
        SearchModel provideSearchModel(){
            return new SearchModel();
        }
    }

    @dagger.Component(dependencies = MainScreen.Component.class, modules = Module.class)
    @DaggerScope(SearchFiltersScreen.class)
    public interface Component{
        void inject(SearchFiltersPresenter searchFiltersPresenter);
        void inject(SearchFiltersView searchFiltersView);

        RootPresenter getRootPresenter();
    }

    //endregion

    public class SearchFiltersPresenter extends AbstractPresenter<SearchFiltersView, SearchModel>{

        @Override
        protected void initActionBar() {
            mRootPresenter.newActionBarBuilder()
                    .setVisibleToolbar(false)
                    .setTab(getView().getViewPager())
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
        }
    }
}
