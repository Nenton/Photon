package com.nenton.photon.ui.screens.search_filters.search;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.SearchModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.screens.main.MainScreen;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;

import dagger.Provides;
import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */
@Screen(R.layout.search_screen)
public class SearchScreen extends AbstractScreen<SearchFiltersScreen.Component> {

    @Override
    public Object createScreenComponent(SearchFiltersScreen.Component parentComponent) {
        return DaggerSearchScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(SearchScreen.class)
        SearchModel provideSearchModel(){
            return new SearchModel();
        }

        @Provides
        @DaggerScope(SearchScreen.class)
        SearchPresenter provideSearchPresenter(){
            return new SearchPresenter();
        }
    }

    @dagger.Component(dependencies = SearchFiltersScreen.Component.class, modules = Module.class)
    @DaggerScope(SearchScreen.class)
    public interface Component{
        void inject(SearchPresenter presenter);
        void inject(SearchView view);
        void inject(TagsAdapter adapter);

        RootPresenter getRootPresenter();
    }

    public class SearchPresenter extends AbstractPresenter<SearchView,SearchModel>{


        @Override
        protected void initActionBar() {
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
