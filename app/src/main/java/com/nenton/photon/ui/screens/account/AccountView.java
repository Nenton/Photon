package com.nenton.photon.ui.screens.account;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AccountView extends AbstractView<AccountScreen.AccountPresenter>{

    @Inject
    Picasso mPicasso;

    @BindView(R.id.no_user_wrap)
    LinearLayout mNoUserWrap;
    @BindView(R.id.user_wrap)
    LinearLayout mUserWrap;

    @BindView(R.id.account_photocard_RV)
    RecyclerView mRecycleView;
    @BindView(R.id.account_avatar_IV)
    ImageView mAvatar;
    @BindView(R.id.account_login_TV)
    TextView mLogin;
    @BindView(R.id.account_name_TV)
    TextView mName;
    @BindView(R.id.account_albums_count)
    TextView mAlbumCount;
    @BindView(R.id.account_photocard_count)
    TextView mPhotocardCount;

    private AccountAdapter mAccountAdapter = new AccountAdapter();

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AccountScreen.Component>getDaggerComponent(context).inject(this);
    }


    public void showAuthState(UserRealm userRealm) {
        mUserWrap.setVisibility(VISIBLE);
        mNoUserWrap.setVisibility(GONE);

        if (userRealm.getAvatar() != null && !userRealm.getAvatar().isEmpty()){
            mPicasso.load(userRealm.getAvatar())
                    .fit()
                    .placeholder(R.color.black)
                    .into(mAvatar);
        } else {
            mPicasso.load(R.color.black)
                    .fit()
                    .into(mAvatar);
        }

        mLogin.setText(userRealm.getLogin());
        mName.setText(userRealm.getName());

        mAlbumCount.setText(String.valueOf(userRealm.getAlbums().size()));

        int countPhotocard = 0;

        for (AlbumRealm albumRealm : userRealm.getAlbums()) {
            countPhotocard += albumRealm.getPhotocards().size();
            mAccountAdapter.addAlbum(albumRealm);
        }

        mPhotocardCount.setText(String.valueOf(countPhotocard));

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAccountAdapter);
    }

    public void showUnAuthState() {
        mUserWrap.setVisibility(GONE);
        mNoUserWrap.setVisibility(VISIBLE);
    }
}
