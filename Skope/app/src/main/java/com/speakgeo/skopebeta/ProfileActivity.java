package com.speakgeo.skopebeta;

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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.speakgeo.skopebeta.adapters.PostsAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.ExpandableHeightListView;
import com.speakgeo.skopebeta.interfaces.ICommentable;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.PostWSObject;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.CommentResponse;
import com.speakgeo.skopebeta.webservices.objects.SearchPostByUserResponse;
import com.speakgeo.skopebeta.webservices.objects.User;

public class ProfileActivity extends CustomActivity implements View.OnClickListener, AbsListView.OnScrollListener, ICommentable {
    private ExpandableHeightListView lstPosts;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost;
    private TextView tvUsername, btnEditName, btnChangePicture;
    private ImageView imgAvatar;
    private ProgressBar prgLoadingImage;

    private PostsAdapter mPostsAdapter;

    private User mUser;
    private int mTotalPost;
    private int mCurrentPage;

    private int mCurrentSelectedPostPos;

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private boolean isLoading;

    private GetPostTask mGetPostTask;
    private CommentTask mCommentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initData();
        initControls();
        initListeners();

        loadPostData();
    }

    @Override
    public void initData() {
        mUser = (User) this.getIntent().getSerializableExtra("USER");

        mPostsAdapter = new PostsAdapter(this, this);
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
        imgAvatar = (ImageView) this.findViewById(R.id.img_avatar);
        prgLoadingImage = (ProgressBar) this.findViewById(R.id.prg_loading_img);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        tvUsername.setText(mUser.getName());

        ImageLoaderSingleton.getInstance(this).load(mUser.getAvatar(), mUser.getId(), new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView) views[0]).setImageBitmap(bitmap);
                views[1].setVisibility(View.GONE);
            }
        }, null, new Option(200, 200), imgAvatar, prgLoadingImage);
    }

    @Override
    public void initListeners() {
        btnPost.setOnClickListener(this);
        vgrCompose.setOnClickListener(this);
        btnEditName.setOnClickListener(this);
        btnChangePicture.setOnClickListener(this);

        registerForContextMenu(btnChangePicture);

        this.lstPosts.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_post) {
            if (edtComposeContent.getText().toString().isEmpty()) return;

            if (mCommentTask != null) mCommentTask.cancel(true);
            mCommentTask = new CommentTask(mCurrentSelectedPostPos);
            mCommentTask.execute(edtComposeContent.getText().toString(), mPostsAdapter.getGroup(mCurrentSelectedPostPos).getId());
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
                            if (!newName.isEmpty())
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
    public void showAddCommentBox(int position) {
        mCurrentSelectedPostPos = position;

        vgrCompose.setVisibility(View.VISIBLE);
        edtComposeContent.requestFocus();
    }

    @Override
    public void like(int position) {
        mCurrentSelectedPostPos = position;

    }

    @Override
    public void dislike(int position) {
        mCurrentSelectedPostPos = position;

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

    public void loadPostData() {
        mTotalPost = 0;
        mCurrentPage = 0;

        if (mGetPostTask != null) mGetPostTask.cancel(true);
        mGetPostTask = new GetPostTask();
        mGetPostTask.execute();
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {

            if (!isLoading && mCurrentPage <= Math.ceil(mTotalPost / UserProfileSingleton.NUM_OF_POST_PER_PAGE)) {
                isLoading = true;
                if (mGetPostTask != null) mGetPostTask.cancel(true);
                mGetPostTask = new GetPostTask();
                mGetPostTask.execute();
            }
        }
    }

    /*--------------- Tasks ----------------*/
    private class GetPostTask extends AsyncTask<Void, Void, SearchPostByUserResponse> {

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        }

        ;

        @Override
        protected SearchPostByUserResponse doInBackground(Void... params) {
            return UserWSObject.searchPostByUser(getApplicationContext(), mUser.getId(), mCurrentPage++, UserProfileSingleton.NUM_OF_POST_PER_PAGE);
        }

        @Override
        protected void onPostExecute(SearchPostByUserResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mPostsAdapter.addData(result.getData().getItems());

                mTotalPost = result.getData().getTotal();
            } else {
                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }


    private class CommentTask extends AsyncTask<String, Void, CommentResponse> {
        private int mGroupPos;

        public CommentTask(int groupPos) {
            mGroupPos = groupPos;
        }

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        }

        @Override
        protected CommentResponse doInBackground(String... params) {
            return PostWSObject
                    .comment(getApplicationContext(), params[0], params[1]);//content, post id
        }

        @Override
        protected void onPostExecute(CommentResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                vgrCompose.setVisibility(View.GONE);
                edtComposeContent.setText("");

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtComposeContent.getWindowToken(), 0);

                mPostsAdapter.addComment(mGroupPos, result.getData().getComment());
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }
}