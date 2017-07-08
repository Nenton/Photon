package com.nenton.photon.mvp.model;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.SearchQuery;

import rx.Observable;

/**
 * Created by serge on 04.06.2017.
 */

public class MainModel extends AbstractModel {

    @RxLogObservable
    public Observable<PhotocardRealm> getPhotocardObs() {
        Observable<PhotocardRealm> disk = mDataManager.getPhotocardsFromRealm();
        Observable<PhotocardRealm> network = mDataManager.getPhotocardsObsFromNetwork();

        return Observable.mergeDelayError(disk, network)
                .distinct(PhotocardRealm::getId);
    }

    public Observable<PhotocardRealm> searchPhoto(SearchQuery sq) {
        return mDataManager.getSearchFromRealm(sq);
    }

    public Observable<PhotocardRealm> searchOnFilterPhoto(SearchFilterQuery sfq) {
        return mDataManager.getSearchFilterFromRealm(sfq);
    }

    public Observable<SignInRes> signIn(UserLoginReq loginReq) {
        return mDataManager.singIn(loginReq);
    }

    public Observable<SignUpRes> signUp(UserCreateReq createReq) {
        return mDataManager.singUp(createReq);
    }

    public boolean isSignIn() {
        return mDataManager.isSignIn();
    }

    public void unAuth() {
        mDataManager.getPreferencesManager().removeUserInfo();
    }

}
