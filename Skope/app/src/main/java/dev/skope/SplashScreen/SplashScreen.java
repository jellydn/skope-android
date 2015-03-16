package dev.skope.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dev.skope.R;
import dev.skope.Skope;
import dev.skope.utils.Utils;

public class SplashScreen extends Activity implements View.OnClickListener{

    // View
    private ImageView mImgLogoView;
    private TextView mTxtBtnLoginFacebook;

    // Value
    private int heightLogoView;
    private int heightOfScreen;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
        setValue();
        setEvent();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final int distance = ( (heightOfScreen - heightLogoView) - (( Utils.getHeightScreen(getApplicationContext()) / 2) - 250) - heightLogoView);
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, distance);
                anim.setDuration(1000);

                anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        mImgLogoView.clearAnimation();
                        heightOfScreen = (Utils.getHeightScreen(getApplicationContext()) / 2) - 150;
                        setPositionOfLogoView();

                        // show button sign in
                        if (Utils.checkIsHeightVersion(getApplicationContext())) {
                            mTxtBtnLoginFacebook.setVisibility(View.VISIBLE);
                            mTxtBtnLoginFacebook.setAlpha(0.0f);
                            mTxtBtnLoginFacebook.animate().alpha(1.0f).setDuration(1000);
                        }


                    }
                });

                mImgLogoView.startAnimation(anim);
            }
        }, 2000);


    }

    private void initialize() {
        // View
        mImgLogoView = (ImageView) findViewById(R.id.img_logo_text);
        ViewTreeObserver vto = mImgLogoView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mImgLogoView.getViewTreeObserver().removeOnPreDrawListener(this);
                heightLogoView = mImgLogoView.getMeasuredHeight();
                heightOfScreen = (Utils.getHeightScreen(getApplicationContext()) / 2);
                // Set Position of View
                setPositionOfLogoView();
                return true;
            }
        });

        mTxtBtnLoginFacebook = (TextView) findViewById(R.id.txt_btn_login_facebook);

    }

    private void setValue() {

    }

    private void setEvent() {
        mTxtBtnLoginFacebook.setOnClickListener(this);
    }

    private void setPositionOfLogoView() {
        if (Utils.checkIsHeightVersion(getApplicationContext())) {
            mImgLogoView.setY(heightOfScreen - heightLogoView) ;
        } else {
            RelativeLayout.LayoutParams mLogoView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLogoView.setMargins(0, heightOfScreen - heightLogoView, 0, 0);
            mImgLogoView.setLayoutParams(mLogoView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_btn_login_facebook:
                Intent startSkope = new Intent(this, Skope.class);
                startActivity(startSkope);
                finish();
                break;
        }
    }
}
