package com.myzyb2.appNYB2.ui.activity.main;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.CommApplication;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.adapter.MyFragmentAdapter;
import com.myzyb2.appNYB2.ui.fragment.GetGoodsFragment;
import com.myzyb2.appNYB2.ui.fragment.GiveGoodsFragment;
import com.myzyb2.appNYB2.ui.fragment.MyFragment;
import com.myzyb2.appNYB2.ui.view.MyViewPager;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, OnTabChangeListener {

    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {GetGoodsFragment.class, GiveGoodsFragment.class,
            MyFragment.class};
    private int imageViewArray[] = {R.drawable.selector_tab_get, R.drawable.selector_tab_give,
            R.drawable.selector_tab_my/*, R.drawable.personal*/};
    private String textViewArray[] = {"取货", "送货", "个人中心"};
    private List<Fragment> list = new ArrayList<Fragment>();
    private MyViewPager vp;
    private TextView mTextView;
    private long exitTime = 0;
    private Context context;

    protected static final String tag = "tfl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommApplication.getInstance().addActvity(this);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
        initPage();
        jumpFragment();
        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(tag, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(tag, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(tag, permission.name + " is denied.");
                        }
                    }
                });
    }

    private void jumpFragment() {

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id > 0) {
            vp.setCurrentItem(id);
        }

    }

    /**
     * 初始化页面
     */
    private void initView() {
        vp = (MyViewPager) findViewById(R.id.pager);
        vp.setOnPageChangeListener(this);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        vp.setSlipping(false);//不可滑动
        layoutInflater = LayoutInflater.from(this);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.pager);
        mTabHost.setOnTabChangedListener(this);


        int count = textViewArray.length;

        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(textViewArray[i])
                    .setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.setTag(i);
            mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
    }

    private void initPage() {
        GetGoodsFragment fragment1 = new GetGoodsFragment();
        GiveGoodsFragment fragment2 = new GiveGoodsFragment();
        MyFragment fragment3 = new MyFragment();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
    }

    private View getTabItemView(int i) {
        View view = layoutInflater.inflate(R.layout.tabhost_content, null);
        ImageView mImageView = (ImageView) view.findViewById(R.id.tab_imageview);
        mTextView = (TextView) view.findViewById(R.id.tab_textview);
        mImageView.setBackgroundResource(imageViewArray[i]);
        mTextView.setText(textViewArray[i]);
        return view;

    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {

        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();//处理子view 和父view的焦点
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(arg0);
        widget.setDescendantFocusability(oldFocusability);

    }

    @Override
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        if (position != 1) {
            vp.setCurrentItem(position,false);
        } else {
            CommonUtil.StartToast(this, "送货开发中");

        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                CommApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
