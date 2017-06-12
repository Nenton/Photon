package com.nenton.photon.mvp.views;

import android.support.annotation.Nullable;

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
}
