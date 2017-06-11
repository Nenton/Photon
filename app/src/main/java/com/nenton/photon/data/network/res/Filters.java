package com.nenton.photon.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serge on 10.06.2017.
 */

public class Filters {

    @SerializedName("dish")
    @Expose
    public String dish;
    @SerializedName("nuances")
    @Expose
    public String nuances;
    @SerializedName("decor")
    @Expose
    public String decor;
    @SerializedName("temperature")
    @Expose
    public String temperature;
    @SerializedName("light")
    @Expose
    public String light;
    @SerializedName("lightDirection")
    @Expose
    public String lightDirection;
    @SerializedName("lightSource")
    @Expose
    public String lightSource;

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
