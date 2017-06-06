package com.nenton.photon.ui.screens.author;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
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

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AccountViewHolder> {

    @Inject
    Picasso picasso;

    private List<AlbumRealm> mAlbums = new ArrayList<>();
    private Context context;

    public void addAlbum(AlbumRealm album){
        mAlbums.add(album);
        notifyDataSetChanged();
    }

    @Override
    public AuthorAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_photo, parent, false);
        return new AccountViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AuthorAdapter.AccountViewHolder holder, int position) {
        AlbumRealm album = mAlbums.get(position);

        holder.mNameAlbum.setText(album.getTitle());
        holder.mCountPhoto.setText(album.getPhotocards().size());
        holder.mShareIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_custom_favorites_white_24dp));
        // TODO: 06.06.2017 другая иконка
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
        @BindView(R.id.share_photo_IV)
        ImageView mShareIcon;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
