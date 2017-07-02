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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
