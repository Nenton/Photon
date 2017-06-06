package com.nenton.photon.ui.screens.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.AlbumDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.utils.PhotoTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    @Inject
    Picasso picasso;

    private List<AlbumRealm> mAlbums = new ArrayList<>();
    private Context context;

    public void addAlbum(AlbumRealm album){
        mAlbums.add(album);
        notifyDataSetChanged();
    }

    @Override
    public AccountAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_photo, parent, false);
        return new AccountViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AccountAdapter.AccountViewHolder holder, int position) {
        AlbumRealm album = mAlbums.get(position);

        holder.mNameAlbum.setText(album.getTitle());
        holder.mCountPhoto.setText(album.getPhotocards().size());
        holder.mFavIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_custom_favorites_white_24dp));
        holder.mSeaIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_custom_views_white_24dp));
        holder.mFavCount.setText(String.valueOf(album.getFavorits()));
        holder.mSeaCount.setText(String.valueOf(album.getViews()));

        picasso.load(album.getPreview())
                .fit()
                .centerCrop()
                .transform(new PhotoTransform())
                .into(holder.mPhoto);
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.photo_card_account_IV)
        ImageView mPhoto;
        @BindView(R.id.album_name_TV)
        TextView mNameAlbum;
        @BindView(R.id.album_photos_count_TV)
        TextView mCountPhoto;
        @BindView(R.id.fav_photo_IV)
        ImageView mFavIcon;
        @BindView(R.id.sea_photo_IV)
        ImageView mSeaIcon;
        @BindView(R.id.fav_photo_TV)
        TextView mFavCount;
        @BindView(R.id.sea_photo_TV)
        TextView mSeaCount;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
