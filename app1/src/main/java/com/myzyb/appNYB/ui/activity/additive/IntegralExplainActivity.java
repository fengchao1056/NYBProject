package com.myzyb.appNYB.ui.activity.additive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class IntegralExplainActivity extends BaseActivity {


    @Bind(R.id.web_view)
    WebView webView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initTitleBar();
        setTitleBar_titletext("积分说明");
        setTitleBar_back();
        getData();
    }

    private void getData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("fname", Constant.jf_integral_info);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("fname", Constant.jf_integral_info);
        params.add("access_token", token);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.JF_TK, params, dictParam, new JsonHttpResponseHandler() {
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
                        Intent intent = new Intent(IntegralExplainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        String list = response.getString("list");
                        if (list != null) {
                            String s = new String(Base64.decode(list.getBytes(), Base64.DEFAULT));
                            //webView.loadData(s, "text/html", "UTF-8");

                            webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
        });

    }

}
