package com.myzyb2.appNYB2.ui.activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.activity.main.MainActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.view.PasswordDialog;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ReplacePayActivity extends BaseActivity {

    @Bind(R.id.et_passwd)
    EditText etPasswd;
    @Bind(R.id.et_passwd_next)
    EditText etPasswdNext;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    private PasswordDialog passwordDialog;
    private String stpasswd;
    boolean isPw = false;
    boolean isPwNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_pay);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("修改支付密码");
        etPasswd.addTextChangedListener(new MyTextWatcher(etPasswd));
        etPasswdNext.addTextChangedListener(new MyTextWatcher(etPasswdNext));
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((etPasswd.getText().toString().equals(etPasswdNext.getText().toString()))) {
                    passwordDialog = new PasswordDialog(context);
                    passwordDialog.builder()
                            .setTitle("请输入旧支付密码")
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
                } else {
                    CommonUtil.StartToast(context, "两次输入密码不一致");

                }

            }
        });

    }

    private void sendDataToServe() {

//        uid	true	int	用户id
//        oldpasswd	true	int	原密码
//        newpasswd	true	int	新密码
//        repasswd	true	string	重复密码

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        String newpasswd = etPasswd.getText().toString();
        String repasswd = etPasswdNext.getText().toString();

        String newpasswd1 = NetUtils.Md5(newpasswd);
        String newpasswd2 = NetUtils.Md5(newpasswd1);

        String repasswd1 = NetUtils.Md5(repasswd);
        String repasswd2 = NetUtils.Md5(repasswd1);

        String stpasswd1 = NetUtils.Md5(stpasswd);
        String stpasswd2 = NetUtils.Md5(stpasswd1);

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("newpasswd", newpasswd2);
        dictParam.put("repasswd", repasswd2);
        dictParam.put("oldpasswd", stpasswd2);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("newpasswd", newpasswd2);
        params.add("repasswd", repasswd2);
        params.add("oldpasswd", stpasswd2);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.replace_passwd, params, dictParam, new JsonHttpResponseHandler() {
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
                    LogUtil.e("response", response.toString());
                    CommonDialog.closeProgressDialog();
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(ReplacePayActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        Intent intent1 = new Intent(ReplacePayActivity.this, MainActivity.class);
                        intent1.putExtra("id", 2);
                        startActivity(intent1);
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
                CommonDialog.closeProgressDialog();
                CommonUtil.StartToast(context, "连接失败");
            }
        });

    }

    class MyTextWatcher implements TextWatcher {
        View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String s1 = s.toString();
            switch (view.getId()) {
                case R.id.et_passwd:
                    if (!TextUtils.isEmpty(s1) && s1.length() > 5) {
                        isPw = true;

                    } else {
                        isPw = false;
                    }
                    break;
                case R.id.et_passwd_next:
                    if (!TextUtils.isEmpty(s1) && s1.length() > 5) {
                        isPwNext = true;

                    } else {
                        isPwNext = false;
                    }

                    break;


            }

            if (isPw && isPwNext) {

                btSubmit.setEnabled(true);

            } else {
                btSubmit.setEnabled(false);
            }
        }
    }
}