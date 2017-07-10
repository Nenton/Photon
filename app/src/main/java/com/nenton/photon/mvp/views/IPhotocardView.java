package com.nenton.photon.mvp.views;

import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;

/**
 * Created by serge on 08.07.2017.
 */

public interface IPhotocardView {
    void initUser(UserInfoDto infoDto);
    void initPhoto(PhotocardRealm photocardRealm);
    void showDialogAddFav();
    void showDialogSharePhoto();
}
