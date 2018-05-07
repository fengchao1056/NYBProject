package com.myzyb.appNYB.ui.activity.bank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;
import com.myzyb.appNYB.util.ValidateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ForgetPayActivity extends BaseActivity {

    @Bind(R.id.et_bank_num)
    EditText etBankNum;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_peoplenum)
    EditText etPeoplenum;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.bt_getCode)
    Button btGetCode;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    boolean isName = false;
    boolean isBankNum = false;
    boolean isPhone = false;
    boolean ispeoplenum = false;
    boolean isgetCode = false;
    boolean isetCode = false;
    private String bank_name;
    private String phone;
    private String bank_id;
    private String identity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pay);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("忘记支付密码");
        initView();
    }

    //    uid	true	int	用户id
//    phone	true	int	手机
    private void initView() {
        etName.addTextChangedListener(new MyTextWatcher(etName));
        etBankNum.addTextChangedListener(new MyTextWatcher(etBankNum));
        etPhone.addTextChangedListener(new MyTextWatcher(etPhone));
        etPeoplenum.addTextChangedListener(new MyTextWatcher(etPeoplenum));
        etCode.addTextChangedListener(new MyTextWatcher(etCode));
        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForCode();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServe();
            }
        });
    }

    private void sendForCode() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String phone = etPhone.getText().toString();
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("phone", phone);
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("phone", phone);
        params.add("uid", uid);
        params.add("access_token", token);

        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.get_code, params, dictParam, new JsonHttpResponseHandler() {
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
                        Intent intent = new Intent(ForgetPayActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        isetCode = true;

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
                CommonUtil.StartToast(context, "连接失败");
            }
        });


    }

    private void sendDataToServe() {

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        phone = etPhone.getText().toString();
        bank_name = etName.getText().toString();
        bank_id = etBankNum.getText().toString();
        identity = etPeoplenum.getText().toString();
        String code = etCode.getText().toString();
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("bank_name", NetUtils.getEncode(bank_name));
        dictParam.put("bank_id", bank_id);
        dictParam.put("code", code);
        dictParam.put("phone", phone);
        dictParam.put("identity", NetUtils.getEncode(identity));
        dictParam.put("access_token", NetUtils.getEncode(token));

        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("bank_name", bank_name);
        params.add("bank_id", bank_id);
        params.add("code", code);
        params.add("phone", phone);
        params.add("identity", identity);
        params.add("access_token", token);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.validate_code, params, dictParam, new JsonHttpResponseHandler() {
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
                    if("40013".equals(status)){
                        //activity跳转
                        Intent intent = new Intent(ForgetPayActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        Intent intent = new Intent(ForgetPayActivity.this, ResettingPayActivity.class);
                        intent.putExtra("phone", phone);
                        intent.putExtra("bank_name", bank_name);
                        intent.putExtra("bank_id", bank_id);
                        intent.putExtra("identity", identity);
                        startActivity(intent);

                    } else if ("1004".equals(status)) {
                        CommonUtil.StartToast(context, "验证码过期");

                    } else if ("1005".equals(status)) {
                        CommonUtil.StartToast(context, "验证码错误");
                        etCode.setTextColor(Color.parseColor("#cf0808"));
                    } else if ("1022".equals(status)) {
                        CommonUtil.StartToast(context, "银行开户人姓名错误");
                        etName.setTextColor(Color.parseColor("#cf0808"));
                    } else if ("1023".equals(status)) {
                        CommonUtil.StartToast(context, "银行卡号错误");
                        etBankNum.setTextColor(Color.parseColor("#cf0808"));
                    } else if ("1024".equals(status)) {
                        CommonUtil.StartToast(context, "身份证错误");
                        etPeoplenum.setTextColor(Color.parseColor("#cf0808"));
                    } else if ("1025".equals(status)) {
                        CommonUtil.StartToast(context, "手机号错误");
                        etPhone.setTextColor(Color.parseColor("#cf0808"));
                    } else {
                        CommonUtil.StartToast(context, "验证失败");
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

        //        @Bind(R.id.et_bank_num)
//        EditText etBankNum;
//        @Bind(R.id.et_name)
//        EditText etName;
//        @Bind(R.id.et_peoplenum)
//        EditText etPeoplenum;
//        @Bind(R.id.et_phone)
//        EditText etPhone;
//        @Bind(R.id.bt_getCode)
//        Button btGetCode;
//        @Bind(R.id.et_code)
//        EditText etCode;
//        @Bind(R.id.bt_submit)
//        Button btSubmit;
        @Override
        public void afterTextChanged(Editable s) {
            String s1 = s.toString();
            ((TextView) view).setTextColor(Color.parseColor("#000000"));
            switch (view.getId()) {

                case R.id.et_bank_num:
                    if (!TextUtils.isEmpty(s1)) {
                        isBankNum = true;

                    } else {
                        isBankNum = false;
                    }
                    break;
                case R.id.et_name:
                    if (!TextUtils.isEmpty(s1)) {
                        isName = true;

                    } else {
                        isName = false;
                    }
                    break;

                case R.id.et_peoplenum:
                    if (!TextUtils.isEmpty(s1)) {
                        ispeoplenum = true;

                    } else {
                        ispeoplenum = false;
                    }

                    break;
                case R.id.et_phone:
                    if (!TextUtils.isEmpty(s1)) {
                        isPhone = true;
                        if (ValidateUtil.isMobile(s1)) {
                            btGetCode.setEnabled(true);
                        } else {
                            btGetCode.setEnabled(false);
                        }

                    } else {
                        isPhone = false;
                    }

                    break;
                case R.id.et_code:
                    if (!TextUtils.isEmpty(s1)) {
                        isetCode = true;

                    } else {
                        isetCode = false;
                    }
                    break;
            }

            if (isName && isBankNum && isPhone && ispeoplenum) {

                btSubmit.setEnabled(true);

            } else {
                btSubmit.setEnabled(false);
            }
        }
    }


}
