package com.nenton.photon.ui.screens.search_filters.search;

import android.os.Bundle;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.model.SearchModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.ISearchPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.main.MainScreen;
import com.nenton.photon.ui.screens.search_filters.SearchEnum;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.nenton.photon.utils.SearchQuery;

import java.util.HashSet;
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
    public class Module {
        @Provides
        @DaggerScope(SearchScreen.class)
        SearchModel provideSearchModel() {
            return new SearchModel();
        }

        @Provides
        @DaggerScope(SearchScreen.class)
        SearchPresenter provideSearchPresenter() {
            return new SearchPresenter();
        }
    }

    @dagger.Component(dependencies = SearchFiltersScreen.Component.class, modules = Module.class)
    @DaggerScope(SearchScreen.class)
    public interface Component {
        void inject(SearchPresenter presenter);

        void inject(SearchTitleView view);

        void inject(SearchAdapter adapter);

        RootPresenter getRootPresenter();
    }

    public class SearchPresenter extends AbstractPresenter<SearchTitleView, SearchModel> implements ISearchPresenter {

        private Set<String> mStringSet = new HashSet<>();

        private SearchQuery mSearchFilterQuery = new SearchQuery();

        public void addString(String s) {
            mStringSet.add(s);
        }

        public void removeString(String s) {
            mStringSet.remove(s);
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
            mCompSubs.add(mModel.getPhotocardTagsObs().subscribe(new ViewSubscriber<String>() {
                @Override
                public void onNext(String s) {
                    if (getView() != null) {
                        if (checkTag(s)) {
                            getView().addViewFlex(s, true);
                        } else {
                            getView().addViewFlex(s, false);
                        }
                    }
                }
            }));
            if (getView() != null) {
                getView().initView(mModel.getStrings());
            }
            if (mRootPresenter.getSearchEnum() == SearchEnum.SEARCH && getView() != null) {
                mStringSet = new HashSet<>(mRootPresenter.getSearchQuery().getTags());
                getView().initSearchTitle(mRootPresenter.getSearchQuery().getTitle());
                getView().setCountTag(mStringSet.size());
                getView().checkState();
            }
        }

        private boolean checkTag(String s) {
            if (mRootPresenter.getSearchEnum() == SearchEnum.SEARCH) {
                for (String string : mRootPresenter.getSearchQuery().getTags()) {
                    if (s.equals(string)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void clickOnStringQuery(String s) {
            if (getView() != null) {
                getView().setTextSearchViewByQueryString(s);
            }
        }

        @Override
        public void clickOnSearch(CharSequence query) {
            if (!query.toString().isEmpty()) {
                mModel.saveSearchString(query.toString());
            }
            mSearchFilterQuery.setTags(mStringSet);
            mSearchFilterQuery.setTitle(query.toString());
            mRootPresenter.setSearchQuery(mSearchFilterQuery);
            mRootPresenter.setSearchEnum(SearchEnum.SEARCH);
            if (getView() != null) {
                Flow.get(getView().getContext()).set(new MainScreen());
            }
        }

        @Override
        public void goBack() {
            if (getRootView() != null) {
                ((RootActivity) getRootView()).onBackPressed();
            }
        }

        public void deleteSuggest(String s) {
            mModel.deleteSuggest(s);
        }
    }
}
