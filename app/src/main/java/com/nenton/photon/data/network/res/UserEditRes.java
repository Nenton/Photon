package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serge on 07.06.2017.
 */

public class UserEditRes {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatar() {
        return avatar;
    }
}
