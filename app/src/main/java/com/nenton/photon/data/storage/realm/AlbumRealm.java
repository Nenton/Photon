package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.network.req.AlbumEditReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignInRes;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class AlbumRealm extends RealmObject implements Serializable {
    @PrimaryKey
    private String id;
    private String owner;
    private String title;
    private String description;
    private boolean isFavorite;
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
        this.isFavorite = album.isFavorite();
        this.description = album.getDescription();
        this.views = album.getViews();
        this.favorits = album.getFavorits();

        photocards = new RealmList<>();
        for (Photocard photocard : album.getPhotocards()) {
            if (photocard.isActive()) {
                photocards.add(new PhotocardRealm(photocard));
            }
        }
    }

    public AlbumRealm(String id, AlbumEditReq albumCreateReq, AlbumRealm first) {
        this.id = id;
        this.owner = first.getOwner();
        this.title = albumCreateReq.getTitle();
        this.isFavorite = first.isFavorite();
        this.description = albumCreateReq.getDescription();
        this.views = first.getViews();
        this.favorits = first.getFavorits();

        photocards = new RealmList<>();
        for (PhotocardRealm photocard : first.getPhotocards()) {
            photocards.add(photocard);
        }
    }

    public AlbumRealm(String id, String name, String description, String userId) {
        this.id = id;
        this.description = description;
        this.title = name;
        this.owner = userId;
        this.isFavorite = false;
        this.views = 0;
        this.favorits = 0;

        photocards = new RealmList<>();
    }

    public AlbumRealm(AlbumRealm album, String name, String description) {
        this.title = name;
        this.description = description;
        this.id = album.getId();
        this.owner = album.getOwner();
        this.isFavorite = album.isFavorite();
        this.views = album.getViews();
        this.favorits = album.getFavorits();
        photocards = album.getPhotocards();
    }

    public boolean isFavorite() {
        return isFavorite;
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
