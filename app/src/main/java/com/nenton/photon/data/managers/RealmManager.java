package com.nenton.photon.data.managers;

import android.support.annotation.Nullable;

import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.UserInfo;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.SearchQuery;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by serge on 09.01.2017.
 */

public class RealmManager {

    private Realm mRealmInstance;

    public Realm getQueryRealmInstance() {
        if (mRealmInstance == null || mRealmInstance.isClosed()) {
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }


    public void savePhotocardTags(List<String> strings) {
        Realm realm = Realm.getDefaultInstance();
        for (String s : strings) {
            StringRealm tagRealm = new StringRealm("#" + s);
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(tagRealm));
        }
        realm.close();
    }

    public Observable<StringRealm> getPhotocardTags(List<String> strings) {
        return getQueryRealmInstance()
                .where(StringRealm.class)
                .contains("string", "#")
                .findAllAsync()
                .asObservable().filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }

    public void savePhotocardResponseToRealm(Photocard photocardRes) {
        Realm realm = Realm.getDefaultInstance();
        PhotocardRealm photocardRealm = new PhotocardRealm(photocardRes);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(photocardRealm));
        realm.close();
    }

    public void saveAlbumResponseToRealm(Album album) {
        Realm realm = Realm.getDefaultInstance();
        AlbumRealm albumRealm = new AlbumRealm(album);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(albumRealm));
        realm.close();
    }

    public void saveAccountInfoToRealm(SignInRes user) {
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = new UserRealm(user);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(userRealm));
        realm.close();
    }

    public Observable<StringRealm> getTags() {
        return getQueryRealmInstance()
                .where(StringRealm.class)
                .contains("string", "#").findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }


    public void deleteFromRealm(Class<? extends RealmObject> entityRealmClass, String id) {
        Realm realm = Realm.getDefaultInstance();
        RealmObject entity = realm.where(entityRealmClass).equalTo("id", id).findFirst();
        if (entity != null) {
            realm.executeTransaction(realm1 -> entity.deleteFromRealm());
        }
        realm.close();
    }

    public Observable<PhotocardRealm> getAllPhotocardFromRealm() {
        RealmResults<PhotocardRealm> photocardRealms = getQueryRealmInstance().where(PhotocardRealm.class).findAllAsync();
        return photocardRealms
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }

    public Observable<PhotocardRealm> getAllPhotocardOnSearchFilterRealm(SearchFilterQuery sfq) {
        RealmQuery<PhotocardRealm> photocardRealms = getQueryRealmInstance()
                .where(PhotocardRealm.class);

        if (sfq.getDish() != null && !sfq.getDish().isEmpty()) {
            photocardRealms.contains("filters.dish", sfq.getDish());
        }

        if (sfq.getDecor() != null && !sfq.getDecor().isEmpty()) {
            photocardRealms.contains("filters.decor", sfq.getDecor());
        }

        if (sfq.getLight() != null && !sfq.getLight().isEmpty()) {
            photocardRealms.contains("filters.light", sfq.getLight());
        }

        if (sfq.getLightDirection() != null && !sfq.getLightDirection().isEmpty()) {
            photocardRealms.contains("filters.lightDirection", sfq.getLightDirection());
        }

        if (sfq.getLightSource() != null && !sfq.getLightSource().isEmpty()) {
            photocardRealms.contains("filters.lightSource", sfq.getLightSource());
        }

        if (sfq.getTemperature() != null && !sfq.getTemperature().isEmpty()) {
            photocardRealms.contains("filters.temperature", sfq.getTemperature());
        }

        if (sfq.getNuances() != null) {
            for (String s : sfq.getNuances()) {
                photocardRealms.contains("filters.nuances", s);
            }
        }
        return photocardRealms
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }

    public Observable<PhotocardRealm> getAllPhotocardOnSearchRealm(SearchQuery sq) {
        RealmQuery<PhotocardRealm> photocardRealms = getQueryRealmInstance()
                .where(PhotocardRealm.class);

        if (sq.getTitle() != null && !sq.getTitle().isEmpty()) {
            photocardRealms.contains("title", sq.getTitle());
        }

        if (sq.getTags() != null) {
            for (StringRealm s : sq.getTags()) {
                photocardRealms.contains("tags.string", s.getString());
            }
        }
        return photocardRealms
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }

    public void saveUserInfo(UserRealm userRealm) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(userRealm));
        realm.close();
    }

    public Observable<UserRealm> getUserById(String id) {
        UserRealm userRealm = getQueryRealmInstance().where(UserRealm.class).equalTo("id", id).findFirstAsync();
        if (userRealm != null) {
            return userRealm.asObservable();
        } else {
            return Observable.error(new Throwable());
        }
    }
}
