package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class EditUserInfoJob extends Job {

    private static final String TAG = "EditUserInfoJob";
    private final UserEditReq mUserEditReq;

    public EditUserInfoJob(UserEditReq userEditReq) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mUserEditReq = userEditReq;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        DataManager.getInstance().getPreferencesManager().saveUserInfo(mUserEditReq);
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId()).findFirst();
        userRealm.setUserInfo(mUserEditReq);
        realm.insertOrUpdate(userRealm);
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().editUserInfoObs(mUserEditReq)
                .subscribe(userEditRes -> {

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
