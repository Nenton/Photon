package com.nenton.photon.ui.screens.author;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IAuthorView;
import com.nenton.photon.ui.screens.account.AccountAdapter;
import com.nenton.photon.ui.screens.account.AccountScreen;
import com.nenton.photon.utils.AvatarTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AuthorView extends AbstractView<AuthorScreen.AuthorPresenter> implements IAuthorView {

    @BindView(R.id.author_photocard_RV)
    RecyclerView mRecycleView;

    @BindView(R.id.author_avatar_IV)
    ImageView mAvatar;
    @BindView(R.id.author_login_TV)
    TextView mLogin;
    @BindView(R.id.author_albums_count)
    TextView mAlbumCount;
    @BindView(R.id.author_photocard_count)
    TextView mPhotocardCount;

    @Inject
    Picasso mPicasso;

    private AuthorAdapter mAuthorAdapter = new AuthorAdapter();

    public AuthorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AuthorScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public void initView(UserRealm userRealm) {

        mPicasso.with(getContext())
                .load(userRealm.getAvatar())
                .error(R.drawable.ic_account_black_24dp)
                .placeholder(R.drawable.ic_account_black_24dp)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(200, 200)
                .centerCrop()
                .transform(new AvatarTransform())
                .into(mAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.with(getContext())
                                .load(userRealm.getAvatar())
                                .error(R.drawable.ic_account_black_24dp)
                                .placeholder(R.drawable.ic_account_black_24dp)
                                .resize(200, 200)
                                .centerCrop()
                                .transform(new AvatarTransform())
                                .into(mAvatar);
                    }
                });

        mLogin.setText(userRealm.getLogin());

        mAlbumCount.setText(String.valueOf(userRealm.getAlbums().size()));

        int countPhotocard = 0;

        for (AlbumRealm albumRealm : userRealm.getAlbums()) {
            countPhotocard += albumRealm.getPhotocards().size();
            mAuthorAdapter.addAlbum(albumRealm);
        }

        mPhotocardCount.setText(String.valueOf(countPhotocard));

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAuthorAdapter);
    }
}
