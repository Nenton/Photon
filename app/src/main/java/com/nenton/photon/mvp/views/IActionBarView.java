package com.nenton.photon.mvp.views;

import android.support.v4.view.ViewPager;

import com.nenton.photon.mvp.presenters.MenuItemHolder;

import java.util.List;

/**
 * Created by serge on 05.01.2017.
 */

public interface IActionBarView {
    void setTitle(CharSequence title);
    void setVisibleToolbar(boolean visible);
    void setBackArrow(boolean enabled);
    void setMenuItem(List<MenuItemHolder> items);
    void setTabLayout(ViewPager pager);
    void removeTabLayout();
}
