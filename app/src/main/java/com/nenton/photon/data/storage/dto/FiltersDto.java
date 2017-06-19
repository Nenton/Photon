package com.nenton.photon.data.storage.dto;

/**
 * Created by serge on 19.06.2017.
 */

public class FiltersDto {
    private String dish;
    private String nuances;
    private String decor;
    private String temperature;
    private String light;
    private String lightDirection;
    private String lightSource;

    public FiltersDto(String dish, String nuances, String decor, String temperature, String light, String lightDirection, String lightSource) {
        this.dish = dish;
        this.nuances = nuances;
        this.decor = decor;
        this.temperature = temperature;
        this.light = light;
        this.lightDirection = lightDirection;
        this.lightSource = lightSource;
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
