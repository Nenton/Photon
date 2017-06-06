package com.nenton.photon.data.network.req;

/**
 * Created by serge on 07.06.2017.
 */

public class AlbumCreateReq {
    public String owner;
    public String title;
    public String preview;
    public String description;

    public AlbumCreateReq(String owner, String title, String preview, String description) {
        this.owner = owner;
        this.title = title;
        this.preview = preview;
        this.description = description;
    }
}
