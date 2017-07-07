package com.nenton.photon.data.managers;

import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.RestCallTransformer;
import com.nenton.photon.data.network.RestService;
import com.nenton.photon.data.network.errors.AccessError;
import com.nenton.photon.data.network.errors.ApiError;
import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.network.req.AlbumEditReq;
import com.nenton.photon.data.network.req.PhotoIdReq;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.IdRes;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.UserEditRes;
import com.nenton.photon.data.network.res.UserInfo;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
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

import okhttp3.MultipartBody;
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
                .doOnNext(photocard -> {
                    if (!photocard.isActive()) {
                        mRealmManager.deleteFromRealm(PhotocardRealm.class, photocard.getId());
                    }
                })
                .filter(Photocard::isActive)
                .doOnNext(productRes -> mRealmManager.savePhotocardResponseToRealm(productRes)
                )
                .retryWhen(errorObservable ->
                        errorObservable
                                .zipWith(Observable.range(1, AppConfig.RETRY_REQUEST_COUNT), (throwable, retryCount) -> retryCount)
                                .doOnNext(retryCount -> Log.e(TAG, "LOCAL UPDATE request retry count: " + retryCount + " " + new Date()))
                                .map(retryCount -> ((long) (AppConfig.RETRY_REQUEST_BASE_DELAY * Math.pow(Math.E, retryCount))))
                                .doOnNext(delay -> Log.e(TAG, "LOCAL UPDATE delay: " + delay))
                                .flatMap(delay -> Observable.timer(delay, TimeUnit.MILLISECONDS)))
                .flatMap(productRes -> Observable.just(null));
    }

    @RxLogObservable
    public Observable<String> getPhotocardTagsObs() {
        return mRestService.getTagsObs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 200:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                })
                .doOnNext(strings -> getRealmManager().savePhotocardTags(strings))
                .flatMap(Observable::from)
                .flatMap(s -> Observable.just("#" + s));
    }

    public Observable<String> getTagsFromRealm() {
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(userInfo -> Observable.just(new UserRealm(userInfo, id)))
                .doOnNext(userRealm -> {
                    mRealmManager.saveUserInfo(userRealm);
                })
                .retryWhen(errorObservable ->
                        errorObservable
                                .zipWith(Observable.range(1, AppConfig.RETRY_REQUEST_COUNT), (throwable, retryCount) -> retryCount)
                                .doOnNext(retryCount -> Log.e(TAG, "LOCAL UPDATE request retry count: " + retryCount + " " + new Date()))
                                .map(retryCount -> ((long) (AppConfig.RETRY_REQUEST_BASE_DELAY * Math.pow(Math.E, retryCount))))
                                .doOnNext(delay -> Log.e(TAG, "LOCAL UPDATE delay: " + delay))
                                .flatMap(delay -> Observable.timer(delay, TimeUnit.MILLISECONDS)));
    }

    public Observable<UserEditRes> editUserInfoObs(UserEditReq userEditReq) {
        return mRestService.editUserInfoObs(getPreferencesManager().getAuthToken(),
                getPreferencesManager().getUserId(), userEditReq)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 202:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                })
                .doOnNext(userEditRes -> getPreferencesManager().editUserInfo(new UserInfoDto(userEditRes)));
    }

    public Observable<String> uploadPhoto(MultipartBody.Part file) {
        return mRestService.uploadPhoto(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(photoResResponse -> {
                    switch (photoResResponse.code()) {
                        case 201:
                            return Observable.just(photoResResponse.body().getImage());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(photoResResponse.code()));
                    }
                })
                .subscribeOn(Schedulers.newThread());
    }

    public Observable<String> createPhotocard(PhotocardReq photocardReq) {
        return mRestService.createPhotocardObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), photocardReq)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(photoResResponse -> {
                    switch (photoResResponse.code()) {
                        case 201:
                            return Observable.just(photoResResponse.body().getId());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(photoResResponse.code()));
                    }
                });
    }

    public Observable<Boolean> addToFav(String photocardId) {
        return mRestService.addPhotocardFavObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), photocardId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(photoResResponse -> {
                    switch (photoResResponse.code()) {
                        case 201:
                            return Observable.just(photoResResponse.body().isSuccess());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(photoResResponse.code()));
                    }
                });
    }

    public Observable<Boolean> deleteFromFav(String photocardId) {
        return mRestService.deletePhotocardFavObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), photocardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(photoResResponse -> {
                    switch (photoResResponse.code()) {
                        case 204:
                            return Observable.just(true);
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(photoResResponse.code()));
                    }
                });
    }

    public Observable<Boolean> addViewsToPhotocard(String photoId) {
        return mRestService.addPhotocardViewsObs(photoId, new PhotoIdReq(getPreferencesManager().getUserId()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 201:
                            return Observable.just(response.body().isSuccess());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                });
    }

    public Observable<PhotocardRealm> getPhotocardObs(String dateLastModified, String userId, String photoId) {
        return mRestService.getPhotocardObs(dateLastModified, userId, photoId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 200:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                })
                .doOnNext(photocard -> getRealmManager().savePhotocardResponseToRealm(photocard))
                .flatMap(photocard -> Observable.just(new PhotocardRealm(photocard)));
    }

    public Observable<Photocard> editPhotocardObs(String photoId, PhotocardReq photocardReq) {
        return mRestService.editPhotocardObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), photoId, photocardReq)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 202:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                });
    }

    public Observable<Object> deletePhotocardObs(String photoId) {
        return mRestService.deletePhotocardObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), photoId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 204:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                });
    }

    public Observable<Album> getAlbumListObs(String userId, int limit, int offset) {
        return mRestService.getAlbumListObs(userId, limit, offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 200:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                })
                .flatMap(Observable::from)
                .doOnNext(album -> getRealmManager().saveAlbumResponseToRealm(album));
    }

    public Observable<AlbumRealm> getAlbumObs(String userId, String id) {
        return mRestService.getAlbumObs(userId, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 200:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                })
                .doOnNext(album -> getRealmManager().saveAlbumResponseToRealm(album))
                .flatMap(album -> Observable.empty());
    }

    public Observable<Album> createAlbumObs(AlbumCreateReq albumCreateReq) {
        return mRestService.createAlbumObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), albumCreateReq)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 201:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                });
    }

    public Observable<Album> editAlbumObs(String id, AlbumEditReq albumEditReq) {
        return mRestService.editAlbumObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), id, albumEditReq)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 202:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                })
                .doOnNext(idRes -> getRealmManager().saveAlbumToRealm(idRes.getId(), albumEditReq));
    }

    public Observable<Object> deleteAlbumObs(String id) {
        return mRestService.deleteAlbumObs(getPreferencesManager().getAuthToken(), getPreferencesManager().getUserId(), id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap(response -> {
                    switch (response.code()) {
                        case 204:
                            return Observable.just(response.body());
                        case 403:
                            return Observable.error(new AccessError());
                        default:
                            return Observable.error(new ApiError(response.code()));
                    }
                });
    }

    public Observable<AlbumRealm> getAlbumById(String id) {
        return mRealmManager.getAlbumById(id);
    }

    public Observable<Boolean> isAlbumFromUser(String owner) {
        return Observable.just(owner.equals(getPreferencesManager().getUserId()));
    }

    public boolean haveAlbumUser() {
        return mRealmManager.haveAlbumUser(getPreferencesManager().getUserId());
    }

    public Observable<AlbumRealm> getAlbumFromTitleDesc(String title, String description) {
        return mRealmManager.getAlbumByTitleDesc(title, description, getPreferencesManager().getUserId());
    }
}
