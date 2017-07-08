package com.nenton.photon.ui.screens.photocard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.StringRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class PhotocardAdapter extends RecyclerView.Adapter<PhotocardAdapter.PhotocardViewHolder> {
    private List<String> mStrings = new ArrayList<>();
    private Context mContext;

    public void addString(StringRealm s){
        mStrings.add(s.getString());
        notifyDataSetChanged();
    }

    @Override
    public PhotocardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_photocard, parent, false);
        return new PhotocardViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(PhotocardViewHolder holder, int position) {
        holder.mTextView.setText(mStrings.get(position));
        setAnimation(holder.itemView);
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class PhotocardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tag_photocard_TV)
        TextView mTextView;

        public PhotocardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}