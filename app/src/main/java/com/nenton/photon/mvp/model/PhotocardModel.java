package com.nenton.photon.mvp.model;

import com.birbit.android.jobqueue.AsyncAddCallback;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.jobs.AddPhotocardToFavJob;
import com.nenton.photon.jobs.CreateAlbumJob;
import com.nenton.photon.jobs.CreatePhotocardJob;
import com.nenton.photon.jobs.EditPhotocardJob;

import java.io.File;
import java.util.List;

import rx.Observable;

/**
 * Created by serge on 08.07.2017.
 */

public class PhotocardModel extends AbstractModel {

    public UserInfoDto getUserInfo() {
        return mDataManager.getUserInfo();
    }

    @RxLogObservable
    public Observable<UserRealm> getUser(String id) {
        Observable<UserRealm> disk = mDataManager.getUserById(id);
        Observable<UserRealm> network = mDataManager.getUserFromNetwork(id);

        return Observable.mergeDelayError(disk, network)
                .distinct(UserRealm::getId);
    }

    @RxLogObservable
    public Observable<String> getPhotocardTagsObs() {
        Observable<String> disk = mDataManager.getTagsFromRealm();
        Observable<String> network = mDataManager.getPhotocardTagsObs();

        return Observable.mergeDelayError(disk, network)
                .distinct(String::toString);
    }

    public void editPhotocards(String photocardId, String idAlbum, PhotocardReq photocardReq, AsyncAddCallback callback) {
        EditPhotocardJob editPhotocardJob = new EditPhotocardJob(photocardId, idAlbum, photocardReq);
        mJobManager.addJobInBackground(editPhotocardJob, callback);
    }

    public Observable<Boolean> addViewsToPhotocard(String photoId) {
        return mDataManager.addViewsToPhotocard(photoId);
    }

    public void createAlbumObs(String name, String description, AsyncAddCallback callback) {
        CreateAlbumJob createAlbumJob = new CreateAlbumJob(name, description);
        mJobManager.addJobInBackground(createAlbumJob, callback);
    }

    public void createPhotocard(String idAlbum, String namePhotocard, File file, String uri, List<String> tags, FiltersDto filters, AsyncAddCallback callback) {
        CreatePhotocardJob createPhotocardJob = new CreatePhotocardJob(idAlbum, namePhotocard, file, uri, tags, filters);
        mJobManager.addJobInBackground(createPhotocardJob, callback);
    }

    public void addToFav(String id, AsyncAddCallback callback) {
        AddPhotocardToFavJob favJob = new AddPhotocardToFavJob(id);
        mJobManager.addJobInBackground(favJob, callback);
    }

    public Observable<Boolean> deleteFromFav(String photocardId) {
        return mDataManager.deleteFromFav(photocardId);
    }

    public boolean isSignIn() {
        return mDataManager.isSignIn();
    }

    public boolean haveAlbumUser() {
        return mDataManager.haveAlbumUser();
    }

    public Observable<Boolean> isPhotoFromFav(String id) {
        return mDataManager.isPhotoFromFav(id);
    }
}
