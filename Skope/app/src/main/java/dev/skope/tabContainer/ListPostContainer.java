package dev.skope.tabContainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.skope.R;
import dev.skope.tabListPost.FragmentListPost;
import dev.skope.ui.BaseContainerFragment;


public class ListPostContainer extends BaseContainerFragment {

    private boolean mIsViewInited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsViewInited) {
            mIsViewInited = true;
            initView();
        }
    }

    private void initView() {
        replaceFragment(new FragmentListPost().newInstance(), false);

    }

    public Fragment getChildFragment() {
        return getChildFragmentManager().findFragmentById(R.id.container_framelayout);
    }
}
