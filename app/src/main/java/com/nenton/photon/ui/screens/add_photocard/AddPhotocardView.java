package com.nenton.photon.ui.screens.add_photocard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IAddPhotocardView;
import com.nenton.photon.ui.dialogs.DialogsAlbum;
import com.nenton.photon.utils.TextWatcherEditText;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by serge on 18.06.2017.
 */

public class AddPhotocardView extends AbstractView<AddPhotocardScreen.AddPhotocardPresenter> implements IAddPhotocardView {

    @BindView(R.id.add_album_for_photocard_rv)
    RecyclerView mAlbums;
    @BindView(R.id.available_tags)
    RecyclerView mAvailableTags;
    @BindView(R.id.add_property_photo_panel)
    LinearLayout mPropertyPanel;
    @BindView(R.id.add_photo_panel)
    LinearLayout mAddPhotoPanel;
    @BindView(R.id.show_not_album)
    LinearLayout mNotAlbum;
    @BindView(R.id.show_not_is_sign)
    LinearLayout mNotIsSign;

    @BindView(R.id.flexbox_tag_selected)
    FlexboxLayout mTagsSelected;

    @BindView(R.id.add_tag_et)
    EditText mAddTags;
    @BindView(R.id.add_name_photocard)
    EditText mNamePhotocard;
    @BindView(R.id.cancel_name_tag)
    ImageButton mCancelTag;
    @BindView(R.id.check_add_tag)
    ImageButton mCheckTag;
    @BindView(R.id.add_album_from_add_photocard)
    Button mButton;

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

    private AddPhotocardSelectAlbumAdapter mAdapter = new AddPhotocardSelectAlbumAdapter();
    private AddPhotocardSuggestionTagsAdapter mTagsSuggestionAdapter = new AddPhotocardSuggestionTagsAdapter();

    public AddPhotocardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addViewTagSelected(final String s) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tag_add_photocard, mTagsSelected, false);
        TextView text = (TextView) view.findViewById(R.id.tag_add_photocard_TV);
        text.setText(s);
        view.findViewById(R.id.cancel_tag_ib).setOnClickListener(v -> {
            mTagsSelected.removeView(view);
            mTagsSuggestionAdapter.addTag(text.getText().toString());
            mTagsSuggestionAdapter.getFilter().filter(mAddTags.getText().toString());
            mPresenter.removeString(s);
        });
        view.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha));
        mTagsSelected.addView(view);
    }

    @Override
    public void showPhotoPanel() {

        TransitionSet set = new TransitionSet();
        set.addTransition(new Fade())
                .setDuration(300)
                .setInterpolator(new FastOutSlowInInterpolator());
        TransitionManager.beginDelayedTransition(this, set);

        mPropertyPanel.setVisibility(GONE);
        mNotIsSign.setVisibility(GONE);
        mNotAlbum.setVisibility(GONE);
        mAddPhotoPanel.setVisibility(VISIBLE);
    }

    @Override
    public void showPropertyPanel() {

        TransitionSet set = new TransitionSet();
        set.addTransition(new Fade())
                .setDuration(300)
                .setInterpolator(new FastOutSlowInInterpolator());
        TransitionManager.beginDelayedTransition(this, set);

        mAddPhotoPanel.setVisibility(GONE);
        mNotIsSign.setVisibility(GONE);
        mNotAlbum.setVisibility(GONE);
        mPropertyPanel.setVisibility(VISIBLE);
    }

    public AddPhotocardSelectAlbumAdapter getAdapter() {
        return mAdapter;
    }

    public AddPhotocardSuggestionTagsAdapter getTagsSuggestionAdapter() {
        return mTagsSuggestionAdapter;
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AddPhotocardScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView() {
        mAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mAlbums.setAdapter(mAdapter);
        mAlbums.setNestedScrollingEnabled(false);
        mAvailableTags.setLayoutManager(new LinearLayoutManager(getContext()));
        mAvailableTags.setAdapter(mTagsSuggestionAdapter);
    }

    @Override
    public void showView(UserRealm userRealm) {
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
    public String getIdAlbum() {
        if (((CheckBox) mAlbums.getLayoutManager().findViewByPosition(mAdapter.getPositionOnSelectItem()).findViewById(R.id.check_album_add)).isChecked()) {
            return mAdapter.getIdAlbum();
        } else {
            return null;
        }
    }

    public void checkedCurrentAlbum(int positionOnSelectItem) {
        ((CheckBox) mAlbums.getLayoutManager().findViewByPosition(positionOnSelectItem).findViewById(R.id.check_album_add)).setChecked(false);
    }

    @Override
    public void addTag(String string) {
        addViewTagSelected(string);
        mPresenter.addString(string);
        mTagsSuggestionAdapter.deleteString(string);
        mTagsSuggestionAdapter.getFilter().filter(mAddTags.getText().toString());
    }

    public void goneAddAlbum() {
        mButton.setVisibility(GONE);
    }

    public void showAddAlbum() {
        mNotIsSign.setVisibility(GONE);
        mNotAlbum.setVisibility(VISIBLE);
    }

    //region ========================= Events =========================

    @OnClick(R.id.add_photo_from_gallery)
    public void clickOnAddPhotocard() {
        mPresenter.chooseGallery();
    }

    @OnClick(R.id.save_photocard_btn)
    public void clickOnSavePhotocard() {
        mPresenter.savePhotocard();
    }

    @OnClick(R.id.cancel_add_photocard_btn)
    public void cancelCreate() {
        mPresenter.cancelCreate();
    }

    @OnClick(R.id.check_add_tag)
    public void checkTag() {
        String s = mAddTags.getText().toString();
        if (!s.contains("#")) {
            s = "#" + s;
        }
        addViewTagSelected(s);
        mPresenter.addString(s);
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

    @OnClick(R.id.add_album_from_add_photocard)
    public void addAlbum() {
        DialogsAlbum.createDialogAddAlbum(getContext(), (name, description) -> {
            mPresenter.addAlbum(name, description);
        }).show();
    }

    @OnClick(R.id.create_album)
    public void addAlbumFrom() {
        DialogsAlbum.createDialogAddAlbum(getContext(), (name, description) -> {
            mPresenter.addAlbumFrom(name, description);
        }).show();
    }

    @OnClick(R.id.go_account_btn)
    public void goAccount() {
        mPresenter.goAccount();
    }

    public void removeTag(String string) {
        mTagsSuggestionAdapter.addTag(string);
    }

    //endregion
}