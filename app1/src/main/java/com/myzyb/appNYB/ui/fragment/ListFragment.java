
package com.myzyb.appNYB.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.service.FragmentListener;
import com.myzyb.appNYB.ui.activity.main.ChooseModelActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 *回收列表
 */

public class ListFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.imgbtn_left)
    ImageButton imgbtnLeft;
    private View view;
    private FragmentListener listener;
    private FrameLayout frgment_base;
    private TextView txt_title;
    private Button bt_alreadyget;
    private Button bt_waitget;
    private View v_line_left;
    private View v_line_right;


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FragmentListener) activity;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initUI();
        initData();

        return view;
    }


    private void initUI() {

        txt_title = (TextView) view.findViewById(R.id.txt_title);
        frgment_base = (FrameLayout) view.findViewById(R.id.frgment_base);
        bt_waitget = (Button) view.findViewById(R.id.bt_waitget);
        bt_alreadyget = (Button) view.findViewById(R.id.bt_alreadyget);
        v_line_left = view.findViewById(R.id.v_line_left);
        v_line_right = view.findViewById(R.id.v_line_right);

    }

    private void initData() {
        imgbtnLeft.setVisibility(View.VISIBLE);
        imgbtnLeft.setImageResource(R.drawable.back_button);
        imgbtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooseModelActivity.class));
            }
        });
        txt_title.setText("回收列表");
        bt_waitget.setOnClickListener(this);
        bt_alreadyget.setOnClickListener(this);
        setDefaultFragment();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setDefaultFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        WaitGetFragment waitGetFragment = new WaitGetFragment();
        transaction.replace(R.id.frgment_base, waitGetFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.bt_waitget:
                WaitGetFragment waitGetFragment = new WaitGetFragment();
                transaction.replace(R.id.frgment_base, waitGetFragment);
                transaction.commit();
                bt_waitget.setTextColor(Color.parseColor("#08cf4e"));
                bt_alreadyget.setTextColor(Color.parseColor("#c7c8cc"));
                bt_waitget.setBackgroundColor(Color.parseColor("#ffffff"));
                bt_alreadyget.setBackgroundColor(Color.parseColor("#F9F9F9"));
                v_line_left.setBackgroundColor(Color.parseColor("#08cf4e"));
                v_line_right.setBackgroundColor(Color.parseColor("#c7c8cc"));

                break;
            case R.id.bt_alreadyget:
                AreadyFragment areadyFragment = new AreadyFragment();
                transaction.replace(R.id.frgment_base, areadyFragment);
                transaction.commit();
                bt_waitget.setTextColor(Color.parseColor("#c7c8cc"));
                bt_alreadyget.setTextColor(Color.parseColor("#08cf4e"));
                bt_waitget.setBackgroundColor(Color.parseColor("#F9F9F9"));
                bt_alreadyget.setBackgroundColor(Color.parseColor("#ffffff"));
                v_line_left.setBackgroundColor(Color.parseColor("#c7c8cc"));
                v_line_right.setBackgroundColor(Color.parseColor("#08cf4e"));
                break;


        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}