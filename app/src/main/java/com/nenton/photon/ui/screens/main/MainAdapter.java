package com.nenton.photon.ui.screens.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.screens.photocard.PhotocardScreen;
import com.nenton.photon.utils.PhotoTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;

/**
 * Created by serge on 04.06.2017.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private final List<PhotocardRealm> mPhotocards = new ArrayList<>();
    private Context mContext;

    @Inject
    Picasso mPicasso;

    public MainAdapter() {
    }

    void addPhoto(PhotocardRealm photocard){
        mPhotocards.add(photocard);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mContext = recyclerView.getContext();
        DaggerService.<MainScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_main, parent, false);
        return new MainViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        PhotocardRealm photocard = mPhotocards.get(position);
        holder.mImageSea.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_custom_views_white_24dp));
        holder.mImageFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_custom_favorites_white_24dp));
        holder.mTextSea.setText(String.valueOf(photocard.getViews()));
        holder.mTextFav.setText(String.valueOf(photocard.getFavorits()));

        mPicasso.load(photocard.getPhoto())
                .fit()
                .centerCrop()
                .transform(new PhotoTransform())
                .into(holder.mPhoto);

        holder.mView.setOnClickListener(v -> {
            Flow.get(mContext).set(new PhotocardScreen(photocard));
        });
    }

    @Override
    public int getItemCount() {
        return mPhotocards.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_photo)
        View mView;
        @BindView(R.id.photo_card_IV)
        ImageView mPhoto;
        @BindView(R.id.fav_photo_IV)
        ImageView mImageFav;
        @BindView(R.id.fav_photo_TV)
        TextView mTextFav;
        @BindView(R.id.sea_photo_count_IV)
        ImageView mImageSea;
        @BindView(R.id.sea_photo_count_TV)
        TextView mTextSea;
        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
