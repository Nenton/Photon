package com.nenton.photon.ui.screens.search_filters.filters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IFilterView;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.ViewHelper;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by serge on 05.06.2017.
 */

public class FilterView extends AbstractView<FilterScreen.FilterPresenter> implements IFilterView {

    @BindViews({R.id.red_cb, R.id.orange_cb, R.id.yellow_cb, R.id.green_cb, R.id.blue_light_cb, R.id.blue_cb, R.id.purple_cb, R.id.brown_cb, R.id.black_cb, R.id.white_cb,})
    List<CheckBox> mNuances;

    @BindView(R.id.radio_group_dish)
    RadioGroup mDish;
    @BindView(R.id.radio_group_decor)
    RadioGroup mDecor;
    @BindView(R.id.radio_group_temperature)
    RadioGroup mTemperature;
    @BindView(R.id.radio_group_light)
    RadioGroup mLight;
    @BindView(R.id.radio_group_dir)
    RadioGroup mDir;
    @BindView(R.id.radio_group_light_source)
    RadioGroup mLightSource;
    @BindView(R.id.search_filters_btn)
    Button mFilterBtn;
    @BindView(R.id.reload_filters_btn)
    Button mReloadBtn;
    @BindView(R.id.btn_wrap_filters)
    LinearLayout mWrapBtn;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @OnClick(R.id.reload_filters_btn)
    void clickOnReload() {
        reloadQuery();
        mPresenter.changeState(mPresenter.STATE_CHECK);
    }

    @OnClick({R.id.red_cb, R.id.orange_cb, R.id.yellow_cb, R.id.green_cb, R.id.blue_light_cb, R.id.blue_cb, R.id.purple_cb, R.id.brown_cb, R.id.black_cb, R.id.white_cb,})
    void clickOnCheckbox() {
        for (CheckBox cb : mNuances) {
            if (cb.isChecked()) {
                mPresenter.changeState(mPresenter.STATE_RELOAD);
                return;
            }
        }
    }

    @OnClick(R.id.search_filters_btn)
    void goSearchOnFilters() {
        List<String> mNuancesString = new ArrayList<>();
        for (CheckBox checkBox : mNuances) {
            if (checkBox.isChecked()) {
                mNuancesString.add(((String) checkBox.getTag()));
            }
        }
        mPresenter.getSearchFilterQuery().setNuances(mNuancesString);
        mPresenter.clickOnSearch();
    }

    private void reloadQuery() {
        mDish.check(RadioGroup.NO_ID);
        mDecor.check(RadioGroup.NO_ID);
        mTemperature.check(RadioGroup.NO_ID);
        mLight.check(RadioGroup.NO_ID);
        mDir.check(RadioGroup.NO_ID);
        mLightSource.check(RadioGroup.NO_ID);
        for (CheckBox box : mNuances) {
            box.setChecked(false);
        }
        mPresenter.reloadFilters();
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<FilterScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public void initView(SearchFilterQuery query) {
        initRadioGroup();
        if (query.getDish() != null)
            ((RadioButton) mDish.findViewWithTag(query.getDish())).setChecked(true);
        if (query.getDecor() != null)
            ((RadioButton) mDecor.findViewWithTag(query.getDecor())).setChecked(true);
        if (query.getTemperature() != null)
            ((RadioButton) mTemperature.findViewWithTag(query.getTemperature())).setChecked(true);
        if (query.getLight() != null)
            ((RadioButton) mLight.findViewWithTag(query.getLight())).setChecked(true);
        if (query.getLightDirection() != null)
            ((RadioButton) mDir.findViewWithTag(query.getLightDirection())).setChecked(true);
        if (query.getLightSource() != null)
            ((RadioButton) mLightSource.findViewWithTag(query.getLightSource())).setChecked(true);
        if (query.getNuances() != null) {
            for (String s : query.getNuances()) {
                ((CheckBox) findViewWithTag(s)).setChecked(true);
            }
        }
        mPresenter.changeState(mPresenter.STATE_RELOAD);
    }

    private void initRadioGroup() {
        mDish.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID)
                mPresenter.getSearchFilterQuery().setDish((String) findViewById(checkedId).getTag());
            mPresenter.changeState(mPresenter.STATE_RELOAD);
        });

        mDecor.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID)
                mPresenter.getSearchFilterQuery().setDecor((String) findViewById(checkedId).getTag());
            mPresenter.changeState(mPresenter.STATE_RELOAD);
        });

        mTemperature.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID)
                mPresenter.getSearchFilterQuery().setTemperature((String) findViewById(checkedId).getTag());
            mPresenter.changeState(mPresenter.STATE_RELOAD);
        });

        mLight.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID)
                mPresenter.getSearchFilterQuery().setLight((String) findViewById(checkedId).getTag());
            mPresenter.changeState(mPresenter.STATE_RELOAD);
        });

        mDir.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID)
                mPresenter.getSearchFilterQuery().setLightDirection((String) findViewById(checkedId).getTag());
            mPresenter.changeState(mPresenter.STATE_RELOAD);
        });

        mLightSource.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID)
                mPresenter.getSearchFilterQuery().setLightSource((String) findViewById(checkedId).getTag());
            mPresenter.changeState(mPresenter.STATE_RELOAD);
        });
    }

    @Override
    public void initView() {
        initRadioGroup();
    }

    public void showReloadBtn() {
        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeBounds())
                .addTransition(new Fade())
                .setDuration(300)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        TransitionManager.beginDelayedTransition(mWrapBtn, set);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.rightMargin = ((int) (ViewHelper.getDensity(getContext()) * 8));
        params.topMargin = ((int) (ViewHelper.getDensity(getContext()) * 16));
        params.bottomMargin = ((int) (ViewHelper.getDensity(getContext()) * 24));

        mFilterBtn.setLayoutParams(params);
        mReloadBtn.setAlpha(1f);
        mReloadBtn.setVisibility(VISIBLE);
    }

    public void hideReloadBtn() {

        ObjectAnimator animator = ObjectAnimator.ofFloat(mReloadBtn, "alpha", 1f, 0f);
        animator.setDuration(300);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mReloadBtn.setVisibility(GONE);

                TransitionSet set = new TransitionSet();
                set.addTransition(new ChangeBounds())
                        .setDuration(300)
                        .setInterpolator(new FastOutSlowInInterpolator());

                TransitionManager.beginDelayedTransition(mWrapBtn, set);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = ((int) (ViewHelper.getDensity(getContext()) * 16));
                params.bottomMargin = ((int) (ViewHelper.getDensity(getContext()) * 24));
                mFilterBtn.setLayoutParams(params);
            }
        });
        animator.start();
    }
}
