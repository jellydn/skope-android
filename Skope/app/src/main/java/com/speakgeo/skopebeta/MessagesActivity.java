package com.speakgeo.skopebeta;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.speakgeo.skopebeta.adapters.MessagesAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;

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