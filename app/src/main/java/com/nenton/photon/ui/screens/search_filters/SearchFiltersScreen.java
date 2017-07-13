package com.nenton.photon.ui.screens.search_filters;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.model.SearchModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;

import dagger.Provides;
import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */
@Screen(R.layout.screen_search_filters)
public class SearchFiltersScreen extends AbstractScreen<RootActivity.RootComponent>{

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerSearchFiltersScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
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

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
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
            if (getView() != null){
                mRootPresenter.newActionBarBuilder()
                        .setVisibleToolbar(false)
                        .setTab(getView().getViewPager())
                        .build();
            }
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
            ((RootActivity) getRootView()).stateBottomNavView(false);
            if (getView() != null){
                if (mRootPresenter.getSearchEnum() == SearchEnum.FILTER){
                    getView().initView(1);
                } else {
                    getView().initView(0);
                }
            }
        }
    }
}
