package com.nenton.photon.data.storage.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class StringRealm extends RealmObject {

    @PrimaryKey
    private String mString;

    public StringRealm() {
    }

    public StringRealm(String string) {
        mString = string;
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
    }
}
