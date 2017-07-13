package com.nenton.photon.ui.screens.author;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.utils.AlbumTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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
        holder.mFav.setText(String.valueOf(album.getFavorits()));
        holder.mSee.setText(String.valueOf(album.getViews()));

        if (album.getPhotocards().isEmpty()){
            holder.mCountsWrap.setVisibility(View.GONE);
        } else {
            holder.mCountsWrap.setVisibility(View.VISIBLE);
        }

        picasso.with(mContext)
                .load(!album.getPhotocards().isEmpty() ? album.getPhotocards().get(0).getPhoto() : null)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
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
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .resize(500, 500)
                                .centerCrop()
                                .transform(new AlbumTransform())
                                .into(holder.mPhoto);
                    }
                });

        holder.mPhoto.setOnClickListener(v -> {
            mPresenter.clickOnAlbum(album);
        });
        setAnimation(holder.itemView);
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        viewToAnimate.startAnimation(animation);
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
        @BindView(R.id.fav_photo_TV)
        TextView mFav;
        @BindView(R.id.sea_photo_TV)
        TextView mSee;
        @BindView(R.id.counts_author_wrap)
        LinearLayout mCountsWrap;

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
