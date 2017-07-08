package com.nenton.photon.mvp.model;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;

import rx.Observable;

/**
 * Created by serge on 08.07.2017.
 */

public class SearchModel extends AbstractModel {

    public List<String> getStrings() {
        return mDataManager.getPreferencesManager().getSearchStrings();
    }

    @RxLogObservable
    public Observable<String> getPhotocardTagsObs() {
        Observable<String> disk = mDataManager.getTagsFromRealm();
        Observable<String> network = mDataManager.getPhotocardTagsObs();

        return Observable.mergeDelayError(disk, network)
                .distinct(String::toString);
    }

    public void saveSearchString(String s) {
        mDataManager.getPreferencesManager().saveSearchString(s);
    }
}
