package com.nenton.photon.ui.screens.add_photocard;

import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 18.06.2017.
 */

public class AddPhotocardSuggestionTagsAdapter extends RecyclerView.Adapter<AddPhotocardSuggestionTagsAdapter.ViewHolder> implements Filterable {

    @Inject
    AddPhotocardScreen.AddPhotocardPresenter mPresenter;

    private List<String> mStringRealms = new ArrayList<>();
    private List<String> mStringResult = new ArrayList<>();
    private TagsFilter mTagsFilter;
    private Context mContext;

    public void addTag(String albumRealm) {
        mStringRealms.add(albumRealm);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<AddPhotocardScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String string = mStringResult.get(position);
        holder.mTag.setText(string);

        holder.mTag.setOnClickListener(v -> {
            mPresenter.clickOnSuggestTag(string);
        });
    }

    @Override
    public int getItemCount() {
        return mStringResult.size();
    }

    @Override
    public Filter getFilter() {
        if (mTagsFilter == null) {
            mTagsFilter = new TagsFilter();
        }
        return mTagsFilter;
    }

    public void deleteString(String string) {
        mStringRealms.remove(string);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.suggest)
        TextView mTag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class TagsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                String sEqual = constraint.toString().toUpperCase();
                for (int i = 0; i < mStringRealms.size(); i++) {
                    String s = mStringRealms.get(i).toUpperCase();
                    if (s.contains(sEqual) && !s.equals(sEqual)) {
                        boolean b = true;
                        for (String s1 : mPresenter.getStrings()) {
                            if (s1.toUpperCase().equals(s)){
                                b = false;
                                break;
                            }
                        }
                        if (b) {
                            filterList.add(mStringRealms.get(i));
                        }
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringRealms.size();
                results.values = mStringRealms;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStringResult = (List<String>) results.values;
            AddPhotocardSuggestionTagsAdapter.this.notifyDataSetChanged();
        }
    }
}