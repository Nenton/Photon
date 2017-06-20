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
        EditText login_ti = (EditText) view.findViewById(R.id.registration_login_til);
        EditText email_ti = (EditText) view.findViewById(R.id.registration_email_til);
        EditText name_ti = (EditText) view.findViewById(R.id.registration_name_til);
        EditText password_ti = (EditText) view.findViewById(R.id.registration_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_up_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_up_negative_btn);

        login_ti.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 2) {
                    login_ti.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    login_ti.setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                    login_ti.setHint("Логин");
                } else {
                    login_ti.setTextColor(context.getResources().getColor(R.color.error));
                    login_ti.setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                    login_ti.setHint("Логин (Не валидный логин)");
                }
            }
        });

        email_ti.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_EMAIL)) {
                    email_ti.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    email_ti.setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                    email_ti.setHint("E-mail");
                } else {
                    email_ti.setTextColor(context.getResources().getColor(R.color.error));
                    email_ti.setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                    email_ti.setHint("E-mail (Не валидный E-mail)");
                }
            }
        });

        name_ti.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 2) {
                    name_ti.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    name_ti.setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                    name_ti.setHint("Имя");
                } else {
                    name_ti.setTextColor(context.getResources().getColor(R.color.error));
                    name_ti.setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                    name_ti.setHint("Имя (Не валидное имя)");
                }
            }
        });

        password_ti.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_PASSWORD) && s.toString().length() > 7) {
                    password_ti.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    password_ti.setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                    password_ti.setHint("Пароль");
                } else {
                    password_ti.setTextColor(context.getResources().getColor(R.color.error));
                    password_ti.setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                    password_ti.setHint("Пароль (Не валидный пароль)");
                }
            }
        });

        okBtn.setOnClickListener(v -> {
            String login = login_ti.getText().toString();
            String email = email_ti.getText().toString();
            String name = name_ti.getText().toString();
            String password = password_ti.getText().toString();

            if (login.length() > 3 && email.matches(ConstantsManager.REG_EXP_EMAIL) && name.length() > 2 &&
                    password.matches(ConstantsManager.REG_EXP_PASSWORD) && password.length() > 7) {
                listener.action(new UserCreateReq(name, login, email, password));
            }
        });


        AlertDialog signUp = builder.setTitle("Регистрация")
                .setView(view)
                .create();

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
        EditText email_til = (EditText) view.findViewById(R.id.sign_in_email_til);
        EditText password_til = (EditText) view.findViewById(R.id.sing_in_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_in_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_in_negative_btn);

        email_til.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_EMAIL)) {
                    email_til.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    email_til.setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                    email_til.setHint("E-mail");
                } else {
                    email_til.setTextColor(context.getResources().getColor(R.color.error));
                    email_til.setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                    email_til.setHint("E-mail (Не валидный E-mail)");
                }
            }
        });

        password_til.addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(ConstantsManager.REG_EXP_PASSWORD) && s.toString().length() > 7) {
                    password_til.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    password_til.setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                    password_til.setHint("Пароль");
                } else {
                    password_til.setTextColor(context.getResources().getColor(R.color.error));
                    password_til.setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                    password_til.setHint("Пароль (Не валидный пароль)");
                }
            }
        });


        okBtn.setOnClickListener(v -> {
            String email = email_til.getText().toString();
            String password = password_til.getText().toString();

            if (email.matches(ConstantsManager.REG_EXP_EMAIL) && password.matches(ConstantsManager.REG_EXP_PASSWORD) && password.length() > 7) {
                listener.action(new UserLoginReq(email, password));

            }
        });

        AlertDialog dialog = builder.setTitle("Вход в аккаунт")
                .setView(view)
                .create();

        cancelBtn.setOnClickListener(v -> {
            dialog.cancel();
        });

        return dialog;
    }

    public interface SignInListener {
        void action(UserLoginReq loginReq);
    }
}
