package com.nenton.photon.ui.screens.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.screens.album.AlbumScreen;
import com.nenton.photon.utils.AlbumTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    @Inject
    Picasso picasso;

    private List<AlbumRealm> mAlbums = new ArrayList<>();
    private Context context;

    public void addAlbum(AlbumRealm album) {
        mAlbums.add(album);
        notifyDataSetChanged();
    }

    public void reloadAdapter() {
        mAlbums.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<AccountScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AccountAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_album, parent, false);
        return new AccountViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AccountAdapter.AccountViewHolder holder, int position) {
        AlbumRealm album = mAlbums.get(position);

        holder.mNameAlbum.setText(album.getTitle());
        holder.mCountPhoto.setText(String.valueOf(album.getPhotocards().size()));
        holder.mFavCount.setText(String.valueOf(album.getFavorits()));
        holder.mSeaCount.setText(String.valueOf(album.getViews()));

        picasso.with(context)
                .load(!album.getPhotocards().isEmpty() ? album.getPhotocards().get(0).getPhoto() : null)
                .placeholder(R.drawable.placeholder_album)
                .error(R.drawable.placeholder_album)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500, 500)
                .centerCrop()
                .transform(new AlbumTransform())
                .into(holder.mPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        picasso.with(context)
                                .load(!album.getPhotocards().isEmpty() ? album.getPhotocards().get(0).getPhoto() : null)
                                .placeholder(R.drawable.placeholder_album)
                                .error(R.drawable.placeholder_album)
                                .resize(500, 500)
                                .centerCrop()
                                .transform(new AlbumTransform())
                                .into(holder.mPhoto);
                    }
                });

        holder.mView.setOnClickListener(v -> {
            Flow.get(context).set(new AlbumScreen(album));
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_album)
        View mView;
        @BindView(R.id.photo_card_account_IV)
        ImageView mPhoto;
        @BindView(R.id.album_name_TV)
        TextView mNameAlbum;
        @BindView(R.id.album_photos_count_TV)
        TextView mCountPhoto;
        @BindView(R.id.fav_photo_TV)
        TextView mFavCount;
        @BindView(R.id.sea_photo_TV)
        TextView mSeaCount;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
