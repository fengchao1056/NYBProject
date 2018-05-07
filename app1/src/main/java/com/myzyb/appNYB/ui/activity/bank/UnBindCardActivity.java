package com.myzyb.appNYB.ui.activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.view.PasswordDialog;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class UnBindCardActivity extends BaseActivity {

    @Bind(R.id.tv_bankcard)
    TextView tvBankcard;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    private PasswordDialog passwordDialog;
    private String stpasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_bind_card);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("解绑银行卡");
        Intent intent = getIntent();
        String bc_id = intent.getStringExtra("bc_id");
        tvBankcard.setText(bc_id);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordDialog = new PasswordDialog(context);
                passwordDialog.builder()
                        .setTitle("请输入支付密码")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                stpasswd = passwordDialog.getEtString();
                                if (!TextUtils.isEmpty(stpasswd) && stpasswd.length() > 5) {
                                    sendDataToServe();
                                    CommonDialog.showProgressDialog(context);
                                } else {
                                    CommonUtil.StartToast(context, "请输入6位支付密码");
                                }
                            }
                        }).show();

            }
        });
    }

    private void sendDataToServe() {

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");

        String pwd1 = NetUtils.Md5(stpasswd);
        String pwd2 = NetUtils.Md5(pwd1);

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("stpasswd", pwd2);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("stpasswd", pwd2);
        params.add("access_token", token);

        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.Un_bank, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                try {
                    LogUtil.e("response", response.toString());
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    LogUtil.e("response", response.toString());
                    String status = response.getString("status");
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(UnBindCardActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        Intent intent1 = new Intent(UnBindCardActivity.this, MainActivity.class);
                        intent1.putExtra("id", 2);
                        startActivity(intent1);
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                        CommonDialog.closeProgressDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                CommonUtil.StartToast(context, "连接失败");
            }
        });

    }

}
