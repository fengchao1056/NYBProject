package com.myzyb.appNYB.ui.activity.regiser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 输入验证码
 * Created by cuiz on 2015/11/18.
 */
public class ImportCodeActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.tv_phone_import)
    protected TextView tv_phone_import;
    @Bind(R.id.bt_time_imp)
    protected Button bt_time_imp;
    @Bind(R.id.bt_sub_imp)
    protected Button bt_sub_imp;
    @Bind(R.id.et_code)
    protected EditText etCode;
    private String mobile;
    private String passwd;
    private String mCode;
    private Context context;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importcode);
        context = this;
        ButterKnife.bind(this);
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("输入验证码");
        initData();
        initButten();
    }

    private void initButten() {

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bt_time_imp.setEnabled(false);
                bt_time_imp.setText((millisUntilFinished / 1000) + "S重新获取");
            }

            @Override
            public void onFinish() {
                bt_time_imp.setText("重新获取");
                bt_time_imp.setEnabled(true);

            }
        }.start();

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCode = s.toString();
                if (!TextUtils.isEmpty(mCode)) {
                    bt_sub_imp.setEnabled(true);

                } else {
                    bt_sub_imp.setEnabled(false);

                }
            }
        });
    }

    private void initData() {
        Bundle bunde = this.getIntent().getExtras();
        mobile = bunde.getString("mobile");
        passwd = bunde.getString("passwd");
        tv_phone_import.setText(mobile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sub_imp:
                //提交数据给服务器
                CommonDialog.showProgressDialog(context);

                String stpasswd1 = NetUtils.Md5(passwd);
                String stpasswd2 = NetUtils.Md5(stpasswd1);

                HashMap<String, String> dictParam = new HashMap<String, String>();
                dictParam.put("mobile", mobile);
                dictParam.put("passwd", stpasswd2);
                dictParam.put("code", mCode);
                RequestParams registerParams = new RequestParams();
                registerParams.add("mobile", mobile);
                registerParams.add("passwd", stpasswd2);
                registerParams.add("code", mCode);
                NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.REGISTERURL, registerParams, dictParam, new JsonHttpResponseHandler() {
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
                            String message = response.getString("message");
                            LogUtil.e("注册接口状态码", response.getString("status"));
                            LogUtil.e("注册接口返回信息", response.getString("message"));
                            if("40013".equals(status)){
                                //activity跳转
                                Intent intent = new Intent(ImportCodeActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else if("1001".equals(status)){
                                SharedPreferenceUtil.saveString(context, Constant.MOBLIE, mobile);
                                login();
                            }else if("1002".equals(status)){
                                CommonDialog.closeProgressDialog();
                                CommonDialog.showInfoDialog(context, "验证码错误");
                            }else{
                                CommonUtil.StartToast(context, message);
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

                break;

            case R.id.bt_time_imp:
                String token1 = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
                HashMap<String, String> dictPara = new HashMap<String, String>();
                dictPara.put("mobile", mobile);
                dictPara.put("access_token", NetUtils.getEncode(token1));
                RequestParams codeParams = new RequestParams();
                codeParams.put("mobile", mobile);
                codeParams.put("access_token", token1);
                NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.GETCODE, codeParams, dictPara, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            LogUtil.e("验证码接口状态码", response.getString("status"));
                            LogUtil.e("验证码接口返回信息", response.getString("message"));
                            timer.start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                    }
                });
                break;
        }
    }

    private void login() {
        String stpasswd1 = NetUtils.Md5(passwd);
        String stpasswd2 = NetUtils.Md5(stpasswd1);
        HashMap<String, String> dictPara = new HashMap<String, String>();
        dictPara.put("phone", mobile);
        dictPara.put("passwd", stpasswd2);
        dictPara.put("type", Constant.type);
        RequestParams params = new RequestParams();
        params.put("phone", mobile);
        params.put("passwd", stpasswd2);
        params.put("type", Constant.type);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.LOGINURL, params, dictPara, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    CommonDialog.closeProgressDialog();
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    String status = response.getString("status");
                    if("40013".equals(status)){
                        //activity跳转
                        Intent intent = new Intent(ImportCodeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        LogUtil.e("验证码登录", response.toString());
                        JSONObject json = response.getJSONObject("list");
                        String token = json.getString("token");
                        String loginSalt = json.getString("login_salt");
                        String key = json.getString("key");
                        String iv = json.getString("iv");
                        String uid = json.getString("uid");
                        LogUtil.e("登录", uid);
                        SharedPreferenceUtil.saveString(context, Constant.MOBLIE, mobile);
                        SharedPreferenceUtil.saveString(context, Constant.YHID, uid);
                        SharedPreferenceUtil.saveString(context, Constant.TOKEN, token);
                        SharedPreferenceUtil.saveString(context, Constant.KEY, key);
                        SharedPreferenceUtil.saveString(context, Constant.IV, iv);
                        SharedPreferenceUtil.saveString(context, Constant.LoginSalt, loginSalt);
                        startActivity(new Intent(context, RegisterMesActivity.class));
                        finish();
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.net_error));

            }
        });
    }
}
