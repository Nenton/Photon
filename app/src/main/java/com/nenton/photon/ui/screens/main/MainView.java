package com.nenton.photon.ui.screens.main;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nenton.photon.R;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.storage.dto.PhotoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IMainView;
import com.nenton.photon.ui.screens.account.AccountScreen;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.nenton.photon.ui.screens.search_filters.search.SearchScreen;
import com.nenton.photon.utils.ConstantsManager;
import com.nenton.photon.utils.TextWatcherEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import io.realm.RealmList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by serge on 04.06.2017.
 */

public class MainView extends AbstractView<MainScreen.MainPresenter> implements IMainView, PopupMenu.OnMenuItemClickListener {

    private MainAdapter mMainAdapter = new MainAdapter();
    @BindView(R.id.list_photos_main_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.anchor_popup_menu)
    View mView;

    @OnClick(R.id.search_btn)
    void clickSearch() {
        Flow.get(getContext()).set(new AccountScreen());
    }
//
//    private void initAdapter() {
//        RealmList<StringRealm> stringRealms = new RealmList<>();
//        stringRealms.add(new StringRealm("Салат"));
//        stringRealms.add(new StringRealm("Груша"));
//        stringRealms.add(new StringRealm("Персик"));
//        stringRealms.add(new StringRealm("Финик"));
//        mMainAdapter.addPhoto(new PhotocardRealm("http://s1.1zoom.me/big7/635/Meat_products_Roast_343470.jpg", 56, 74, "Салат", stringRealms));
//        mMainAdapter.addPhoto(new PhotocardRealm("http://i1.imageban.ru/out/2017/05/18/d20adec5c79e16a60bca28e9b8c7aa36.jpg", 13, 42, "Манты", stringRealms));
//        mMainAdapter.addPhoto(new PhotocardRealm("http://boombob.ru/img/picture/Jul/10/85af725d3426a6e6ca9838a5ef16c182/8.jpg", 55, 16, " Ребра",stringRealms));
//        mMainAdapter.addPhoto(new PhotocardRealm("http://vkusnoe.biz/uploads/taginator/Aug-2015/vtorye-blyuda.jpg", 98, 14, "Стейк",stringRealms));
//    }

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
    public void showPhotos(List<PhotoDto> photoList) {

    }

    @Override
    public void showSearch() {

    }

    @Override
    public void showSettings() {
        PopupMenu menu = new PopupMenu(getContext(), mView, Gravity.RIGHT);
        menu.inflate(R.menu.main_settings_menu);
        MenuItem itemEnter = menu.getMenu().getItem(0);
        MenuItem itemExit = menu.getMenu().getItem(1);
        MenuItem itemRegistration = menu.getMenu().getItem(2);
        if (mPresenter.isAuth()){
            itemExit.setVisible(true);
            itemEnter.setVisible(false);
            itemRegistration.setVisible(false);
        } else {
            itemExit.setVisible(false);
            itemEnter.setVisible(true);
            itemRegistration.setVisible(true);
        }
        menu.setOnMenuItemClickListener(this);
        menu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.enter_dial:
                signIn();
                return true;
            case R.id.exit_dial:
                mPresenter.exitUser();
                return true;
            case R.id.registration_dial:
                signUp();
                return true;
            default:
                return false;
        }
    }

    private void signUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.registration_account_dialog, null);
        TextInputLayout login_ti = (TextInputLayout) view.findViewById(R.id.registration_login_til);
        TextInputLayout email_ti = (TextInputLayout) view.findViewById(R.id.registration_email_til);
        TextInputLayout name_ti = (TextInputLayout) view.findViewById(R.id.registration_name_til);
        TextInputLayout password_ti = (TextInputLayout) view.findViewById(R.id.registration_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_up_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_up_negative_btn);

        login_ti.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 2){
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
                if(s.toString().matches(ConstantsManager.REG_EXP_EMAIL)){
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
                if(s.toString().length() > 2){
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
                if(s.toString().matches(ConstantsManager.REG_EXP_PASSWORD) && s.toString().length() > 7){
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

            if (validateSignUp(login, email, name, password)){
                mPresenter.signUp(new UserCreateReq(name, login, email, password));
            }
        });

        AlertDialog dialog = builder.setTitle("Регистрация")
                .setView(view)
                .create();

        cancelBtn.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }

    private boolean validateSignUp(String login, String email, String name, String password) {
        return login.length() > 3 && email.matches(ConstantsManager.REG_EXP_EMAIL) && name.length() > 2 && password.matches(ConstantsManager.REG_EXP_PASSWORD) && password.length() > 7;
    }

    private void signIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.enter_account_dialog, null);
        TextInputLayout email_til = (TextInputLayout) view.findViewById(R.id.sign_in_email_til);
        TextInputLayout password_til = (TextInputLayout) view.findViewById(R.id.sing_in_password_til);
        Button okBtn = (Button) view.findViewById(R.id.sign_in_positive_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.sign_in_negative_btn);

        email_til.getEditText().addTextChangedListener(new TextWatcherEditText() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().matches(ConstantsManager.REG_EXP_EMAIL)){
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
                if(s.toString().matches(ConstantsManager.REG_EXP_PASSWORD) && s.toString().length() > 7){
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


        AlertDialog dialog = builder.setTitle("Вход в аккаунт")
                .setView(view)
                .create();

        okBtn.setOnClickListener(v -> {
            String email = email_til.getEditText().getText().toString();
            String password = password_til.getEditText().getText().toString();

//            if (email.matches(ConstantsManager.REG_EXP_EMAIL) && password.matches(ConstantsManager.REG_EXP_PASSWORD) && password.length() > 7){
//                mPresenter.signIn(new UserLoginReq(email, password));
//            }
            if (true){
                mPresenter.signIn(new UserLoginReq(email, password));
            }
        });

        cancelBtn.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();
    }

    @Override
    public void showPhoto(int id) {

    }

    @Override
    public void editCountFav(int id) {

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
