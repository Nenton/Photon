package com.nenton.photon.ui.screens.search_filters.search;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.util.AttributeSet;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

import butterknife.BindView;

/**
 * Created by serge on 05.06.2017.
 */

public class SearchView extends AbstractView<SearchScreen.SearchPresenter> {

    @BindView(R.id.tags_RV)
    RecyclerView mRecyclerViewTags;

    private TagsAdapter mTagsAdapter = new TagsAdapter();

    public SearchView(Context context, AttributeSet attrs) {
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

    void initView(){
        mRecyclerViewTags.setLayoutManager(new WordsLayoutManager(getContext()));
        initAdapter();
        mRecyclerViewTags.setAdapter(mTagsAdapter);
    }

    private void initAdapter() {

    }

    public TagsAdapter getAdapter() {
        return mTagsAdapter;
    }
}
