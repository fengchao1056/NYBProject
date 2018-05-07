package com.myzyb2.appNYB2.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.ui.activity.main.MapActivity;
import com.myzyb2.appNYB2.ui.adapter.MyFragmentAdapter;
import com.myzyb2.appNYB2.ui.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GetGoodsFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    @Bind(R.id.mViewPager)
    MyViewPager mViewPager;

    @Bind(R.id.title_bar)
    RadioGroup title_bar;

    @OnClick(R.id.tv_map)
    public void openMap(){
        Intent mIntent=new Intent(context, MapActivity.class);
        startActivity(mIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_viewpager, null);
        ButterKnife.bind(this, view);
        initPage();
        return view;
    }

    private void initPage() {
        mViewPager.setSlipping(false);
        List<Fragment> list=new ArrayList<>();
        GrabFragment fragment1 = new GrabFragment();
        CollectFragment fragment2 = new CollectFragment();
        list.add(fragment1);
        list.add(fragment2);
        mViewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager(), list));
        mViewPager.setCurrentItem(0,false);
        title_bar.setOnCheckedChangeListener(this);
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.bt_right_list){
            mViewPager.setCurrentItem(1,false);
        }else if(checkedId==R.id.bt_left_list){
            mViewPager.setCurrentItem(0,false);
        }
    }
}