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

public class DialogsAlbum {

    public static AlertDialog createDialogAddAlbum(Context context, AlbumAction action) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_album, null);
        TextInputLayout name_уе = (TextInputLayout) view.findViewById(R.id.add_album_name_til);
        TextInputLayout description_et = (TextInputLayout) view.findViewById(R.id.add_album_description_til);
        Button ok = (Button) view.findViewById(R.id.add_album_ok_btn);
        Button cancel = (Button) view.findViewById(R.id.add_album_cancel_btn);

        ok.setOnClickListener(v -> {
            String name = name_уе.getEditText().getText().toString();
            String description = description_et.getEditText().getText().toString();

            if (name.matches(ConstantsManager.REG_EXP_NAME_ALBUM) && description.matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Создание альбома")
                        .setMessage("Вы точно хотите создать альбом?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            action.action(name, description);
                        })
                        .setNegativeButton("Нет", (dialog, which) -> {
                            dialog.cancel();
                        });
                builder.create().show();
            } else {

                name_уе.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_NAME)) {
                            name_уе.getEditText().setTextColor(context.getResources().getColor(R.color.black));
                            name_уе.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            name_уе.setHint("Имя");
                        } else {
                            name_уе.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            name_уе.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            name_уе.setHint("Имя (более 2, нет (!@#$%^&*()\\|_=+-)");
                        }
                    }
                });
                name_уе.getEditText().setText(name_уе.getEditText().getText());

                if (!name.matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                    AnimatorSet set = ((AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.invalid_field_animator));
                    set.setTarget(name_уе.getEditText());
                    set.setDuration(100);
                    set.start();
                }

                description_et.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                            description_et.getEditText().setTextColor(context.getResources().getColor(R.color.black));
                            description_et.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            description_et.setHint("Описание");
                        } else {
                            description_et.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            description_et.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            description_et.setHint("Описание (от 3 до 400)");
                        }
                    }
                });

                description_et.getEditText().setText(description_et.getEditText().getText());

                if (!(description.matches(ConstantsManager.REG_EXP_NAME_ALBUM))) {
                    AnimatorSet set = ((AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.invalid_field_animator));
                    set.setTarget(description_et.getEditText());
                    set.setDuration(100);
                    set.start();
                }
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle("Новый альбом")
                .setView(view)
                .create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        return dialog;
    }

    public static AlertDialog editAlbum(Context context, String titleOld, String descriptionOld, AlbumAction action) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_album, null);
        TextInputLayout name_til = (TextInputLayout) view.findViewById(R.id.add_album_name_til);
        TextInputLayout description_til = (TextInputLayout) view.findViewById(R.id.add_album_description_til);
        Button ok = (Button) view.findViewById(R.id.add_album_ok_btn);
        Button cancel = (Button) view.findViewById(R.id.add_album_cancel_btn);

        name_til.getEditText().setText(titleOld);
        description_til.getEditText().setText(descriptionOld);

        ok.setOnClickListener(v -> {
            String name = name_til.getEditText().getText().toString();
            String description = description_til.getEditText().getText().toString();

            if (name.matches(ConstantsManager.REG_EXP_NAME_ALBUM) && description.matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Сохранение альбома")
                        .setMessage("Вы действительно хотите редактировать альбом?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            action.action(name, description);
                        })
                        .setNegativeButton("Нет", (dialog, which) -> {
                            dialog.cancel();
                        });
                builder.create().show();
            } else {
                name_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                            name_til.getEditText().setTextColor(context.getResources().getColor(R.color.black));
                            name_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            name_til.setHint("Имя");
                        } else {
                            name_til.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            name_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            name_til.setHint("Имя (более 2, нет (!@#$%^&*()\\|_=+-)");
                        }
                    }
                });

                name_til.getEditText().setText(name_til.getEditText().getText());

                if (!name.matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                    AnimatorSet set = ((AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.invalid_field_animator));
                    set.setTarget(name_til.getEditText());
                    set.setDuration(100);
                    set.start();
                }

                description_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().matches(ConstantsManager.REG_EXP_NAME_ALBUM)) {
                            description_til.getEditText().setTextColor(context.getResources().getColor(R.color.black));
                            description_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.stroke_field));
                            description_til.setHint("Описание");
                        } else {
                            description_til.getEditText().setTextColor(context.getResources().getColor(R.color.error));
                            description_til.getEditText().setBackground(context.getResources().getDrawable(R.drawable.et_error_state));
                            description_til.setHint("Описание (от 3 до 400)");
                        }
                    }
                });

                description_til.getEditText().setText(description_til.getEditText().getText());

                if (!(description.matches(ConstantsManager.REG_EXP_NAME_ALBUM))) {
                    AnimatorSet set = ((AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.invalid_field_animator));
                    set.setTarget(description_til.getEditText());
                    set.setDuration(100);
                    set.start();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle("Редактирование альбома")
                .setView(view)
                .create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        return dialog;
    }

    public interface AlbumAction {
        void action(String name, String description);
    }
}
