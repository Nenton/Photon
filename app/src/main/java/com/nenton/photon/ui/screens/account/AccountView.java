package com.nenton.photon.ui.screens.account;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.ui.dialogs.DialogEditUser;
import com.nenton.photon.ui.dialogs.DialogSign;
import com.nenton.photon.ui.dialogs.DialogsAlbum;
import com.nenton.photon.utils.AvatarTransform;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.TextWatcherEditText;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AccountView extends AbstractView<AccountScreen.AccountPresenter> {

    @Inject
    Picasso mPicasso;

    @BindView(R.id.no_user_wrap)
    LinearLayout mNoUserWrap;
    @BindView(R.id.user_wrap)
    LinearLayout mUserWrap;

    @BindView(R.id.account_photocard_RV)
    RecyclerView mRecycleView;
    @BindView(R.id.account_avatar_IV)
    ImageView mAvatar;
    @BindView(R.id.account_login_TV)
    TextView mLogin;
    @BindView(R.id.account_albums_count)
    TextView mAlbumCount;
    @BindView(R.id.account_photocard_count)
    TextView mPhotocardCount;

    private AlertDialog dialogSignIn = null;
    private AlertDialog dialogSignUp = null;
    private AlertDialog dialogAddAlbum = null;
    private AlertDialog dialogEditUserInfo = null;

    private AccountAdapter mAccountAdapter = new AccountAdapter();

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AccountScreen.Component>getDaggerComponent(context).inject(this);
    }


    public void showAuthState(UserRealm userRealm) {
        mUserWrap.setVisibility(VISIBLE);
        mNoUserWrap.setVisibility(GONE);

        if (userRealm.getAvatar() != null && !userRealm.getAvatar().isEmpty()) {
            mPicasso.load(userRealm.getAvatar())
                    .fit()
                    .centerCrop()
                    .transform(new AvatarTransform())
                    .into(mAvatar);
        } else {
            mPicasso.load(R.color.black)
                    .fit()
                    .centerCrop()
                    .transform(new AvatarTransform())
                    .into(mAvatar);
        }

        mLogin.setText(userRealm.getLogin());
//        mName.setText(userRealm.getName());

        mAlbumCount.setText(String.valueOf(userRealm.getAlbums().size()));

        int countPhotocard = 0;

        mAccountAdapter.reloadAdapter();

        for (AlbumRealm albumRealm : userRealm.getAlbums()) {
            countPhotocard += albumRealm.getPhotocards().size();
            mAccountAdapter.addAlbum(albumRealm);
        }

        mPhotocardCount.setText(String.valueOf(countPhotocard));

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAccountAdapter);
    }

    public void showUnAuthState() {
        mUserWrap.setVisibility(GONE);
        mNoUserWrap.setVisibility(VISIBLE);
    }

    //region ========================= Events =========================

    @OnClick(R.id.sign_in_btn)
    public void clickOnSignBtn() {
        mPresenter.clickOnSignIn();
    }

    @OnClick(R.id.sign_up_btn)
    public void clickOnSignUpBtn() {
        mPresenter.clickOnSignUp();
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
            dialogSignUp = null;
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
            dialogSignIn = null;
        }
    }

    public void showDialogAddAlbum() {
        if (dialogAddAlbum == null) {
            dialogAddAlbum = DialogsAlbum.createDialogAddAlbum(getContext(), (name, description) -> {
                mPresenter.addAlbum(name, description);
            });
        }
        dialogAddAlbum.show();
    }

    public void cancelAddAlbum() {
        if (dialogAddAlbum != null) {
            dialogAddAlbum.cancel();
            dialogAddAlbum = null;
        }
    }

    public void showDialogEditUserInfo() {
        if (dialogEditUserInfo == null) {
            dialogEditUserInfo = DialogEditUser.editUserInfoDialog(getContext(), (name, login) -> {
                mPresenter.editUserInfo(name, login);
            });
        }
        dialogEditUserInfo.show();
    }

    public void cancelEditUserInfo() {
        if (dialogEditUserInfo != null) {
            dialogEditUserInfo.cancel();
            dialogEditUserInfo = null;
        }
    }
    //endregion
}
