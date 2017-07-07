package com.nenton.photon.data.network.req;

import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.realm.FiltersRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by serge on 07.06.2017.
 */

public class PhotocardReq implements Serializable{
    private String title;
    private String photo;
    private String album;
    private List<String> tags = null;
    private Filters filters;

    public PhotocardReq(String namePhoto, String url, String album, List<String> tags, FiltersDto filters) {
        this.title = namePhoto;
        this.photo = url;
        this.album = album;
        this.filters = new Filters(filters);
        this.tags = tags;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public List<String> getTags() {
        return tags;
    }

    public Filters getFilters() {
        return filters;
    }

    public class Filters implements Serializable{
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
}
