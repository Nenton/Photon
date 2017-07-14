package com.nenton.photon.data.managers;

import android.support.annotation.Nullable;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.network.req.AlbumEditReq;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SignInRes;
import com.nenton.photon.data.network.res.UserInfo;
import com.nenton.photon.data.storage.dto.PhotocardDto;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.utils.SearchFilterQuery;
import com.nenton.photon.utils.SearchQuery;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.ManagableObject;
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

    public void savePhotocardResponseToRealm(PhotocardRealm photocardRes) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(photocardRes));
        realm.close();
    }

    public void saveAlbumResponseToRealm(AlbumRealm album) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(album));
        realm.close();
    }

    public void saveAccountInfoToRealm(SignInRes user) {
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = new UserRealm(user);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(userRealm));
        realm.close();
    }

    public Observable<String> getTags() {
        return getQueryRealmInstance()
                .where(StringRealm.class)
                .contains("string", "#")
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from)
                .flatMap(stringRealm -> Observable.just(stringRealm.getString()));
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
            photocardRealms.contains("title", sq.getTitle()).or().contains("tags.string", sq.getTitle());
        }

        if (sq.getTags() != null) {
            for (String s : sq.getTags()) {
                photocardRealms.contains("tags.string", s);
            }
        }
        return photocardRealms
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from);
    }

    public Observable<AlbumRealm> getAlbumById(String id) {
        RealmResults<AlbumRealm> albumRealms = getQueryRealmInstance().where(AlbumRealm.class).findAllAsync();
        return albumRealms
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from)
                .filter(albumRealm1 -> albumRealm1.getId().equals(id))
                .first();
    }

    public void saveUserInfo(UserRealm userRealm) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(userRealm));
        realm.close();
    }

    @RxLogObservable
    public Observable<UserRealm> getUserById(String id) {
        RealmResults<UserRealm> userRealms = getQueryRealmInstance().where(UserRealm.class).findAllAsync();
        return userRealms
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from)
                .filter(userRealm -> userRealm.getId().equals(id));
    }

    public Observable<PhotocardRealm> getPhotoById(String id) {
        RealmResults<PhotocardRealm> photocardRealms = getQueryRealmInstance().where(PhotocardRealm.class).findAllAsync();
        return photocardRealms
                .asObservable()
                .filter(RealmResults::isLoaded)
                .flatMap(Observable::from)
                .filter(photocardRealm1 -> photocardRealm1.getId().equals(id));
    }

    public void saveAlbumToRealm(String id, AlbumEditReq albumCreateReq) {
        Realm realm = Realm.getDefaultInstance();
        AlbumRealm first = getQueryRealmInstance().where(AlbumRealm.class).equalTo("id", id).findFirst();
        AlbumRealm albumRealm = new AlbumRealm(id, albumCreateReq, first);
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(albumRealm));
        realm.close();
    }

    public boolean haveAlbumUser(String userId) {
        Realm realm = Realm.getDefaultInstance();
        UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", userId).findFirst();
        realm.close();
        return userRealm != null && !userRealm.getAlbums().isEmpty();
    }

    public Observable<AlbumRealm> getAlbumByTitleDesc(String title, String description, String userId) {
        AlbumRealm albumRealm = getQueryRealmInstance()
                .where(AlbumRealm.class)
                .equalTo("owner", userId)
                .equalTo("title", title)
                .equalTo("description", description)
                .findFirstAsync();
        if (albumRealm != null) {
            return albumRealm.<AlbumRealm>asObservable()
                    .filter(albumRealm1 -> albumRealm1.isLoaded())
                    .first();
        } else {
            return Observable.error(new Throwable());
        }
    }

    public Observable<Boolean> isPhotoFromFav(String userId, String id) {
        Realm realm = Realm.getDefaultInstance();
        UserRealm user = realm.where(UserRealm.class).equalTo("id", userId).findFirst();
        if (user == null || user.getAlbums().isEmpty() || !user.getAlbums().get(0).isFavorite()) {
            realm.close();
            return Observable.just(false);
        }

        for (PhotocardRealm photocardRealm : user.getAlbums().get(0).getPhotocards()) {
            if (photocardRealm.getId().equals(id)){
                realm.close();
                return Observable.just(true);
            }
        }

        realm.close();
        return Observable.just(false);
    }
}
