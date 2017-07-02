package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.UserInfo;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class UserRealm extends RealmObject implements Serializable{

    @PrimaryKey
    private String id;
    private String name;
    private String login;
    private String avatar;
    private RealmList<AlbumRealm> albums = new RealmList<>();

    public UserRealm() {
    }

    public UserRealm(SignInRes user) {
        this.id = user.getId();
        this.name = user.getName();
        this.login = user.getLogin();
        this.avatar = user.getAvatar();
        for (Album album : user.getAlbums()) {
            if (album.isActive()){
                this.albums.add(new AlbumRealm(album));
            }
        }
    }

    public UserRealm(UserInfo userInfo, String id) {
        this.id = id;
        this.name = userInfo.getName();
        this.login = userInfo.getLogin();
        this.avatar = userInfo.getAvatar();
        for (Album album : userInfo.getAlbums()) {
            if (album.isActive()){
                this.albums.add(new AlbumRealm(album));
            }
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

    public void setUserInfo(UserEditReq userEditReq) {
        this.name = userEditReq.getName();
        this.login = userEditReq.getLogin();
        this.avatar = userEditReq.getAvatar();
    }
}