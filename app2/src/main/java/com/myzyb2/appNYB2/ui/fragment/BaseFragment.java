package com.myzyb2.appNYB2.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myzyb2.appNYB2.http.NetUtils;

/**
 * Created by cuiz on 2015/11/30.
 */
public abstract class BaseFragment extends Fragment {

    public Context context;
    public View view;
    public NetUtils netUtils;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        netUtils = NetUtils.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        context = getActivity();
//        setTitle();
        return view;
    }

}
