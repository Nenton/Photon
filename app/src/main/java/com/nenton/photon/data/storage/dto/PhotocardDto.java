package com.nenton.photon.data.storage.dto;

import com.nenton.photon.data.network.req.PhotocardReq;

import java.util.List;

/**
 * Created by serge on 19.06.2017.
 */

public class PhotocardDto {

    private String id;
    private String owner;
    private String title;
    private String photo;
    private int views;
    private int favorits;
    private List<String> tags;
    private FiltersDto filters;

    public PhotocardDto(String id, String namePhotocard, String url, List<String> tags, FiltersDto filters) {
        this.id = id;
        this.title = namePhotocard;
        this.photo = url;
        this.views = 0;
        this.favorits = 0;
        this.tags = tags;
        this.filters = filters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getFavorits() {
        return favorits;
    }

    public void setFavorits(int favorits) {
        this.favorits = favorits;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public FiltersDto getFilters() {
        return filters;
    }

    public void setFilters(FiltersDto filters) {
        this.filters = filters;
    }
}
