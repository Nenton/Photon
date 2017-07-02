package com.nenton.photon.jobs;

import android.net.Uri;
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

import java.io.File;
import java.net.URI;
import java.util.List;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by serge on 02.07.2017.
 */

public class CreatePhotocardJob extends Job {

    private static final String TAG = "CreatePhotocardJob";
    private final String mIdAlbum;
    private final String mNamePhoto;
    private final String mUri;
    private final File mFile;
    private final List<String> mTags;
    private final FiltersDto mFilters;
    private final String mIdPhoto;

    public CreatePhotocardJob(String idAlbum, String namePhotocard, File file, String uri, List<String> tags, FiltersDto filters) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .persist()
                .groupBy("Photocard"));
        this.mIdAlbum = idAlbum;
        this.mNamePhoto = namePhotocard;
        this.mFile = file;
        this.mUri = uri;
        this.mTags = tags;
        this.mFilters = filters;
        this.mIdPhoto = String.valueOf(mNamePhoto.hashCode() + mTags.hashCode());
    }

    @Override
    public void onAdded() {
        Log.e(TAG, " onAdded: ");
        Realm realm = Realm.getDefaultInstance();
        AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
        PhotocardRealm photocardRealm = new PhotocardRealm(DataManager.getInstance().getPreferencesManager().getUserId(), mIdPhoto, mNamePhoto, mUri, mTags, mFilters);
        realm.executeTransaction(realm1 -> {
            albumRealm.getPhotocards().add(photocardRealm);
        });
        realm.close();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, " onRun: ");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", mFile.getName(), requestBody);
        DataManager.getInstance().uploadPhoto(part).subscribe(uri -> {
            DataManager.getInstance().createPhotocard(new PhotocardReq(mNamePhoto, uri, mTags, mFilters))
                    .subscribe(s -> {
                        Realm realm = Realm.getDefaultInstance();
                        PhotocardRealm photocardStorage = realm.where(PhotocardRealm.class).equalTo("id", mIdPhoto).findFirst();
                        AlbumRealm albumRealm = realm.where(AlbumRealm.class).equalTo("id", mIdAlbum).findFirst();
                        realm.executeTransaction(realm1 -> {
                            photocardStorage.deleteFromRealm();
                            albumRealm.getPhotocards().add(new PhotocardRealm(s, photocardStorage));
                        });
                        realm.close();
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
