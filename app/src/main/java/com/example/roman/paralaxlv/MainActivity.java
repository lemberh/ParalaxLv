package com.example.roman.paralaxlv;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static final int OPAQUE = 255;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ParallaxView mParallaxView;
    private Drawable mDrawable;
    private PagerSlidingTabStrip mTabStrip;

    private int mActBarHeight;
    private int mTabStripHeight;
    private int mCurrentTranslation;
    private int mTopMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "oncreate");
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        mDrawable = new ColorDrawable(Color.GRAY);
        setContentView(R.layout.activity_main);
        mParallaxView = (ParallaxView) findViewById(R.id.parallaxView);
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mTabStrip.setViewPager(mViewPager);
        mTabStrip.setOnPageChangeListener(mPageChangeListener);

        mDrawable.setCallback(new Drawable.Callback() {
            @Override
            public void invalidateDrawable(Drawable drawable) {
                getSupportActionBar().setBackgroundDrawable(drawable);
            }

            @Override
            public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
            }

            @Override
            public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
            }
        });

        getSupportActionBar().setBackgroundDrawable(mDrawable);

        ViewTreeObserver vto = mTabStrip.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mActBarHeight = getSupportActionBar().getHeight();
                mTabStripHeight = mTabStrip.getHeight();
                mTopMargin = mActBarHeight + mTabStripHeight;

                ViewTreeObserver obs = mTabStrip.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public int getActionBarHeight() {
//        if (mActionBarHeight != 0) {
//            return mActionBarHeight;
//        }
//
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
//            getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
//        }else{
//            getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
//        }
//
//        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
//
//        return mActionBarHeight;
//    }

    private SampleListFragment.ScrollTabHolder mOnScrollListener = new SampleListFragment.ScrollTabHolder() {

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i3, int position) {
            if (position == mViewPager.getCurrentItem()) {
                if (visibleItemCount == 0 || firstVisibleItem != 0) {
                    mDrawable.setAlpha(OPAQUE);
                    return;
                }
                final int top = absListView.getChildAt(0).getTop();
                int alpha = OPAQUE - (top - mTopMargin);
                mDrawable.setAlpha(alpha < 0 ? 0 : alpha > OPAQUE ? OPAQUE : alpha);
                mParallaxView.setCurrentTranslation(top);
                ((FrameLayout) mTabStrip.getParent()).setMinimumHeight(Math.max(top, mTopMargin));
//            mTabStrip.setCurrentTranslation(top);
                mCurrentTranslation = top;
            }
        }
    };

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            SampleListFragment fragment = (SampleListFragment) mPagerAdapter.mFragmentHolder[i];

            fragment.adjustScroll(mCurrentTranslation);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };


    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Page 1", "Page 2", "Page 3", "Page 4"};

        public SampleListFragment[] mFragmentHolder = new SampleListFragment[TITLES.length];

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
//            SampleListFragment fragment = mFragmentHolder[position];
//            if (fragment == null) {
//                fragment = (SampleListFragment) SampleListFragment.newInstance(position);
//                mFragmentHolder[position] = fragment;
//            }
            SampleListFragment fragment = (SampleListFragment) SampleListFragment.newInstance(position);
            fragment.setScrollListener(mOnScrollListener);
            mFragmentHolder[position] = fragment;

            return fragment;
        }

    }


}
