package com.nenton.photon.data.managers;

import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.RestCallTransformer;
import com.nenton.photon.data.network.RestService;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.TagsRes;
import com.nenton.photon.data.network.res.UserInfo;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.components.DaggerDataManagerComponent;
import com.nenton.photon.di.components.DataManagerComponent;
import com.nenton.photon.di.modules.LocalModule;
import com.nenton.photon.di.modules.NetworkModule;
import com.nenton.photon.utils.App;
import com.nenton.photon.utils.AppConfig;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.SearchQuery;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by serge on 03.06.2017.
 */

public class DataManager {

    private static final String TAG = "DataManager";
    private static DataManager ourInstance;
    private final RestCallTransformer mRestCallTransformer;

    @Inject
    RestService mRestService;
    @Inject
    PreferencesManager mPreferencesManager;
    @Inject
    Retrofit mRetrofit;
    @Inject
    RealmManager mRealmManager;

    public RestService getRestService() {
        return mRestService;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public RealmManager getRealmManager() {
        return mRealmManager;
    }

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    private DataManager() {
        DataManagerComponent component = DaggerService.getComponent(DataManagerComponent.class);
        if (component == null) {
            component = DaggerDataManagerComponent.builder()
                    .appComponent(App.getAppComponent())
                    .localModule(new LocalModule())
                    .networkModule(new NetworkModule())
                    .build();
            DaggerService.registerComponent(DataManagerComponent.class, component);
        }
        component.inject(this);
        mRestCallTransformer = new RestCallTransformer<>();
    }

    public Observable<PhotocardRealm> getPhotocardObsFromNetwork() {
        return mRestService.getPhotocardListObs(50, 0)
                .compose(((RestCallTransformer<List<Photocard>>) mRestCallTransformer))
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(productRes -> mRealmManager.savePhotocardResponseToRealm(productRes)
                )
                .retryWhen(errorObservable ->
                        errorObservable
                                .zipWith(Observable.range(1, AppConfig.RETRY_REQUEST_COUNT), (throwable, retryCount) -> retryCount)
                                .doOnNext(retryCount -> Log.e(TAG, "LOCAL UPDATE request retry count: " + retryCount + " " + new Date()))
                                .map(retryCount -> ((long) (AppConfig.RETRY_REQUEST_BASE_DELAY * Math.pow(Math.E, retryCount))))
                                .doOnNext(delay -> Log.e(TAG, "LOCAL UPDATE delay: " + delay))
                                .flatMap(delay -> Observable.timer(delay, TimeUnit.MILLISECONDS)))
                .flatMap(productRes -> Observable.empty());
    }

    public Observable<TagsRes> getPhotocardTagsObs() {
        return mRestService.getTagsObs()
                .compose(((RestCallTransformer<TagsRes>) mRestCallTransformer))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(tagsRes -> mRealmManager.savePhotocardTags(tagsRes.getTags()))
                .flatMap(tagsObs -> Observable.empty());
    }

    public Observable<StringRealm> getTagsFromRealm() {
        return mRealmManager.getTags();
    }

    public Observable<PhotocardRealm> getPhotocardFromRealm() {
        return mRealmManager.getAllPhotocardFromRealm();
    }

    public Observable<PhotocardRealm> getSearchFromRealm(SearchQuery sq) {
        return mRealmManager.getAllPhotocardOnSearchRealm(sq);
    }

    public Observable<PhotocardRealm> getSearchFilterFromRealm(SearchFilterQuery sfq) {
        return mRealmManager.getAllPhotocardOnSearchFilterRealm(sfq);
    }

    @RxLogObservable
    public Observable<SignInRes> singIn(UserLoginReq loginReq) {
        return mRestService.signIn(loginReq)
                .compose(((RestCallTransformer<SignInRes>) mRestCallTransformer))
                .observeOn(Schedulers.io())
                .doOnNext(userRes -> {
                            saveUserInfo(new UserInfoDto(userRes.getId(), userRes.getName(), userRes.getLogin(), userRes.getAvatar()), userRes.getToken());
                            mRealmManager.saveAccountInfoToRealm(userRes);
                        }
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::just);
    }

    public Observable<SignUpRes> singUp(UserCreateReq createReq) {
        return mRestService.signUp(createReq)
                .compose(((RestCallTransformer<SignUpRes>) mRestCallTransformer))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(Observable::just);
    }

    private void saveUserInfo(UserInfoDto userLoginRes, String token) {
        getPreferencesManager().saveUserInfo(userLoginRes, token);
    }

    public UserInfoDto getUserInfo() {
        return mPreferencesManager.getUserInfo();
    }

    public boolean isSignIn() {
        return mPreferencesManager.isUserAuth();
    }

    public Observable<UserRealm> getUserById(String id) {
        return mRealmManager.getUserById(id);
    }

    public Observable<UserRealm> getUserFromNetwork(String id) {
        return mRestService.getUserInfoObs(id)
                .compose(((RestCallTransformer<UserInfo>) mRestCallTransformer))
                .observeOn(Schedulers.io())
                .flatMap(userInfo -> Observable.just(new UserRealm(userInfo, id)))
                .doOnNext(userRealm -> {
                    mRealmManager.saveUserInfo(userRealm);
                })
                .subscribeOn(Schedulers.newThread())
                .retryWhen(errorObservable ->
                        errorObservable
                                .zipWith(Observable.range(1, AppConfig.RETRY_REQUEST_COUNT), (throwable, retryCount) -> retryCount)
                                .doOnNext(retryCount -> Log.e(TAG, "LOCAL UPDATE request retry count: " + retryCount + " " + new Date()))
                                .map(retryCount -> ((long) (AppConfig.RETRY_REQUEST_BASE_DELAY * Math.pow(Math.E, retryCount))))
                                .doOnNext(delay -> Log.e(TAG, "LOCAL UPDATE delay: " + delay))
                                .flatMap(delay -> Observable.timer(delay, TimeUnit.MILLISECONDS)))
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::just);
    }
}
