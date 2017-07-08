package com.nenton.photon.mvp.presenters;

import com.nenton.photon.data.storage.realm.PhotocardRealm;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAlbumPresenter {
    void editAlbum(String name, String description);
    void deleteAlbum();
    void editPhoto(PhotocardRealm photocardRealm);
    void clickOnPhotocard(PhotocardRealm mPhoto);
    void deletePhotocard(String id);

}
