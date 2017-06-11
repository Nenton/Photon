package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 10.06.2017.
 */

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

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public int getViews() {
        return views;
    }

    public int getFavorits() {
        return favorits;
    }

    public List<String> getTags() {
        return tags;
    }

    public Filters getFilters() {
        return filters;
    }
}
