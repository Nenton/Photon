package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.utils.AppConfig;

/**
 * Created by serge on 02.07.2017.
 */

public class UploadPhotoJob extends Job {

    private static final String TAG = "UploadPhotoJob";

    protected UploadPhotoJob() {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
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
