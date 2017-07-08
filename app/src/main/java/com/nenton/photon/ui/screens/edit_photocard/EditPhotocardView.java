package com.nenton.photon.ui.screens.edit_photocard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IEditPhotocardView;
import com.nenton.photon.ui.screens.add_photocard.AddPhotocardSelectAlbumAdapter;
import com.nenton.photon.ui.screens.add_photocard.AddPhotocardSelectTagsAdapter;
import com.nenton.photon.ui.screens.add_photocard.AddPhotocardSuggestionTagsAdapter;
import com.nenton.photon.utils.TextWatcherEditText;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by serge on 07.07.2017.
 */

public class EditPhotocardView extends AbstractView<EditPhotocardScreen.EditPhotocardPresenter> implements IEditPhotocardView {

    @BindView(R.id.edit_album_for_photocard_rv)
    RecyclerView mAlbums;
    @BindView(R.id.edit_tags_selected_rv)
    RecyclerView mSelectedTags;
    @BindView(R.id.edit_available_tags)
    RecyclerView mAvailableTags;

    @BindView(R.id.edit_tag_et)
    EditText mAddTags;
    @BindView(R.id.edit_name_photocard)
    EditText mNamePhotocard;
    @BindView(R.id.cancel_name_tag)
    ImageButton mCancelTag;
    @BindView(R.id.check_edit_tag)
    ImageButton mCheckTag;

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


    @Inject
    EditPhotocardSelectTagsAdapter mTagsSelectedAdapter;

    private EditPhotocardSelectAlbumAdapter mAdapter = new EditPhotocardSelectAlbumAdapter();
    private EditPhotocardSuggestionTagsAdapter mTagsSuggestionAdapter = new EditPhotocardSuggestionTagsAdapter();

