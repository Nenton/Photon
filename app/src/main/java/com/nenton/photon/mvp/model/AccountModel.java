package com.nenton.photon.mvp.model;

import com.birbit.android.jobqueue.AsyncAddCallback;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.jobs.CreateAlbumJob;
import com.nenton.photon.jobs.EditUserInfoJob;
import com.nenton.photon.jobs.UploadUserAvatarJob;

import java.io.File;

import rx.Observable;

/**
 * Created by serge on 08.07.2017.
 */

public class AccountModel extends AbstractModel {

    public UserInfoDto getUserInfo() {
        return mDataManager.getUserInfo();
    }

    @RxLogObservable
    public Observable<UserRealm> getUser(String id) {
        Observable<UserRealm> disk = mDataManager.getUserById(id);
        Observable<UserRealm> network = mDataManager.getUserFromNetwork(id);

        return Observable.mergeDelayError(disk, network)
                .distinct(UserRealm::getId);
    }

    public void editUserInfoAvatarObs(String s, AsyncAddCallback callback) {
        EditUserInfoJob editUserInfoJob = new EditUserInfoJob(new UserEditReq(mDataManager.getPreferencesManager().getUserName(), mDataManager.getPreferencesManager().getUserLogin(), s));
        mJobManager.addJobInBackground(editUserInfoJob, callback);
    }

    public void editUserInfoObs(String name, String login, AsyncAddCallback asyncAddCallback) {
        EditUserInfoJob editUserInfoJob = new EditUserInfoJob(new UserEditReq(name, login, mDataManager.getPreferencesManager().getUserAvatar()));
        mJobManager.addJobInBackground(editUserInfoJob, asyncAddCallback);
    }

    public void createAlbumObs(String name, String description, AsyncAddCallback callback) {
        CreateAlbumJob createAlbumJob = new CreateAlbumJob(name, description);
        mJobManager.addJobInBackground(createAlbumJob, callback);
    }

    public void uploadUserAvatar(String avatarUri, File file, AsyncAddCallback callback) {
        UploadUserAvatarJob uploadUserAvatarJob = new UploadUserAvatarJob(avatarUri, file);
        mJobManager.addJobInBackground(uploadUserAvatarJob, callback);
    }

    @RxLogObservable
    public Observable<SignInRes> signIn(UserLoginReq loginReq) {
        return mDataManager.singIn(loginReq);
    }

    public Observable<SignUpRes> signUp(UserCreateReq createReq) {
        return mDataManager.singUp(createReq);
    }

    public boolean isSignIn() {
        return mDataManager.isSignIn();
    }

    public void unAuth() {
        mDataManager.getPreferencesManager().removeUserInfo();
    }

    public Observable<AlbumRealm> getAlbumFromTitleDesc(String title, String description) {
        return mDataManager.getAlbumFromTitleDesc(title, description);
    }
}
