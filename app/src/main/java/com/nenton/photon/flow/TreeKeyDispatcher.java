package com.nenton.photon.flow;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.nenton.photon.R;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.mortar.ScreenScoper;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.photocard.PhotocardScreen;
import com.nenton.photon.utils.ViewHelper;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.Collections;
import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.KeyChanger;
import flow.MultiKey;
import flow.State;
import flow.Traversal;
import flow.TraversalCallback;
import flow.TreeKey;
import mortar.MortarScope;

public class TreeKeyDispatcher implements Dispatcher, KeyChanger {

    private Activity mActivity;
    private Object inKey;
    @Nullable
    private Object outKey;
    private FrameLayout mRootFrame;

    public TreeKeyDispatcher(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
        Map<Object, Context> contexts;
        State inState = traversal.getState(traversal.destination.top());
        inKey = inState.getKey();
        State outState = traversal.origin == null ? null : traversal.getState(traversal.origin.top());
        outKey = outState == null ? null : outState.getKey();

        mRootFrame = (FrameLayout) mActivity.findViewById(R.id.root_frame);

        if (inKey.equals(outKey)) {
            callback.onTraversalCompleted();
            return;
        }

        Context flowContext = traversal.createContext(inKey, mActivity);
        Context mortarContext = ScreenScoper.getScreenScope((AbstractScreen) inKey).createContext(flowContext);
        contexts = Collections.singletonMap(inKey, mortarContext);
        changeKey(outState, inState, traversal.direction, contexts, callback);
    }

    public static Context createProductContext(PhotocardRealm product, Context parentContext) {
        MortarScope parentScope = MortarScope.getScope(parentContext);
        MortarScope childScope = null;
        PhotocardScreen screen = new PhotocardScreen(product);
        String scopeName = String.format("%s_%s", screen.getScopeName(), product.getId());

        if (parentScope.findChild(scopeName) == null) {
            childScope = parentScope.buildChild()
                    .withService(DaggerService.SERVICE_NAME,
                            screen.createScreenComponent(DaggerService.<RootActivity.RootComponent>getDaggerComponent(parentContext)))
                    .build(scopeName);
        } else {
            childScope = parentScope.findChild(scopeName);
        }
        return childScope.createContext(parentContext);
    }


    @Override
    public void changeKey(@Nullable State outgoingState, @NonNull State incomingState, @NonNull Direction direction, @NonNull Map<Object, Context> incomingContexts, @NonNull TraversalCallback callback) {
        Context context = incomingContexts.get(inKey);

        //save prev View

        if (outgoingState != null) {
            outgoingState.save(mRootFrame.getChildAt(0));
        }

        //create new view
        Screen screen;
        screen = inKey.getClass().getAnnotation(Screen.class);
        if (screen == null) {
            throw new IllegalStateException("@Screen annotation is missing on screen " + ((AbstractScreen) inKey).getScopeName());
        } else {
            int layout = screen.value();

            LayoutInflater inflater = LayoutInflater.from(context);
            View newView = inflater.inflate(layout, mRootFrame, false);
            View oldView = mRootFrame.getChildAt(0);
            // restore state to new view
            incomingState.restore(newView);

            ViewHelper.waitForMeasure(newView, (view, width, height) -> runAnimation(mRootFrame, oldView, newView, () -> {
                if ((outKey) != null && !(inKey instanceof TreeKey)) {
                    ((AbstractScreen) outKey).unregisterScope();
                }
                callback.onTraversalCompleted();
            }));
            mRootFrame.addView(newView);
        }
    }

    private void runAnimation(FrameLayout container, View from, View to, TraversalCallback callback) {

        Animator animator = createAnimation(from, to);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (from != null){
                    container.removeView(from);
                }
                callback.onTraversalCompleted();
            }
        });
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.start();

    }

    @NonNull
    private Animator createAnimation(@Nullable View from, View to) {

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator outAnimation = null;
        if (from != null){
            outAnimation = ObjectAnimator.ofFloat(from, "alpha", 1f, 0f);
            outAnimation.setDuration(300);
        }

        if (from == null){
            ObjectAnimator toAnimation = ObjectAnimator.ofFloat(to, "alpha", 0f, 1f);
            toAnimation.setDuration(300);
            set.play(toAnimation);
        } else {
            ObjectAnimator toAnimation = ObjectAnimator.ofFloat(to, "alpha", 0f, 0f, 1f);
            toAnimation.setDuration(600);
            set.playTogether(outAnimation, toAnimation);
        }
        return set;
    }
}
