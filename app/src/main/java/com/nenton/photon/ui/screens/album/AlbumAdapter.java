package com.nenton.photon.ui.screens.album;

import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.custom_views.ImageViewSquare;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AccountViewHolder> {

    @Inject
    Picasso picasso;

    @Inject
    AlbumScreen.AlbumPresenter mPresenter;

    private List<PhotocardRealm> mPhotocards = new ArrayList<>();
    private int posLongTap = -1;
    private Context mContext;
    private ViewGroup parentWrap;

    public void addPhotocard(PhotocardRealm album) {
        mPhotocards.add(album);
        notifyDataSetChanged();
    }

    public void deletePhotocard(int position) {
        mPhotocards.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public AlbumAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        parentWrap = parent;
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
        PhotocardRealm mPhoto = mPhotocards.get(position);

        picasso.with(mContext)
                .load(mPhoto.getPhoto())
//                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(250, 250)
                .centerCrop()
                .into(holder.mPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        picasso.load(mPhoto.getPhoto())
//                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .resize(250, 250)
                                .centerCrop()
                                .into(holder.mPhoto);
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
        return mPhotocards.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_card_album_IV)
        ImageViewSquare mPhoto;
        @BindView(R.id.long_tap_photo)
        LinearLayout mLongTapWrap;
        @BindView(R.id.edit_long_tap)
        ImageButton mLongTapEdit;
        @BindView(R.id.delete_long_tap)
        ImageButton mLongTapDelete;

        @OnClick(R.id.photo_card_album_IV)
        void clickOnPhoto() {
            mPresenter.clickOnPhotocard(mPhotocards.get(getAdapterPosition()));
        }

        @OnLongClick(R.id.photo_card_album_IV)
        boolean clickOnLongTap() {
            if (mPresenter.isAlbumOnUser()) {
                if (posLongTap != -1) {
                    mPresenter.updateLongTapAdapter(posLongTap);
                }
                posLongTap = getAdapterPosition();
                Transition transition = new Fade();
                transition.setDuration(300);
                transition.addTarget(mLongTapWrap);
                transition.setInterpolator(new FastOutSlowInInterpolator());
                TransitionManager.beginDelayedTransition(parentWrap, transition);
                mLongTapWrap.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        }

        @OnClick(R.id.long_tap_photo)
        void clickOnWrap() {
            Transition transition = new Fade();
            transition.setDuration(300);
            transition.addTarget(mLongTapWrap);
            transition.setInterpolator(new FastOutSlowInInterpolator());
            TransitionManager.beginDelayedTransition(parentWrap, transition);
            mLongTapWrap.setVisibility(View.GONE);
        }

        @OnClick(R.id.edit_long_tap)
        void clickOnEdit() {
            mPresenter.editPhoto(mPhotocards.get(getAdapterPosition()));
            posLongTap = -1;
            mLongTapWrap.setVisibility(View.GONE);
        }

        @OnClick(R.id.delete_long_tap)
        void clickOnDelete() {
            mPresenter.showDeletePhoto(mPhotocards.get(getAdapterPosition()).getId(), getAdapterPosition());
            posLongTap = -1;
            mLongTapWrap.setVisibility(View.GONE);
        }

        public AccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
