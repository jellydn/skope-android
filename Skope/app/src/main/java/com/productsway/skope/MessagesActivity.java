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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.productsway.skope.adapters.ChatAdapter;
import com.productsway.skope.adapters.ComposePreviewAdapter;
import com.productsway.skope.adapters.MessagesAdapter;
import com.productsway.skope.custom.CircularPick;
import com.productsway.skope.custom.CustomActivity;
import com.productsway.skope.custom.HorizontalListView;
import com.productsway.skope.utils.MeasureUtil;

public class MessagesActivity extends CustomActivity implements AdapterView.OnItemClickListener {

    private ListView lstMessages;

    MessagesAdapter mMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initData();
        initControls();
        initListeners();
    }

    @Override
    public void initData() {
        mMessagesAdapter = new MessagesAdapter(getApplicationContext());

        //TODO temp data
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
        mMessagesAdapter.addItem("-------");
    }

    @Override
    public void initControls() {
        lstMessages = (ListView) this.findViewById(R.id.lst_messages);

        lstMessages.setAdapter(mMessagesAdapter);
    }

    @Override
    public void initListeners() {
        lstMessages.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, MessageDetailActivity.class));
    }
}