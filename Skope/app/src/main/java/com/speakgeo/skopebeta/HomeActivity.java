package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.speakgeo.skopebeta.adapters.ComposePreviewAdapter;
import com.speakgeo.skopebeta.custom.CircularPick;
import com.speakgeo.skopebeta.custom.CircularPickChangeListener;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.HorizontalListView;
import com.speakgeo.skopebeta.fragments.UsersFragment;
import com.speakgeo.skopebeta.utils.MeasureUtil;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.webservices.PostWSObject;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.CommonResponse;
import com.speakgeo.skopebeta.webservices.objects.SearchPostResponse;
import com.speakgeo.skopebeta.webservices.objects.SearchUserResponse;

public class HomeActivity extends CustomActivity implements View.OnClickListener, CircularPickChangeListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {
    private TextView tvUsers, tvPosts, tvRadius;
    private GoogleMap googleMap;
    private CircularPick circularPick;
    private View vgrHelp, vgrCompose;
    private ImageButton btnComposePost, btnProfile;
    private EditText edtComposeContent;
    private Button btnAttach, btnPost;
    private ImageButton btnLeft, btnRight, btnMessage;
    private DrawerLayout mDrawerLayout;
    private HorizontalListView lstComposePreview;

    private UsersFragment userFragment;

    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private Marker mCurrentMark;
    private LatLng mLastLocation;
    private int mCurrentRadius;

    ComposePreviewAdapter mComposePreviewAdapter;

    private SearchUserTask mSearchUserTask;
    private SearchPostTask mSearchPostTask;
    private PostTask mPostTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();
        initControls();
        initListeners();

        circularPick.setProgress(1);

        buildGoogleApiClient();
    }

    @Override
    public void initData() {
        mLastLocation = new LatLng(15.8907709f,108.3219069f);
        mCurrentRadius = 1;

        mComposePreviewAdapter = new ComposePreviewAdapter(getApplicationContext());
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
        btnMessage = (ImageButton) this.findViewById(R.id.btn_message);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
        btnAttach = (Button) this.findViewById(R.id.btn_attach);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        btnLeft = (ImageButton) this.findViewById(R.id.btn_left);
        btnRight = (ImageButton) this.findViewById(R.id.btn_right);
        lstComposePreview = (HorizontalListView) this.findViewById(R.id.lst_compose_preview);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        userFragment = (UsersFragment) this.getFragmentManager().findFragmentById(R.id.user_fragment);

        //
        if(UserProfileSingleton.getConfig(this).isFisrtTime()) {
            vgrHelp.setVisibility(View.VISIBLE);
            UserProfileSingleton.getConfig(this).setIsFisrtTime(false);
        }

        //
        circularPick.setMaxProgress(101);
        if (null != googleMap) {
            mCurrentMark = googleMap.addMarker(new MarkerOptions()
                            .position(mLastLocation).anchor(0.5f, 0.5f)
                            .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
            );
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLastLocation, MeasureUtil.calculateZoomLevel(this, 1)));
        tvRadius.setText(String.format("%d km", 1));
        tvUsers.setText("0");
        tvPosts.setText("0");

        lstComposePreview.setAdapter(mComposePreviewAdapter);
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
        btnMessage.setOnClickListener(this);
        lstComposePreview.setOnItemClickListener(this);

        registerForContextMenu(btnAttach);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    public void closeAllDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onProgressChange(CircularPick view, int newProgress) {
        mCurrentRadius = newProgress == 0 ? 1 : newProgress;

        updateCurrentLocationGUI(mLastLocation);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.vgr_help) {
            vgrHelp.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btn_compose_post) {
            vgrCompose.setVisibility(View.VISIBLE);
            edtComposeContent.requestFocus();
        } else if (v.getId() == R.id.btn_post) {
            if(!edtComposeContent.getText().toString().isEmpty()) {
                if (mPostTask != null) mPostTask.cancel(true);
                mPostTask = new PostTask();
                mPostTask.execute(edtComposeContent.getText().toString());
            }
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
        } else if (v.getId() == R.id.btn_message) {
            startActivity(new Intent(this, MessagesActivity.class));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((ComposePreviewAdapter) parent.getAdapter()).removeItem(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            lstComposePreview.setVisibility(View.VISIBLE);

            //TODO temp data
            mComposePreviewAdapter.addItem("TEMP");
        }
    }

    /*------------- location -------------- */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateCurrentLocationGUI(new LatLng(location.getLatitude(),location.getLongitude()));
        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("SAN","HomeActivity-onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("SAN","HomeActivity-onConnectionFailed: "+connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        updateCurrentLocationGUI(new LatLng(location.getLatitude(),location.getLongitude()));
    }

    private void updateCurrentLocationGUI(LatLng location) {
        mLastLocation = location;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLastLocation, MeasureUtil.calculateZoomLevel(this, mCurrentRadius)));
        mCurrentMark.setPosition(mLastLocation);

        tvRadius.setText(String.format("%d km", mCurrentRadius));


        if(mSearchUserTask != null) mSearchUserTask.cancel(true);
        mSearchUserTask = new SearchUserTask();
        mSearchUserTask.execute();

        if(mSearchPostTask != null) mSearchPostTask.cancel(true);
        mSearchPostTask = new SearchPostTask();
        mSearchPostTask.execute();
    }

    /*--------------- Tasks ----------------*/
    private class SearchUserTask extends AsyncTask<Void, Void, SearchUserResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected SearchUserResponse doInBackground(Void... params) {
            return UserWSObject
                    .search(getApplicationContext(),mLastLocation.longitude,mLastLocation.latitude,mCurrentRadius,1,UserProfileSingleton.NUM_OF_USER_PER_PAGE);
        }

        @Override
        protected void onPostExecute(SearchUserResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                tvUsers.setText(String.valueOf(result.getData().getTotal()));

                userFragment.setUserData(result.getData().getItems(),result.getData().getTotal(),mLastLocation, mCurrentRadius);
            } else {
                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            }

            hideLoadingBar();
        }
    }

    private class SearchPostTask extends AsyncTask<Void, Void, SearchPostResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected SearchPostResponse doInBackground(Void... params) {
            return PostWSObject
                    .search(getApplicationContext(),mLastLocation.longitude,mLastLocation.latitude,mCurrentRadius,1,UserProfileSingleton.NUM_OF_POST_PER_PAGE);
        }

        @Override
        protected void onPostExecute(SearchPostResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                tvPosts.setText(String.valueOf(result.getData().getTotal()));
            } else {
                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            }

            hideLoadingBar();
        }
    }

    private class PostTask extends AsyncTask<String, Void, CommonResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected CommonResponse doInBackground(String... params) {
            return PostWSObject
                    .post(getApplicationContext(), params[0], mLastLocation.longitude, mLastLocation.latitude);
        }

        @Override
        protected void onPostExecute(CommonResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                vgrCompose.setVisibility(View.GONE);
                lstComposePreview.setVisibility(View.GONE);
                mComposePreviewAdapter.reset();

                edtComposeContent.setText("");

                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                Toast.makeText(getApplicationContext(), getString(R.string.post_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            hideLoadingBar();
        }
    }
}
