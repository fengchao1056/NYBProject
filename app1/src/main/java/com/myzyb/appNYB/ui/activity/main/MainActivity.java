package com.myzyb.appNYB.ui.activity.main;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.service.FragmentListener;
import com.myzyb.appNYB.ui.adapter.MyFragmentAdapter;
import com.myzyb.appNYB.ui.adapter.NoGetExAdapter;
import com.myzyb.appNYB.ui.fragment.FirstFragment;
import com.myzyb.appNYB.ui.fragment.ListFragment;
import com.myzyb.appNYB.ui.fragment.MyFragment;
import com.myzyb.appNYB.ui.view.GrabDialog;
import com.myzyb.appNYB.ui.view.MyViewPager;
import com.myzyb.appNYB.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页
 * cuiz
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, OnTabChangeListener, FragmentListener {

    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {FirstFragment.class, Fragment.class,
            MyFragment.class};
    private int imageViewArray[] = {R.drawable.selector_tab_frist, R.drawable.selector_tab_list,
            R.drawable.selector_tab_my/*, R.drawable.personal*/};
    private String textViewArray[] = {"首页", "回收列表", "个人中心"/*, "第四页"*/};
    private List<Fragment> list = new ArrayList<Fragment>();
    private MyViewPager vp;
    private TextView mTextView;
    private long exitTime = 0;
    private ListFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommApplication.getInstance().addActvity(this);
        setContentView(R.layout.activity_main);
        initView();
        initPage();
        jumpFragment();
    }

    //跳转监听
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
        FirstFragment fragment1 = new FirstFragment();
        fragment2 = new ListFragment();
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
        if(arg0==1&& !TextUtils.isEmpty(tel)){
            showDialog(tel);
        }

    }

    public int getSelectPosition(){
        return vp.getCurrentItem();
    }

    @Override
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        vp.setCurrentItem(position);

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


    @Override
    public void onFragmentClickListener(int item, NoGetExAdapter fragment) {
        switch (item) {
            case 0:
                break;

        }

    }
    public String tel="";

    public void showDialog(final String tel){
        GrabDialog mGrabDialog=new GrabDialog(this).builder();
        mGrabDialog.setCancelable(false);
        mGrabDialog.setTelCall(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ToastUtil.showToast("请开启打电话权限");
                    return;
                }
                startActivity(intent);
            }
        });
        mGrabDialog.show();
    }
}
