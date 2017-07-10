package com.nenton.photon.ui.screens.search_filters.search;

import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.android.flexbox.FlexboxLayout;
import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.ISearchView;
import com.nenton.photon.utils.TextWatcherEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge on 05.06.2017.
 */

public class SearchTitleView extends AbstractView<SearchScreen.SearchPresenter> implements ISearchView {

    @BindView(R.id.search_rv)
    RecyclerView mSearchRV;
//    @BindView(R.id.tags_RV)
//    RecyclerView mRecyclerViewTags;
    @BindView(R.id.search_view)
    EditText mSearchView;
    @BindView(R.id.reboot_settings_search)
    ImageButton mRebootBtn;
    @BindView(R.id.back_and_check)
    ImageButton mBackCheckBtn;
    @BindView(R.id.flexbox_search)
    FlexboxLayout mFlexboxLayout;

    @OnClick(R.id.reboot_settings_search)
    public void clickSearch() {
        mSearchView.setText("");
    }

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

    public void addViewFlex(String s){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tag, mFlexboxLayout, false);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.tag_TV);
        checkBox.setText(s);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mPresenter.addString(checkBox.getText().toString());
            } else {
                mPresenter.removeString(checkBox.getText().toString());
            }
        });
        mFlexboxLayout.addView(view);
    }

    @Override
    public void initView(List<String> strings) {

        adapter.addStrings(strings);

        mSearchRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchRV.setAdapter(adapter);

        mSearchView.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    mBackCheckBtn.setBackground(getContext().getResources().getDrawable(R.drawable.ic_check_black_24dp));
                    mBackCheckBtn.setOnClickListener(v -> {
                        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
                        mPresenter.clickOnSearch(mSearchView.getText());
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

    @Override
    public void setTextSearchViewByQueryString(String s) {
        mSearchView.setText(s);
    }
}
