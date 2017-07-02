package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class AddPhotocardToFavJob extends Job {

    private final String mId;
    private static final String TAG = "AddPhotocardToFavJob";

    public AddPhotocardToFavJob(String id) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        mId = id;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        UserRealm user = realm.where(UserRealm.class)
                .equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId())
                .findFirst();

        PhotocardRealm photocardRealm = realm.where(PhotocardRealm.class)
                .equalTo("id", mId)
                .findFirst();

        realm.executeTransaction(realm1 -> {
            user.getAlbums().get(0).getPhotocards().add(photocardRealm);
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().addToFav(mId)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        Realm realm = Realm.getDefaultInstance();
                        AlbumRealm albumRealm = realm.where(UserRealm.class)
                                .equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId())
                                .findFirst().getAlbums().get(0);
                        PhotocardRealm photo = realm.where(PhotocardRealm.class)
                                .equalTo("id", mId)
                                .findFirst();
                        realm.executeTransaction(realm1 -> {
                            albumRealm.getPhotocards().remove(photo);
                        });
                        realm.close();
                    }
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
