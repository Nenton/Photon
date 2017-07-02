package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class CreateAlbumJob extends Job {

    private static final String TAG = "CreateAlbumJob";
    private final String mName;
    private final String mDescription;
    private final String mIdAlbum;

    public CreateAlbumJob(String name, String description) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mName = name;
        this.mDescription = description;
        this.mIdAlbum = String.valueOf(mName.hashCode() + description.hashCode());
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId()).findFirst();
        AlbumRealm albumRealm = new AlbumRealm(mIdAlbum, mName, mDescription, DataManager.getInstance().getPreferencesManager().getUserId());
        realm.executeTransaction(realm1 -> {
            userRealm.getAlbums().add(albumRealm);
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");

        DataManager.getInstance().createAlbumObs(new AlbumCreateReq(mName, mDescription))
                .subscribe(album -> {
                    Realm realm = Realm.getDefaultInstance();
                    AlbumRealm albumRealm = realm.where(AlbumRealm.class)
                            .equalTo("id", mIdAlbum).findFirst();

                    UserRealm userRealm = realm.where(UserRealm.class)
                            .equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId())
                            .findFirst();

                    realm.executeTransaction(realm1 -> {
                        albumRealm.deleteFromRealm();
                        userRealm.getAlbums().add(new AlbumRealm(album));
                    });
                    realm.close();
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
