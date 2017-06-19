package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serge on 07.06.2017.
 */

public class UploadPhotoRes {
    public String getImage() {
        return image;
    }

    @SerializedName("image")
    @Expose
    private String image;
}
