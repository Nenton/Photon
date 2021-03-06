package com.nenton.photon.ui.screens.account;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
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
import com.nenton.photon.mvp.views.IAccountView;
import com.nenton.photon.ui.dialogs.DialogEditUser;
import com.nenton.photon.ui.dialogs.DialogSign;
import com.nenton.photon.ui.dialogs.DialogsAlbum;
import com.nenton.photon.utils.AvatarTransform;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.TextWatcherEditText;
import com.nenton.photon.utils.ViewHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AccountView extends AbstractView<AccountScreen.AccountPresenter> implements IAccountView {

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
    @BindView(R.id.info_counts_wrap)
    LinearLayout mInfoCountsWrap;
    @BindView(R.id.show_not_album)
    TextView mNotAlbum;

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

    @Override
    public void showAuthState(UserRealm userRealm) {
        mRecycleView.setNestedScrollingEnabled(false);
        mUserWrap.setVisibility(VISIBLE);
        mNoUserWrap.setVisibility(GONE);

        insertAvatar(userRealm.getAvatar());

        mLogin.setText(userRealm.getLogin() + " / " + userRealm.getName());

        mAccountAdapter.reloadAdapter();

        if (!userRealm.getAlbums().isEmpty()) {
            mInfoCountsWrap.setVisibility(VISIBLE);
            mNotAlbum.setVisibility(GONE);
            int countPhotocard = 0;
            for (AlbumRealm albumRealm : userRealm.getAlbums()) {
                countPhotocard += albumRealm.getPhotocards().size();
                mAccountAdapter.addAlbum(albumRealm);
            }
            mAlbumCount.setText(String.valueOf(userRealm.getAlbums().size()));
            mPhotocardCount.setText(String.valueOf(countPhotocard));
        } else {
            mInfoCountsWrap.setVisibility(GONE);
            mNotAlbum.setVisibility(VISIBLE);
        }

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAccountAdapter);
        showAnim();
    }

    private void showAnim() {
        final int cx = (int) ViewHelper.getDensity(getContext()) * 44;
        final int cy = (int) ViewHelper.getDensity(getContext()) * 44;

        Animator showCircleAnim = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showCircleAnim = ViewAnimationUtils.createCircularReveal(mUserWrap, cx, cy, ViewHelper.getDensity(getContext()) * 28, 1500);
            showCircleAnim.setDuration(600);
            showCircleAnim.setStartDelay(mPresenter.getDelayAnim());
            showCircleAnim.start();
        }
        mPresenter.setDelay(0);
    }

    @Override
    public void showUnAuthState() {
        final Fade fade = new Fade();
        fade.setInterpolator(new FastOutSlowInInterpolator());
        fade.setDuration(300);
        TransitionManager.beginDelayedTransition(this, fade);
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
            dialogSignUp = null;
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
            dialogSignIn = null;
        }
    }

    @Override
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

    @Override
    public void showDialogEditUserInfo(String loginOld, String nameOld) {
        if (dialogEditUserInfo == null) {
            dialogEditUserInfo = DialogEditUser.editUserInfoDialog(getContext(), loginOld, nameOld, (name, login) -> {
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

    @Override
    public void showExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выход из профиля")
                .setMessage("Вы действительной хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> {
                    mPresenter.exitAccount();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    dialog.cancel();
                })
                .create()
                .show();
    }

    public void insertAvatar(String mAvatarUri) {
        mPicasso.with(getContext())
                .load(mAvatarUri)
                .error(R.drawable.ic_account_black_24dp)
//                .placeholder(R.drawable.ic_account_black_24dp)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(200, 200)
                .centerCrop()
                .transform(new AvatarTransform())
                .into(mAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.with(getContext())
                                .load(mAvatarUri)
                                .error(R.drawable.ic_account_black_24dp)
//                                .placeholder(R.drawable.ic_account_black_24dp)
                                .resize(200, 200)
                                .centerCrop()
                                .transform(new AvatarTransform())
                                .into(mAvatar);
                    }
                });
    }
    //endregion
}
