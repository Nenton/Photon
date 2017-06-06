package com.nenton.photon.ui.screens.author;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nenton.photon.R;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.ui.screens.account.AccountAdapter;
import com.nenton.photon.ui.screens.account.AccountScreen;

import butterknife.BindView;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class AuthorView extends AbstractView<AuthorScreen.AuthorPresenter>{

    @BindView(R.id.author_photocard_RV)
    RecyclerView mRecycleView;

    private AccountAdapter mAccountAdapter = new AccountAdapter();

    public AuthorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<AuthorScreen.Component>getDaggerComponent(context).inject(this);
    }

    public void initView() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mAccountAdapter);
        // TODO: 06.06.2017 добавить фотографий в аккаунт
    }
}
