package com.nenton.photon.ui.screens.album;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.ui.screens.account.AccountAdapter;
import com.nenton.photon.ui.screens.account.AccountScreen;

import butterknife.BindView;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AlbumView extends AbstractView<AlbumScreen.AlbumPresenter>{

    @BindView(R.id.album_RV)
    RecyclerView mRecycleView;

    @BindView(R.id.album_screen_name_TV)
    TextView mNameAlbum;
    @BindView(R.id.album_screen_count_TV)
    TextView mCountPhoto;
    @BindView(R.id.album_description)
    TextView mSeaCount;

    private AccountAdapter mAccountAdapter = new AccountAdapter();

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AlbumScreen.Component>getDaggerComponent(context).inject(this);

    }

    public void initView(AlbumRealm mAlbum) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAccountAdapter);
        // TODO: 06.06.2017 добавить фотографий в аккаунт
    }
}
