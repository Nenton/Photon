package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.res.Filters;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class FiltersRealm extends RealmObject{

    @PrimaryKey
    private String id;
    private String dish;
    private String nuances;
    private String decor;
    private String temperature;
    private String light;
    private String lightDirection;
    private String lightSource;


    public FiltersRealm() {
    }

    public FiltersRealm(String id, Filters filters) {
        this.id = id;
        this.dish = filters.getDish();
        this.nuances = filters.getNuances();
        this.decor = filters.getDecor();
        this.temperature = filters.getTemperature();
        this.light = filters.getLight();
        this.lightDirection = filters.getLightDirection();
        this.lightSource = filters.getLightSource();
    }

    public String getId() {
        return id;
    }

    public String getDish() {
        return dish;
    }

    public String getNuances() {
        return nuances;
    }

    public String getDecor() {
        return decor;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getLight() {
        return light;
    }

    public String getLightDirection() {
        return lightDirection;
    }

    public String getLightSource() {
        return lightSource;
    }
}