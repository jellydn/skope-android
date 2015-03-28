package vn.dev.Skope;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import vn.dev.Skope.fragments.HomeFragment;
import vn.dev.Skope.views.HackyViewPager;

/**
 * Created by tientun on 3/5/15.
 */

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActionBarActivity {

    @ViewById(R.id.viewPager)
    protected HackyViewPager mHackyViewPager;

    private PageAdapter mPagerAdapter;

    @Override
    void afterView() {
        // Adapter
        mPagerAdapter = new PageAdapter(getSupportFragmentManager());
        mHackyViewPager.setAdapter(mPagerAdapter);
        mHackyViewPager.setOffscreenPageLimit(4);
        mHackyViewPager.setCurrentItem(2);

    }

    /**
     * Setup adapter hackyView
     */

    public class PageAdapter extends FragmentStatePagerAdapter {
        Context context;

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "position: " + position;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return HomeFragment.newInstance();
                case 2:
                    return HomeFragment.newInstance();
                default:
                    return HomeFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();

            super.destroyItem(container, position, object);
        }

    }
}
