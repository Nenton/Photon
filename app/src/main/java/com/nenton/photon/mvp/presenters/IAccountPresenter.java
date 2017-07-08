package com.nenton.photon.mvp.presenters;

import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAccountPresenter {
    void exitAccount();
    void editUserInfo(String name, String login);
    void addAlbum(String name, String description);
    void loadUserInfo();
    void signIn(UserLoginReq loginReq);
    void signUp(UserCreateReq createReq);
}
