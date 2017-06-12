package com.nenton.photon.ui.screens.search_filters.filters;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.SearchModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;

import dagger.Provides;
import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */
@Screen(R.layout.filters_screen)
public class FilterScreen extends AbstractScreen<SearchFiltersScreen.Component> {
    @Override
    public Object createScreenComponent(SearchFiltersScreen.Component parentComponent) {
        return DaggerFilterScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module{
        @Provides
        @DaggerScope(FilterScreen.class)
        SearchModel provideSearchModel(){
            return new SearchModel();
        }

        @Provides
        @DaggerScope(FilterScreen.class)
        FilterPresenter provideFilterPresenter(){
            return new FilterPresenter();
        }
    }

    @dagger.Component(dependencies = SearchFiltersScreen.Component.class, modules = Module.class)
    @DaggerScope(FilterScreen.class)
    public interface Component{
        void inject(FilterPresenter presenter);
        void inject(FilterView view);
    }

    public class FilterPresenter extends AbstractPresenter<FilterView, SearchModel>{

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
    }
}
