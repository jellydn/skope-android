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

    public static class UserDetailActivity extends CustomActivity implements View.OnClickListener, CircularPickChangeListener {
        private TextView tvUsers, tvPosts, tvRadius;
        private GoogleMap googleMap;
        private CircularPick circularPick;
        private View vgrHelp, vgrCompose, vgrMain;
        private ImageButton btnComposePost;
        private EditText edtComposeContent;
        private Button btnAttach, btnPost;

        private int mCurrentRadius;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            initData();
            initControls();
            initListeners();
        }

        @Override
        public void initData() {

        }

        @Override
        public void initControls() {
            tvUsers = (TextView) this.findViewById(R.id.tv_users);
            tvPosts = (TextView) this.findViewById(R.id.tv_posts);
            tvRadius = (TextView) this.findViewById(R.id.tv_radius);
            circularPick = (CircularPick) this.findViewById(R.id.clp_zoom);
            vgrHelp = this.findViewById(R.id.vgr_help);
            vgrCompose = this.findViewById(R.id.vgr_compose);
            vgrMain = this.findViewById(R.id.vgr_main);
            btnComposePost = (ImageButton) this.findViewById(R.id.btn_compose_post);
            edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
            btnAttach = (Button) this.findViewById(R.id.btn_attach);
            btnPost = (Button) this.findViewById(R.id.btn_post);

            circularPick.setProgress(1);
            circularPick.setMaxProgress(101);

            try {
                if (null == googleMap) {
                    googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                            R.id.map)).getMap();
                    if (null == googleMap) {
                        Toast.makeText(getApplicationContext(),
                                "Error creating map", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NullPointerException exception) {
                Log.e("mapApp", exception.toString());
            }

            if (null != googleMap) {
                googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(15.8893392,108.3205417)).anchor(0.5f, 0.5f)
                                .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
                );
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.8893392, 108.3205417), MeasureUtil.calculateZoomLevel(this, 1)));
            tvRadius.setText(String.format("%d km", 1));

            //TODO temp data
            tvUsers.setText("14");
            tvPosts.setText("12");
        }

        @Override
        public void initListeners() {
            circularPick.setCircularPickChangeListener(this);
            vgrHelp.setOnClickListener(this);
            btnComposePost.setOnClickListener(this);
            btnPost.setOnClickListener(this);
            btnAttach.setOnClickListener(this);
            vgrMain.setOnClickListener(this);
        }

        @Override
        public void onProgressChange(CircularPick view, int newProgress) {
            mCurrentRadius = newProgress==0?1:newProgress;

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.8893392, 108.3205417), MeasureUtil.calculateZoomLevel(this,newProgress)));

            tvRadius.setText(String.format("%d km", mCurrentRadius));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vgr_help) {
                vgrHelp.setVisibility(View.GONE);
            }
            else if (v.getId() == R.id.btn_compose_post) {
                vgrCompose.setVisibility(View.VISIBLE);
                edtComposeContent.requestFocus();
            }
            else if (v.getId() == R.id.btn_post) {
                vgrCompose.setVisibility(View.GONE);
                edtComposeContent.setText("");

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

            }
            else if (v.getId() == R.id.btn_attach) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 1);
            }
            else if (v.getId() == R.id.vgr_main) {
                if(vgrCompose.getVisibility() == View.VISIBLE) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                    vgrCompose.setVisibility(View.GONE);
                }
            }
        }
    }
}