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
import com.nenton.photon.di.DaggerService;
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

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AccountViewHolder> {

    @Inject
    Picasso picasso;
    @Inject
    AuthorScreen.AuthorPresenter mPresenter;

    private List<AlbumRealm> mAlbums = new ArrayList<>();
    private Context mContext;

    public void addAlbum(AlbumRealm album){
        mAlbums.add(album);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<AuthorScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AuthorAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author_album, parent, false);
        return new AccountViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AuthorAdapter.AccountViewHolder holder, int position) {
        AlbumRealm album = mAlbums.get(position);

        holder.mNameAlbum.setText(album.getTitle());
        holder.mCountPhoto.setText(String.valueOf(album.getPhotocards().size()));

        picasso.with(mContext)
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
                        picasso.with(mContext)
                                .load(!album.getPhotocards().isEmpty() ? album.getPhotocards().get(0).getPhoto() : null)
                                .placeholder(R.drawable.placeholder_album)
                                .error(R.drawable.placeholder_album)
                                .resize(500, 500)
                                .centerCrop()
                                .transform(new AlbumTransform())
                                .into(holder.mPhoto);
                    }
                });

        holder.mPhoto.setOnClickListener(v -> {
            mPresenter.clickOnAlbum(album);
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.album_author_item_IV)
        ImageView mPhoto;
        @BindView(R.id.album_author_name_TV)
        TextView mNameAlbum;
        @BindView(R.id.album_author_count_TV)
        TextView mCountPhoto;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
