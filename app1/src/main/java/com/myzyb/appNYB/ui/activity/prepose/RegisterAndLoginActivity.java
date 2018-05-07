package com.myzyb.appNYB.ui.activity.prepose;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.activity.regiser.RegisterActivity;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.FontsUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.reactivex.functions.Consumer;

/**
 * Created by cuiz on 2015/11/16.
 */
public class RegisterAndLoginActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.bt_log)
    protected Button bt_log;
    @Bind(R.id.bt_reg)
    protected Button bt_reg;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_xh1)
    TextView tvXh1;
    @Bind(R.id.tv_xh2)
    TextView tvXh2;
    private Intent intent;
    private Context context;
    private long exitTime = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            tvNum.setText(prize);
            tvXh1.setText("6-DZM-12  " + prize1);
            tvXh2.setText("6-DZM-20  " + prize2);

        }
    };
    private String prize;
    private String prize1;
    private String prize2;

    protected static final String tag = "tfl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerandlogin);
        ButterKnife.bind(this);
        CommApplication.getInstance().addActvity(this);
        context = this;
        initTitleBar();
        setTitleBar_left_bn2(R.drawable.logo);
        setTitleBar_titletext("能源宝");
        requestPermissions();
        initData();
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(RegisterAndLoginActivity.this);
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(tag, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(tag, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(tag, permission.name + " is denied.");
                        }
                    }
                });
    }

    private void initData() {
        HashMap<String, String> dictParam = new HashMap<String, String>();
        RequestParams params = new RequestParams();
//        String area_id = SharedPreferenceUtil.getString(context, Constant.DQID, "");
//        dictParam.put("area_id", area_id);
//        params.put("area_id", area_id);
        //字体
        FontsUtil.FontsUtil(tvNum, context);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.RECOVERYPRICE, params, dictParam, new JsonHttpResponseHandler() {

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
                    JSONObject list = response.getJSONObject("list");
                    prize = list.getString("prize");
                    prize1 = list.getString("prize1");
                    prize2 = list.getString("prize2");
                    mHandler.sendEmptyMessage(1);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                LogUtil.e("responseString", responseString);
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                CommApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reg:
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;


            case R.id.bt_log:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;

        }
    }


}
