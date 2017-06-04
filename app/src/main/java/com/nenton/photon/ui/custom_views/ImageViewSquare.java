package com.nenton.photon.ui.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nenton.photon.R;

/**
 * Created by serge on 03.06.2017.
 */

public class ImageViewSquare extends android.support.v7.widget.AppCompatImageView {

    private static final float DEFAULT_ASPECT_RATIO = 2f;
    private final float mAspectRatio;

    public ImageViewSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        mAspectRatio = array.getFloat(R.styleable.AspectRatioImageView_aspect_ratio,DEFAULT_ASPECT_RATIO);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth;
        int mHeight;

        mWidth = getMeasuredWidth();
        mHeight = (int) (mWidth/mAspectRatio);

        setMeasuredDimension(mWidth, mHeight);
    }
}
