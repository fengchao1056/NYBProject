package com.myzyb.appNYB.ui.activity.bank;

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
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
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

public class ResettingPayActivity extends BaseActivity {

    @Bind(R.id.et_passwd)
    EditText etPasswd;
    @Bind(R.id.et_passwd_next)
    EditText etPasswdNext;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    boolean isPw = false;
    boolean isPwNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetting_pay);
        ButterKnife.bind(this);
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("重置支付密码");

        etPasswd.addTextChangedListener(new MyTextWatcher(etPasswd));
        etPasswdNext.addTextChangedListener(new MyTextWatcher(etPasswdNext));
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((etPasswd.getText().toString().equals(etPasswdNext.getText().toString()))) {
                    CommonDialog.showProgressDialog(context);
                    sendDataToServe();

                } else {
                    CommonUtil.StartToast(context, "两次输入密码不一致");

                }

            }
        });
    }

    private void sendDataToServe() {


//        uid	true	int	用户id
//        stpasswd	true	int	密码
//        bank_name	true	int	姓名
//        bank_id	true	int	银行卡
//        identity	true	int	身份证
//        phone	true	int	手机  Intent intent = new Intent();


        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String bank_name = intent.getStringExtra("bank_name");
        String bank_id = intent.getStringExtra("bank_id");
        String identity = intent.getStringExtra("identity");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        String stpasswd = etPasswd.getText().toString();

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String pwd1 = NetUtils.Md5(stpasswd);
        String pwd2 = NetUtils.Md5(pwd1);
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("phone", phone);
        dictParam.put("bank_name", NetUtils.getEncode(bank_name));
        dictParam.put("stpasswd", pwd2);
        dictParam.put("bank_id", bank_id);
        dictParam.put("identity", identity);
        dictParam.put("access_token", NetUtils.getEncode(token));

        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("phone", phone);
        params.add("bank_name", bank_name);
        params.add("stpasswd", pwd2);
        params.add("bank_id", bank_id);
        params.add("identity", identity);
        params.add("access_token", token);

        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.resetting_passwd, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    LogUtil.e("response", response.toString());
                    CommonDialog.closeProgressDialog();
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    LogUtil.e("response", response.toString());
                    String status = response.getString("status");
                    CommonDialog.closeProgressDialog();
                    if("40013".equals(status)){
                        //activity跳转
                        Intent intent = new Intent(ResettingPayActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        Intent intent1 = new Intent(ResettingPayActivity.this, MainActivity.class);
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
