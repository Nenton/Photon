package com.nenton.photon.mvp.model;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.network.req.AlbumEditReq;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.IdRes;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.UserEditRes;
import com.nenton.photon.data.storage.dto.PhotocardDto;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.SearchQuery;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.schedulers.Schedulers;

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
        Observable<UserRealm> disk = mDataManager.getUserById(id).subscribeOn(Schedulers.io());
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
    public Observable<String> getPhotocardTagsObs() {
        Observable<String> disk = mDataManager.getTagsFromRealm();
        Observable<String> network = mDataManager.getPhotocardTagsObs();

        return Observable.mergeDelayError(disk, network)
                .distinct(String::toString);
    }


    public Observable<String> uploadPhotoToNetwork(String avatarUri, File file) {
        if (avatarUri != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

            return mDataManager.uploadPhoto(mDataManager.getUserInfo().getId(), part);

        } else {
            return Observable.empty();
        }
    }

    public Observable<String> createPhotocard(PhotocardReq photocardReq) {
        return mDataManager.createPhotocard(mDataManager.getPreferencesManager().getUserId(), photocardReq);
    }

    public void savePhotoToRealm(PhotocardDto photocardDto) {
        photocardDto.setOwner(mDataManager.getPreferencesManager().getUserId());
        mDataManager.getRealmManager().saveCreatePhotocard(photocardDto);
    }

    public Observable<Boolean> addToFav(String photocardId) {
        return mDataManager.addToFav(photocardId);
    }

    public Observable<Boolean> deleteFromFav(String photocardId) {
        return mDataManager.deleteFromFav(photocardId);
    }

    public Observable<Boolean> addViewsToPhotocard(String photoId) {
        return mDataManager.addViewsToPhotocard(photoId);
    }


    public Observable<Album> createAlbumObs(AlbumCreateReq albumCreateReq) {
        return mDataManager.createAlbumObs(albumCreateReq);
    }

    public Observable<Void> deleteAlbumObs(String idAlbum) {
        return mDataManager.deleteAlbumObs(idAlbum);
    }

    public Observable<Album> editAlbumObs(String id, AlbumEditReq albumEditReq) {
        return mDataManager.editAlbumObs(id, albumEditReq);
    }

    public Observable<AlbumRealm> getAlbumFromRealm(String id){
        return mDataManager.getAlbumById(id);
    }

    public Observable<UserEditRes> editUserInfoAvatarObs(String s) {
        return mDataManager.editUserInfoObs(new UserEditReq(mDataManager.getPreferencesManager().getUserName(),mDataManager.getPreferencesManager().getUserLogin(),s));
    }

    public Observable<UserEditRes> editUserInfoObs(String name, String login) {
        return mDataManager.editUserInfoObs(new UserEditReq(name, login, mDataManager.getPreferencesManager().getUserAvatar()));
    }
}
