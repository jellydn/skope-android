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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.speakgeo.skopebeta.adapters.PostsAdapter;
import com.speakgeo.skopebeta.custom.CustomActivity;
import com.speakgeo.skopebeta.custom.CustomScrollView;
import com.speakgeo.skopebeta.custom.ExpandableHeightListView;
import com.speakgeo.skopebeta.interfaces.ICommentable;
import com.speakgeo.skopebeta.utils.HttpFileUploadUtil;
import com.speakgeo.skopebeta.utils.ImageUtil;
import com.speakgeo.skopebeta.utils.UserProfileSingleton;
import com.speakgeo.skopebeta.utils.imageloader.ImageLoaderSingleton;
import com.speakgeo.skopebeta.utils.imageloader.listeners.OnCompletedDownloadListener;
import com.speakgeo.skopebeta.utils.imageloader.objects.Option;
import com.speakgeo.skopebeta.webservices.PostWSObject;
import com.speakgeo.skopebeta.webservices.UserWSObject;
import com.speakgeo.skopebeta.webservices.objects.CommentResponse;
import com.speakgeo.skopebeta.webservices.objects.CommonResponse;
import com.speakgeo.skopebeta.webservices.objects.SearchPostByUserResponse;
import com.speakgeo.skopebeta.webservices.objects.UpdateProfileResponse;
import com.speakgeo.skopebeta.webservices.objects.User;
import com.speakgeo.skopebeta.webservices.objects.VoteResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ProfileActivity extends CustomActivity implements View.OnClickListener, CustomScrollView.OnScrollListener, ICommentable {
    private ExpandableHeightListView lstPosts;
    private View vgrCompose;
    private EditText edtComposeContent;
    private Button btnPost;
    private TextView tvUsername, btnEditName, btnChangePicture;
    private ImageView imgAvatar;
    private ProgressBar prgLoadingImage;
    private CustomScrollView scrMain;

    private PostsAdapter mPostsAdapter;

    private User mUser;
    private int mTotalPost;
    private int mCurrentPage;

    private int mCurrentSelectedPostPos;

    private boolean isLoading;

    private GetPostTask mGetPostTask;
    private CommentTask mCommentTask;
    private VoteTask mVoteTask;
    private UpdateProfileTask mUpdateProfile;
    private UploadAvatarTask mUploadAvatarTask;

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
        super.initControls();

        lstPosts = (ExpandableHeightListView) this.findViewById(R.id.lst_posts);
        vgrCompose = this.findViewById(R.id.vgr_compose);
        btnPost = (Button) this.findViewById(R.id.btn_post);
        edtComposeContent = (EditText) this.findViewById(R.id.edt_compose_content);
        tvUsername = (TextView) this.findViewById(R.id.tv_username);
        btnEditName = (TextView) this.findViewById(R.id.btn_edit_name);
        btnChangePicture = (TextView) this.findViewById(R.id.btn_change_picture);
        imgAvatar = (ImageView) this.findViewById(R.id.img_avatar);
        prgLoadingImage = (ProgressBar) this.findViewById(R.id.prg_loading_img);
        scrMain = (CustomScrollView) this.findViewById(R.id.scr_main);

        mPostsAdapter.setParent(lstPosts);
        lstPosts.setAdapter(mPostsAdapter);
        lstPosts.setExpanded(true);

        tvUsername.setText(UserProfileSingleton.getConfig(getApplicationContext()).getName());

        ImageLoaderSingleton.getInstance(this).load(mUser.getAvatar(), "User_"+mUser.getId(), new OnCompletedDownloadListener() {
            @Override
            public void onComplete(View[] views, Bitmap bitmap) {
                ((ImageView) views[0]).setImageBitmap(ImageUtil.getRoundedCornerBitmap(bitmap));
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

        scrMain.setOnScrollListener(this);

        registerForContextMenu(btnChangePicture);
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
                            if(!input.getText().toString().isEmpty()) {
                                if (mUpdateProfile != null) mUpdateProfile.cancel(true);
                                mUpdateProfile = new UpdateProfileTask();
                                mUpdateProfile.execute(input.getText().toString());
                            }
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

        if (mVoteTask != null) mVoteTask.cancel(true);
        mVoteTask = new VoteTask(mCurrentSelectedPostPos);
        mVoteTask.execute("true", mPostsAdapter.getGroup(mCurrentSelectedPostPos).getId());
    }

    @Override
    public void dislike(int position) {
        mCurrentSelectedPostPos = position;

        if (mVoteTask != null) mVoteTask.cancel(true);
        mVoteTask = new VoteTask(mCurrentSelectedPostPos);
        mVoteTask.execute("false", mPostsAdapter.getGroup(mCurrentSelectedPostPos).getId());
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
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, getAvatarUri());
                startActivityForResult(intent1, 1);

                return true;
            case R.id.menu_existing_file:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                startActivityForResult(intent2, 2);

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

    public void onScrollToEnd() {
        if (!isLoading && mCurrentPage <= Math.ceil(mTotalPost / UserProfileSingleton.NUM_OF_POST_PER_PAGE)) {
                isLoading = true;
                if (mGetPostTask != null) mGetPostTask.cancel(true);
                mGetPostTask = new GetPostTask();
                mGetPostTask.execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = getAvatarUri();

        if(resultCode == RESULT_OK) {
            if(requestCode == 2) { //pick from storage
                selectedImage = data.getData();
            }

            if (mUploadAvatarTask != null) mUploadAvatarTask.cancel(true);
            mUploadAvatarTask = new UploadAvatarTask();
            mUploadAvatarTask.execute(selectedImage);
        }
    }

    private Uri getAvatarUri() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "avatar");

        return Uri.fromFile(file);
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
            return UserWSObject.searchPostByUser(getApplicationContext(), mUser.getId(), ++mCurrentPage, UserProfileSingleton.NUM_OF_POST_PER_PAGE);
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

    private class VoteTask extends AsyncTask<String, Void, VoteResponse> {
        private int mGroupPos;

        public VoteTask(int groupPos) {
            mGroupPos = groupPos;
        }

        @Override
        protected void onPreExecute() {
            showLoadingBar();
        }

        ;

        @Override
        protected VoteResponse doInBackground(String... params) {
            return PostWSObject
                    .vote(getApplicationContext(), Boolean.parseBoolean(params[0]), params[1]);//type, post id
        }

        @Override
        protected void onPostExecute(VoteResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                mPostsAdapter.updateVote(mGroupPos, result.getData().getPost());

                Toast.makeText(getApplicationContext(), result.getData().getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }

    private class UpdateProfileTask extends AsyncTask<String, Void, CommonResponse> {
        private String name;
        @Override
        protected void onPreExecute() {
            showLoadingBar();
        }

        @Override
        protected CommonResponse doInBackground(String... params) {
            name = params[0];
            return UserWSObject.updateProfile(getApplicationContext(), name);
        }

        @Override
        protected void onPostExecute(CommonResponse result) {
            super.onPostExecute(result);

            if (!result.hasError()) {
                UserProfileSingleton.getConfig(getApplicationContext()).setName(name);
                tvUsername.setText(name);
            } else {
                Toast.makeText(getApplicationContext(), result.getMeta().getMessage(), Toast.LENGTH_LONG).show();
            }

            isLoading = false;
            hideLoadingBar();
        }
    }

    private class UploadAvatarTask extends AsyncTask<Uri, Void, String> {
        @Override
        protected void onPreExecute() {
            showLoadingBar();
        };

        @Override
        protected String doInBackground(Uri... params) {
            ByteArrayOutputStream baOS = new ByteArrayOutputStream();
            Bitmap bitmap = ImageUtil.resizeBitmapFromUri(
                    getApplicationContext(), params[0]);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baOS);
            byte[] imageData = baOS.toByteArray();

            HttpFileUploadUtil uploader = new HttpFileUploadUtil(
                    UserProfileSingleton.END_POINT + "user/avatar?access_token="+UserProfileSingleton.getConfig(getApplicationContext()).getAccessToken());
            uploader.addData("file", imageData, "avatar.png");
            return uploader.doUpload();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new GsonBuilder().create();
            UpdateProfileResponse res = gson.fromJson(result, UpdateProfileResponse.class);

            if(res != null) {
                if (res.getMeta().getCode() == 400) {
                    Toast.makeText(getApplicationContext(), res.getMeta().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    mUser = res.getData().getUser();

                    ImageLoaderSingleton.getInstance(getApplicationContext()).clearCacheById("User_" + mUser.getId());

                    ImageLoaderSingleton.getInstance(getApplicationContext()).load(mUser.getAvatar(), "User_" + mUser.getId(), new OnCompletedDownloadListener() {
                        @Override
                        public void onComplete(View[] views, Bitmap bitmap) {
                            ((ImageView) views[0]).setImageBitmap(ImageUtil.getRoundedCornerBitmap(bitmap));
                            views[1].setVisibility(View.GONE);
                        }
                    }, null, new Option(200, 200), imgAvatar, prgLoadingImage);
                }
            }
            hideLoadingBar();
        }
    }
}