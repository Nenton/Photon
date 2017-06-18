package com.nenton.photon.mvp.model;

import android.support.annotation.Nullable;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.TagsRes;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.SearchQuery;

import java.util.List;

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
    public Observable<PhotocardRealm> fromNetwork() {
        return mDataManager.getPhotocardObsFromNetwork();
    }

    @RxLogObservable
    public Observable<PhotocardRealm> fromDisk() {
        return mDataManager.getPhotocardFromRealm();
    }

    @RxLogObservable
    public Observable<PhotocardRealm> searchPhoto(SearchQuery sq) {
        return mDataManager.getSearchFromRealm(sq);
    }

    @RxLogObservable
    public Observable<PhotocardRealm> searchOnFilterPhoto(SearchFilterQuery sfq) {
        return mDataManager.getSearchFilterFromRealm(sfq);
    }
    @RxLogObservable
    public Observable<SignInRes> signIn(UserLoginReq loginReq) {
        return mDataManager.singIn(loginReq);
    }

    public Observable<SignUpRes> signUp(UserCreateReq createReq) {
        return mDataManager.singUp(createReq);
    }

    public UserInfoDto getUserInfo() {
        return mDataManager.getUserInfo();
    }

    public boolean isSignIn() {
        return mDataManager.isSignIn();
    }

    @RxLogObservable
    public Observable<UserRealm> getUser(String id) {
        Observable<UserRealm> disk = mDataManager.getUserById(id);
        Observable<UserRealm> network = mDataManager.getUserFromNetwork(id);

        return Observable.mergeDelayError(disk, network)
                .distinct(UserRealm::getId);
    }

    public void unAuth() {
        mDataManager.getPreferencesManager().removeUserInfo();
    }

    public List<String> getStrings() {
        return mDataManager.getPreferencesManager().getSearchStrings();
    }

    public void saveSearchString(String s) {
        mDataManager.getPreferencesManager().saveSearchString(s);
    }

    @RxLogObservable
    public Observable<StringRealm> getPhotocardTagsObs() {
        Observable<StringRealm> disk = mDataManager.getTagsFromRealm();
        Observable<StringRealm> network = mDataManager.getPhotocardTagsObs()
                .flatMap(tagsRes -> Observable.from(tagsRes.getTags()))
                .flatMap(s -> Observable.just(new StringRealm(s)));

        return Observable.mergeDelayError(disk, network)
                .distinct(StringRealm::getString);
    }



}
