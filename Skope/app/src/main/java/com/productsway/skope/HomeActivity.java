package com.productsway.skope;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class HomeActivity extends CustomActivity implements View.OnClickListener, CircularPickChangeListener {
    private TextView tvUsers, tvPosts, tvRadius;
    private GoogleMap googleMap;
    private CircularPick circularPick;
    private View vgrHelp, vgrCompose;
    private ImageButton btnComposePost, btnProfile;
    private EditText edtComposeContent;
    private Button btnAttach, btnPost;
    private ImageButton btnLeft, btnRight;
    private DrawerLayout mDrawerLayout;

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
        btnComposePost = (ImageButton) this.findViewById(R.id.btn_compose_post);
        btnProfile = (ImageButton) this.findViewById(R.id.btn_profile);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
        btnAttach = (Button) this.findViewById(R.id.btn_attach);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        btnLeft = (ImageButton) this.findViewById(R.id.btn_left);
        btnRight = (ImageButton) this.findViewById(R.id.btn_right);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        //TODO temp data
        circularPick.setProgress(1);
        circularPick.setMaxProgress(101);
        if (null != googleMap) {
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(15.8893392, 108.3205417)).anchor(0.5f, 0.5f)
                            .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
            );
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.8893392, 108.3205417), MeasureUtil.calculateZoomLevel(this, 1)));
        tvRadius.setText(String.format("%d km", 1));
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
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
        btnProfile.setOnClickListener(this);

        registerForContextMenu(btnAttach);
    }

    public void closeAllDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onProgressChange(CircularPick view, int newProgress) {
        mCurrentRadius = newProgress == 0 ? 1 : newProgress;

        //TODO temp data
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.8893392, 108.3205417), MeasureUtil.calculateZoomLevel(this, newProgress)));

        tvRadius.setText(String.format("%d km", mCurrentRadius));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.vgr_help) {
            vgrHelp.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btn_compose_post) {
            vgrCompose.setVisibility(View.VISIBLE);
            edtComposeContent.requestFocus();
        } else if (v.getId() == R.id.btn_post) {
            vgrCompose.setVisibility(View.GONE);
            edtComposeContent.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

        } else if (v.getId() == R.id.btn_attach) {
            openContextMenu(btnAttach);
        } else if (v.getId() == R.id.vgr_compose) {
            if (vgrCompose.getVisibility() == View.VISIBLE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                vgrCompose.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.btn_left) {
            mDrawerLayout.openDrawer(Gravity.START);
        } else if (v.getId() == R.id.btn_right) {
            mDrawerLayout.openDrawer(Gravity.END);
        } else if (v.getId() == R.id.btn_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_video_photo_storage, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_photo:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, 1);

                return true;
            case R.id.menu_video:
                Intent intent2 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent2, 2);

                return true;
            case R.id.menu_existing_file:
                Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
                intent3.setType("image/*");
                intent3.setType("video/*");
                startActivityForResult(intent3, 3);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}