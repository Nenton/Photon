package com.nenton.photon.ui.screens.main;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuInflater;
import android.widget.Button;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.PhotoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IMainView;
import com.nenton.photon.ui.screens.search_filters.SearchFiltersScreen;
import com.nenton.photon.ui.screens.search_filters.search.SearchScreen;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import io.realm.RealmList;

/**
 * Created by serge on 04.06.2017.
 */

public class MainView extends AbstractView<MainScreen.MainPresenter> implements IMainView {

    private MainAdapter mMainAdapter = new MainAdapter();
    @BindView(R.id.list_photos_main_rv)
    RecyclerView mRecyclerView;

    @OnClick(R.id.search_btn)
    void clickSearch() {
        Flow.get(getContext()).set(new SearchFiltersScreen());
    }

    private void initAdapter() {
        RealmList<StringRealm> stringRealms = new RealmList<>();
        stringRealms.add(new StringRealm("Салат"));
        stringRealms.add(new StringRealm("Груша"));
        stringRealms.add(new StringRealm("Персик"));
        stringRealms.add(new StringRealm("Финик"));
        mMainAdapter.addPhoto(new PhotocardRealm("http://s1.1zoom.me/big7/635/Meat_products_Roast_343470.jpg", 56, 74, "Салат", stringRealms));
        mMainAdapter.addPhoto(new PhotocardRealm("http://i1.imageban.ru/out/2017/05/18/d20adec5c79e16a60bca28e9b8c7aa36.jpg", 13, 42, "Манты", stringRealms));
        mMainAdapter.addPhoto(new PhotocardRealm("http://boombob.ru/img/picture/Jul/10/85af725d3426a6e6ca9838a5ef16c182/8.jpg", 55, 16, " Ребра",stringRealms));
        mMainAdapter.addPhoto(new PhotocardRealm("http://vkusnoe.biz/uploads/taginator/Aug-2015/vtorye-blyuda.jpg", 98, 14, "Стейк",stringRealms));
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return true;
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

    }

    @Override
    public void showPhoto(int id) {

    }

    @Override
    public void editCountFav(int id) {

    }

    public void initView() {
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        initAdapter();
        mRecyclerView.setAdapter(mMainAdapter);
    }

}
