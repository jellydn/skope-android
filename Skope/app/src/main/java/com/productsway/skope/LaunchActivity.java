package com.productsway.skope;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.productsway.skope.custom.CircularPick;
import com.productsway.skope.custom.CircularPickChangeListener;
import com.productsway.skope.custom.CustomActivity;
import com.productsway.skope.utils.MeasureUtil;

public class LaunchActivity extends Activity {

    protected int _splashTime = 2000;

    private Thread splashTread;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        showSplash();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashTread) {
                splashTread.notifyAll();
            }
        }
        return true;
    }

    public void showSplash() {
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // -------

                        wait(_splashTime);
                    }

                } catch (Exception e) {
                    Log.e("SAN", "Splash:" + e.getMessage());
                } finally {

                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();

                    LaunchActivity.this.overridePendingTransition(
                            R.anim.appear, R.anim.disappear);

                }
            }
        };

        splashTread.start();
    }
}