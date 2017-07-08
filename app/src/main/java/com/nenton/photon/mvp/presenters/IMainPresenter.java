package com.nenton.photon.mvp.presenters;

import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.storage.realm.PhotocardRealm;

import rx.Subscription;

/**
 * Created by serge on 08.07.2017.
 */

public interface IMainPresenter {
    void exitUser();
    void initView();
    Subscription subscribeOnProductRealmObs();
    Subscription subscribeOnSearchFilterRealmObs();
    Subscription subscribeOnSearchRealmObs();
    void signIn(UserLoginReq loginReq);
    void signUp(UserCreateReq createReq);
    void changeStateAuth();
    void clickOnPhoto(PhotocardRealm photocard);
}