    public EditPhotocardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public EditPhotocardSuggestionTagsAdapter getTagsSuggestionAdapter() {
        return mTagsSuggestionAdapter;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<EditPhotocardScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView() {
        mAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mAlbums.setAdapter(mAdapter);
        mSelectedTags.setLayoutManager(new WordsLayoutManager(getContext()));
        mSelectedTags.setAdapter(mTagsSelectedAdapter);
        mAvailableTags.setLayoutManager(new LinearLayoutManager(getContext()));
        mAvailableTags.setAdapter(mTagsSuggestionAdapter);
    }

    @Override
    public void showView(PhotocardRealm photocardRealm) {
        mNamePhotocard.setText(photocardRealm.getTitle());
        ((RadioButton) findViewWithTag(photocardRealm.getFilters().getDish())).setChecked(true);
        ((RadioButton) findViewWithTag(photocardRealm.getFilters().getLightSource())).setChecked(true);
        ((RadioButton) findViewWithTag(photocardRealm.getFilters().getLight())).setChecked(true);
        ((RadioButton) findViewWithTag(photocardRealm.getFilters().getLightDirection())).setChecked(true);
        ((RadioButton) findViewWithTag(photocardRealm.getFilters().getTemperature())).setChecked(true);
        ((RadioButton) findViewWithTag(photocardRealm.getFilters().getDecor())).setChecked(true);

        String s = photocardRealm.getFilters().getNuances();
        for (CheckBox cb : mNuances) {
            if (s.contains(((String) cb.getTag()))) {
                cb.setChecked(true);
            }
        }

        for (StringRealm string : photocardRealm.getTags()) {
            mTagsSelectedAdapter.addTag(string.getString());
        }
    }

    @Override
    @Nullable
    public FiltersDto getFilters() {
        String dish = mDish.getCheckedRadioButtonId() != -1 ? (String) findViewById(mDish.getCheckedRadioButtonId()).getTag() : "";
        String decor = mDecor.getCheckedRadioButtonId() != -1 ? (String) findViewById(mDecor.getCheckedRadioButtonId()).getTag() : "";
        String temperature = mTemperature.getCheckedRadioButtonId() != -1 ? (String) findViewById(mTemperature.getCheckedRadioButtonId()).getTag() : "";
        String light = mLight.getCheckedRadioButtonId() != -1 ? (String) findViewById(mLight.getCheckedRadioButtonId()).getTag() : "";
        String dir = mDir.getCheckedRadioButtonId() != -1 ? (String) findViewById(mDir.getCheckedRadioButtonId()).getTag() : "";
        String lightSource = mLightSource.getCheckedRadioButtonId() != -1 ? (String) findViewById(mLightSource.getCheckedRadioButtonId()).getTag() : "";

        String nuances = "";
        for (CheckBox checkBox : mNuances) {
            if (checkBox.isChecked()) {
                String s = (String) checkBox.getTag();
                if (nuances.isEmpty()) {
                    nuances += s;
                } else {
                    nuances += ", " + s;
                }
            }
        }
        if (dish.isEmpty() || decor.isEmpty() || temperature.isEmpty() || light.isEmpty() || dir.isEmpty() || lightSource.isEmpty() || nuances.isEmpty()) {
            return null;
        } else {
            return new FiltersDto(dish, nuances, decor, temperature, light, dir, lightSource);
        }
    }

    @Override
    @Nullable
    public String getNamePhotocard() {
        if (mNamePhotocard.getText().toString().isEmpty()) {
            return null;
        } else {
            return mNamePhotocard.getText().toString();
        }
    }

    @Override
    @Nullable
    public List<String> getTags() {
        if (mTagsSelectedAdapter.getStrings().size() == 0) {
            return null;
        } else {
            return mTagsSelectedAdapter.getStrings();
        }
    }

    @Override
    @Nullable
    public String getIdAlbum() {
        if (((CheckBox) mAlbums.getLayoutManager().findViewByPosition(mAdapter.getPositionOnSelectItem()).findViewById(R.id.check_album_add)).isChecked()) {
            return mAdapter.getIdAlbum();
        } else {
            return null;
        }
    }

    @Override
    public void addTag(String string) {
        mTagsSelectedAdapter.addTag(string);
        mTagsSuggestionAdapter.deleteString(string);
        mTagsSuggestionAdapter.getFilter().filter(mAddTags.getText().toString());
    }

    public void checkedCurrentAlbum(int positionOnSelectItem) {
        ((CheckBox) mAlbums.getLayoutManager().findViewByPosition(positionOnSelectItem).findViewById(R.id.check_album_add)).setChecked(false);
    }

    @Override
    public void initAlbums(UserRealm userRealm) {
        for (AlbumRealm albumRealm : userRealm.getAlbums()) {
            mAdapter.addAlbum(albumRealm);
        }
        mTagsSuggestionAdapter.getFilter().filter("");
        mAddTags.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    mCheckTag.setVisibility(GONE);
                } else {
                    mCheckTag.setVisibility(VISIBLE);
                }
                mTagsSuggestionAdapter.getFilter().filter(s.toString());
            }
        });
        mCheckTag.setVisibility(GONE);
    }

    public void removeTag(String string) {
        mTagsSuggestionAdapter.addTag(string);
    }

    //region ========================= Events =========================

    @OnClick(R.id.save_edit_photocard_btn)
    void clickOnSave() {
        mPresenter.savePhotocard();
    }

    @OnClick(R.id.cancel_edit_photocard_btn)
    void clickOnCancel() {
        mPresenter.cancelEdit();
    }

    @OnClick(R.id.check_edit_tag)
    public void checkTag() {
        mTagsSelectedAdapter.addTag("#" + mAddTags.getText().toString());
        mAddTags.setText("");
    }

    @OnClick(R.id.cancel_name_tag)
    public void cancelTag() {
        mAddTags.setText("");
    }

    @OnClick(R.id.cancel_name)
    public void cancelName() {
        mNamePhotocard.setText("");
    }

    //endregion
}