package com.nenton.photon.data.storage.realm;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class FiltersRealm extends RealmObject{

    @PrimaryKey
    private String id;
    private RealmList<StringRealm> dish;
    private RealmList<StringRealm> nuances;
    private RealmList<StringRealm> decor;
    private RealmList<StringRealm> temperature;
    private RealmList<StringRealm> light;
    private RealmList<StringRealm> lightDirection;
    private RealmList<StringRealm> lightSource;


    public FiltersRealm() {
    }
}