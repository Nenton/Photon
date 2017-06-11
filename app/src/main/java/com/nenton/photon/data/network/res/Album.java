package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

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

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getDescription() {
        return description;
    }

    public int getViews() {
        return views;
    }

    public int getFavorits() {
        return favorits;
    }

    public List<Photocard> getPhotocards() {
        return photocards;
    }
}
