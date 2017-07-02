package com.nenton.photon.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.utils.AppConfig;

import java.util.List;

import io.realm.Realm;

/**
 * Created by serge on 02.07.2017.
 */

public class CreatePhotocardJob extends Job {

    private static final String TAG = "CreatePhotocardJob";
    private final String mIdAlbum;
    private final String mNamePhoto;
    private final String mUrl;
    private final List<String> mTags;
    private final FiltersDto mFilters;

    public CreatePhotocardJob(String idAlbum, String namePhotocard, String url, List<String> tags, FiltersDto filters) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mIdAlbum = idAlbum;
        this.mNamePhoto = namePhotocard;
        this.mUrl = url;
        this.mTags = tags;
        this.mFilters = filters;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
        PhotocardRealm photocardRealm = new PhotocardRealm(DataManager.getInstance().getPreferencesManager().getUserId(), mNamePhoto, mUrl, mTags, mFilters);
        realm.executeTransaction(realm1 -> {
            albumRealm.getPhotocards().add(mPhotocardRealm);
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        DataManager.getInstance().createPhotocard(new PhotocardReq(mPhotocardRealm))
                .subscribe(s -> {
                    Realm realm = Realm.getDefaultInstance();
                    PhotocardRealm photocardStorage = realm.where(PhotocardRealm.class).equalTo("id", mPhotocardRealm.getId()).findFirst();
                    AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
                    realm.executeTransaction(realm1 -> {
                        photocardStorage.deleteFromRealm();
                        albumRealm.getPhotocards().add(new PhotocardRealm(s, mPhotocardRealm));
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
