package com.nenton.photon.data.network.req;

import com.nenton.photon.data.storage.realm.AlbumRealm;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class AlbumEditReq {
    private String title;
    private String description;

    public AlbumEditReq(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public AlbumEditReq(AlbumRealm album) {
        this.title = album.getTitle();
        this.description = album.getDescription();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
