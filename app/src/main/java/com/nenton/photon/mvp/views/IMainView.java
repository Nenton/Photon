package com.nenton.photon.mvp.views;

import com.nenton.photon.data.storage.dto.PhotoDto;

import java.util.List;

/**
 * Created by serge on 04.06.2017.
 */

public interface IMainView {
    void showPhotos(List<PhotoDto> photoList);
    void showSearch();
    void showPhoto(int id);
    void editCountFav(int id);
}
