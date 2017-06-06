package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class UserLoginRes {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("login")
    @Expose
    public String login;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("albumCount")
    @Expose
    public int albumCount;
    @SerializedName("photocardCount")
    @Expose
    public int photocardCount;
    @SerializedName("albums")
    @Expose
    public List<Album> albums = null;

    public class Album {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("owner")
        @Expose
        public String owner;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("preview")
        @Expose
        public String preview;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("views")
        @Expose
        public int views;
        @SerializedName("favorits")
        @Expose
        public int favorits;
        @SerializedName("photocards")
        @Expose
        public List<Photocard> photocards = null;

    }

    public class Photocard {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("owner")
        @Expose
        public String owner;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("photo")
        @Expose
        public String photo;
        @SerializedName("views")
        @Expose
        public int views;
        @SerializedName("favorits")
        @Expose
        public int favorits;
        @SerializedName("tags")
        @Expose
        public List<String> tags = null;
        @SerializedName("filters")
        @Expose
        public Filters filters;

    }

    public class Filters {

        @SerializedName("dish")
        @Expose
        public String dish;
        @SerializedName("nuances")
        @Expose
        public String nuances;
        @SerializedName("decor")
        @Expose
        public String decor;
        @SerializedName("temperature")
        @Expose
        public String temperature;
        @SerializedName("light")
        @Expose
        public String light;
        @SerializedName("lightDirection")
        @Expose
        public String lightDirection;
        @SerializedName("lightSource")
        @Expose
        public String lightSource;

    }
}
