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
            dialogSignUp = createDialogSignUp();
        }
        dialogSignUp.show();
    }

    private AlertDialog createDialogSignUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_registration_account, null);
        TextInputLayout login_ti = (TextInputLayout) view.findViewById(R.id.registration_login_til);
        TextInputLayout email_ti = (TextInputLayout) view.findViewById(R.id.registration_email_til);
        TextInputLayout name_ti = (TextInputLayout) view.findViewById(R.id.registration_name_til);
        TextInputLayout password_ti = (TextInputLayout) view.findViewById(R.id.registration_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_up_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_up_negative_btn);

        login_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 2) {
                    login_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    login_ti.setBackground(null);
                    login_ti.setHint("Логин");
                } else {
                    login_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.error));
                    login_ti.setBackground(getContext().getResources().getDrawable(R.drawable.et_normal_state));
                    login_ti.setHint("Логин (Не валидный логин)");
                }
            }
        });

        email_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_EMAIL)) {
                    email_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    email_ti.setBackground(null);
                    email_ti.setHint("E-mail");
                } else {
                    email_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.error));
                    email_ti.setBackground(getContext().getResources().getDrawable(R.drawable.et_normal_state));
                    email_ti.setHint("E-mail (Не валидный E-mail)");
                }
            }
        });

        name_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 2) {
                    name_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    name_ti.setBackground(null);
                    name_ti.setHint("Имя");
                } else {
                    name_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.error));
                    name_ti.setBackground(getContext().getResources().getDrawable(R.drawable.et_normal_state));
                    name_ti.setHint("Имя (Не валидное имя)");
                }
            }
        });

        password_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_PASSWORD) && s.toString().length() > 7) {
                    password_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    password_ti.setBackground(null);
                    password_ti.setHint("Пароль");
                } else {
                    password_ti.getEditText().setTextColor(getContext().getResources().getColor(R.color.error));
                    password_ti.setBackground(getContext().getResources().getDrawable(R.drawable.et_normal_state));
                    password_ti.setHint("Пароль (Не валидный пароль)");
                }
            }
        });

        okBtn.setOnClickListener(v -> {
            String login = login_ti.getEditText().getText().toString();
            String email = email_ti.getEditText().getText().toString();
            String name = name_ti.getEditText().getText().toString();
            String password = password_ti.getEditText().getText().toString();

            if (login.length() > 3 && email.matches(ConstantsManager.REG_EXP_EMAIL) && name.length() > 2 &&
                    password.matches(ConstantsManager.REG_EXP_PASSWORD) && password.length() > 7) {
                mPresenter.signUp(new UserCreateReq(name, login, email, password));
            }
        });

        cancelBtn.setOnClickListener(v -> {
            cancelSignUp();
        });

        return builder.setTitle("Регистрация")
                .setView(view)
                .create();
    }

    public void cancelSignUp() {
        if (dialogSignUp != null) {
            dialogSignUp.cancel();
        }
    }

    public void signIn() {
        if (dialogSignIn == null) {
            dialogSignIn = createDialogSignIn();
        }
        dialogSignIn.show();
    }

    private AlertDialog createDialogSignIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_enter_account, null);
        TextInputLayout email_til = (TextInputLayout) view.findViewById(R.id.sign_in_email_til);
        TextInputLayout password_til = (TextInputLayout) view.findViewById(R.id.sing_in_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_in_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_in_negative_btn);

        email_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_EMAIL)) {
                    email_til.getEditText().setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    email_til.setBackground(null);
                    email_til.setHint("E-mail");
                } else {
                    email_til.getEditText().setTextColor(getContext().getResources().getColor(R.color.error));
                    email_til.setBackground(getContext().getResources().getDrawable(R.drawable.et_normal_state));
                    email_til.setHint("E-mail (Не валидный E-mail)");
                }
            }
        });

        password_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_PASSWORD) && s.toString().length() > 7) {
                    password_til.getEditText().setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    password_til.setBackground(null);
                    password_til.setHint("Пароль");
                } else {
                    password_til.getEditText().setTextColor(getContext().getResources().getColor(R.color.error));
                    password_til.setBackground(getContext().getResources().getDrawable(R.drawable.et_normal_state));
                    password_til.setHint("Пароль (Не валидный пароль)");
                }
            }
        });

        cancelBtn.setOnClickListener(v -> {
            cancelSignIn();
        });

        okBtn.setOnClickListener(v -> {
            String email = email_til.getEditText().getText().toString();
            String password = password_til.getEditText().getText().toString();

            if (email.matches(ConstantsManager.REG_EXP_EMAIL) && password.matches(ConstantsManager.REG_EXP_PASSWORD) && password.length() > 7) {
                mPresenter.signIn(new UserLoginReq(email, password));
            }
        });

        return builder.setTitle("Вход в аккаунт")
                .setView(view)
                .create();
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
