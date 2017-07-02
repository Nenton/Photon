package com.nenton.photon.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.ActivityResultDto;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.components.AppComponent;
import com.nenton.photon.di.modules.PicassoCacheModule;
import com.nenton.photon.di.modules.RootModule;
import com.nenton.photon.di.sqopes.RootScope;
import com.nenton.photon.flow.TreeKeyDispatcher;
import com.nenton.photon.mvp.presenters.MenuItemHolder;
import com.nenton.photon.mvp.presenters.PopupMenuItem;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.mvp.views.IActionBarView;
import com.nenton.photon.mvp.views.IRootView;
import com.nenton.photon.mvp.views.IView;
import com.nenton.photon.ui.screens.account.AccountScreen;
import com.nenton.photon.ui.screens.add_photocard.AddPhotocardScreen;
import com.nenton.photon.ui.screens.main.MainScreen;
import com.nenton.photon.ui.screens.search_filters.SearchEnum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import rx.subjects.PublishSubject;

public class RootActivity extends AppCompatActivity implements IRootView, IActionBarView {


    @Inject
    RootPresenter mRootPresenter;
    @BindView(R.id.root_frame)
    FrameLayout mFrameContainer;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_root)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.popup_menu)
    View mView;

    private ActionBarDrawerToggle mToggle;
    private ActionBar mActionBar;
    private List<MenuItemHolder> mActionBarMenuItems;

    private List<PopupMenuItem> mMenuPopups;
    private int mMenuIdRes;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        ButterKnife.bind(this);
        RootComponent rootComponent = DaggerService.getDaggerComponent(this);
        rootComponent.inject(this);
        mRootPresenter.takeView(this);
        initToolbar();
        initBottomNavView();
    }

    private void initBottomNavView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Object key = null;
            switch (item.getItemId()) {
                case R.id.action_home:
                    key = new MainScreen();
                    break;
                case R.id.action_account:
                    key = new AccountScreen();
                    break;
                case R.id.action_upload:
                    key = new AddPhotocardScreen();
                    break;
            }

            if (key != null) {
                Flow.get(RootActivity.this).set(key);
            }
            return true;
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        mRootPresenter.dropView(this);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Flow.configure(newBase, this)
                .defaultKey(new MainScreen())
                .dispatcher(new TreeKeyDispatcher(this))
                .install();
        super.attachBaseContext(newBase);
    }

    @Override
    public Object getSystemService(String name) {
        MortarScope rootActivityScope = MortarScope.findChild(getApplicationContext(), RootActivity.class.getName());
        return rootActivityScope.hasService(name) ? rootActivityScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void showSearchSetting(String message, SnackBarAction action) {
        snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Отменить", v -> {
            mRootPresenter.setSearchEnum(SearchEnum.NONE);
            action.action();
            snackbar.dismiss();
            snackbar = null;
        });
        snackbar.setActionTextColor(getApplicationContext().getResources().getColor(R.color.white));
        View view = snackbar.getView();
        view.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        TextView textView = ((TextView) view.findViewById(android.support.design.R.id.snackbar_text));
        textView.setTextColor(getApplicationContext().getResources().getColor(R.color.blackMessage));
        snackbar.show();
    }

    public void showFilterSetting(String message, SnackBarAction action) {
        snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Отменить", v -> {
            mRootPresenter.setSearchEnum(SearchEnum.NONE);
            action.action();
            snackbar.dismiss();
            snackbar = null;
        });
        snackbar.setActionTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        View view = snackbar.getView();
        view.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.blackMessage));
        TextView textView = ((TextView) view.findViewById(android.support.design.R.id.snackbar_text));
        textView.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        snackbar.show();

    }

    public void hideSnackbar() {
        if (snackbar != null) {
            snackbar.dismiss();
            snackbar = null;
        }
    }

    public interface SnackBarAction {
        void action();
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
    public void setVisibleToolbar(boolean visible) {
        if (visible) {
            mActionBar.show();
        } else {
            mActionBar.hide();
        }
    }

    @Override
    public void setBackArrow(boolean enabled) {
        if (mToggle != null && mActionBar != null) {
            if (enabled) {
                mToggle.setDrawerIndicatorEnabled(false);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                if (mToggle.getToolbarNavigationClickListener() == null) {
                    mToggle.setToolbarNavigationClickListener(v -> onBackPressed());
                }
            } else {
                mToggle.setDrawerIndicatorEnabled(false);
                mActionBar.setDisplayHomeAsUpEnabled(false);
                mToggle.setToolbarNavigationClickListener(null);
            }

            mToggle.syncState();
        }
    }

    @Override
    public void setMenuItem(List<MenuItemHolder> items) {
        mActionBarMenuItems = items;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mActionBarMenuItems != null && !mActionBarMenuItems.isEmpty()) {
            for (MenuItemHolder menuItem : mActionBarMenuItems) {
                MenuItem item = menu.add(menuItem.getTitle());
                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                        .setIcon(menuItem.getIconResId())
                        .setOnMenuItemClickListener(menuItem.getListener());
            }
        } else {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTabLayout(ViewPager pager) {
        TabLayout tabView = new TabLayout(this);
        tabView.setupWithViewPager(pager);
        mAppBarLayout.addView(tabView);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabView));
    }

    @Override
    public void removeTabLayout() {
        View tabView = mAppBarLayout.getChildAt(1);
        if (tabView != null && tabView instanceof TabLayout) {
            mAppBarLayout.removeView(tabView);
        }
    }

    @Nullable
    @Override
    public IView getCurrentScreen() {
        return (IView) mFrameContainer.getChildAt(0);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentScreen() != null && !getCurrentScreen().viewOnBackPressed() && !Flow.get(this).goBack()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Выход")
                    .setPositiveButton("Да", (dialog, which) -> super.onBackPressed())
                    .setNegativeButton("Нет", (dialog, which) -> dialog.cancel())
                    .setMessage("Вы действительно хотите выйти?")
                    .show();
        }
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

    public void setMenuIdRes(int menuIdRes) {
        this.mMenuIdRes = menuIdRes;
    }

    public void setMenuPopup(List<PopupMenuItem> menuPopup) {
        mMenuPopups = menuPopup;
    }

    @Override
    public void showSettings() {
        if (mMenuPopups != null && !mMenuPopups.isEmpty()) {
            PopupMenu menu = new PopupMenu(this, mView, Gravity.RIGHT);
            menu.inflate(mMenuIdRes);
            menu.setOnMenuItemClickListener(item -> {
                for (PopupMenuItem menuPopup : mMenuPopups) {
                    if (item.getItemId() == menuPopup.getItemId()) {
                        menuPopup.getAction().action();
                        return true;
                    }
                }
                return false;
            });
            menu.show();
        }
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
