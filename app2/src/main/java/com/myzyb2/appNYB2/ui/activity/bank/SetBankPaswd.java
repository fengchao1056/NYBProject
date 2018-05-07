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

public class SetBankPaswd extends BaseActivity {

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
        setContentView(R.layout.activity_set_bank_paswd);
        ButterKnife.bind(this);

        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("设置支付密码");
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

//        bank_name	必须		开卡人名称
//        bank_id	必须		卡号
//        identity	必须		身份证号
//        bank_code	必须		所属银行
//        phone	必须		手机号
//        stpasswd	必须		密码
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        Intent intent = getIntent();
        String bank_name = intent.getStringExtra("bank_name");
        String bank_id = intent.getStringExtra("bank_id");
        String bc_area = intent.getStringExtra("bc_area");
        String dotText = intent.getStringExtra("dotText");
        String phone = intent.getStringExtra("phone");
        String identity = intent.getStringExtra("identity");
        String stpasswd = etPasswd.getText().toString();

        String stpasswd1 = NetUtils.Md5(stpasswd);
        String stpasswd2 = NetUtils.Md5(stpasswd1);

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("bank_name", NetUtils.getEncode(bank_name));
        dictParam.put("uid",NetUtils.getEncode(uid));
        dictParam.put("bank_id",NetUtils.getEncode(bank_id));
        dictParam.put("bank_code", NetUtils.getEncode(dotText));
        dictParam.put("phone",NetUtils.getEncode(phone));
        dictParam.put("identity",NetUtils.getEncode(identity));
        dictParam.put("stpasswd", stpasswd2);
        dictParam.put("bc_area", NetUtils.getEncode(bc_area));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("bank_name", bank_name);
        params.add("uid", uid);
        params.add("bank_id", bank_id);
        params.add("bank_code", dotText);
        params.add("phone", phone);
        params.add("identity", identity);
        params.add("stpasswd", stpasswd2);
        params.add("bc_area", bc_area);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.Bank_passwd, params, dictParam, new JsonHttpResponseHandler() {
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
                    String status = response.getString("status");
                    CommonDialog.closeProgressDialog();
                    if("40013".equals(status)){
                        //activity跳转
                        Intent intent = new Intent(SetBankPaswd.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        Intent intent1 = new Intent(SetBankPaswd.this, MainActivity.class);
                        //判断是从那个页面打开的绑定银行卡 false是个人中心，true是物流付款页面
                        boolean aBoolean = SharedPreferenceUtil.getBoolean(context, Constant.GO_MAIN, true);
                        if (!aBoolean) {
                            //跳转到个人中心
                            intent1.putExtra("id", 2);
                        }
                        startActivity(intent1);


                    } else {

                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
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
