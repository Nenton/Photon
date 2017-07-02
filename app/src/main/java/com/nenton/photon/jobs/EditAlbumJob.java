package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.req.AlbumEditReq;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.AppConfig;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class EditAlbumJob extends Job {

    private static final String TAG = "EditAlbumJob";
    private final String mAlbumId;
    private final String mName;
    private final String mDescription;

    public EditAlbumJob(String albumId, String name, String description) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mAlbumId = albumId;
        this.mName = name;
        this.mDescription = description;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId()).findFirst();
        AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mAlbumId).findFirst();
        realm.executeTransaction(realm1 -> {
            albumRealm.deleteFromRealm();
            albumRealm.setTitle(mName);
            albumRealm.setDescription(mDescription);
            userRealm.getAlbums().add(albumRealm);
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().editAlbumObs(mAlbumId, new AlbumEditReq(mName, mDescription))
                .subscribe(album -> {
                    Realm realm = Realm.getDefaultInstance();
                    AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mAlbumId).findFirst();
                    UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", DataManager.getInstance().getPreferencesManager().getUserId()).findFirst();
                    AlbumRealm albumNetwork = new AlbumRealm(album);
                    realm.executeTransaction(realm1 -> {
                        albumRealm.deleteFromRealm();
                        userRealm.getAlbums().add(albumNetwork);
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
