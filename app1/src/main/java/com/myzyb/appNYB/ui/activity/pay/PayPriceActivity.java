package com.myzyb.appNYB.ui.activity.pay;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.allinpay.appayassistex.APPayAssistEx;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.common.PaaCreator;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
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
 * 输入充值金额
 * Created by cuiz on 2016/1/26.
 */
public class PayPriceActivity extends BaseActivity {


    @Bind(R.id.imb_back)
    ImageButton imbBack;
    @Bind(R.id.et_name)
    EditText etMoney;
    @Bind(R.id.bt_next)
    Button btNext;
    private Context context;
    private String timeStr;
    private String pay_count;
    private String cardnumber;
    private String bc_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_payprice);
        ButterKnife.bind(this);
        initTitle();
        initUI();
        getData();

    }

    private void getData() {

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("gid", gid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("gid", gid);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.bank_number, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("pay", response.toString());
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(PayPriceActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        bc_ids = response.getJSONObject("list").getString("bc_ids");
                        LogUtil.e("bc_ids", bc_ids + "ddfsdf");
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

    private void initUI() {

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etMoney.getText().toString();
                LogUtil.e("s1", s);
                if (s != null) {

                    SendPayToserve(s);
                }
            }
        });
    }

    private void SendPayToserve(String s) {
        pay_count = String.valueOf((int) (Double.parseDouble(s) * 100));
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");

        /**
         * 通联参数
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        timeStr = dateFormat.format(new Date());
        String orderStr = timeStr + "0000";
        cardnumber = bc_ids;
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("gid", gid);
        dictParam.put("uid", uid);
        dictParam.put("type", Constant.paytype);
        dictParam.put("money", s);
        dictParam.put("pay_no", orderStr);
        dictParam.put("access_token", NetUtils.getEncode(token));

        RequestParams params = new RequestParams();
        params.add("gid", gid);
        params.add("uid", uid);
        params.add("type", Constant.paytype);
        params.add("money", s);
        params.add("pay_no", orderStr);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.pay_and_get, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("payzz", response.toString());
                try {
                    if (statusCode == 200) {
                        if(response.has("data")){
                            String s = response.getString("data");
                            response = AES.desEncrypt(s);
                        }else{
                            response = response.getJSONObject("result");
                        }
                        if("40013".equals(response.getString("status"))){
                            //activity跳转
                            Intent intent = new Intent(PayPriceActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else if ("1001".equals(response.getString("status"))) {
                            JSONObject payData = PaaCreator.randomPaa(pay_count, timeStr, cardnumber);
                            APPayAssistEx.startPay(PayPriceActivity.this, payData.toString(), APPayAssistEx.MODE_PRODUCT);
                        } else {
                            CommonUtil.StartToast(context, response.getString("message"));
                        }
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

//                    gid	必须	int	网点id,或物流的gid
//                    uid	必须	int	用户id
//                    money	必须		钱
//                    pay_no	必须		订单号
//                    type	必须		1充值,2提现


    }

    private void initTitle() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (!TextUtils.isEmpty(s.toString())) {
                    btNext.setEnabled(true);
                    if (s1.contains(".")) {
                        int i = s1.indexOf(".");
                        if (s.length() - i - 1 > 2) {
                            //CommonUtil.startToast(context, "最多只能输入两位小数");
                            etMoney.setText(s.delete(i + 3, i + 4));
                            etMoney.setSelection(i + 3);
                            //btNext.setEnabled(false);
                        } else {
                            btNext.setEnabled(true);
                        }

                    }
                    if (s1.indexOf(".") == 0) {

                        btNext.setEnabled(false);
                    }

                } else {
                    btNext.setEnabled(false);
                }


            }
        });
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (APPayAssistEx.REQUESTCODE == requestCode) {
            if (null != data) {
                String payRes = null;
                String payAmount = null;
                String payTime = null;
                String payOrderId = null;
                try {
                    JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
                    payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
                    payAmount = resultJson.getString("payAmount");
                    payTime = resultJson.getString("payTime");
                    payOrderId = resultJson.getString("payOrderId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
                    //showAppayRes("支付成功！");
                } else {
                    showAppayRes("支付失败！");
                }
                Log.d("payResult", "payRes: " + payRes + "  payAmount: " + payAmount + "  payTime: " + payTime + "  payOrderId: " + payOrderId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showAppayRes(String res) {
        new AlertDialog.Builder(this)
                .setMessage(res)
                .setPositiveButton("确定", null)
                .show();
    }

}
