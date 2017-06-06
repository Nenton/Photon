package com.nenton.photon.data.network.req;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class AlbumEditReq {
    public AlbumEditReq(String owner, String title, String photo, List<String> keyWord, List<String> tag, Filters filters) {
        this.owner = owner;
        this.title = title;
        this.photo = photo;
        this.keyWord = keyWord;
        this.tag = tag;
        this.filters = filters;
    }

    public String owner;
    public String title;
    public String photo;
    public List<String> keyWord = null;
    public List<String> tag = null;
    public Filters filters;

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
