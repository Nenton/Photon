package com.nenton.photon.data.managers;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by serge on 09.01.2017.
 */

public class RealmManager {

    private Realm mRealmInstance;

    public void saveProductResponseToRealm(){
        Realm realm = Realm.getDefaultInstance();

//        ProductRealm productRealm = new ProductRealm(productRes);
//
//        if (!productRes.getComments().isEmpty()){
//            Observable.from(productRes.getComments())
//                    .doOnNext(comment -> {
//                        if (!comment.isActive()){
//                            deleteFromRealm(CommentRealm.class, comment.getId());
//                        }})
//                    .filter(Comment::isActive)
//                    .map(CommentRealm::new)
//                    .subscribe(commentRealm -> productRealm.getCommentRealms().add(commentRealm));
//
//        }
//
//        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(productRealm));
        realm.close();
    }

    public void deleteFromRealm(Class<? extends RealmObject> entityRealmClass, String id) {
        Realm realm = Realm.getDefaultInstance();
//        RealmObject entity = realm.where(entityRealmClass).equalTo("id", id).findFirst();
//        if (entity != null){
//            realm.executeTransaction(realm1 -> entity.deleteFromRealm());
//        }
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

    public Realm getQueryRealmInstance() {
        if (mRealmInstance == null || mRealmInstance.isClosed()){
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }
}
