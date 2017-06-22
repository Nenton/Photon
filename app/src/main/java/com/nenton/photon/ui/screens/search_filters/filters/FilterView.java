package com.nenton.photon.ui.screens.search_filters.filters;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.utils.SearchFilterQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by serge on 05.06.2017.
 */

public class FilterView extends AbstractView<FilterScreen.FilterPresenter> {

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
    Button mButton;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @OnClick(R.id.search_filters_btn)
    void goSearchOnFilters(){
        if (mButton.getText().equals("Найти")){
            List<String> mNuancesString = new ArrayList<>();
            for (CheckBox checkBox : mNuances) {
                if (checkBox.isChecked()){
                    mNuancesString.add(((String) checkBox.getTag()));
                }
            }
            mPresenter.getSearchFilterQuery().setNuances(mNuancesString);
            mPresenter.clickOnSearch();
        } else if (mButton.getText().equals("Сбросить фильтры")){
            reloadQuery();
            mButton.setText("Найти");
        }

    }

    private void reloadQuery() {
        mDish.check(-1);
        mDecor.check(-1);
        mTemperature.check(-1);
        mLight.check(-1);
        mDir.check(-1);
        mLightSource.check(-1);
        for (CheckBox box: mNuances) {
            box.setChecked(false);
        }
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<FilterScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView(SearchFilterQuery query) {
        mButton.setText("Сбросить фильтры");
        initRadioGroup();
        if (query.getDish() != null)((RadioButton) mDish.findViewWithTag(query.getDish())).setChecked(true);
        if (query.getDecor() != null)((RadioButton) mDecor.findViewWithTag(query.getDecor())).setChecked(true);
        if (query.getTemperature() != null)((RadioButton) mTemperature.findViewWithTag(query.getTemperature())).setChecked(true);
        if (query.getLight() != null)((RadioButton) mLight.findViewWithTag(query.getLight())).setChecked(true);
        if (query.getLightDirection() != null)((RadioButton) mDir.findViewWithTag(query.getLightDirection())).setChecked(true);
        if (query.getLightSource() != null)((RadioButton) mLightSource.findViewWithTag(query.getLightSource())).setChecked(true);
        if (query.getNuances() != null){
            for (String s : query.getNuances()) {
                ((CheckBox) findViewWithTag(s)).setChecked(true);
            }
        }
    }

    private void initRadioGroup(){
        mDish.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID) mPresenter.getSearchFilterQuery().setDish((String) findViewById(checkedId).getTag());
        });

        mDecor.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID) mPresenter.getSearchFilterQuery().setDecor((String) findViewById(checkedId).getTag());
        });

        mTemperature.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID) mPresenter.getSearchFilterQuery().setTemperature((String) findViewById(checkedId).getTag());
        });

        mLight.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID) mPresenter.getSearchFilterQuery().setLight((String) findViewById(checkedId).getTag());
        });

        mDir.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID) mPresenter.getSearchFilterQuery().setLightDirection((String) findViewById(checkedId).getTag());
        });

        mLightSource.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != RadioGroup.NO_ID) mPresenter.getSearchFilterQuery().setLightSource((String) findViewById(checkedId).getTag());
        });
    }

    public void initView() {
        mButton.setText("Найти");
        initRadioGroup();
    }
}
