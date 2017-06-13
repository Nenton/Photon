package com.nenton.photon.ui.screens.search_filters.filters;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

import butterknife.BindView;

/**
 * Created by serge on 05.06.2017.
 */

public class FilterView extends AbstractView<FilterScreen.FilterPresenter> {

    @BindView(R.id.ic_meat)
    RadioButton mMeat;
    @BindView(R.id.ic_fish)
    RadioButton mFish;

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

    public void initView() {
    }
}
