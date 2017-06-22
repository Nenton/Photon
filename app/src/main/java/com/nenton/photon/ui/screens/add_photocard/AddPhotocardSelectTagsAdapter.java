package com.nenton.photon.ui.screens.add_photocard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nenton.photon.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 18.06.2017.
 */

public class AddPhotocardSelectTagsAdapter extends RecyclerView.Adapter<AddPhotocardSelectTagsAdapter.ViewHolder> {

    private List<String> mStrings = new ArrayList<>();

    public List<String> getStrings() {
        return mStrings;
    }

    public void addTag(String albumRealm) {
        mStrings.add(albumRealm.substring(1));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_add_photocard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String albumRealm = mStrings.get(position);

        holder.mTag.setText("#" + albumRealm);

        holder.mImageButton.setOnClickListener(v -> {
            mStrings.remove(albumRealm);
            notifyDataSetChanged();
        });
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
