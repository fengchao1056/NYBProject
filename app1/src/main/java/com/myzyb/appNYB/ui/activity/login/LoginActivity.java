package com.myzyb.appNYB.ui.activity.login;


import android.content.Context;
import android.content.Intent;
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
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.activity.regiser.AuditInformationActivity;
import com.myzyb.appNYB.ui.activity.regiser.RegisterMesActivity;
import com.myzyb.appNYB.util.CommonDialog;
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

/**
 * 登录页面
 * cuiz
 */
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
    private String wd_id;//网点ID
    private String shzt;
    private String token;
    private String loginSalt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommApplication.getInstance().addActvity(this);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_left_bn(R.drawable.back_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBar_titletext("登录能源宝");
        initMobile();
        initDate();
    }

    private void initMobile() {
        mobile = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "");
        if (!TextUtils.isEmpty(mobile)) {
            etPhoneLog.setText(mobile);
            phoneEmpty = true;
        }
    }

    private void initDate() {
//登录页显示版本名称
        String versionName = CommonUtil.getVersionName(context);
        if (versionName != null && !TextUtils.isEmpty(versionName)) {
            tvVersion.setText("版本：V" + versionName);
        }
//监听每个输入框是否录入信息，任何一个未录入，置灰登录按钮
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
                //是否符合手机号格式
                if (ValidateUtil.isMobile(mobile)) {
                    //是否符合密码格式
                    if (ValidateUtil.isPasswd(passwd)) {
                        CommonDialog.showProgressDialog(context);
                        upload();
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
                startActivity(new Intent(this, FargetPwActivity.class));
                break;
            default:
                break;
        }

    }

    private void upload() {
        //提交网络请求
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
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.LOGINURL, params, dictParam, new JsonHttpResponseHandler() {
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
                    LogUtil.e("登录页面登录接口", response.toString());
                    switch (status) {
                        case "1006":
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
                            SharedPreferenceUtil.saveString(context, Constant.MOBLIE, mobile);
                            SharedPreferenceUtil.saveString(context, Constant.YHID, uid);
                            SharedPreferenceUtil.saveString(context, Constant.TOKEN, token);
                            SharedPreferenceUtil.saveString(context, Constant.KEY, key);
                            SharedPreferenceUtil.saveString(context, Constant.IV, iv);
                            SharedPreferenceUtil.saveString(context, Constant.LoginSalt, loginSalt);
                            swichActivity();
                            break;
                        case "1009":
                            CommonDialog.closeProgressDialog();
                            //TODO 账号锁定逻辑
                            CommonDialog.showInfoDialog(context, "密码错误次数超过5次，账号被锁定");
                        default:
                            CommonDialog.showInfoDialog(context, response.getString("message"));
                            break;
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

    //根据服务端返回值，判断跳转到哪个页面
    private void swichActivity() {
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
                            switch (status) {
                                case "1001":
                                    JSONObject list = response.getJSONObject("list");
                                    LogUtil.e("list", list.toString());
                                    dq_id = list.getString("area_id");
                                    wd_id = list.getString("id");
                                    LogUtil.e("dq_id", dq_id);
                                    LogUtil.e("wd_id", wd_id);
                                    SharedPreferenceUtil.saveString(context, Constant.DQID, dq_id);
                                    SharedPreferenceUtil.saveString(context, Constant.WDID, wd_id);
                                    //审核状态
                                    shzt = list.getString("examine");
                                    switch (shzt) {
                                        case "1":
                                            startActivityForResult(new Intent(context, MainActivity.class), Constant.NOTSHEHE);
                                            finish();
                                            break;
                                        default:
                                            startActivity(new Intent(context, AuditInformationActivity.class));
                                            finish();
                                            break;
                                    }
                                    break;
                                case "1013"://网点为空跳转到完善信息页面
                                    startActivityForResult(new Intent(context, RegisterMesActivity.class), Constant.WAITADDMES);
                                    finish();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

        );

    }

}