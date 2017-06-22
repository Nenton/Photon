package com.nenton.photon.ui.screens.search_filters.search;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.WordsLayoutManager;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.utils.TextWatcherEditText;

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
    EditText mSearchView;
    @BindView(R.id.reboot_settings_search)
    ImageButton mRebootBtn;
    @BindView(R.id.back_and_check)
    ImageButton mBackCheckBtn;

    @OnClick(R.id.reboot_settings_search)
    public void clickSearch() {
        mSearchView.setText("");
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
        mRecyclerViewTags.setLayoutManager(new WordsLayoutManager(getContext()));
        mRecyclerViewTags.setAdapter(mTagsAdapter);

        adapter.addStrings(strings);

        mSearchRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchRV.setAdapter(adapter);

        mSearchView.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    mBackCheckBtn.setBackground(getContext().getResources().getDrawable(R.drawable.ic_check_black_24dp));
                    mBackCheckBtn.setOnClickListener(v -> {
                        mPresenter.clickOnSearch(mSearchView.getText(), mTagsAdapter.getStringSet());
                        adapter.addString(mSearchView.getText().toString());
                    });
                } else {
                    mBackCheckBtn.setBackground(getContext().getResources().getDrawable(R.drawable.ic_custom_back_arrow_black_24dp));
                    mBackCheckBtn.setOnClickListener(v -> mPresenter.goBack());
                }
                adapter.getFilter().filter(s.toString());
            }
        });
        adapter.getFilter().filter("");
        mBackCheckBtn.setOnClickListener(v -> mPresenter.goBack());
    }

    public TagsAdapter getAdapter() {
        return mTagsAdapter;
    }

    public void setTextSearchViewByQueryString(String s) {
        mSearchView.setText(s);
    }
}
