package com.myzyb.appNYB.ui.activity.my;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.regiser.WidgetBaseActivity;
import com.myzyb.appNYB.ui.map.LocationDemo;
import com.myzyb.appNYB.util.ClickUtil;
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
 * 修改地址
 * cuiz
 */

public class ReplaceAdressActivity extends WidgetBaseActivity implements View.OnClickListener {
    @Bind(R.id.rl_baidumapdw)
    RelativeLayout rlBaidumapdw;
    @Bind(R.id.tv_type_choose)
    TextView tvTypeChoose;
    @Bind(R.id.tv_xxdz)
    TextView tvXxdz;
    @Bind(R.id.ed_xxdz)
    EditText edXxdz;
    @Bind(R.id.bt_get_rech)
    Button btGetRech;

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
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private String height;
    private String coordinate;
    private String name;
    private boolean isHasMap = false;
    private boolean isHasAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_adress);
        ButterKnife.bind(this);
        initTitleBar();
        setTitleBar_left_bn(R.drawable.back_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBar_titletext("修改地址");
        application.addActvity(this);
        rlBaidumapdw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReplaceAdressActivity.this, LocationDemo.class), 2);
            }
        });
        edXxdz.addTextChangedListener(new MyTextWatchar());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mProvince = data.getStringExtra("mProvince");
            if (mProvince.contains("市")) {
                mProvince = mProvince.replace("市", "");

            }
            if (mProvince.contains("省")) {
                mProvince = mProvince.replace("省", "");

            }
            mCity = data.getStringExtra("mCity");
            mDistrict = data.getStringExtra("mDistrict");
            height = String.valueOf(data.getDoubleExtra("x-height",0.1));
            coordinate = String.valueOf(data.getDoubleExtra("Y-coordinate",0.1));
            name = data.getStringExtra("name");
            tvTypeChoose.setText(mProvince + mCity + mDistrict + name);
            isHasMap = true;
            isClicable();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_rech:
                if (!ClickUtil.isFastClick()) {
                    showProgressDialog("正在修改");
                    sentToRecevice();
                }
                break;
            default:
                break;
        }
    }

    private void isClicable() {

        if (isHasMap && isHasAdress) {
            btGetRech.setEnabled(true);
        } else {
            btGetRech.setEnabled(false);
        }
    }

    private void sentToRecevice() {
        String xxadress_text = edXxdz.getText().toString();
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("gid", gid);
        dictParam.put("province", NetUtils.getEncode(mProvince));
        dictParam.put("city", NetUtils.getEncode(mCity));
        dictParam.put("county", NetUtils.getEncode(mDistrict));
        dictParam.put("area_x", NetUtils.getEncode(height));
        dictParam.put("area_y", NetUtils.getEncode(coordinate));
        dictParam.put("address", NetUtils.getEncode(xxadress_text));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("gid", gid);
        params.add("province", mProvince);
        params.add("city", mCity);
        params.add("county", mDistrict);
        params.add("area_x", height);
        params.add("area_y", coordinate);
        params.add("address", xxadress_text);
        params.add("access_token", token);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.Replace_Adress, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    LogUtil.e(context, response.toString());
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(ReplaceAdressActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        showProgressDialog("修改成功");

                        mHandler.sendEmptyMessageDelayed(1, 1000);

                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                    closeProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }


    private class MyTextWatchar implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!TextUtils.isEmpty(s.toString())) {

                isHasAdress = true;

            } else {
                isHasAdress = false;
            }
            isClicable();
        }
    }
}
