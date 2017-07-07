package com.nenton.photon.ui.screens.album;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.AlbumRealm;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.ui.dialogs.DialogsAlbum;

import butterknife.BindView;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AlbumView extends AbstractView<AlbumScreen.AlbumPresenter> {

    @BindView(R.id.album_RV)
    RecyclerView mRecycleView;
    @BindView(R.id.album_screen_name_TV)
    TextView mNameAlbum;
    @BindView(R.id.album_screen_count_TV)
    TextView mCountPhoto;
    @BindView(R.id.album_description)
    TextView mDescription;
    @BindView(R.id.anchor_popup_menu)
    View mView;

    private AlbumAdapter mAccountAdapter = new AlbumAdapter();
    private AlertDialog dialogEditAlbum = null;

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AlbumScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView(AlbumRealm mAlbum) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAccountAdapter);
        mNameAlbum.setText(mAlbum.getTitle());
        mCountPhoto.setText(String.valueOf(mAlbum.getPhotocards().size()));
        mDescription.setText(mAlbum.getDescription());
        for (PhotocardRealm photo : mAlbum.getPhotocards()) {
            mAccountAdapter.addPhotocard(photo);
        }
    }

    public void showDeleteAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Удаление альбома")
                .setMessage("Вы действительно хотите удалить этот альбом?")
                .setPositiveButton("Да", (dialog, which) -> mPresenter.deleteAlbum())
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    public void showEditAlbum() {
        if (dialogEditAlbum == null) {
            dialogEditAlbum = DialogsAlbum.editAlbum(getContext(), (name, description) -> {
                mPresenter.editAlbumObs(name, description);
                cancelEditAlbum();
            });
        }
        dialogEditAlbum.show();
    }

    public void cancelEditAlbum() {
        if (dialogEditAlbum != null) {
            dialogEditAlbum.cancel();
            dialogEditAlbum = null;
        }
    }

    public void updateLongTap(int posLongTap) {
        mRecycleView.getLayoutManager().findViewByPosition(posLongTap).findViewById(R.id.long_tap_photo).setVisibility(GONE);
    }

    public void showDeletePhoto(String id, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Удаление фотокарточки")
                .setMessage("Вы действительно хотите удалить эту фотокарточку?")
                .setPositiveButton("Да", (dialog, which) -> {
                    mAccountAdapter.deletePhotocard(adapterPosition);
                    mPresenter.deletePhotocard(id);
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}
