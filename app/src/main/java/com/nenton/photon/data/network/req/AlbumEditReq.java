package com.nenton.photon.data.network.req;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class AlbumEditReq {
    public String title;
    public String description;

    public AlbumEditReq(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
