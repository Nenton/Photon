package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class DeletePhotocardJob extends Job {

    private static final String TAG = "DeletePhotocardJob";
    private final String mIdPhotocard;

    public DeletePhotocardJob(String id) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        mIdPhotocard = id;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        PhotocardRealm photocardRealm = realm.where(PhotocardRealm.class).equalTo("id", mIdPhotocard).findFirst();
        realm.executeTransaction(realm1 -> {
            photocardRealm.deleteFromRealm();
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().deletePhotocardObs(mIdPhotocard)
                .subscribe(o -> {

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
