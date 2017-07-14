package com.nenton.photon.ui.screens.main;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IMainView;
import com.nenton.photon.ui.dialogs.DialogSign;

import butterknife.BindView;

/**
 * Created by serge on 04.06.2017.
 */

public class MainView extends AbstractView<MainScreen.MainPresenter> implements IMainView {

    private MainAdapter mMainAdapter = new MainAdapter();
    @BindView(R.id.list_photos_main_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.network_dis_wrap)
    LinearLayout networkWrap;
    @BindView(R.id.search_wrap)
    LinearLayout searchWrap;

    private AlertDialog dialogSignIn = null;
    private AlertDialog dialogSignUp = null;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<MainScreen.Component>getDaggerComponent(context).inject(this);
    }

    @Override
    public void signUp() {
        if (dialogSignUp == null) {
            dialogSignUp = DialogSign.createDialogSignUp(getContext(), createReq -> mPresenter.signUp(createReq));
        }
        dialogSignUp.show();
    }

    public void cancelSignUp() {
        if (dialogSignUp != null) {
            dialogSignUp.cancel();
        }
    }

    @Override
    public void signIn() {
        if (dialogSignIn == null) {
            dialogSignIn = DialogSign.createDialogSignIn(getContext(), loginReq -> mPresenter.signIn(loginReq));
        }
        dialogSignIn.show();
    }

    public void cancelSignIn() {
        if (dialogSignIn != null) {
            dialogSignIn.cancel();
        }
    }

    @Override
    protected void afterInflate() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mMainAdapter);
    }

    public MainAdapter getAdapter() {
        return mMainAdapter;
    }

    public void reloadAdapter() {
        mMainAdapter.reload();
    }

    public void showExitUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выход из профиля")
                .setMessage("Вы действительной хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> {
                    mPresenter.exitUser();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    dialog.cancel();
                })
                .create()
                .show();
    }


    public void showNetworkWrap(){
        networkWrap.setVisibility(VISIBLE);
    }

    public void hideNetworkWrap(){
        if (!(networkWrap.getVisibility() == GONE)){
            networkWrap.setVisibility(GONE);
        }
    }

    public void showSearchWrap(){
        searchWrap.setVisibility(VISIBLE);
    }

    public void hideSearchWrap(){
        if (!(searchWrap.getVisibility() == GONE)){
            searchWrap.setVisibility(GONE);
        }
    }
}
