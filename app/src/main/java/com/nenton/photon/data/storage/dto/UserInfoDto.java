package com.nenton.photon.data.storage.dto;

/**
 * Created by serge on 10.06.2017.
 */

public class UserInfoDto {
    private String id;
    private String name;
    private String login;
    private String avatar;
    private String token;

    public UserInfoDto(String id, String name, String login, String avatar, String token) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.avatar = avatar;
        this.token = token;
    }

    public UserInfoDto(String id, String name, String login, String avatar) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.avatar = avatar;
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

    public String getToken() {
        return token;
    }
}
