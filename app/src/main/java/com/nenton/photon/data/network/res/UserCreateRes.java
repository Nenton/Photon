package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class UserCreateRes {
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

    public class Album {

        @SerializedName("owner")
        @Expose
        public String owner;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("photocards")
        @Expose
        public List<Object> photocards = null;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("views")
        @Expose
        public int views;
        @SerializedName("favorits")
        @Expose
        public int favorits;

    }
}
