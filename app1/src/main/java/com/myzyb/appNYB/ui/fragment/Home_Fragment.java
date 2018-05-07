package com.myzyb.appNYB.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.myzyb.appNYB.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Fragment extends BaseFragment {


    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,null);
        return view;
    }
}
