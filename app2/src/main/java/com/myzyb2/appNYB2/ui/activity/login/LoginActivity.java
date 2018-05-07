package com.myzyb2.appNYB2.ui.activity.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.CommApplication;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.activity.prepose.SplashActivity;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.ValidateUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.reactivex.functions.Consumer;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_phone_log)
    EditText etPhoneLog;
    @Bind(R.id.et_pass_log)
    EditText etPassLog;
    @Bind(R.id.bt_login_log)
    protected Button bt_login_log;
    @Bind(R.id.tv_forget_pw_login)
    protected TextView tv_forget_pw_login;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    private boolean phoneEmpty = false;
    private boolean pwEmpty = false;
    private Context context;
    private String passwd;
    private String mobile;
    private String uid;
    private String dq_id;//大区ID
    private String WL_ID;//网点ID
    private String token;
    private String loginSalt;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    startActivity(new Intent(context, WelcomeActivity.class));
                    //startActivity(new Intent(context, MainActivity.class));
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommApplication.getInstance().addActvity(this);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_titletext("登录能源宝");
        initMobile();
        initDate();
    }

    private void initMobile() {
        String versionName = CommonUtil.getVersionName(context);
        if (versionName != null && !TextUtils.isEmpty(versionName)) {
            tvVersion.setText("版本：V" + versionName);
        }
        mobile = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "");
        if (!TextUtils.isEmpty(mobile)) {
            etPhoneLog.setText(mobile);
            phoneEmpty = true;
        }
    }

    private void initDate() {
        etPhoneLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobile = s.toString();

                if (!TextUtils.isEmpty(mobile)) {
                    phoneEmpty = true;

                } else {
                    phoneEmpty = false;
                }

                if (pwEmpty && phoneEmpty) {

                    bt_login_log.setEnabled(true);
                } else {
                    bt_login_log.setEnabled(false);
                }
            }
        });
        etPassLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwd = s.toString();

                if (!TextUtils.isEmpty(passwd) && passwd.length() > 5) {
                    pwEmpty = true;

                } else {
                    pwEmpty = false;
                }

                if (pwEmpty && phoneEmpty) {

                    bt_login_log.setEnabled(true);
                } else {
                    bt_login_log.setEnabled(false);
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_log:
                //手机号格式
                if (ValidateUtil.isMobile(mobile)) {
                    //密码格式
                    if (ValidateUtil.isPasswd(passwd)) {
                        upload();
                        CommonDialog.showProgressDialog(context);
                    } else {
                        CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.passwd_error));
                        break;
                    }
                } else {
                    CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.mobile_error));
                    break;
                }

                break;

            case R.id.tv_forget_pw_login:
                Intent intent = new Intent(this, FargetPwActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void upload() {
        //修改后
        String pwd1 = NetUtils.Md5(passwd);
        String pwd2 = NetUtils.Md5(pwd1);
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("phone", mobile);
        dictParam.put("passwd", pwd2);
        dictParam.put("type", Constant.type);
        RequestParams params = new RequestParams();
        params.put("phone", mobile);
        params.put("passwd", pwd2);
        params.put("type", Constant.type);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.LOGINURL, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.has("data")){
                        response = response.getJSONObject("data");
                    }else{
                        response = response.getJSONObject("result");
                    }
                    String status = response.getString("status");
//                    JSONObject json = response.getJSONObject("list");
                    LogUtil.e("登录页面登录接口", response.toString());
                    switch (status) {
                        case "1006":
                            //TODO  用户不存在
                            CommonDialog.closeProgressDialog();
                            CommonDialog.showInfoDialog(context, "用户不存在");
                            break;
                        case "1002":
                            CommonDialog.closeProgressDialog();
                            CommonDialog.showInfoDialog(context, "用户不存在");
                            break;
                        case "1008":
                            CommonDialog.closeProgressDialog();
                            CommonDialog.showInfoDialog(context, "密码错误");
                            break;
                        case "1001":
                            JSONObject json = response.getJSONObject("list");
                            token = json.getString("token");
                            loginSalt = json.getString("login_salt");
                            String key = json.getString("key");
                            String iv = json.getString("iv");
                            uid = json.getString("uid");
                            LogUtil.e("登录", uid);
                            //物流Uid，手机号
                            SharedPreferenceUtil.saveString(context, Constant.MOBLIE, mobile);
                            SharedPreferenceUtil.saveString(context, Constant.YHID, uid);
                            SharedPreferenceUtil.saveString(context, Constant.TOKEN, token);
                            SharedPreferenceUtil.saveString(context, Constant.KEY, key);
                            SharedPreferenceUtil.saveString(context, Constant.IV, iv);
                            SharedPreferenceUtil.saveString(context, Constant.LoginSalt, loginSalt);
                            getUid();
                            break;
                        case "1009":
                            CommonDialog.closeProgressDialog();
                            //TODO 账号锁定逻辑
                            CommonDialog.showInfoDialog(context, "密码错误次数超过5次，账号被锁定");
                        default:
                            closeProgressDialog();
                            CommonUtil.StartToast(context, response.getString("message"));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                CommonDialog.closeProgressDialog();
                CommonDialog.showInfoDialogFailure(context);
            }
        });
    }

    private void getUid() {
        //修改后
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("mobile", mobile);
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("mobile", mobile);
        params.add("uid", uid);
        params.add("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.SELLERURL, params, dictParam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        LogUtil.e("response", response.toString());
                        try {
                            CommonDialog.closeProgressDialog();
                            if(response.has("data")){
                                String s = response.getString("data");
                                response = AES.desEncrypt(s);
                            }else{
                                response = response.getJSONObject("result");
                            }
                            String status = response.getString("status");
                            JSONObject data = response.getJSONObject("list");
                            switch (status) {
                                case "1001":
                                    dq_id = data.getString("area_id");
                                    //物流gid
                                    WL_ID = data.getString("id");
                                    LogUtil.e("dq_id", dq_id);
                                    LogUtil.e("WL_ID", WL_ID);
                                    //物流大区ID，gid
                                    SharedPreferenceUtil.saveString(context, Constant.DQID, dq_id);
                                    SharedPreferenceUtil.saveString(context, Constant.WL_ID, WL_ID);
                                    mHandler.sendEmptyMessage(1);
                                    break;
                                default:
                                    CommonDialog.closeProgressDialog();
                                    CommonDialog.showInfoDialogFailure(context);
                                    break;
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }

        );


    }

}