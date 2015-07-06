package com.speakgeo.skopebeta;

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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.speakgeo.skopebeta.adapters.ChatAdapter;
import com.speakgeo.skopebeta.adapters.ComposePreviewAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.HorizontalListView;

public class MessageDetailActivity extends CustomActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lstChat;
    private Button btnAttach, btnPost;
    private HorizontalListView lstComposePreview;
    private EditText edtComposeContent;

    ChatAdapter mChatAdapter;
    ComposePreviewAdapter mComposePreviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        initData();
        initControls();
        initListeners();
    }

    @Override
    public void initData() {
        mChatAdapter = new ChatAdapter(getApplicationContext());
        mComposePreviewAdapter = new ComposePreviewAdapter(getApplicationContext());

        //TODO temp data
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
        mChatAdapter.addItem("-------");
    }

    @Override
    public void initControls() {
        super.initControls();

        lstChat = (ListView) this.findViewById(R.id.lst_chat);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
        btnAttach = (Button) this.findViewById(R.id.btn_attach);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        lstComposePreview = (HorizontalListView) this.findViewById(R.id.lst_compose_preview);

        lstChat.setAdapter(mChatAdapter);
        lstComposePreview.setAdapter(mComposePreviewAdapter);

        //TODO temp data
        addMessage("-------");
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        btnAttach.setOnClickListener(this);
        lstChat.setOnItemClickListener(this);
        lstComposePreview.setOnItemClickListener(this);

        registerForContextMenu(btnAttach);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            lstComposePreview.setVisibility(View.GONE);
            mComposePreviewAdapter.reset();
            edtComposeContent.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

        } else if (v.getId() == R.id.btn_attach) {
            openContextMenu(btnAttach);
        }
    }


    private void addMessage(String message) {
        mChatAdapter.addItem(message);
        lstChat.setSelection(lstChat.getCount() - 1);
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
        if (parent.getId() == R.id.lst_chat) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);
        } else if (parent.getId() == R.id.lst_compose_preview) {
            ((ComposePreviewAdapter) parent.getAdapter()).removeItem(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            lstComposePreview.setVisibility(View.VISIBLE);

            mComposePreviewAdapter.addItem(data.getData());
        }
    }
}