package com.nenton.photon.mvp.views;

import android.support.annotation.Nullable;

import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;

import java.util.List;

/**
 * Created by serge on 08.07.2017.
 */

public interface IEditPhotocardView {
    void showView(PhotocardRealm photocardRealm);
    @Nullable
    FiltersDto getFilters();
    @Nullable
    String getNamePhotocard();
    @Nullable
    List<String> getTags();
    @Nullable
    String getIdAlbum();
    void addTag(String string);
    void initAlbums(UserRealm userRealm);
}
