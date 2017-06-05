package com.nenton.photon.ui.screens.search_filters.filters;

import android.content.Context;
import android.util.AttributeSet;

import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

/**
 * Created by serge on 05.06.2017.
 */

public class FilterView extends AbstractView<FilterScreen.FilterPresenter> {

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<FilterScreen.Component>getDaggerComponent(context).inject(this);
    }
}
