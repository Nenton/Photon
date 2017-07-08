package com.nenton.photon.mvp.presenters;

/**
 * Created by serge on 08.07.2017.
 */

public interface IAddPhotocardPresenter {
    void initPropertyView();
    void savePhotocard();
    void cancelCreate();
    void addAlbum(String name, String description);
    void addAlbumFrom(String name, String description);
    void goAccount();
}
