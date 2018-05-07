package com.myzyb2.appNYB2.ui.activity.login;

import android.app.Activity;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.util.ClickUtil;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.ValidateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class FargetPwActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.et_moblie)
    EditText etMoblie;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.et_passwd)
    EditText etPasswd;
    @Bind(R.id.bt_time_imp)
    Button btTimeImp;
    @Bind(R.id.bt_sub_imp)
    Button btSubImp;
    private String mobile;
    private String passwd;
    private String mCode;
    private Context context;
    private boolean notEmptyMobile = false;
    private boolean notEmptyPasswd = false;
    private boolean notEmptyCode = false;
    private boolean isSendCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farget_pw);
        ButterKnife.bind(this);
        context = this;

        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("忘记密码");
        onTextChange();
    }

    private void initTimer() {
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btTimeImp.setEnabled(false);
                btTimeImp.setText((millisUntilFinished / 1000) + "S重新获取");
            }

            @Override
            public void onFinish() {
                btTimeImp.setText("重新获取");
                btTimeImp.setEnabled(true);

            }
        }.start();
    }

    private void onTextChange() {

        etMoblie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobile = s.toString();
                if (!TextUtils.isEmpty(s.toString()) && ValidateUtil.isMobile(s.toString())) {
                    notEmptyMobile = true;
                    btTimeImp.setEnabled(true);
                    isSubmit();
                } else {
                    notEmptyMobile = false;
                    btTimeImp.setEnabled(false);
                    isSubmit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwd = s.toString();
                if (!TextUtils.isEmpty(s.toString()) && passwd.length() > 5) {
                    notEmptyPasswd = true;
                    isSubmit();
                } else {
                    notEmptyPasswd = false;
                    isSubmit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCode.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCode = s.toString();
                if (!TextUtils.isEmpty(s.toString())) {
                    notEmptyCode = true;
                    isSubmit();
                } else {
                    notEmptyCode = false;
                    isSubmit();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void isSubmit() {

        if (notEmptyMobile && notEmptyPasswd && notEmptyCode) {
            btSubImp.setEnabled(true);
        } else {
            btSubImp.setEnabled(false);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sub_imp:
                if (!ClickUtil.isFastClick()) {
                    submit();
                }
                break;

            case R.id.bt_time_imp:
                if (!ClickUtil.isFastClick()) {
                    sendCode();
                }
                break;
        }
    }

    private void sendCode() {

        if (ValidateUtil.isMobile(mobile)) {
            isSendCode = true;

            HashMap<String, String> dictParam = new HashMap<String, String>();
            dictParam.put("phone", mobile);
            dictParam.put("type", Constant.type);
            RequestParams codeParams = new RequestParams();
            codeParams.put("phone", mobile);
            codeParams.put("type", Constant.type);
            netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.FORGETCODE, codeParams, dictParam, new JsonHttpResponseHandler() {
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
                        if("40013".equals(status)){
                            //activity跳转
                            Intent intent = new Intent(FargetPwActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if ("1001".equals(status)) {
                            initTimer();
                        } else {
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
        } else {
            CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.mobile_error));
        }
    }

    private void submit() {
        //提交数据给服务器
        if (isSendCode) {

            if (ValidateUtil.isPasswd(passwd)) {
                submitReciver();
            } else {
                CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.passwd_error));
            }

        } else {

            CommonDialog.showInfoDialog(context, "请先获取验证码");
        }
    }

    private void submitReciver() {
        showProgressDialog();
        String pwd1 = NetUtils.Md5(passwd);
        String pwd2 = NetUtils.Md5(pwd1);
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("phone", mobile);
        dictParam.put("passwd", pwd2);
        dictParam.put("code", mCode);
        RequestParams registerParams = new RequestParams();
        registerParams.add("phone", mobile);
        registerParams.add("passwd", pwd2);
        registerParams.add("code", mCode);
        //registerParams.add("type", Constant.type);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.FORGETREPLACEPAW, registerParams, dictParam, new JsonHttpResponseHandler() {
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
                    switch (status) {
                        case "40013":
                            //activity跳转
                            Intent intent = new Intent(FargetPwActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "1001":
                            closeProgressDialog();
                            showProgressDialog("修改成功");
                            SharedPreferenceUtil.saveString(context, Constant.MOBLIE, mobile);
                            startActivity(new Intent(context, LoginActivity.class));
                            break;
                        //TODO  操作失败弹框
                        case "1002":
                            CommonDialog.showInfoDialog(context, "验证码错误");
                            break;
                        default:
                            closeProgressDialog();
                            CommonDialog.showInfoDialog(context, message);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                closeProgressDialog();
                CommonDialog.showInfoDialog(context, "连接失败");
            }
        });
    }

    @Override
    protected void goActivity(Activity activity, Intent intent) {
        super.goActivity(activity, intent);
    }
}
