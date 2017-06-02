package com.nenton.photon.data.storage.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class UserRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String login;
    private String mail;
    private String avatar;
    private String token;
    private int albumCount;
    private int photocardCount;
    private RealmList<AlbumRealm> albums;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getMail() {
        return mail;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getToken() {
        return token;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public int getPhotocardCount() {
        return photocardCount;
    }

    public RealmList<AlbumRealm> getAlbums() {
        return albums;
    }

    public UserRealm() {
    }
}
