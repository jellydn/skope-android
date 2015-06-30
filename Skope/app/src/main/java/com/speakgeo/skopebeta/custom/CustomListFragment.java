package com.speakgeo.skopebeta.custom;


import android.app.Fragment;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.speakgeo.skopebeta.R;
import com.speakgeo.skopebeta.interfaces.IFragmentInitialization;

public abstract class CustomListFragment extends ListFragment implements IFragmentInitialization {
    protected ListView mainList;
    private View footer;

    @Override
    public void initControls(View container) {
        mainList = (ListView)container.findViewById(android.R.id.list);
        footer = getActivity().getLayoutInflater().inflate(R.layout.footer, null);
    }

    protected void showLoadingBar() {
        if(mainList != null) {
            mainList.addFooterView(footer);
        }
    }

    protected void hideLoadingBar() {
        if(mainList != null) {
            //View view = getActivity().getLayoutInflater().inflate(R.layout.footer, null);
           // mainList.removeFooterView(footer);
        }
    }
}