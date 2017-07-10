package com.nenton.photon.mvp.views;

import android.support.annotation.Nullable;

import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.realm.UserRealm;

import java.util.List;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAddPhotocardView {
    void showPhotoPanel();
    void showPropertyPanel();
    void showView(UserRealm userRealm);
    @Nullable
    FiltersDto getFilters();
    @Nullable
    String getNamePhotocard();
    @Nullable
    String getIdAlbum();
    void addTag(String string);
}
