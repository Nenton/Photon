package com.nenton.photon.data.network.req;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class PhotocardReq {
    public String album;
    public String title;
    public String photo;
    public List<String> tags = null;
    public Filters filters;

    public PhotocardReq(String album, String title, String photo, List<String> tags, Filters filters) {
        this.album = album;
        this.title = title;
        this.photo = photo;
        this.tags = tags;
        this.filters = filters;
    }

    public class Filters {
        public Filters(String dish, String nuances, String decor, String temperature, String light, String lightDirection, String lightSource) {
            this.dish = dish;
            this.nuances = nuances;
            this.decor = decor;
            this.temperature = temperature;
            this.light = light;
            this.lightDirection = lightDirection;
            this.lightSource = lightSource;
        }

        public String dish;
        public String nuances;
        public String decor;
        public String temperature;
        public String light;
        public String lightDirection;
        public String lightSource;

    }
}
