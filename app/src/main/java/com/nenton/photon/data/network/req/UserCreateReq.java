package com.nenton.photon.data.network.req;

/**
 * Created by serge on 07.06.2017.
 */

public class UserCreateReq {
    public String name;
    public String login;
    public String email;
    public String password;

    public UserCreateReq(String name, String login, String email, String password) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
    }
}
