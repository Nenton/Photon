package com.nenton.photon.data.network.errors;

/**
 * Created by serge on 02.04.2017.
 */
public class AccessError extends Exception{
    public AccessError() {
        super("Неверный логин или пароль");
    }
}
