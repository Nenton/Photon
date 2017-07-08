package com.nenton.photon.mvp.presenters;

import com.nenton.photon.data.storage.realm.AlbumRealm;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAuthorPresenter {
    void clickOnAlbum(AlbumRealm album);
}
