package com.nenton.photon.data.managers;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.nenton.photon.utils.App;

/**
 * Created by serge on 20.05.2017.
 */

public class FireBaseManager {
    private static FireBaseManager ourInstance;

    private final FirebaseAnalytics mAnalytics;

    public static FireBaseManager getInstance() {
        if (ourInstance == null){
            ourInstance = new FireBaseManager();
        }
        return ourInstance;
    }


    private FireBaseManager() {
        mAnalytics = FirebaseAnalytics.getInstance(App.getContext());
        mAnalytics.setUserProperty("any_property", "any_value");
    }

    public FirebaseAnalytics getAnalytics() {
        return mAnalytics;
    }

}