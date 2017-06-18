package com.nenton.photon.ui.screens.add_photocard;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.UserRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by serge on 18.06.2017.
 */

public class AddPhotocardView extends AbstractView<AddPhotocardScreen.AddPhotocardPresenter> {

    @BindView(R.id.add_album_for_photocard_rv)
    RecyclerView mAlbums;

    private AddPhotocardSelectAlbumAdapter mAdapter = new AddPhotocardSelectAlbumAdapter();

    public AddPhotocardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AddPhotocardScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mAlbums.setLayoutManager(manager);
        mAlbums.setAdapter(mAdapter);
    }

    public void showView(UserRealm userRealm) {
        for (AlbumRealm albumRealm : userRealm.getAlbums()) {
            mAdapter.addAlbum(albumRealm);
        }
        RealmList<PhotocardRealm> photocardRealms = new RealmList<>();
        photocardRealms.add(new PhotocardRealm("http://img08.deviantart.net/41d0/i/2013/056/1/a/fox_paper_craft_by_veavictis-d5vsw6m.png"));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
    }

    public void showView() {
        RealmList<PhotocardRealm> photocardRealms = new RealmList<>();
        photocardRealms.add(new PhotocardRealm("http://img08.deviantart.net/41d0/i/2013/056/1/a/fox_paper_craft_by_veavictis-d5vsw6m.png"));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
        mAdapter.addAlbum(new AlbumRealm("Пироженки", photocardRealms));
    }

    public void checkedCurrentAlbum(int positionOnSelectItem) {
        ((CheckBox) mAlbums.getLayoutManager().findViewByPosition(positionOnSelectItem).findViewById(R.id.check_album_add)).setChecked(false);
    }
}
