package com.nenton.photon.ui.screens.search_filters.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.ui.screens.main.MainScreen;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 05.06.2017.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {

    private final List<String> mStrings = new ArrayList<>();

    public void addString(String s){
        mStrings.add(s);
        notifyDataSetChanged();
    }

    @Override
    public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new TagsViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(TagsViewHolder holder, int position) {
        String s = mStrings.get(position);
        holder.mTextView.setText(s);
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    class TagsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tag_TV)
        TextView mTextView;

        public TagsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
