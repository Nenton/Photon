package com.nenton.photon.data.managers;

import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by serge on 09.01.2017.
 */

public class RealmManager {

    private Realm mRealmInstance;

    public Realm getQueryRealmInstance() {
        if (mRealmInstance == null || mRealmInstance.isClosed()){
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }


    public void savePhotocardResponseToRealm(Photocard photocardRes){
        Realm realm = Realm.getDefaultInstance();
        PhotocardRealm photocardRealm = new PhotocardRealm(photocardRes);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(photocardRealm));
        realm.close();
    }

    public void saveAlbumResponseToRealm(Album album){
        Realm realm = Realm.getDefaultInstance();
        AlbumRealm albumRealm = new AlbumRealm(album);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(albumRealm));
        realm.close();
    }

    public void saveAccountInfoToRealm(SignInRes user){
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = new UserRealm(user);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(userRealm));
        realm.close();
    }

    public Observable<UserRealm> getUserById(String id){
        UserRealm userRealms = getQueryRealmInstance().where(UserRealm.class).equalTo("id", id).findFirst();
        return userRealms.asObservable();
    }


    public void deleteFromRealm(Class<? extends RealmObject> entityRealmClass, String id) {
        Realm realm = Realm.getDefaultInstance();
        RealmObject entity = realm.where(entityRealmClass).equalTo("id", id).findFirst();
        if (entity != null){
            realm.executeTransaction(realm1 -> entity.deleteFromRealm());
        }
        realm.close();
    }

//    public Observable<ProductRealm> getAllProductFromRealm() {
//        RealmResults<ProductRealm> managedProduct = getQueryRealmInstance().where(ProductRealm.class).findAllAsync();
//        return managedProduct
//                .asObservable()
//                .filter(RealmResults::isLoaded)
//                //.first()// if need cold observable
//                .flatMap(Observable::from);
//    }

    public Observable<PhotocardRealm> getAllPhotocardFromRealm() {
        RealmResults<PhotocardRealm> photocardRealms = getQueryRealmInstance().where(PhotocardRealm.class).findAllAsync();
        return photocardRealms
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }
}
