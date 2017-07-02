package com.nenton.photon.data.network.req;

import java.io.Serializable;

/**
 * Created by serge on 07.06.2017.
 */

public class UserEditReq implements Serializable{
    private String name;
    private String login;
    private String avatar;

    public UserEditReq(String name, String login, String avatar) {
        this.name = name;
        this.login = login;
        this.avatar = avatar;
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
}