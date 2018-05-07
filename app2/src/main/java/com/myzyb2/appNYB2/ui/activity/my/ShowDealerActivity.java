package com.myzyb2.appNYB2.ui.activity.my;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.Dealer;
import com.myzyb2.appNYB2.javabean.Dealer.ListDealer;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.adapter.ShowDealerAdapter;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 个人中心——网点列表
 */
public class ShowDealerActivity extends BaseActivity {
    @Bind(R.id.lv_show_dealer)
    ListView lvShowDealer;
    List<ListDealer> list;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showDealerAdapter.setLIst(list);
                    showDealerAdapter.notifyDataSetChanged();
                    break;


            }
        }
    };
    private ShowDealerAdapter showDealerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dealer);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("网点列表");
        getDataFromServic();
        initList();

    }

    private void initList() {
        showDealerAdapter = new ShowDealerAdapter(context);
        lvShowDealer.setAdapter(showDealerAdapter);

    }

    private void getDataFromServic() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String WL_ID = SharedPreferenceUtil.getString(context, Constant.WL_ID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("ems_gid", WL_ID);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("ems_gid", WL_ID);
        params.add("access_token", token);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.Dealer_show, params, dictParam, new JsonHttpResponseHandler() {


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
                    String status = response.getString("status");
                    LogUtil.e("sohow", response.toString());
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(ShowDealerActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        Dealer dealer = JsonUtil.getSingleBean(response.toString(), Dealer.class);
                        if (dealer != null) {
                            list = dealer.getList();
                            mHandler.sendEmptyMessage(1);
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
                CommonUtil.StartToast(context, "连接失败" + statusCode);
            }
        });


    }


}
