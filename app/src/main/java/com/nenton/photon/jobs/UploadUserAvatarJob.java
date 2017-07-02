package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.utils.AppConfig;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by serge on 02.07.2017.
 */

public class UploadUserAvatarJob extends Job {

    private static final String TAG = "UploadUserAvatarJob";
    private final String mAvatarUri;
    private final File mFile;

    public UploadUserAvatarJob(String avatarUri, File file) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mAvatarUri = avatarUri;
        this.mFile = file;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        DataManager.getInstance().getPreferencesManager().saveUserAvatar(mAvatarUri);
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        if (mAvatarUri != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", mFile.getName(), requestBody);

            DataManager.getInstance().uploadPhoto(part)
                    .subscribe(s -> {
                        DataManager.getInstance().getPreferencesManager().saveUserAvatar(s);
                        DataManager.getInstance().editUserInfoObs(
                                new UserEditReq(DataManager.getInstance().getPreferencesManager().getUserName()
                                        , DataManager.getInstance().getPreferencesManager().getUserLogin()
                                        , s))
                                .subscribe(userEditRes -> {

                                });
                    });
        }
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
