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
    private String id;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("views")
    @Expose
    private int views;
    @SerializedName("favorits")
    @Expose
    private int favorits;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("filters")
    @Expose
    private Filters filters;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("created")
    @Expose
    private String created;

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

    public boolean isActive() {
        return active;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCreated() {
        return created;
    }
}
