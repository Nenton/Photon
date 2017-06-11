package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class SignUpRes {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("login")
    @Expose
    public String login;
    @SerializedName("albums")
    @Expose
    public List<Album> albums = null;

    public SignUpRes(String id, String name, String login, List<Album> albums) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.albums = albums;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public List<Album> getAlbums() {
        return albums;
    }
}
