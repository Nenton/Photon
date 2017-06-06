package com.nenton.photon.data.network.req;

/**
 * Created by serge on 07.06.2017.
 */

public class UserEditReq {
    public String name;
    public String login;
    public String avatar;

    public UserEditReq(String name, String login, String avatar) {
        this.name = name;
        this.login = login;
        this.avatar = avatar;
    }
}
