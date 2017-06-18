package com.nenton.photon.utils;

import java.util.List;

/**
 * Created by serge on 13.06.2017.
 */

public class SearchFilterQuery {
    private String dish;
    private List<String> nuances;
    private String decor;
    private String temperature;
    private String light;
    private String lightDirection;
    private String lightSource;

    public void setDish(String dish) {
        this.dish = dish;
    }

    public void setNuances(List<String> nuances) {
        this.nuances = nuances;
    }

    public void setDecor(String decor) {
        this.decor = decor;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public void setLightDirection(String lightDirection) {
        this.lightDirection = lightDirection;
    }

    public void setLightSource(String lightSource) {
        this.lightSource = lightSource;
    }

    public String getDish() {
        return dish;
    }

    public List<String> getNuances() {
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

    public boolean isReadySearchModel(){
        return dish != null || !nuances.isEmpty() || decor != null || temperature != null || light != null || lightDirection != null || lightSource != null;
    }
}
