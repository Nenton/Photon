package com.nenton.photon.data.storage.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class PhotocardRealm extends RealmObject {

    public PhotocardRealm() {
    }

    public PhotocardRealm(String photo, int views, int favorits) {
        this.photo = photo;
        this.views = views;
        this.favorits = favorits;
    }

    @PrimaryKey
    private String id;
    private String owner;
    private String title;
    private String photo;
    private int views;
    private int favorits;
    private RealmList<StringRealm> tags;

    public RealmList<StringRealm> getTags() {
        return tags;
    }

    public FiltersRealm getFilters() {
        return filters;
    }

    private FiltersRealm filters;

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
}
