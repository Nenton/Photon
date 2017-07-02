package com.nenton.photon.ui.screens.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.custom_views.ImageViewSquare;

import com.nenton.photon.utils.PhotoTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 04.06.2017.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private final List<PhotocardRealm> mPhotocards = new ArrayList<>();

    @Inject
    Picasso mPicasso;
    @Inject
    MainScreen.MainPresenter mPresenter;

    void addPhoto(PhotocardRealm photocard){
        mPhotocards.add(photocard);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
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
        final PhotocardRealm photocard = mPhotocards.get(position);
        holder.mTextSea.setText(String.valueOf(photocard.getViews()));
        holder.mTextFav.setText(String.valueOf(photocard.getFavorits()));

        mPicasso.load(photocard.getPhoto())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500,500)
                .centerCrop()
                .transform(new PhotoTransform())

                .into(holder.mPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.load(photocard.getPhoto())
                                .resize(500,500)
                                .centerCrop()
                                .transform(new PhotoTransform())
                                .into(holder.mPhoto);
                    }
                });

        holder.mView.setOnClickListener(v -> {
            mPresenter.clickOnPhoto(photocard);
        });
    }

    @Override
    public int getItemCount() {
        return mPhotocards.size();
    }

    public void reload() {
        mPhotocards.clear();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_photo)
        FrameLayout mView;
        @BindView(R.id.photo_card_IV)
        ImageViewSquare mPhoto;
        @BindView(R.id.fav_photo_TV)
        TextView mTextFav;
        @BindView(R.id.sea_photo_count_TV)
        TextView mTextSea;
        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
