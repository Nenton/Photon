package com.nenton.photon.utils;

import com.nenton.photon.data.storage.realm.StringRealm;

import java.util.List;
import java.util.Set;

/**
 * Created by serge on 16.06.2017.
 */

public class SearchQuery {
    private String title;
    private Set<StringRealm> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<StringRealm> getTags() {
        return tags;
    }

    public void setTags(Set<StringRealm> tags) {
        this.tags = tags;
    }
}
