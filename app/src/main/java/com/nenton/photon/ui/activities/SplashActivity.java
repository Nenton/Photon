package com.nenton.photon.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.nenton.photon.R;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.utils.NetworkStatusChecker;

import rx.Subscriber;

public class SplashActivity extends AppCompatActivity {

    Thread splashTread;
    private boolean b = false;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        start();
    }

    private void start() {

        if (!NetworkStatusChecker.isNetworkAvailible()) {
            b = true;
        }

        DataManager.getInstance().getPhotocardsObsFromNetwork().subscribe(new Subscriber<PhotocardRealm>() {
            int i = 0;

            @Override
            public void onCompleted() {
                Log.e("onCompleted ", " Load photocard");
                b = true;
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PhotocardRealm photocardRealm) {
                Log.e("item " + i, " Load photocard");
                i++;
                if (i > 60) {
                    b = true;
                }
            }
        });
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3000 || !b) {
                        sleep(100);
                        waited += 100;
                    }
                    startActivity();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, RootActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}
