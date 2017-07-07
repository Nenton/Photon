package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class DeleteAlbumJob extends Job {

    private static final String TAG = "DeleteAlbumJob";
    private final String mIdAlbum;

    public DeleteAlbumJob(String idAlbum) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mIdAlbum = idAlbum;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
        realm.executeTransaction(realm1 -> {
            albumRealm.deleteFromRealm();
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().deleteAlbumObs(mIdAlbum)
                .subscribe(aVoid -> {

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
