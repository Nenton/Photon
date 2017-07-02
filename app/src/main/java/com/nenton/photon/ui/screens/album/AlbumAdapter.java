package com.nenton.photon.ui.screens.album;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.custom_views.ImageViewSquare;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AccountViewHolder> {

    @Inject
    Picasso picasso;

    @Inject
    AlbumScreen.AlbumPresenter mPresenter;

    private List<PhotocardRealm> mAlbums = new ArrayList<>();

    public void addAlbum(PhotocardRealm album) {
        mAlbums.add(album);
        notifyDataSetChanged();
    }

    @Override
    public AlbumAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_album, parent, false);
        return new AccountViewHolder(convertView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<AlbumScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.AccountViewHolder holder, int position) {
        PhotocardRealm mPhoto = mAlbums.get(position);

        picasso.load(mPhoto.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE)
                .resize(250, 250)
                .centerCrop()
                .into(holder.mPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        picasso.load(mPhoto.getPhoto())
                                .resize(250, 250)
                                .centerCrop()
                                .into(holder.mPhoto);
                    }
                });

        holder.mPhoto.setOnClickListener(v -> {
            mPresenter.clickOnPhotocard(mPhoto);
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_card_album_IV)
        ImageViewSquare mPhoto;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
