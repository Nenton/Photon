package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignInRes;

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
    private String description;
    private boolean isFavorite;
    private boolean active;
    private int views;
    private int favorits;
    private RealmList<PhotocardRealm> photocards = new RealmList<>();

    public AlbumRealm(String title, RealmList<PhotocardRealm> photocards) {
        this.title = title;
        this.photocards = photocards;
    }

    public AlbumRealm() {
    }

    public AlbumRealm(Album album) {
        this.id = album.getId();
        this.owner = album.getOwner();
        this.title = album.getTitle();
        this.active = album.isActive();
        this.isFavorite = album.isFavorite();
        this.description = album.getDescription();
        this.views = album.getViews();
        this.favorits = album.getFavorits();

        photocards = new RealmList<>();
        for (Photocard photocard : album.getPhotocards()) {
            if (photocard.isActive()){
                photocards.add(new PhotocardRealm(photocard));
            }
        }
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isActive() {
        return active;
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
