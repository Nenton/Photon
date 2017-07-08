package com.nenton.photon.ui.screens.add_photocard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.custom_views.ImageViewSquare;
import com.nenton.photon.utils.AlbumTransform;
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
 * Created by serge on 18.06.2017.
 */

public class AddPhotocardSelectAlbumAdapter extends RecyclerView.Adapter<AddPhotocardSelectAlbumAdapter.ViewHolder> {

    private int positionOnSelectItem = 0;
    private List<AlbumRealm> mAlbumRealmList = new ArrayList<>();
    private Context mContext;

    public void addAlbum(AlbumRealm albumRealm) {
        mAlbumRealmList.add(albumRealm);
        notifyDataSetChanged();
    }

    public int getPositionOnSelectItem() {
        return positionOnSelectItem;
    }

    public String getIdAlbum(){
        return mAlbumRealmList.get(positionOnSelectItem).getId();
    }

    @Inject
    Picasso mPicasso;
    @Inject
    AddPhotocardScreen.AddPhotocardPresenter mPresenter;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_album_to_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<AddPhotocardScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumRealm albumRealm = mAlbumRealmList.get(position);

        holder.mNameAlbum.setText(albumRealm.getTitle());
        holder.mCountPhotocards.setText(String.valueOf(albumRealm.getPhotocards().size()));

        mPicasso.with(mContext)
                .load(!albumRealm.getPhotocards().isEmpty() ? albumRealm.getPhotocards().get(0).getPhoto() : null)
                .placeholder(R.drawable.placeholder_album)
                .error(R.drawable.placeholder_album)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(250, 250)
                .centerCrop()
                .transform(new AlbumTransform())
                .into(holder.mView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.with(mContext)
                                .load(!albumRealm.getPhotocards().isEmpty() ? albumRealm.getPhotocards().get(0).getPhoto() : null)
                                .placeholder(R.drawable.placeholder_album)
                                .error(R.drawable.placeholder_album)
                                .resize(250, 250)
                                .centerCrop()
                                .transform(new AlbumTransform())
                                .into(holder.mView);
                    }
                });

        if (position == positionOnSelectItem) {
            holder.mCheckBox.setChecked(true);
        }

        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (positionOnSelectItem != position) {
                mPresenter.clickAlbum(positionOnSelectItem);
                positionOnSelectItem = position;
            }
        });
        setAnimation(holder.itemView);
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        viewToAnimate.startAnimation(animation);
    }
    @Override
    public int getItemCount() {
        return mAlbumRealmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.add_album_name_TV)
        TextView mNameAlbum;
        @BindView(R.id.add_album_count_TV)
        TextView mCountPhotocards;
        @BindView(R.id.add_photocard_select_album__IV)
        ImageViewSquare mView;
        @BindView(R.id.check_album_add)
        CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
