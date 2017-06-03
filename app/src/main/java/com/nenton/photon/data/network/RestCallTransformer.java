package com.nenton.photon.data.network;

import android.support.annotation.VisibleForTesting;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.errors.ErrorUtils;
import com.nenton.photon.data.network.errors.ForbiddenApiError;
import com.nenton.photon.data.network.errors.NetworkAvailableError;
import com.nenton.photon.utils.NetworkStatusChecker;

import retrofit2.Response;
import rx.Observable;

import static android.support.annotation.VisibleForTesting.NONE;

/**
 * Created by serge on 03.01.2017.
 */

public class RestCallTransformer<R> implements Observable.Transformer<Response<R>, R> {
    private boolean mTestMode;

    @Override
    @RxLogObservable
    public Observable<R> call(Observable<Response<R>> responseObservable) {
        Observable<Boolean> networkStatus;

        if (mTestMode) {
            networkStatus = Observable.just(true);
        } else {
            networkStatus = NetworkStatusChecker.isInternetAvailible();
        }
        return networkStatus
                .flatMap(aBoolean -> aBoolean ? responseObservable : Observable.error(new NetworkAvailableError()))
                .flatMap(rResponse -> {
                    switch (rResponse.code()){
                        case 200:
//                            String lastModified = rResponse.headers().get(ConstantsManager.LAST_MODIFIED_HEADER);
//                            if (lastModified != null){
//                                DataManager.getInstance().getPreferencesManager().saveLastProductUpdate(lastModified);
//                            }
                            return Observable.just(rResponse.body());
                        case 304:
                            return Observable.empty();
                        case 403:
                            return Observable.error(new ForbiddenApiError());
                            default:
                                return Observable.error(ErrorUtils.parseError(rResponse));
                    }
                });
    }

    @VisibleForTesting(otherwise = NONE)
    public void setTestMode() {
        mTestMode = true;
    }
}
