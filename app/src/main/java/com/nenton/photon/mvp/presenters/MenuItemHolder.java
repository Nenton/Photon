package com.nenton.photon.mvp.presenters;

import android.view.MenuItem;

/**
 * Created by serge on 05.01.2017.
 */
public class MenuItemHolder {
    private final CharSequence itemTitle;
    private final int iconResId;
    private final MenuItem.OnMenuItemClickListener mListener;

    public MenuItemHolder(CharSequence itemTitle, int iconResId, MenuItem.OnMenuItemClickListener listener) {
        this.itemTitle = itemTitle;
        this.iconResId = iconResId;
        mListener = listener;
    }

    public CharSequence getTitle() {
        return itemTitle;
    }

    public int getIconResId() {
        return iconResId;
    }

    public MenuItem.OnMenuItemClickListener getListener() {
        return mListener;
    }
}
