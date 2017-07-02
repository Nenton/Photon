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
                    name_уе.setHint("Имя (Не валидное имя)");
                }
            }
        });

        ok.setOnClickListener(v -> {
            String name = name_уе.getEditText().getText().toString();
            String description = description_et.getEditText().getText().toString();

            if (name.matches(ConstantsManager.REG_EXP_NAME) && description.length() > 2 && description.length() < 400) {
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
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle("Новый альбом")
                .setView(view)
                .create();

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        return dialog;
    }

    public static AlertDialog editAlbum(Context context, AlbumAction action) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_album, null);
        TextInputLayout name_уе = (TextInputLayout) view.findViewById(R.id.add_album_name_til);
        TextInputLayout description_et = (TextInputLayout) view.findViewById(R.id.add_album_description_til);
        Button ok = (Button) view.findViewById(R.id.add_album_ok_btn);
        Button cancel = (Button) view.findViewById(R.id.add_album_cancel_btn);

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
                    name_уе.setHint("Имя (Не валидное имя)");
                }
            }
        });

        ok.setOnClickListener(v -> {
            String name = name_уе.getEditText().getText().toString();
            String description = description_et.getEditText().getText().toString();

            if (name.matches(ConstantsManager.REG_EXP_NAME) && description.length() > 2 && description.length() < 400) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Сохранение альбома")
                        .setMessage("Вы точно хотите редактировать альбом?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            action.action(name, description);
                        })
                        .setNegativeButton("Нет", (dialog, which) -> {
                            dialog.cancel();
                        });
                builder.create().show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle("Редактирование альбома")
                .setView(view)
                .create();

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        return dialog;
    }

    public interface AlbumAction {
        void action(String name, String description);
    }
}
