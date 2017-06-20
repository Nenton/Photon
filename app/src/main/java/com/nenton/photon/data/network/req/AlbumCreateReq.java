package com.nenton.photon.data.network.req;

/**
 * Created by serge on 07.06.2017.
 */

public class AlbumCreateReq {
    private String title;
    private String description;

    public AlbumCreateReq(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
