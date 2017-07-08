package com.nenton.photon.mvp.views;

import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;

/**
 * Created by serge on 08.07.2017.
 */

public interface IPhotocardView {
    void initView(PhotocardRealm photocardRealm, UserInfoDto infoDto);
    void showDialogAddFav();
    void showDialogSharePhoto();
}
