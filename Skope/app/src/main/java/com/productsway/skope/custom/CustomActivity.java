package com.productsway.skope.custom;


import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import com.productsway.skope.R;
import com.productsway.skope.interfaces.IActivityInitialization;

public abstract class CustomActivity extends Activity implements IActivityInitialization {
    protected ProgressBar prgLoading;

    @Override
    public void initControls() {
        prgLoading = (ProgressBar) this.findViewById(R.id.prg_loading);
    }

    protected void showLoadingBar() {
        if(prgLoading != null) prgLoading.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingBar() {
        if(prgLoading != null) prgLoading.setVisibility(View.GONE);
    }
}