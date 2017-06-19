package com.nenton.photon.ui.screens.search_filters.search;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.screens.main.MainScreen;
import com.nenton.photon.ui.screens.search_filters.SearchEnum;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.nenton.photon.utils.SearchQuery;

import java.util.Set;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */
@Screen(R.layout.screen_search)
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
        MainModel provideSearchModel(){
            return new MainModel();
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
        void inject(SearchTitleView view);
        void inject(TagsAdapter adapter);
        void inject(SearchAdapter adapter);

        RootPresenter getRootPresenter();
    }

    public class SearchPresenter extends AbstractPresenter<SearchTitleView,MainModel>{

        public SearchQuery getSearchFilterQuery() {
            return mSearchFilterQuery;
        }

        private SearchQuery mSearchFilterQuery = new SearchQuery();

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
            mCompSubs.add(mModel.getPhotocardTagsObs().subscribe(stringRealm -> {
                    getView().getAdapter().addString(stringRealm);
            }, throwable -> {}));
            getView().initView(mModel.getStrings());
        }

        public void clickOnStringQuery(String s) {
            getView().setTextSearchViewByQueryString(s);
        }

        public void clickOnSearch(CharSequence query, Set<String> stringSet) {
            mModel.saveSearchString(query.toString());
            mSearchFilterQuery.setTags(stringSet);
            mSearchFilterQuery.setTitle(query.toString());
            mRootPresenter.setSearchQuery(mSearchFilterQuery);
            mRootPresenter.setSearchEnum(SearchEnum.SEARCH);
            Flow.get(getView().getContext()).set(new MainScreen());
        }
    }
}
