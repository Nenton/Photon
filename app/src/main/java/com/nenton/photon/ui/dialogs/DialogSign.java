package com.nenton.photon.ui.dialogs;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.TextWatcherEditText;

/**
 * Created by serge on 19.06.2017.
 */

public class DialogSign {
    public static AlertDialog createDialogSignUp(Context context, SignUpListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_registration_account, null);
        TextInputLayout login_ti = (TextInputLayout) view.findViewById(R.id.registration_login_til);
        TextInputLayout email_ti = (TextInputLayout) view.findViewById(R.id.registration_email_til);
        TextInputLayout name_ti = (TextInputLayout) view.findViewById(R.id.registration_name_til);
        TextInputLayout password_ti = (TextInputLayout) view.findViewById(R.id.registration_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_up_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_up_negative_btn);

        okBtn.setOnClickListener(v -> {
            String login = login_ti.getEditText().getText().toString();
            String email = email_ti.getEditText().getText().toString();
            String name = name_ti.getEditText().getText().toString();
            String password = password_ti.getEditText().getText().toString();

            if (login.matches(ConstantsManager.REG_EXP_LOGIN)
                    && email.matches(ConstantsManager.REG_EXP_EMAIL)
                    && name.matches(ConstantsManager.REG_EXP_NAME)
                    && password.matches(ConstantsManager.REG_EXP_PASSWORD_SIMPLE)) {
                listener.action(new UserCreateReq(name, login, email, password));
            } else {
                login_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_LOGIN)) {
                            login_ti.getEditText().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            login_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            login_ti.setHint("Логин");
                        } else {
                            login_ti.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            login_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            login_ti.setHint("Логин (Не валидный логин)");
                        }
                    }
                });

                email_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_EMAIL)) {
                            email_ti.getEditText().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            email_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            email_ti.setHint("E-mail");
                        } else {
                            email_ti.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            email_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            email_ti.setHint("E-mail (Не валидный E-mail)");
                        }
                    }
                });

                name_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_NAME)) {
                            name_ti.getEditText().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            name_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            name_ti.setHint("Имя");
                        } else {
                            name_ti.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            name_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            name_ti.setHint("Имя (Не валидное имя)");
                        }
                    }
                });

                password_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_PASSWORD_SIMPLE)) {
                            password_ti.getEditText().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            password_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            password_ti.setHint("Пароль");
                        } else {
                            password_ti.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            password_ti.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            password_ti.setHint("Пароль (Не валидный пароль)");
                        }
                    }
                });
            }
        });


        AlertDialog signUp = builder.setTitle("Регистрация")
                .setView(view)
                .create();
        signUp.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        cancelBtn.setOnClickListener(v -> {
            signUp.cancel();
        });
        return signUp;
    }

    public interface SignUpListener {
        void action(UserCreateReq createReq);
    }

    public static AlertDialog createDialogSignIn(Context context, SignInListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_enter_account, null);
        TextInputLayout email_til = (TextInputLayout) view.findViewById(R.id.sign_in_email_til);
        TextInputLayout password_til = (TextInputLayout) view.findViewById(R.id.sing_in_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_in_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_in_negative_btn);

        okBtn.setOnClickListener(v -> {
            String email = email_til.getEditText().getText().toString();
            String password = password_til.getEditText().getText().toString();

            if (email.matches(ConstantsManager.REG_EXP_EMAIL) && password.matches(ConstantsManager.REG_EXP_PASSWORD_SIMPLE)) {
                listener.action(new UserLoginReq(email, password));
            } else {
                email_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_EMAIL)) {
                            email_til.getEditText().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            email_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            email_til.setHint("E-mail");
                        } else {
                            email_til.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            email_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            email_til.setHint("E-mail (Не валидный E-mail)");
                        }
                    }
                });

                password_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_PASSWORD_SIMPLE)) {
                            password_til.getEditText().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            password_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            password_til.setHint("Пароль");
                        } else {
                            password_til.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            password_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            password_til.setHint("Пароль (Не валидный пароль)");
                        }
                    }
                });

            }
        });

        AlertDialog dialog = builder.setTitle("Вход в аккаунт")
                .setView(view)
                .create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        cancelBtn.setOnClickListener(v -> {
            dialog.cancel();
        });

        return dialog;
    }

    public interface SignInListener {
        void action(UserLoginReq loginReq);
    }
}
