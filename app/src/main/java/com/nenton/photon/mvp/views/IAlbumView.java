package com.nenton.photon.mvp.views;

import com.nenton.photon.data.storage.realm.AlbumRealm;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAlbumView {
    void initView(AlbumRealm mAlbum);
    void showDeleteAlbum();
    void showEditAlbum(String title, String description);
    void showDeletePhoto(String id, int adapterPosition);
}
