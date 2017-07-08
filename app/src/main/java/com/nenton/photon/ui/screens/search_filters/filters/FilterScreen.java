package com.nenton.photon.ui.screens.search_filters.filters;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.model.SearchModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.IFilterPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.main.MainScreen;
import com.nenton.photon.ui.screens.search_filters.SearchEnum;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.nenton.photon.utils.SearchFilterQuery;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */
@Screen(R.layout.screen_filters)
public class FilterScreen extends AbstractScreen<SearchFiltersScreen.Component> {
    @Override
    public Object createScreenComponent(SearchFiltersScreen.Component parentComponent) {
        return DaggerFilterScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(FilterScreen.class)
        SearchModel provideSearchModel() {
            return new SearchModel();
        }

        @Provides
        @DaggerScope(FilterScreen.class)
        FilterPresenter provideFilterPresenter() {
            return new FilterPresenter();
        }
    }

    @dagger.Component(dependencies = SearchFiltersScreen.Component.class, modules = Module.class)
    @DaggerScope(FilterScreen.class)
    public interface Component {
        void inject(FilterPresenter presenter);
        void inject(FilterView view);
    }

    public class FilterPresenter extends AbstractPresenter<FilterView, SearchModel> implements IFilterPresenter{

        private SearchFilterQuery mSearchFilterQuery = new SearchFilterQuery();

        public SearchFilterQuery getSearchFilterQuery() {
            return mSearchFilterQuery;
        }

        @Override
        protected void initActionBar() {

        }

        @Override
        protected void initMenuPopup() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            SearchEnum searchEnum = mRootPresenter.getSearchEnum();
            if (getView() != null){
                if (searchEnum == SearchEnum.FILTER){
                    getView().initView(mRootPresenter.getSearchFilterQuery());
                } else {
                    getView().initView();
                }
            }
        }

        @Override
        public void clickOnSearch() {
            if (mSearchFilterQuery.isReadySearchModel()) {
                mRootPresenter.setSearchFilterQuery(mSearchFilterQuery);
                mRootPresenter.setSearchEnum(SearchEnum.FILTER);
                if (getView() != null){
                    Flow.get(getView().getContext()).set(new MainScreen());
                }
            } else {
                if (getRootView() != null){
                    getRootView().showMessage("Ни один из параметров не был выбран");
                }
            }
        }
    }
}
