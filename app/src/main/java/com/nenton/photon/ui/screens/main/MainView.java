package com.nenton.photon.ui.screens.main;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IMainView;
import com.nenton.photon.ui.dialogs.DialogSign;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.TextWatcherEditText;

import butterknife.BindView;

/**
 * Created by serge on 04.06.2017.
 */

public class MainView extends AbstractView<MainScreen.MainPresenter> implements IMainView {

    private MainAdapter mMainAdapter = new MainAdapter();
    @BindView(R.id.list_photos_main_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.anchor_popup_menu)
    View mView;

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
}
