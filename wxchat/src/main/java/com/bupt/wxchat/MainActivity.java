package com.bupt.wxchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.bupt.wxchat.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private List<String> mTitles = Arrays.asList("短信1", "收藏2", "推荐3","短信4", "收藏5", "推荐6","短信7", "收藏8", "推荐9");
    private List<ViewPagerFragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        mIndicator.setVisibleTabNum(3);
        mIndicator.setTabItemTitles(mTitles);

        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager,0);

    }

    /**
     * 初始化View
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        for (String title :
                mTitles) {
            ViewPagerFragment fragment = ViewPagerFragment.newInstance(title);
            fragments.add(fragment);
        }
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
            @Override
            public int getCount() {
                return fragments.size();
            }
        };
    }
}
