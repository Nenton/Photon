package com.nenton.photon.mvp.models;

import com.nenton.photon.data.managers.FireBaseManager;

public class AccountModel extends AbstractModel {

    private FireBaseManager mFirebaseManager;

    public AccountModel() {
        mFirebaseManager = FireBaseManager.getInstance();
    }
}