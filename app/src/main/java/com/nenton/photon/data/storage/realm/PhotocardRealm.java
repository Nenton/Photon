package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.res.Photocard;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class PhotocardRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String owner;
    private String title;
    private String photo;
    private int views;
    private int favorits;
    private RealmList<StringRealm> tags;
    private FiltersRealm filters;

    public PhotocardRealm() {
    }

    public PhotocardRealm(String photo) {
        this.photo = photo;
    }

    public PhotocardRealm(String photo, int views, int favorits) {
        this.photo = photo;
        this.views = views;
        this.favorits = favorits;
    }

    public PhotocardRealm(String photo, int views, int favorits, String title, RealmList<StringRealm> tags) {
        this.title = title;
        this.photo = photo;
        this.views = views;
        this.favorits = favorits;
        this.tags = tags;
    }

    public PhotocardRealm(Photocard photocardRes) {
        this.id = photocardRes.getId();
        this.owner = photocardRes.getOwner();
        this.title = photocardRes.getTitle();
        this.photo = photocardRes.getPhoto();
        this.views = photocardRes.getViews();
        this.favorits = photocardRes.getFavorits();
        this.filters = new FiltersRealm(photocardRes.getId(), photocardRes.getFilters());

        tags = new RealmList<>();
        for (String s : photocardRes.getTags()) {
            tags.add(new StringRealm("#" + s));
        }
    }


    public RealmList<StringRealm> getTags() {
        return tags;
    }

    public FiltersRealm getFilters() {
        return filters;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public int getViews() {
        return views;
    }

    public int getFavorits() {
        return favorits;
    }
}
