package com.nenton.photon.data.storage.dto;

import com.nenton.photon.data.network.res.UserEditRes;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;

import rx.Observable;

/**
 * Created by serge on 10.06.2017.
 */

public class UserInfoDto {
    private String id;
    private String name;
    private String login;
    private String avatar;
    private int countAlbum;
    private int countPhoto;

    public UserInfoDto(String id, String name, String login, String avatar) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.avatar = avatar;
    }

    public UserInfoDto(UserRealm userRealm) {
        this.id = userRealm.getId();
        this.name = userRealm.getName();
        this.login = userRealm.getLogin();
        this.avatar = userRealm.getAvatar();
        this.countAlbum = userRealm.getAlbums().size();

        for (AlbumRealm albumRealm : userRealm.getAlbums()) {
            this.countPhoto += albumRealm.getPhotocards().size();
        }
    }

    public UserInfoDto(UserEditRes userEditRes) {
        this.name = userEditRes.getName();
        this.login = userEditRes.getLogin();
        this.avatar = userEditRes.getAvatar();
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

    public int getCountAlbum() {
        return countAlbum;
    }

    public int getCountPhoto() {
        return countPhoto;
    }
}