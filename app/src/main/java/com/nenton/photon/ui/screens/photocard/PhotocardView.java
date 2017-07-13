package com.nenton.photon.ui.screens.photocard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.nenton.photon.R;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mvp.views.AbstractView;
import com.nenton.photon.mvp.views.IPhotocardView;
import com.nenton.photon.ui.custom_views.ImageViewSquare;
import com.nenton.photon.utils.AvatarTransform;
import com.nenton.photon.utils.PhotoBigTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeImageTransform;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by serge_000 on 06.06.2017.
 */

public class PhotocardView extends AbstractView<PhotocardScreen.PhotocardPresenter> implements IPhotocardView {

    @BindView(R.id.photo_IV)
    ImageViewSquare mPhoto;
    @BindView(R.id.name_photocard)
    TextView mName;
    @BindView(R.id.album_count_TV)
    TextView mAlbumCount;
    @BindView(R.id.photocard_count_TV)
    TextView mPhotocardCount;
    @BindView(R.id.full_name_TV)
    TextView mFullName;
    @BindView(R.id.avatar_photocard_IV)
    ImageView mAvatar;
    @BindView(R.id.nestedScroll)
    NestedScrollView mNested;
    @BindView(R.id.fav_icon)
    ImageView mFavIcon;
    @BindView(R.id.flexbox_photocard)
    FlexboxLayout mFlexboxLayout;
    @BindView(R.id.info_user_wrap)
    LinearLayout mUserWrap;
    @BindView(R.id.photo_wrap)
    FrameLayout mPhotoWrap;

    @OnClick(R.id.avatar_photocard_IV)
    public void clickOnAuthor() {
        mPresenter.clickOnAuthor();
    }

    private float y;

    @Inject
    Picasso mPicasso;

    public PhotocardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<PhotocardScreen.Component>getDaggerComponent(getContext()).inject(this);
    }

    public void addView(StringRealm s) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tag_photocard, mFlexboxLayout, false);
        ((TextView) view.findViewById(R.id.tag_photocard_TV)).setText(s.getString());
        setAnimation(view);
        mFlexboxLayout.addView(view);
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    protected void afterInflate() {
        mNested.setOnTouchListener((v, event) -> {
            float width = mPhoto.getWidth();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    y = event.getY();
                }
                case MotionEvent.ACTION_MOVE: {
                    width = (mPhoto.getHeight() + (event.getY() - y)) / mPhoto.getHeight() * mPhoto.getWidth();
                    if (mPhotoWrap.getMeasuredWidth() < width && width < mPhotoWrap.getMeasuredWidth() * 1.5) {
                        y = event.getY();
                        LayoutParams layoutParams = new LayoutParams((int) width, ((int) (mPhoto.getHeight() + event.getY() - y)));
                        mPhoto.setX((mPhotoWrap.getMeasuredWidth() - width) / 2);
                        mPhoto.setLayoutParams(layoutParams);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    normalStateWithAnim();
                    break;
                }
            }
            return super.onTouchEvent(event);
        });
    }

    private void normalStateWithAnim() {
        ValueAnimator animWidth = ValueAnimator.ofInt(mPhoto.getMeasuredWidth(), mPhotoWrap.getMeasuredWidth());
        animWidth.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = mPhoto.getLayoutParams();
            layoutParams.width = val;
            mPhoto.setX((mPhotoWrap.getMeasuredWidth() - val) / 2);
            mPhoto.setLayoutParams(layoutParams);
        });
        animWidth.setDuration(300);
        animWidth.setInterpolator(new FastOutSlowInInterpolator());
        animWidth.start();
    }

    @Override
    public void initUser(UserInfoDto infoDto) {

        mPicasso.with(getContext())
                .load(infoDto.getAvatar())
                .error(R.drawable.ic_account_black_24dp)
                .placeholder(R.drawable.ic_account_black_24dp)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(200, 200)
                .centerCrop()
                .transform(new AvatarTransform())

                .into(mAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.with(getContext())
                                .load(infoDto.getAvatar())
                                .error(R.drawable.ic_account_black_24dp)
                                .placeholder(R.drawable.ic_account_black_24dp)
                                .resize(200, 200)
                                .centerCrop()
                                .transform(new AvatarTransform())
                                .into(mAvatar);
                    }
                });

        mFullName.setText(infoDto.getName() + " " + infoDto.getLogin());
        mAlbumCount.setText(String.valueOf(infoDto.getCountAlbum()));
        mPhotocardCount.setText(String.valueOf(infoDto.getCountPhoto()));

        mUserWrap.setVisibility(VISIBLE);
    }

    @Override
    public void showDialogAddFav() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Добавить в избранное")
                .setMessage("Изображение будет добавлено в альбом \"Избранное\"")
                .setPositiveButton("Ок", (dialog, which) -> {
                    mPresenter.addTofav();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    dialog.cancel();
                })
                .create()
                .show();
    }

    @Override
    public void showDialogSharePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Платный контент")
                .setMessage("Покупать изображения возможно после оплаты этой функции. Выберите удобный для вас способ")
                .setPositiveButton("Выбрать", (dialog, which) -> {
                    mPresenter.sharePhoto();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    dialog.cancel();
                })
                .create()
                .show();
    }

    public void showFavIcon(boolean b) {
        if (b) {
            mFavIcon.setVisibility(VISIBLE);
        } else {
            mFavIcon.setVisibility(GONE);
        }
    }

    @Override
    public void initPhoto(PhotocardRealm photocard) {
        mName.setText(photocard.getTitle());

        mPicasso.with(getContext())
                .load(photocard.getPhoto())
                .placeholder(R.drawable.splash)
                .error(R.drawable.splash)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .transform(new PhotoBigTransform())
                .into(mPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.load(photocard.getPhoto())
                                .placeholder(R.drawable.splash)
                                .error(R.drawable.splash)
                                .fit()
                                .centerCrop()
                                .transform(new PhotoBigTransform())
                                .into(mPhoto);
                    }
                });


        for (StringRealm s : photocard.getTags()) {
            addView(s);
        }

        mUserWrap.setVisibility(GONE);
    }

}
