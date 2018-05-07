package com.myzyb2.appNYB2.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/****
 * 页面适配器
 *
 * @author lambert_lei
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    /**
     * The m fragment list.
     */
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private int currentPageIndex = 0;
    private String[] titles = null;
    private FragmentManager mFragmentManager;

    public MyFragmentPagerAdapter(FragmentManager mFragmentManager, String[] titles) {
        super(mFragmentManager);
        this.titles = titles;
        this.mFragmentManager = mFragmentManager;
    }

    public void setList(ArrayList<Fragment> fragmentList) {
        this.mFragmentList = fragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        if (titles != null && titles.length > 0) {
            return titles[position];
        } else {
            return super.getPageTitle(position);
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mFragmentList.get(position).getView());
    }

    /**
     * 页面数
     *
     * @return the count
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * 获取大每个Fragment.
     *
     * @param position the position
     * @return the item
     * @see FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position < mFragmentList.size()) {
            fragment = mFragmentList.get(position);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;

    }
}
