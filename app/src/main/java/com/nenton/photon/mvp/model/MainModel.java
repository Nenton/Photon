package com.nenton.photon.mvp.model;

import com.birbit.android.jobqueue.AsyncAddCallback;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.dto.PhotocardDto;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.jobs.AddPhotocardToFavJob;
import com.nenton.photon.jobs.CreateAlbumJob;
import com.nenton.photon.jobs.CreatePhotocardJob;
import com.nenton.photon.jobs.DeleteAlbumJob;
import com.nenton.photon.jobs.DeletePhotocardJob;
import com.nenton.photon.jobs.EditAlbumJob;
import com.nenton.photon.jobs.EditPhotocardJob;
import com.nenton.photon.jobs.EditUserInfoJob;
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

    //region ========================= Getters =========================

    @RxLogObservable
    public Observable<PhotocardRealm> getPhotocardObs() {
        Observable<PhotocardRealm> disk = mDataManager.getPhotocardFromRealm();
        Observable<PhotocardRealm> network = mDataManager.getPhotocardObsFromNetwork();

        return Observable.mergeDelayError(disk, network)
                .distinct(PhotocardRealm::getId);
    }

    public UserInfoDto getUserInfo() {
        return mDataManager.getUserInfo();
    }

    @RxLogObservable
    public Observable<UserRealm> getUser(String id) {
        Observable<UserRealm> disk = mDataManager.getUserById(id).subscribeOn(Schedulers.io());
        Observable<UserRealm> network = mDataManager.getUserFromNetwork(id);

        return Observable.mergeDelayError(disk, network)
                .distinct(UserRealm::getId);
    }

    public List<String> getStrings() {
        return mDataManager.getPreferencesManager().getSearchStrings();
    }

    @RxLogObservable
    public Observable<String> getPhotocardTagsObs() {
        Observable<String> disk = mDataManager.getTagsFromRealm();
        Observable<String> network = mDataManager.getPhotocardTagsObs();

        return Observable.mergeDelayError(disk, network)
                .distinct(String::toString);
    }

    public Observable<AlbumRealm> getAlbumFromRealm(String id, String owner) {
        Observable<AlbumRealm> disk = mDataManager.getAlbumById(id);
        Observable<AlbumRealm> network = mDataManager.getAlbumObs(owner, id);

        return Observable.mergeDelayError(disk, network)
                .distinct(AlbumRealm::getId);
    }

    //endregion

    //region ========================= Edits =========================

    public void editAlbum(AlbumRealm album, String name, String description) {
        EditAlbumJob editAlbumJob = new EditAlbumJob(album, name, description);
        mJobManager.addJobInBackground(editAlbumJob);
    }

    public void editPhotocards(PhotocardRealm photocardRealm, String idAlbum, PhotocardReq photocardReq) {
        EditPhotocardJob editPhotocardJob = new EditPhotocardJob(photocardRealm, idAlbum, photocardReq);
        mJobManager.addJobInBackground(editPhotocardJob);
    }

    public void editUserInfoAvatarObs(String s, AsyncAddCallback asyncAddCallback) {
        EditUserInfoJob editUserInfoJob = new EditUserInfoJob(new UserEditReq(mDataManager.getPreferencesManager().getUserName(), mDataManager.getPreferencesManager().getUserLogin(), s));
        mJobManager.addJobInBackground(editUserInfoJob, asyncAddCallback);
//        return mDataManager.editUserInfoObs(new UserEditReq(mDataManager.getPreferencesManager().getUserName(), mDataManager.getPreferencesManager().getUserLogin(), s));
    }

    public void editUserInfoObs(String name, String login, AsyncAddCallback asyncAddCallback) {
        EditUserInfoJob editUserInfoJob = new EditUserInfoJob(new UserEditReq(name, login, mDataManager.getPreferencesManager().getUserAvatar()));
        mJobManager.addJobInBackground(editUserInfoJob, asyncAddCallback);
//        return mDataManager.editUserInfoObs(new UserEditReq(name, login, mDataManager.getPreferencesManager().getUserAvatar()));
    }

    //endregion

    //region ========================= Creates =========================

    public Observable<Boolean> addViewsToPhotocard(String photoId) {
        return mDataManager.addViewsToPhotocard(photoId);
    }

    public void createAlbumObs(String name, String description, AsyncAddCallback callback) {
        CreateAlbumJob createAlbumJob = new CreateAlbumJob(name, description);
        mJobManager.addJobInBackground(createAlbumJob, callback);
    }

    public void saveSearchString(String s) {
        mDataManager.getPreferencesManager().saveSearchString(s);
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

    public void createPhotocard(String idAlbum, String namePhotocard, String url, List<String> tags, FiltersDto filters) {
        CreatePhotocardJob createPhotocardJob = new CreatePhotocardJob(idAlbum, namePhotocard, url, tags, filters);
        mJobManager.addJobInBackground(createPhotocardJob);
    }

    public void addToFav(String id, AsyncAddCallback callback) {
        AddPhotocardToFavJob favJob = new AddPhotocardToFavJob(id);
        mJobManager.addJobInBackground(favJob, callback);
    }

    //endregion

    //region ========================= Deletes =========================

    public Observable<Boolean> deleteFromFav(String photocardId) {
        return mDataManager.deleteFromFav(photocardId);
    }

    public void deleteAlbumObs(String idAlbum,AsyncAddCallback callback) {
        DeleteAlbumJob deleteAlbumJob = new DeleteAlbumJob(idAlbum);
        mJobManager.addJobInBackground(deleteAlbumJob, callback);
    }

    public void deletePhotocard(String idPhoto) {
        DeletePhotocardJob deletePhotocardJob = new DeletePhotocardJob(idPhoto);
        mJobManager.addJobInBackground(deletePhotocardJob);
    }

    //endregion

    //region ========================= Search =========================

    @RxLogObservable
    public Observable<PhotocardRealm> searchPhoto(SearchQuery sq) {
        return mDataManager.getSearchFromRealm(sq);
    }

    @RxLogObservable
    public Observable<PhotocardRealm> searchOnFilterPhoto(SearchFilterQuery sfq) {
        return mDataManager.getSearchFilterFromRealm(sfq);
    }

    //endregion

    @RxLogObservable
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

    public Observable<Boolean> isAlbumFromUser(String owner) {
        return mDataManager.isAlbumFromUser(owner);
    }

    //region ========================= JobQ =========================



    //endregion
}
