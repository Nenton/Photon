package com.nenton.photon.ui.dialogs;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nenton.photon.R;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.TextWatcherEditText;

/**
 * Created by serge on 20.06.2017.
 */

public class DialogEditUser {

    public static AlertDialog editUserInfoDialog(Context context, String loginOld, String nameOld, EditUserInfo listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_account, null);
        TextInputLayout login_ti = (TextInputLayout) view.findViewById(R.id.edit_login_til);
        TextInputLayout name_ti = (TextInputLayout) view.findViewById(R.id.edit_name_til);

        login_ti.getEditText().setText(loginOld);
        name_ti.getEditText().setText(nameOld);

        Button okBtn = (Button) view.findViewById(R.id.edit_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.edit_negative_btn);


        okBtn.setOnClickListener(v -> {
            String login = login_ti.getEditText().getText().toString();
            String name = name_ti.getEditText().getText().toString();

            if (login.matches(ConstantsManager.REG_EXP_LOGIN) && name.matches(ConstantsManager.REG_EXP_NAME)) {
                listener.action(name, login);
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
                login_ti.getEditText().setText(login_ti.getEditText().getText());

                if (!login.matches(ConstantsManager.REG_EXP_LOGIN)) {
                    AnimatorSet set = ((AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.invalid_field_animator));
                    set.setTarget(login_ti.getEditText());
                    set.setDuration(300);
                    set.start();
                }

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
                name_ti.getEditText().setText(name_ti.getEditText().getText());
                if (!name.matches(ConstantsManager.REG_EXP_NAME)) {
                    AnimatorSet set = ((AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.invalid_field_animator));
                    set.setTarget(name_ti.getEditText());
                    set.setDuration(300);
                    set.start();
                }
            }
        });


        AlertDialog dialog = builder.setTitle("Редактирование профиля")
                .setView(view)
                .create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        cancelBtn.setOnClickListener(v -> {
            dialog.cancel();
        });
        return dialog;
    }

    public interface EditUserInfo {
        void action(String name, String login);
    }
}
