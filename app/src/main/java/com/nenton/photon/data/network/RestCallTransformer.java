package com.nenton.photon.data.network;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.network.errors.ErrorUtils;
import com.nenton.photon.data.network.errors.ForbiddenApiError;
import com.nenton.photon.data.network.errors.NetworkAvailableError;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.NetworkStatusChecker;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by serge on 03.01.2017.
 */

public class RestCallTransformer<R> implements Observable.Transformer<Response<R>, R> {

    @Override
    @RxLogObservable
    public Observable<R> call(Observable<Response<R>> responseObservable) {
        Observable<Boolean> networkStatus;

        networkStatus = NetworkStatusChecker.isInternetAvailible();
        return networkStatus
                .flatMap(aBoolean -> aBoolean ? responseObservable : Observable.error(new NetworkAvailableError()))
                .flatMap(rResponse -> {
                    switch (rResponse.code()){
                        case 200:
                            String lastModified = rResponse.headers().get(ConstantsManager.LAST_MODIFIED_HEADER);
                            if (lastModified != null){
                                DataManager.getInstance().getPreferencesManager().saveLastProductUpdate(lastModified);
                            }
                            return Observable.just(rResponse.body());
                        case 201:
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
}