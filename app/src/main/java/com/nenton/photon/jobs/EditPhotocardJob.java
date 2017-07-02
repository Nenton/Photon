package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class EditPhotocardJob extends Job {

    private static final String TAG = "EditPhotocardJob";
    private final String mPhotoId;
    private final PhotocardReq mPhotoReq;
    private final String mIdAlbum;

    public EditPhotocardJob(String photocardId, String idAlbum, PhotocardReq photocardReq) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mPhotoId = photocardId;
        this.mPhotoReq = photocardReq;
        this.mIdAlbum = idAlbum;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        PhotocardRealm photoStorage = realm.where(PhotocardRealm.class).equalTo("id", mPhotoId).findFirst();
        PhotocardRealm photocardRealm = new PhotocardRealm(photoStorage, mPhotoReq);
        AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
        realm.executeTransaction(realm1 -> {
            photoStorage.deleteFromRealm();
            albumRealm.getPhotocards().add(photocardRealm);
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().editPhotocardObs(mPhotoId, mPhotoReq)
                .subscribe(photocard -> {
                    Realm realm = Realm.getDefaultInstance();
                    AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
                    PhotocardRealm photocardRealm = realm.where(PhotocardRealm.class).equalTo("id", mPhotoId).findFirst();

                    realm.executeTransaction(realm1 -> {
                        photocardRealm.deleteFromRealm();
                        albumRealm.getPhotocards().add(new PhotocardRealm(photocard));
                    });
                });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.e(TAG, " onCancel: ");
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.e(TAG, " shouldReRunOnThrowable: ");
        return RetryConstraint.createExponentialBackoff(runCount, AppConfig.INITIAL_BACK_OFF_IN_MS);
    }
}
