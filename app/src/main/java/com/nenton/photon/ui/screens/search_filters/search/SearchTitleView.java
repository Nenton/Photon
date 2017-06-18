package com.nenton.photon.ui.screens.search_filters.search;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.WordsLayoutManager;
import android.util.AttributeSet;
import android.widget.Button;


import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge on 05.06.2017.
 */

public class SearchTitleView extends AbstractView<SearchScreen.SearchPresenter> {

    @BindView(R.id.search_rv)
    RecyclerView mSearchRV;
    @BindView(R.id.tags_RV)
    RecyclerView mRecyclerViewTags;
    @BindView(R.id.search_view)
    SearchView mSearchView;

    @OnClick(R.id.search_btn)
    public void clickSearch(){
        mPresenter.clickOnSearch(mSearchView.getQuery(), mTagsAdapter.getStringSet());
        adapter.addString(mSearchView.getQuery().toString());
    }

    private TagsAdapter mTagsAdapter = new TagsAdapter();
    private SearchAdapter adapter = new SearchAdapter();

    public SearchTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<SearchScreen.Component>getDaggerComponent(context).inject(this);
    }

    void initView(List<String> strings) {
//        initAdapter();
        mRecyclerViewTags.setLayoutManager(new WordsLayoutManager(getContext()));
        mRecyclerViewTags.setAdapter(mTagsAdapter);

        adapter.addStrings(strings);

        mSearchRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchRV.setAdapter(adapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
//
//    private void initAdapter() {
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//        mTagsAdapter.addString("ыпыап");
//    }

    public TagsAdapter getAdapter() {
        return mTagsAdapter;
    }

    public void setTextSearchViewByQueryString(String s) {
        mSearchView.setQuery(s,false);
    }
}
