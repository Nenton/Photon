package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class TagsRes {
    @SerializedName("tags")
    @Expose
    public List<String> tags = null;
}
