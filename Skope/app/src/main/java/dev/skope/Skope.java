package dev.skope;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import dev.skope.tabContainer.HomeContainer;
import dev.skope.tabContainer.ListPostContainer;
import dev.skope.tabContainer.ListUserContainer;
import dev.skope.tabContainer.UserProfileContainer;
import dev.skope.ui.HackyViewPager;


public class Skope extends FragmentActivity {

    private HackyViewPager mHackyViewPager;

    private PageAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skope);

        initialize();
        setValue();
        setEvent();
    }

    private void initialize() {
        // HackyView
        mHackyViewPager = (HackyViewPager) findViewById(R.id.viewPager);

        // Adapter
        mPagerAdapter = new PageAdapter(getSupportFragmentManager());
    }

    private void setValue() {
        mHackyViewPager.setAdapter(mPagerAdapter);
        mHackyViewPager.setOffscreenPageLimit(4);
        mHackyViewPager.setCurrentItem(2);
    }

    private void setEvent() {

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
                    return new UserProfileContainer();
                case 1:
                    return new ListUserContainer();
                case 2:
                    return new HomeContainer();
                default:
                    return new ListPostContainer();
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
