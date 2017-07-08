package com.nenton.photon.mvp.views;

import com.nenton.photon.data.storage.realm.UserRealm;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAccountView {
    void showAuthState(UserRealm userRealm);
    void showUnAuthState();
    void signUp();
    void signIn();
    void showExit();
    void showDialogEditUserInfo(String loginOld, String nameOld);
    void showDialogAddAlbum();
}
