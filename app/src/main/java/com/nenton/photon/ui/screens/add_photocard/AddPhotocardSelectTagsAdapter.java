package com.nenton.photon.ui.screens.add_photocard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 18.06.2017.
 */

public class AddPhotocardSelectTagsAdapter extends RecyclerView.Adapter<AddPhotocardSelectTagsAdapter.ViewHolder> {

    @Inject
    AddPhotocardScreen.AddPhotocardPresenter mPresenter;

    private List<String> mStrings = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    public List<String> getStrings() {
        return mStrings;
    }

    public void addTag(String albumRealm) {
        mStrings.add(albumRealm.substring(1));
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<AddPhotocardScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_add_photocard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String string = mStrings.get(position);

        holder.mTag.setText("#" + string);

        holder.mImageButton.setOnClickListener(v -> {
            mStrings.remove(string);
            mPresenter.removeTag("#" + string);
            notifyDataSetChanged();
        });
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tag_add_photocard_TV)
        TextView mTag;
        @BindView(R.id.cancel_tag_ib)
        ImageButton mImageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
