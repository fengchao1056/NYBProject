package com.myzyb2.appNYB2.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.myzyb2.appNYB2.ui.activity.login.FargetPwActivity;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
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

/**
 * 个人中心——修改密码
 */

public class ReplacePwActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_passwd)
    EditText etPasswd;
    @Bind(R.id.et_sure_pw)
    EditText etSurePw;
    @Bind(R.id.et_still_sure_pw)
    EditText etStillSurePw;
    @Bind(R.id.bt_sub_imp)
    Button btSubImp;
    private String passwd;
    private String sure_passwd;
    private String still_passwd;
    private boolean notEmpty_pw = false;
    private boolean notEmpty_surepw = false;
    private boolean notEmpty_stillpw = false;
    private Context context;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    closeProgressDialog();
                    finish();
                    break;


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_pw);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("修改密码");
        onTextChange();

    }


    private void onTextChange() {

        etPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwd = s.toString();
                if (!TextUtils.isEmpty(s.toString()) && passwd.length() > 5) {
                    notEmpty_pw = true;

                } else {
                    notEmpty_pw = false;
                }
                isSubmit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSurePw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sure_passwd = s.toString();
                if (!TextUtils.isEmpty(s.toString()) && sure_passwd.length() > 5) {
                    notEmpty_surepw = true;
                } else {
                    notEmpty_surepw = false;
                }
                isSubmit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etStillSurePw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                still_passwd = s.toString();
                if (!TextUtils.isEmpty(s.toString()) && still_passwd.length() > 5) {
                    notEmpty_stillpw = true;
                } else {
                    notEmpty_stillpw = false;
                }
                isSubmit();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void isSubmit() {

        if (notEmpty_pw && notEmpty_surepw && notEmpty_stillpw) {
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
            case R.id.tv_forget_pw_login:
                startActivity(new Intent(context, FargetPwActivity.class));

        }

    }

    private void submit() {

        if (ValidateUtil.isPasswd(passwd) && ValidateUtil.isPasswd(sure_passwd) && ValidateUtil.isPasswd(still_passwd)) {

            if (sure_passwd.equals(still_passwd)) {
                showProgressDialog("修改中");
                sendPWToRecevice();

            } else {

                CommonDialog.showInfoDialog(context, "新密码与确认密码不一致");
            }

        } else {

            CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.passwd_error));

        }

    }

    private void sendPWToRecevice() {

        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");

        String passwd1 = NetUtils.Md5(passwd);
        String passwd2 = NetUtils.Md5(passwd1);

        String sure_passwd1 = NetUtils.Md5(sure_passwd);
        String sure_passwd2 = NetUtils.Md5(sure_passwd1);

        String still_passwd1 = NetUtils.Md5(still_passwd);
        String still_passwd2 = NetUtils.Md5(still_passwd1);

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("oldpasswd", passwd2);
        dictParam.put("newpasswd", sure_passwd2);
        dictParam.put("repasswd", still_passwd2);
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("oldpasswd", passwd2);
        params.add("newpasswd", sure_passwd2);
        params.add("repasswd", still_passwd2);


        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.REPLACEPAW, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                closeProgressDialog();
                LogUtil.e("pw", response.toString());
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(ReplacePwActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if ("1001".equals(response.getString("status"))) {
                        showProgressDialog("修改成功");
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.StartToast(context, "原密码错误");

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }


        });


    }
}