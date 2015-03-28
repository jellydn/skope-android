package vn.dev.Skope.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

/**
 * Created by tientun on 3/5/15.
 */
public class BaseFragment extends Fragment {
    private final SparseArray<Object> mRequestCodes = new SparseArray<Object>();

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (getParentFragment() instanceof BaseFragment) {
            if (getId() > 0) {
                ((BaseFragment) getParentFragment()).registerRequestCode(requestCode, getId());
            }else if (getTag() != null) {
                ((BaseFragment) getParentFragment()).registerRequestCode(requestCode, getTag());
            }
            getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!checkNestedFragmentsForResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    protected boolean checkNestedFragmentsForResult(int requestCode, int resultCode, Intent data) {
        Object obj = mRequestCodes.get(requestCode);
        if (obj == null) {
            return false;
        }
        mRequestCodes.remove(requestCode);
        Fragment fragment;
        if (obj instanceof Integer) {
            fragment = getChildFragmentManager().findFragmentById((Integer) obj);
        } else if (obj instanceof String) {
            fragment = getChildFragmentManager().findFragmentByTag((String) obj);
        } else {
            return false;
        }
        fragment.onActivityResult(requestCode, resultCode, data);
        return true;
    }

    public void registerRequestCode(int requestCode, int fragmentId) {
        mRequestCodes.put(requestCode, fragmentId);
    }

    public void registerRequestCode(int requestCode, String fragmentTag) {
        mRequestCodes.put(requestCode, fragmentTag);
    }
}
