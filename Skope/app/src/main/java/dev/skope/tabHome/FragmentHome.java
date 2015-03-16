package dev.skope.tabHome;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dev.skope.R;
import dev.skope.Skope;
import dev.skope.ui.BaseFragment;
import dev.skope.ui.CircleImageView;

public class FragmentHome extends BaseFragment implements View.OnClickListener{

    //UI
    private TextView mTxtBtnComposePost;
    private ImageView mImgBtnToListMyMessage;
    private CircleImageView mImgMyProfile;
    private View mLayoutToListUser;
    private View mLayoutToListPost;
    private ImageButton mImgBtnToListUser;
    private ImageButton mImgBtnToListPost;

    // Animation
    private AlphaAnimation mAnimationAlpha;

    private Activity mParent;

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        // UI
        mTxtBtnComposePost = (TextView) layout.findViewById(R.id.txt_btn_compose_post);
        mImgBtnToListMyMessage = (ImageView) layout.findViewById(R.id.img_to_list_my_message);
        mImgMyProfile = (CircleImageView) layout.findViewById(R.id.img_btn_my_profile);
        mLayoutToListUser = (View) layout.findViewById(R.id.layout_to_list_user);
        mLayoutToListPost = (View) layout.findViewById(R.id.layout_to_list_post);
        mImgBtnToListUser = (ImageButton) layout.findViewById(R.id.img_btn_to_list_user);
        mImgBtnToListPost = (ImageButton) layout.findViewById(R.id.img_btn_to_list_post);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEvent();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setEvent() {
        mTxtBtnComposePost.setOnClickListener(this);
        mImgMyProfile.setOnClickListener(this);
        mImgBtnToListMyMessage.setOnClickListener(this);
        mLayoutToListUser.setOnClickListener(this);
        mLayoutToListPost.setOnClickListener(this);
        mImgBtnToListUser.setOnClickListener(this);
        mImgBtnToListPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAnimationAlpha = new AlphaAnimation(1.0f, 0.3f);
        mAnimationAlpha.setDuration(30);
        mAnimationAlpha.setFillAfter(false);

        switch (v.getId()) {
            case R.id.txt_btn_compose_post:
                Toast.makeText(getActivity(), "Compose Post", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_btn_my_profile:
                v.startAnimation(mAnimationAlpha);
                ((Skope)mParent).onClickToMyPage();
                break;
            case R.id.img_to_list_my_message:
                v.startAnimation(mAnimationAlpha);
                ((Skope)mParent).onClickToMessage();
                break;
            case R.id.img_btn_to_list_user:
            case R.id.layout_to_list_user:
                ((Skope)mParent).onClickToListUser();
                break;
            case R.id.img_btn_to_list_post:
            case R.id.layout_to_list_post:
                ((Skope)mParent).onClickToListPost();
                break;
        }
    }

}
