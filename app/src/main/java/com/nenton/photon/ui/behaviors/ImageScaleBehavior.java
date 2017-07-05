package com.nenton.photon.ui.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nenton.photon.ui.custom_views.ImageViewSquare;

/**
 * Created by serge on 03.07.2017.
 */

@SuppressWarnings("unused")
public class ImageScaleBehavior extends CoordinatorLayout.Behavior<ImageViewSquare> {

    public ImageScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageViewSquare child, View dependency) {
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageViewSquare child, View dependency) {
        // Adjust the child View accordingly
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, ImageViewSquare child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        child.setLayoutParams(new LinearLayout.LayoutParams(dxConsumed, dyConsumed));
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ImageViewSquare child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.e("onStartNestedScroll", "djn");
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

}
