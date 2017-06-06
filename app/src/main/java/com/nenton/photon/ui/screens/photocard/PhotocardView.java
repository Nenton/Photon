package com.nenton.photon.ui.screens.photocard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

import butterknife.BindView;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class PhotocardView extends AbstractView<PhotocardScreen.PhotocardPresenter> {
    private PhotocardAdapter mAdapter = new PhotocardAdapter();

    @BindView(R.id.photocard_RV)
    RecyclerView mRecyclerView;
    @BindView(R.id.album_count_TV)
    TextView mAlbumCount;
    @BindView(R.id.photocard_count_TV)
    TextView mPhotocardCount;
    @BindView(R.id.full_name_TV)
    TextView mFullName;
    @BindView(R.id.avatar_photocard_IV)
    ImageView mAvatar;

    public PhotocardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<PhotocardScreen.Component>getDaggerComponent(getContext()).inject(this);
    }

    public void initView() {
        mRecyclerView.setLayoutManager(new WordsLayoutManager(getContext()));
        mAdapter.addString("Привет");
        mAdapter.addString("Привет");
        mAdapter.addString("Привет");
        mAdapter.addString("Привет");
        mRecyclerView.setAdapter(mAdapter);
    }
}
