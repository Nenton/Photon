package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.SignInRes;

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
    private String avatar;
    private RealmList<AlbumRealm> albums;

    public UserRealm(SignInRes user) {
        this.id = user.getId();
        this.name = user.getName();
        this.login = user.getLogin();
        this.avatar = user.getAvatar();
        this.albums = new RealmList<>();
        for (Album album : user.getAlbums()) {
            albums.add(new AlbumRealm(album));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatar() {
        return avatar;
    }

    public RealmList<AlbumRealm> getAlbums() {
        return albums;
    }

    public UserRealm() {
    }
}
