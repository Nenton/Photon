package com.nenton.photon.mvp.views;

import java.util.List;

/**
 * Created by serge on 04.06.2017.
 */

public interface ISearchView {
    void initView(List<String> strings);
    void setTextSearchViewByQueryString(String s);
}
