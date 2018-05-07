package com.myzyb.appNYB.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.ChooseModelActivity;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.FontsUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
/**
 *首页
 */


public class FirstFragment extends BaseFragment {


    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_xh1)
    TextView tvXh1;
    @Bind(R.id.tv_xh2)
    TextView tvXh2;
    private View view;
    private ImageButton ib_submit_first;
    private String prize;
    private String prize1;
    private String prize2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_first, null);
        ImageButton imgbtn_left2 = (ImageButton) view.findViewById(R.id.imgbtn_left2);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText("能源宝");
        imgbtn_left2.setVisibility(View.VISIBLE);
        imgbtn_left2.setBackgroundResource(R.drawable.logo);
        ib_submit_first = (ImageButton) view.findViewById(R.id.ib_submit_first);
        ib_submit_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ChooseModelActivity.class));
            }
        });

        initData();
        ButterKnife.bind(this, view);
        return view;

    }

    private void initData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String area_id = SharedPreferenceUtil.getString(context, Constant.DQID, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", NetUtils.getEncode(uid));
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.put("area_id", area_id);
        params.put("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.RECOVERYPRICE, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        JSONObject list = response.getJSONObject("list");
                        prize = list.getString("prize");
                        prize1 = list.getString("prize1");
                        prize2 = list.getString("prize2");
                        if (tvNum != null && prize != null) {
                            tvNum.setText(prize);
                            FontsUtil.FontsUtil(tvNum, context);
                            tvXh1.setText("6-DZM-12  " + prize1);
                            tvXh2.setText("6-DZM-20  " + prize2);
                        }
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                LogUtil.e("responseString", responseString);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}