package com.nenton.photon.mvp.model;

import com.birbit.android.jobqueue.AsyncAddCallback;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.jobs.DeleteAlbumJob;
import com.nenton.photon.jobs.DeletePhotocardJob;
import com.nenton.photon.jobs.EditAlbumJob;

import rx.Observable;

/**
 * Created by serge on 08.07.2017.
 */

public class AlbumModel extends AbstractModel {

    public Observable<AlbumRealm> getAlbumFromRealm(String id, String owner) {
        Observable<AlbumRealm> disk = mDataManager.getAlbumById(id);
        Observable<AlbumRealm> network = mDataManager.getAlbumObs(owner, id);

        return Observable.mergeDelayError(disk, network)
                .distinct(AlbumRealm::getId);
    }

    public void editAlbum(String albumId, String name, String description, AsyncAddCallback callback) {
        EditAlbumJob editAlbumJob = new EditAlbumJob(albumId, name, description);
        mJobManager.addJobInBackground(editAlbumJob, callback);
    }

    public void deleteAlbumObs(String idAlbum,AsyncAddCallback callback) {
        DeleteAlbumJob deleteAlbumJob = new DeleteAlbumJob(idAlbum);
        mJobManager.addJobInBackground(deleteAlbumJob, callback);
    }

    public void deletePhotocard(String idPhoto, AsyncAddCallback callback) {
        DeletePhotocardJob deletePhotocardJob = new DeletePhotocardJob(idPhoto);
        mJobManager.addJobInBackground(deletePhotocardJob, callback);
    }

    public Observable<Boolean> isAlbumFromUser(String owner) {
        return mDataManager.isAlbumFromUser(owner);
    }
}
