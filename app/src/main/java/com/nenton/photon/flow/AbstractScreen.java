package com.nenton.photon.flow;

import android.util.Log;

import com.nenton.photon.mortar.ScreenScoper;

import flow.ClassKey;

public abstract class AbstractScreen<T>  extends ClassKey {

    private static final String TAG = "AbstractScreen";

    public String getScopeName(){
        return getClass().getName();
    }

    public abstract Object createScreenComponent(T parentComponent);

    public void unregisterScope(){
        Log.e(TAG," unregister scope " + this.getScopeName());
        ScreenScoper.destroyScreenScope(getScopeName());
    }

    public int getLayoutResId(){
        int layout = 0;

        Screen screen;
        screen = this.getClass().getAnnotation(Screen.class);
        if (screen == null){
            throw new IllegalStateException("@Screen annotation is missing on screen " + getScopeName());
        } else {
            layout = screen.value();
        }
        return layout;
    }
}
