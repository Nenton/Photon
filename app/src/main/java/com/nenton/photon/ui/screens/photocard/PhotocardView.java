package com.nenton.photon.ui.screens.photocard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.utils.AvatarTransform;
import com.nenton.photon.utils.PhotoTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class PhotocardView extends AbstractView<PhotocardScreen.PhotocardPresenter> {
    private PhotocardAdapter mAdapter = new PhotocardAdapter();

    @BindView(R.id.photocard_RV)
    RecyclerView mRecyclerView;
    @BindView(R.id.photo_IV)
    ImageView mPhoto;
    @BindView(R.id.name_photocard)
    TextView mName;
    @BindView(R.id.album_count_TV)
    TextView mAlbumCount;
    @BindView(R.id.photocard_count_TV)
    TextView mPhotocardCount;
    @BindView(R.id.full_name_TV)
    TextView mFullName;
    @BindView(R.id.avatar_photocard_IV)
    ImageView mAvatar;

    @OnClick(R.id.avatar_photocard_IV)
    public void clickOnAuthor() {
        mPresenter.clickOnAuthor();
    }

    @Inject
    Picasso mPicasso;

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

    public void initView(PhotocardRealm photocardRealm, UserInfoDto infoDto) {
        mName.setText(photocardRealm.getTitle());

        mPicasso.load(infoDto.getAvatar())
                .fit()
                .centerCrop()
                .transform(new AvatarTransform())
                .into(mAvatar);

        mPicasso.load(photocardRealm.getPhoto())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500,500)
                .centerCrop()
                .transform(new PhotoTransform())

                .into(mPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.load(photocardRealm.getPhoto())
                                .resize(500,500)
                                .centerCrop()
                                .transform(new PhotoTransform())
                                .into(mPhoto);
                    }
                });

        mPicasso.load(infoDto.getAvatar())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(200,200)
                .centerCrop()
                .transform(new AvatarTransform())

                .into(mAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.load(infoDto.getAvatar())
                                .resize(200,200)
                                .centerCrop()
                                .transform(new AvatarTransform())
                                .into(mAvatar);
                    }
                });

        mFullName.setText(infoDto.getName() + " " + infoDto.getLogin());
        mAlbumCount.setText(String.valueOf(infoDto.getCountAlbum()));
        mPhotocardCount.setText(String.valueOf(infoDto.getCountPhoto()));

        mRecyclerView.setLayoutManager(new WordsLayoutManager(getContext()));
        for (StringRealm s : photocardRealm.getTags()) {
            mAdapter.addString(s);
        }
        mRecyclerView.setAdapter(mAdapter);
    }
}
