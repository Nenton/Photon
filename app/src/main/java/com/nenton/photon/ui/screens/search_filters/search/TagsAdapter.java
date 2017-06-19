package com.nenton.photon.ui.screens.search_filters.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.StringRealm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 05.06.2017.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {

    private final List<String> mStrings = new ArrayList<>();
    private Set<String> mStringSet = new HashSet<>();

    public Set<String> getStringSet() {
        return mStringSet;
    }

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
        holder.mTagCheckBox.setText(s);
        holder.mTagCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mStringSet.add(s);
            } else {
                mStringSet.remove(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    class TagsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tag_TV)
        CheckBox mTagCheckBox;

        TagsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
