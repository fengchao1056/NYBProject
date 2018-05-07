package com.myzyb.appNYB.ui.activity.pay;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.myzyb.appNYB.ui.view.PasswordDialog;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 提现
 * Created by cuiz on 2016/1/29.
 */
public class WithdrawCashActivity extends BaseActivity {

    @Bind(R.id.imb_back)
    ImageButton imbBack;
    @Bind(R.id.remain_money)
    TextView remainMoney;
    @Bind(R.id.card_type)
    TextView cardType;
    @Bind(R.id.et_price)
    EditText etMoney;
    @Bind(R.id.bt_next)
    Button btNext;
    private String bc_id;
    private String money;
    private String imput_money;
    private String bank_zd;
    private PasswordDialog passwordDialog;
    private String stpasswd;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawcash);
        context = this;
        ButterKnife.bind(this);

        initView();

        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        bank_zd = SharedPreferenceUtil.getString(context, Constant.Bank_zd, "");
        if (money != null && bank_zd != null) {
            remainMoney.setText(money);
            cardType.setText(bank_zd);

//            if (Double.parseDouble(money)){
//
//            }
        }
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imput_money = s.toString();
                if (!TextUtils.isEmpty(imput_money)) {
                    if (Double.parseDouble(imput_money) > Double.parseDouble(money)) {
                        btNext.setEnabled(false);
                    } else {
                        btNext.setEnabled(true);
                    }
                } else {
                    btNext.setEnabled(false);
                }

            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                passwordDialog = new PasswordDialog(context);
                passwordDialog.builder()

                        .setTitle("请输入支付密码")
                        .setMsg("提现金额为： ¥" + imput_money)
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
                                    sendData();
                                    CommonDialog.showProgressDialog(context);
                                } else {
                                    CommonUtil.StartToast(context, "请输入6位支付密码");
                                }
                            }
                        }).show();


            }
        });


    }
//    参数名	必须	类型	说明
//    gid	必须	int	网点id,或物流的gid
//    uid	必须	int	用户id
//    money	必须		钱
//    pay_no	必须		订单号
//    type	必须		1充值,2提现

    private void sendData() {

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String timeStr = dateFormat.format(new Date());
        String orderStr = timeStr + "0000";
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");

        String stpasswd1 = NetUtils.Md5(stpasswd);
        String stpasswd2 = NetUtils.Md5(stpasswd1);

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("gid", gid);
        dictParam.put("uid", uid);
        dictParam.put("type", Constant.gettype);
        dictParam.put("money", imput_money);
        dictParam.put("pay_no", orderStr);
        dictParam.put("stpasswd", stpasswd2);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("gid", gid);
        params.add("uid", uid);
        params.add("type", Constant.gettype);
        params.add("money", imput_money);
        params.add("pay_no", orderStr);
        params.add("stpasswd", stpasswd2);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.pay_and_get, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("pay", response.toString());
                try {
                    CommonDialog.closeProgressDialog();
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    String status = response.getString("status");
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(WithdrawCashActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(status)) {
                        CommonUtil.StartToast(context, response.getString("message"));
                        Intent intent = new Intent(WithdrawCashActivity.this, FinishCashActivity.class);
                        intent.putExtra("bank_zd", bank_zd);
                        intent.putExtra("imput_money", imput_money);
                        startActivity(intent);
                    } else if ("1017".equals(status)) {
                        CommonUtil.StartToast(context, "余额不足");
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
                CommonUtil.StartToast(context, "网络异常");
            }
        });


    }


}
