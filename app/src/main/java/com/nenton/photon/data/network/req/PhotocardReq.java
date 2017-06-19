package com.nenton.photon.data.network.req;

import com.nenton.photon.data.storage.dto.FiltersDto;

import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class PhotocardReq {
    private String album;
    private String title;
    private String photo;
    private List<String> tags = null;
    private Filters filters;

    public PhotocardReq(String album, String title, String photo, List<String> tags, FiltersDto filters) {
        this.album = album;
        this.title = title;
        this.photo = photo;
        this.tags = tags;
        this.filters =  new Filters(filters);
    }

    public class Filters {
        private String dish;
        private String nuances;
        private String decor;
        private String temperature;
        private String light;
        private String lightDirection;
        private String lightSource;

        public Filters(FiltersDto filters) {
            this.dish = filters.getDish();
            this.nuances = filters.getNuances();
            this.decor = filters.getDecor();
            this.temperature = filters.getTemperature();
            this.light = filters.getLight();
            this.lightDirection = filters.getLightDirection();
            this.lightSource = filters.getLightSource();
        }
    }
}
