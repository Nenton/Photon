package com.nenton.photon.data.managers;

/**
 * Created by serge on 03.06.2017.
 */

public class DataManager {

    private static DataManager ourInstance;

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }
}
