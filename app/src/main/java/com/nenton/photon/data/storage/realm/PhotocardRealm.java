package com.nenton.photon.data.storage.realm;

import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.dto.PhotocardDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serge on 01.06.2017.
 */

public class PhotocardRealm extends RealmObject implements Serializable{

    @PrimaryKey
    private String id;
    private String owner;
    private String title;
    private String photo;
    private int views;
    private int favorits;
    private RealmList<StringRealm> tags;
    private FiltersRealm filters;
    private String updated;
    private String created;


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
        this.updated = photocardRes.getUpdated();
        this.created = photocardRes.getCreated();
        this.favorits = photocardRes.getFavorits();
        this.filters = new FiltersRealm(photocardRes.getId(), photocardRes.getFilters());

        tags = new RealmList<>();
        for (String s : photocardRes.getTags()) {
            tags.add(new StringRealm("#" + s));
        }
    }

    public PhotocardRealm(PhotocardDto photocardDto) {
        this.id = photocardDto.getId();
        this.owner = photocardDto.getOwner();
        this.title = photocardDto.getTitle();
        this.photo = photocardDto.getPhoto();
        this.views = photocardDto.getViews();
        this.favorits = photocardDto.getFavorits();
        this.filters = new FiltersRealm(photocardDto.getId(), photocardDto.getFilters());

        tags = new RealmList<>();
        for (String s : photocardDto.getTags()) {
            tags.add(new StringRealm("#" + s));
        }
    }

    public PhotocardRealm(String id, PhotocardReq photocardReq, PhotocardRealm first) {
        this.id = id;
        this.owner = first.getOwner();
        this.title = photocardReq.getTitle();
        this.photo = photocardReq.getPhoto();
        this.views = first.getViews();
        this.updated = first.getUpdated();
        this.created = first.getCreated();
        this.favorits = first.getFavorits();
        this.filters = new FiltersRealm(id, photocardReq.getFilters());

        tags = new RealmList<>();
        for (String s : photocardReq.getTags()) {
            tags.add(new StringRealm("#" + s));
        }
    }

    public PhotocardRealm(String userId, String namePhotocard, String url, List<String> tags, FiltersDto filters) {
        this.id = String.valueOf(this.hashCode());
        this.owner = userId;
        this.title = namePhotocard;
        this.photo = url;
        this.views = 0;
        this.updated = String.valueOf(new Date());
        this.created = String.valueOf(new Date());
        this.favorits = 0;
        this.filters = new FiltersRealm(id, filters);

        this.tags = new RealmList<>();

        for (String s : tags) {
            this.tags.add(new StringRealm(s));
        }
    }

    public PhotocardRealm(String s, PhotocardRealm photocardRealm) {
        this.id = s;
        this.owner = photocardRealm.getOwner();
        this.title = photocardRealm.getTitle();
        this.photo = photocardRealm.getPhoto();
        this.views = photocardRealm.getViews();
        this.updated = photocardRealm.getUpdated();
        this.created = photocardRealm.getCreated();
        this.favorits = photocardRealm.getFavorits();
        this.filters = photocardRealm.getFilters();

        this.tags = photocardRealm.getTags();
    }

    public PhotocardRealm(PhotocardRealm photoRealm, PhotocardReq photoReq) {
        this.id = photoRealm.getId();
        this.owner = photoRealm.getOwner();
        this.title = photoReq.getTitle();
        this.photo = photoReq.getPhoto();
        this.views = photoRealm.getViews();
        this.updated = photoRealm.getUpdated();
        this.created = photoRealm.getCreated();
        this.favorits = photoRealm.getFavorits();

        this.filters = new FiltersRealm(photoRealm.getId(), photoReq.getFilters());

        this.tags = new RealmList<>();
        for (String s : photoReq.getTags()) {
            tags.add(new StringRealm(s));
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

    public String getUpdated() {
        return updated;
    }

    public String getCreated() {
        return created;
    }
}
