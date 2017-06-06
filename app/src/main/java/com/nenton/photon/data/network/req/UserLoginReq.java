package com.nenton.photon.data.network.req;

/**
 * Created by serge on 07.06.2017.
 */

public class UserLoginReq {
    public String email;
    public String password;

    public UserLoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
