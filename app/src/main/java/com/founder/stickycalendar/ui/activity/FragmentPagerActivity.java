package com.founder.stickycalendar.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.founder.stickycalendar.R;
import com.founder.stickycalendar.ui.fragment.CalendarListViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>Title:</P>
 * <P>Description::</P>
 *
 * @author liu-yuwu
 * @date 16-5-19
 */
public class FragmentPagerActivity extends AppCompatActivity {


    public static final int TITLE1 = 0;
    public static final int TITLE2 = 1;
    public static final int TITLE3 = 2;
    public static final int TITLE4 = 3;

    private TabLayout mTablayout;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_pager);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);
        mTablayout.addTab(mTablayout.newTab().setText(R.string.title1));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.title2));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.title3));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.title4));
        mTablayout.setupWithViewPager(mViewPager);
    }
    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CalendarListViewFragment.newInstance(TITLE1), getString(R.string.title1));
        adapter.addFragment(CalendarListViewFragment.newInstance(TITLE2), getString(R.string.title2));
        adapter.addFragment(CalendarListViewFragment.newInstance(TITLE3), getString(R.string.title3));
        adapter.addFragment(CalendarListViewFragment.newInstance(TITLE4), getString(R.string.title4));
        mViewPager.setAdapter(adapter);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
