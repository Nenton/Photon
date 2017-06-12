package com.nenton.photon.mvp.presenters;

import android.support.v7.widget.PopupMenu;

/**
 * Created by serge on 12.06.2017.
 */

public class PopupMenuItem {
    private final int itemId;
    private final MenuPopup mAction;

    public PopupMenuItem(int itemId, MenuPopup action) {
        this.itemId = itemId;
        mAction = action;
    }

    public int getItemId() {
        return itemId;
    }

    public MenuPopup getAction() {
        return mAction;
    }

    public interface MenuPopup{
        void action();
    }
}
