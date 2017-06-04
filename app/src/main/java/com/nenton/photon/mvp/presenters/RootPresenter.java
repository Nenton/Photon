package com.nenton.photon.mvp.presenters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.ActivityResultDto;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.IRootView;
import com.nenton.photon.ui.activities.RootActivity;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mortar.MortarScope;
import mortar.Presenter;
import mortar.bundler.BundleService;

import rx.subjects.PublishSubject;

public class RootPresenter extends Presenter<IRootView> {

    private PublishSubject<ActivityResultDto> mActivityResultSubject = PublishSubject.create();

    private static int DEFAULT_MODE = 0;
    private static int TAB_MODE = 1;

    FloatingActionButton mButton;

    @Override
    protected BundleService extractBundleService(IRootView view) {
        return BundleService.getBundleService((RootActivity) view);
    }

    public PublishSubject<ActivityResultDto> getActivityResultSubject() {
        return mActivityResultSubject;
    }

    @Override
    protected void onEnterScope(MortarScope scope) {
        super.onEnterScope(scope);
        ((RootActivity.RootComponent) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
    }

    @Nullable
    public IRootView getRootView() {
        return getView();
    }

    public ActionBarBuilder newActionBarBuilder() {
        return this.new ActionBarBuilder();
    }

    public boolean checkPermissionsAndRequestIfNotGranted(@NonNull String[] permissions, int requestCode) {
        boolean allGranted = true;
        allGranted = ((RootActivity) getView()).isAllGranted(permissions, allGranted);

        if (!allGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((RootActivity) getView()).requestPermissions(permissions, requestCode);
            }
            return false;
        }
        return allGranted;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mActivityResultSubject.onNext(new ActivityResultDto(requestCode, resultCode, intent));
    }

    public void onRequestPermissionResult(int requetCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        // TODO: 18.12.2016 implement me
    }

    public class ActionBarBuilder {
        private boolean isGoBack = false;
        private boolean isVisable = true;
        private CharSequence title;
        private List<MenuItemHolder> items = new ArrayList<>();
        private ViewPager pager;
        private int toolbarMode = DEFAULT_MODE;

        public ActionBarBuilder setBackArrow(boolean enable) {
            this.isGoBack = enable;
            return this;
        }

        public ActionBarBuilder setVisable(boolean visibale) {
            this.isVisable = visibale;
            return this;
        }

        public ActionBarBuilder addAction(MenuItemHolder menuItem) {
            this.items.add(menuItem);
            return this;
        }

        public ActionBarBuilder setTab(ViewPager pager) {
            this.toolbarMode = TAB_MODE;
            this.pager = pager;
            return this;
        }

        public ActionBarBuilder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public void build() {
            if (getRootView() != null) {
                RootActivity activity = (RootActivity) getRootView();
                activity.setVisable(isVisable);
                activity.setTitle(title);
                activity.setBackArrow(isGoBack);
                activity.setMenuItem(items);
                if (toolbarMode == TAB_MODE) {
                    activity.setTabLayout(pager);
                } else {
                    activity.removeTabLayout();
                }
            }
        }
    }
}