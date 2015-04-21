package com.productsway.skope;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.productsway.skope.adapters.PostsAdapter;
import com.productsway.skope.custom.CustomActivity;
import com.productsway.skope.custom.ExpandableHeightListView;
import com.productsway.skope.interfaces.ICommentable;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends CustomActivity implements View.OnClickListener, ICommentable {
    private ExpandableHeightListView lstPosts;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost;
    private TextView tvUsername, btnEditName, btnChangePicture;

    private PostsAdapter mPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initData();
        initControls();
        initListeners();
    }

    @Override
    public void initData() {
        //TODO temp data
        ArrayList<String> mPosts = new ArrayList<String>();
        HashMap<String, ArrayList<String>> mComments = new HashMap<String, ArrayList<String>>();
        mPosts.add("Top 250");
        mPosts.add("Now Showing");
        mPosts.add("Coming Soon..");
        ArrayList<String> top250 = new ArrayList<String>();
        top250.add("The Shaw shank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");
        ArrayList<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");
        ArrayList<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
        mComments.put(mPosts.get(0), top250);
        mComments.put(mPosts.get(1), nowShowing);
        mComments.put(mPosts.get(2), comingSoon);

        mPostsAdapter = new PostsAdapter(this, mPosts, mComments, this);
        mPostsAdapter.isShowPostDate(true);
    }

    @Override
    public void initControls() {
        lstPosts = (ExpandableHeightListView) this.findViewById(R.id.lst_posts);
        vgrCompose = this.findViewById(R.id.vgr_compose);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
        tvUsername = (TextView) this.findViewById(R.id.tv_username);
        btnEditName = (TextView) this.findViewById(R.id.btn_edit_name);
        btnChangePicture = (TextView) this.findViewById(R.id.btn_change_picture);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        //TODO temp data
        tvUsername.setText("San Vo");
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
        btnEditName.setOnClickListener(this);
        btnChangePicture.setOnClickListener(this);

        registerForContextMenu(btnChangePicture);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            vgrCompose.setVisibility(View.GONE);
            edtComposeContent.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

        } else if (v.getId() == R.id.vgr_compose) {
            if (vgrCompose.getVisibility() == View.VISIBLE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                vgrCompose.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.btn_edit_name) {
            final EditText input = new EditText(this);
            input.setSingleLine();
            new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("Change username")
                .setMessage("Please input new username")
                .setView(input)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newName = input.getText().toString();
                        if(!newName.isEmpty())
                            tvUsername.setText(newName);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }
            ).show();
        } else if (v.getId() == R.id.btn_change_picture) {
            openContextMenu(btnChangePicture);
        }
    }

    @Override
    public void showAddCommentBox() {
        vgrCompose.setVisibility(View.VISIBLE);
        edtComposeContent.requestFocus();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_storage, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_photo:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, 1);

                return true;
            case R.id.menu_existing_file:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                startActivityForResult(intent2, 3);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}