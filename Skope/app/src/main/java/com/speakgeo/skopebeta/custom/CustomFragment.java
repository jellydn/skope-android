package com.speakgeo.skopebeta.custom;


import android.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.interfaces.IFragmentInitialization;

public abstract class CustomFragment extends Fragment implements IFragmentInitialization {
    protected ProgressBar prgLoading;

    @Override
    public void initControls(View container) {
        prgLoading = (ProgressBar) container.findViewById(R.id.prg_loading);
    }

    protected void showLoadingBar() {
        if(prgLoading != null) prgLoading.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingBar() {
        if(prgLoading != null) prgLoading.setVisibility(View.GONE);
    }
}