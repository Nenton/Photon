package com.nenton.photon.mvp.presenters;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.nenton.photon.mvp.model.AbstractModel;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IRootView;

import javax.inject.Inject;

import mortar.MortarScope;
import mortar.ViewPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class AbstractPresenter<V extends AbstractView, M extends AbstractModel> extends ViewPresenter<V> {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    protected M mModel;

    @Inject
    protected RootPresenter mRootPresenter;

    protected CompositeSubscription mCompSubs;

    @Override
    protected void onEnterScope(MortarScope scope) {
        super.onEnterScope(scope);
        initDagger(scope);
    }


    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        mCompSubs = new CompositeSubscription();
        initActionBar();
        initMenuPopup();
    }

    @Override
    public void dropView(V view) {
        if (mCompSubs.hasSubscriptions()){
            mCompSubs.unsubscribe();
        }
        super.dropView(view);
    }

    protected abstract void initActionBar();
    protected abstract void initMenuPopup();
    protected abstract void initDagger(MortarScope scope);


    protected IRootView getRootView(){
        return mRootPresenter.getRootView();
    }

    protected abstract class ViewSubscriber<T> extends Subscriber<T> {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleter observable");
        }

        @Override
        public void onError(Throwable e) {
            if (getRootView() != null){
                getRootView().showError(e);
            }
        }

        @Override
        public abstract void onNext(T t);
    }

    protected <T> Subscription subscribe(Observable<T> observable, ViewSubscriber<T> subscriber){
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
