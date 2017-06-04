package com.nenton.photon.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.ActivityResultDto;
import com.nenton.photon.di.components.AppComponent;
import com.nenton.photon.di.modules.PicassoCacheModule;
import com.nenton.photon.di.modules.RootModule;
import com.nenton.photon.di.sqopes.RootScope;
import com.nenton.photon.mvp.presenters.MenuItemHolder;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.mvp.views.IActionBarView;
import com.nenton.photon.mvp.views.IRootView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.subjects.PublishSubject;

public class RootActivity extends AppCompatActivity implements IRootView, IActionBarView{

    private PublishSubject<ActivityResultDto> mActivityResultSubject = PublishSubject.create();

    @Inject
    RootPresenter mRootPresenter;

    @BindView(R.id.root_frame)
    FrameLayout mFrameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showLoad() {

    }

    @Override
    public void hideLoad() {

    }

    @Override
    public void setVisable(boolean visable) {

    }

    @Override
    public void setBackArrow(boolean enabled) {

    }

    @Override
    public void setMenuItem(List<MenuItemHolder> items) {

    }

    @Override
    public void setTabLayout(ViewPager pager) {

    }

    @Override
    public void removeTabLayout() {

    }

    public PublishSubject<ActivityResultDto> getActivityResultSubject() {
        return mActivityResultSubject;
    }

    public boolean isAllGranted(@NonNull String[] permissions, boolean allGranted) {
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission((this), permission);
            if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRootPresenter.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRootPresenter.onActivityResult(requestCode, resultCode, data);
    }

    //region ========================= DI =========================

    @dagger.Component(dependencies = AppComponent.class, modules = {RootModule.class, PicassoCacheModule.class})
    @RootScope
    public interface RootComponent {
        void inject(RootActivity rootActivity);
        void inject(RootPresenter rootPresenter);
        RootPresenter getRootPresenter();
        Picasso getPicasso();
    }
    //endregion
}
