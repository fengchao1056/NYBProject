package com.myzyb.appNYB.ui.activity.regiser;

import android.content.Context;
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
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.ValidateUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * 注册信息
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_phone_reg;
    private EditText et_pass_reg;
    private Button bt_reg_reg;
    private boolean phoneEmpty = false;
    private boolean pwEmpty = false;
    private String passwd;
    private String mobile;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("加入能源宝");
        initView();
        initButten();
    }

    private void initButten() {
        et_phone_reg.addTextChangedListener(new TextWatcher() {
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

                    bt_reg_reg.setEnabled(true);
                } else {
                    bt_reg_reg.setEnabled(false);
                }
            }
        });
        et_pass_reg.addTextChangedListener(new TextWatcher() {
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

                // LogUtil.e("text", pwEmpty + "");

                if (pwEmpty && phoneEmpty) {

                    bt_reg_reg.setEnabled(true);
                } else {
                    bt_reg_reg.setEnabled(false);
                }
            }

        });


    }

    private void initView() {

        et_phone_reg = (EditText) findViewById(R.id.et_phone_reg);
        et_pass_reg = (EditText) findViewById(R.id.et_pass_reg);
        bt_reg_reg = (Button) findViewById(R.id.bt_reg_reg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reg_reg:

                if (ValidateUtil.isMobile(mobile)) {
                    if (ValidateUtil.isPasswd(passwd)) {
                        sendToRecivice();
                        showProgressDialog("注册中");
                    } else {

                        CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.passwd_error));
                        break;

                    }
                } else {

                    CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.mobile_error));
                }


        }


    }

    private void sendToRecivice() {
        LogUtil.e("mobile", mobile);
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("mobile", mobile);
        RequestParams params = new RequestParams();
        params.add("mobile", mobile);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.GETCODE, params, dictParam, new JsonHttpResponseHandler() {
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
                    LogUtil.e("服务器状态码", response.getString("status"));
                    String message = response.getString("message");
                    String status = response.getString("status");
                    switch (status) {
                        case "40013":
                            //activity跳转
                            Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(login);
                            break;
                        case "1003":
                            closeProgressDialog();
                            CommonUtil.StartToast(context, "该手机号码已经注册");
                            break;
                        case "1001":
                            closeProgressDialog();
                            Intent intent = new Intent(context, ImportCodeActivity.class);
                            intent.putExtra("mobile", mobile);
                            intent.putExtra("passwd", passwd);
                            startActivityForResult(intent, 1);
                            break;
                        default:
                            CommonUtil.StartToast(context, message);
                            break;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtil.toastOnFailure();

            }
        });


    }


}
