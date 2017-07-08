package com.nenton.photon.mvp.views;

import com.nenton.photon.utils.SearchFilterQuery;

/**
 * Created by serge on 08.07.2017.
 */

public interface IFilterView {
    void initView(SearchFilterQuery query);
    void initView();
}
