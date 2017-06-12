package com.nenton.photon.mvp.model;

import android.support.annotation.Nullable;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;

import rx.Observable;
import rx.Subscription;

/**
 * Created by serge on 04.06.2017.
 */

public class MainModel extends AbstractModel {
    @RxLogObservable
    public Observable<PhotocardRealm> getPhotocardObs() {
        Observable<PhotocardRealm> disk = fromDisk();
        Observable<PhotocardRealm> network = fromNetwork();

        return Observable.mergeDelayError(disk, network)
                .distinct(PhotocardRealm::getId);
    }

    @RxLogObservable
    public Observable<PhotocardRealm> fromNetwork(){
        return mDataManager.getPhotocardObsFromNetwork();
    }

    @RxLogObservable
    public Observable<PhotocardRealm> fromDisk(){
        return mDataManager.getPhotocardFromRealm();
    }

    public Observable<SignInRes> signIn(UserLoginReq loginReq){
        return mDataManager.singIn(loginReq);
    }

    public Observable<SignUpRes> signUp(UserCreateReq createReq){
        return mDataManager.singUp(createReq);
    }

    public UserInfoDto getUserInfo(){
        return mDataManager.getUserInfo();
    }

    public boolean isSignIn(){
        return mDataManager.isSignIn();
    }

    public Observable<UserRealm> getUserRealm(String id) {
        return mDataManager.getRealmManager().getUserById(id);
    }

    public void unAuth() {
        mDataManager.getPreferencesManager().removeUserInfo();
    }
}
