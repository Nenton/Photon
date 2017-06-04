package com.nenton.photon.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private final SharedPreferences mSharedPreferences;

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public PreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences("softesing", Context.MODE_PRIVATE);
    }
}