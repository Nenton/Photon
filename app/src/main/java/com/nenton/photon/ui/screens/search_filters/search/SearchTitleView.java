package com.nenton.photon.ui.screens.search_filters.search;

import android.content.Context;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.google.android.flexbox.FlexboxLayout;
import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.ISearchView;
import com.nenton.photon.utils.TextWatcherEditText;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge on 05.06.2017.
 */

public class SearchTitleView extends AbstractView<SearchScreen.SearchPresenter> implements ISearchView {

    @BindView(R.id.search_rv)
    RecyclerView mSearchRV;
    @BindView(R.id.search_view)
    EditText mSearchView;
    @BindView(R.id.reboot_settings_search)
    ImageButton mRebootBtn;
    @BindView(R.id.back_and_check)
    ImageButton mBackCheckBtn;
    @BindView(R.id.flexbox_search)
    FlexboxLayout mFlexboxLayout;
    @BindView(R.id.nested_scroll_tags_search)
    NestedScrollView mScrollView;
    @BindView(R.id.search_wrap)
    LinearLayout mAnimWrap;
    @BindView(R.id.suggest_search_wrap)
    LinearLayout mSuggestWrap;

    private int mCountTag = 0;

    private boolean mCheckQuery = false;

    private final int STATE_HIDE_SEARCH = 300;
    private final int STATE_SHOW_SEARCH = 400;

    private int mStateSearch = STATE_HIDE_SEARCH;

    public float y;
    public float y_;
    public final ChangeBounds mChangeBounds;
    public final Fade mFade;
    public boolean hiden = true;
    final boolean[] state = {false};

    @OnClick(R.id.reboot_settings_search)
    public void clickSearch() {
        mSearchView.setText("");
    }

    private SearchAdapter adapter = new SearchAdapter();

    public SearchTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mChangeBounds = new ChangeBounds();
        mFade = new Fade();
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public void setCountTag(int countTag) {
        mCountTag = countTag;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<SearchScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void addViewFlex(String s, boolean checked) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tag, mFlexboxLayout, false);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.tag_TV);
        checkBox.setChecked(checked);
        checkBox.setText(s);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mPresenter.addString(checkBox.getText().toString());
                mCountTag++;
                checkState();
            } else {
                mPresenter.removeString(checkBox.getText().toString());
                mCountTag--;
                checkState();
            }
        });
        mFlexboxLayout.addView(view);
    }

    public void checkState() {
        int state = mCheckQuery || mCountTag > 0 ? STATE_SHOW_SEARCH : STATE_HIDE_SEARCH;
        if (!(state == mStateSearch)) {
            mStateSearch = state;
            changeState();
        }
    }

    public void changeState() {
        if (mStateSearch == STATE_SHOW_SEARCH) {
            mBackCheckBtn.setBackground(getContext().getResources().getDrawable(R.drawable.ic_check_black_24dp));
            mBackCheckBtn.setOnClickListener(v -> {
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                mPresenter.clickOnSearch(mSearchView.getText());
            });
        } else {
            mBackCheckBtn.setBackground(getContext().getResources().getDrawable(R.drawable.ic_custom_back_arrow_black_24dp));
            mBackCheckBtn.setOnClickListener(v -> mPresenter.goBack());
        }
    }

    private void animateShowSuggest() {
        TransitionSet setShow = new TransitionSet();
        setShow.addTransition(mChangeBounds)
                .addTransition(mFade)
                .setDuration(300)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setOrdering(TransitionSet.ORDERING_TOGETHER);

        setShow.addListener(new Transition.TransitionListenerAdapter() {
            @Override
            public void onTransitionStart(Transition transition) {
                super.onTransitionStart(transition);
                hiden = false;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                hiden = true;
                state[0] = false;
            }
        });
        TransitionManager.beginDelayedTransition(mAnimWrap, setShow);
        mSearchRV.setVisibility(VISIBLE);
    }

    private void animateHideSuggest() {
        TransitionSet setHide = new TransitionSet();
        Transition fade = new Fade();
        fade.addTarget(mSuggestWrap.getChildAt(0));
        setHide.addTransition(fade)
                .addTransition(mChangeBounds)
                .addTransition(mFade)
                .setDuration(300)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setOrdering(TransitionSet.ORDERING_TOGETHER);
        setHide.addListener(new Transition.TransitionListenerAdapter() {
            @Override
            public void onTransitionStart(Transition transition) {
                super.onTransitionStart(transition);
                hiden = false;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                hiden = true;
                state[0] = false;
                y = y_;
            }
        });
        TransitionManager.beginDelayedTransition(mAnimWrap, setHide);
        mSearchRV.setVisibility(GONE);
    }

    @Override
    public void initView(List<String> strings) {
        mScrollView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                case MotionEvent.ACTION_MOVE:
                    y_ = event.getY();
                    if (!state[0]) {
                        y = event.getY();
                        state[0] = !state[0];
                    }
                    if (event.getY() - y > 10f && hiden) {
                        hiden = false;
                        animateShowSuggest();
                    } else if (event.getY() - y < -10f && hiden) {
                        hiden = false;
                        animateHideSuggest();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    state[0] = false;
                    break;
            }
            return super.onTouchEvent(event);
        });

        adapter.addStrings(strings);
        mSearchRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchRV.setAdapter(adapter);
        mSearchView.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    mCheckQuery = true;
                    checkState();
                } else {
                    mCheckQuery = false;
                    checkState();
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

    public void initSearchTitle(String title) {
        mSearchView.setText(title);
    }
}
