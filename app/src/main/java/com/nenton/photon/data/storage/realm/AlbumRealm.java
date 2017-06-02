package com.nenton.photon.data.storage.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class AlbumRealm extends RealmObject {
    @PrimaryKey
    private String id;
    private String owner;
    private String title;
    private String preview;
    private String description;
    private int views;
    private int favorits;
    private RealmList<PhotocardRealm> photocards;

    public AlbumRealm() {
    }

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

    public RealmList<PhotocardRealm> getPhotocards() {
        return photocards;
    }

}
