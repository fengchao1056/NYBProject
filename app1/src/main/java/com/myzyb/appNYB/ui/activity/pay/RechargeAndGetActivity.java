package com.myzyb.appNYB.ui.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.my.SpecificationActivity;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 充值提现
 */

public class RechargeAndGetActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {

    @Bind(R.id.imb_back)
    ImageButton imbBack;
    @Bind(R.id.bt_mxlist)
    Button btMxlist;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.bt_add)
    Button btAdd;
    @Bind(R.id.bt_get)
    Button btGet;
    @Bind(R.id.sl_view)
    PullToRefreshScrollView slView;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_and_get);
        ButterKnife.bind(this);
        initTitle();
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
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.balance_lc, params, dictParam, new JsonHttpResponseHandler() {

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
                        Intent intent = new Intent(RechargeAndGetActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        money = response.getString("money");
                        tvMoney.setText(money);
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

    private void initTitle() {
        imbBack.setOnClickListener(this);
        btMxlist.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        btGet.setOnClickListener(this);
        slView.setOnRefreshListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_back:
                finish();
                break;
            case R.id.bt_mxlist:
                startActivity(new Intent(RechargeAndGetActivity.this, SpecificationActivity.class));
                break;
            case R.id.bt_add:

                startActivity(new Intent(RechargeAndGetActivity.this, PayPriceActivity.class));
                break;
            case R.id.bt_get:
                //TODO 提现

                String bank_card = SharedPreferenceUtil.getString(context, Constant.Bank_card, "");
                if (bank_card != null && bank_card.length() > 0) {
                    Intent intent = new Intent(RechargeAndGetActivity.this, WithdrawCashActivity.class);
                    intent.putExtra("money", money);
                    startActivity(new Intent(intent));
                } else {
                    CommonUtil.StartToast(context, "请先绑定银行卡");
                }
                break;
            default:
                break;

        }


    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                slView.onRefreshComplete();
            }
        }, 2000);
    }
}
