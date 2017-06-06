package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serge on 07.06.2017.
 */

public class UserEditRes {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("login")
    @Expose
    public String login;
    @SerializedName("avatar")
    @Expose
    public String avatar;
}
