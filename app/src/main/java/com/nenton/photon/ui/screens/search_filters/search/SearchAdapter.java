package com.nenton.photon.ui.screens.search_filters.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by serge on 05.06.2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchTV> implements Filterable {

    private List<String> mStrings = new ArrayList<>();
    private List<String> mStringFilterList = new ArrayList<>();
    private ValueFilter valueFilter;

    @Inject
    SearchScreen.SearchPresenter mPresenter;

    public void addString(String s){
        mStringFilterList.add(s);
        notifyDataSetChanged();
    }

    public void addStrings(List<String> ss){
        mStringFilterList = ss;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DaggerService.<SearchScreen.Component>getDaggerComponent(recyclerView.getContext()).inject(this);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SearchTV onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new SearchTV(convertView);
    }

    @Override
    public void onBindViewHolder(SearchTV holder, int position) {
        String s = mStrings.get(position);
        holder.mTextView.setText(s);
        holder.mTextView.setOnClickListener(v -> {
            mPresenter.clickOnStringQuery(s);
        });
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    class SearchTV extends RecyclerView.ViewHolder{

        @BindView(R.id.suggest)
        TextView mTextView;

        public SearchTV(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                String sEqual = constraint.toString().toUpperCase();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    String s = mStringFilterList.get(i).toUpperCase();
                    if (s.contains(sEqual) && !s.equals(sEqual)) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStrings = (List<String>) results.values;
            SearchAdapter.this.notifyDataSetChanged();
        }

    }
}
