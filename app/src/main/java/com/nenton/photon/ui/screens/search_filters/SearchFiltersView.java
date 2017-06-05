package com.nenton.photon.ui.screens.search_filters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

import butterknife.BindView;

/**
 * Created by serge on 05.06.2017.
 */

public class SearchFiltersView extends AbstractView<SearchFiltersScreen.SearchFiltersPresenter> {

    @BindView(R.id.search_filter_pager)
    ViewPager mViewPager;

    public SearchFiltersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<SearchFiltersScreen.Component>getDaggerComponent(context).inject(this);
    }

    public ViewPager getViewPager(){
        return mViewPager;
    }

    public void initView(){
        SearchFiltersAdapter adapter = new SearchFiltersAdapter();
        mViewPager.setAdapter(adapter);
    }
}
