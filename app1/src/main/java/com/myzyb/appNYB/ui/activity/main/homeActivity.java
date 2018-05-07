package com.myzyb.appNYB.ui.activity.main;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.service.FragmentListener;
import com.myzyb.appNYB.ui.adapter.MyFragmentAdapter;
import com.myzyb.appNYB.ui.adapter.NoGetExAdapter;
import com.myzyb.appNYB.ui.fragment.Home_Fragment;
import com.myzyb.appNYB.ui.fragment.RecycleFragment;
import com.myzyb.appNYB.ui.fragment.walletFragment;
import com.myzyb.appNYB.ui.view.HomeViewPager;

import java.util.ArrayList;

public class homeActivity extends FragmentActivity implements FragmentListener {
    private HomeViewPager Vp;
    private FragmentTabHost mTabHost;
    private int imageArr[];
    private String textArray[];
    private Class[] list;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
        initView();
        initFragment();
    }

    private void initData(){
        list = new Class[]{Home_Fragment.class, RecycleFragment.class,walletFragment.class};
        imageArr = new int[]
                { R.drawable.selector_tab_home
                , R.drawable.selector_recycle_tab
                , R.drawable.selector_wallet_tab
                };
        textArray = new String[]{"采购","回收","钱包"};
    }


    private void initView(){
        Vp = (HomeViewPager)findViewById(R.id.homePage);
        Vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        Vp.setisSlipping(false);//不可滑动
        layoutInflater = LayoutInflater.from(this);//

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.homePage);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int position =   mTabHost.getCurrentTab();
                Vp.setCurrentItem(position);
            }
        });

        class ItemtabSpec{
            public  View getTabItemView(int i){
                View view =layoutInflater.inflate(R.layout.tabhost_content,null);
                ImageView mImageView = (ImageView)view.findViewById(R.id.tab_imageview);
                mImageView.setBackgroundResource(imageArr[i]);
                TextView mTxt= (TextView)view.findViewById(R.id.tab_textview);
                mTxt.setText(textArray[i]);
                return  view;
            }
        }
        ItemtabSpec space = new ItemtabSpec();
        for(int i = 0 ; i < textArray.length ;i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(textArray[i]).setIndicator(space.getTabItemView(i));
            mTabHost.addTab(tabSpec, list[i], null);
            mTabHost.setTag(i);
            mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
    }
    private void initFragment(){
        Home_Fragment home = new Home_Fragment();
        RecycleFragment recycle  = new RecycleFragment();
        walletFragment wallet = new walletFragment();
        ArrayList list = new ArrayList();
        list.add(home);
        list.add(recycle);
        list.add(wallet);
        Vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),list));
        Vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabWidget widget = mTabHost.getTabWidget();
                int oldFocusability = widget.getDescendantFocusability();//处理子view 和父view的焦点
                widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                mTabHost.setCurrentTab(position);
                widget.setDescendantFocusability(oldFocusability);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onFragmentClickListener(int item, NoGetExAdapter fragment) {
        switch (item) {
            case 0:
                break;

        }
    }
}
