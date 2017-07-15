package com.nenton.photon.mvp.views;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.nenton.photon.mvp.presenters.PopupMenuItem;
import com.nenton.photon.ui.activities.RootActivity;

import java.util.List;

/**
 * Created by serge on 03.06.2017.
 */

public interface IRootView extends IView{
    void showMessage(String message);
    void showError(Throwable e);

    void showLoad();
    void hideLoad();

    void showSettings();

    @Nullable
    IView getCurrentScreen();

    void changeOnBottom(@IdRes int id);

    void hideSnackbar();
    void showSearchSetting(String message, RootActivity.SnackBarAction action);
    void showFilterSetting(String message, RootActivity.SnackBarAction action);
    void setMenuIdRes(int menuIdRes);
    void setMenuPopup(List<PopupMenuItem> menuPopup);

    void checkBottomNavView(View view);
}
