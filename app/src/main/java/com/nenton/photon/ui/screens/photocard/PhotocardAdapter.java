package com.nenton.photon.ui.screens.photocard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.photon.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class PhotocardAdapter extends RecyclerView.Adapter<PhotocardAdapter.PhotocardViewHolder> {
    private List<String> mStrings = new ArrayList<>();

    public void addString(String s){
        mStrings.add(s);
        notifyDataSetChanged();
    }
    @Override
    public PhotocardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new PhotocardViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(PhotocardViewHolder holder, int position) {
        String s = mStrings.get(position);
        holder.mTextView.setText(s);
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class PhotocardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tag_TV)
        TextView mTextView;

        public PhotocardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
