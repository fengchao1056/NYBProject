package com.myzyb.appNYB.ui.activity.regiser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.activity.prepose.RegisterAndLoginActivity;
import com.myzyb.appNYB.util.ClickUtil;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.IonUtils;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 信息审核界面
 */
public class AuditInformationActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {

    @Bind(R.id.tv_wdmc)
    TextView tvWdmc;
    @Bind(R.id.tv_dw)
    TextView tvWdlx;
    @Bind(R.id.tv_lxr)
    TextView tvLxr;
    @Bind(R.id.tv_dizhi)
    TextView tvDizhi;
    @Bind(R.id.iv_yyzz)
    ImageView ivYyzz;
    @Bind(R.id.tv_tjr)
    TextView tvTjr;
    @Bind(R.id.bt_skip)
    Button bt_skip;
    @Bind(R.id.tv_shzt)
    TextView tvShzt;
    @Bind(R.id.sl_info)
    PullToRefreshScrollView slInfo;
    private Context context;
    private String dq_id;//大区ID
    private String wd_id;//网点ID
    private String wd_mc;//网点名称
    private String wd_lx;//网点类型
    private String lxr;//联系人
    private String yyzz;//营业执照URL
    private String tjr_id;//推荐人ID
    private String shzt;//审核状态
    private String shsbyy;//审核失败原因
    private StringBuffer dizhi;//地址全称
    private StringBuffer ssx;
    private String xxdz;
    private String county;
    private String city;
    private String province;
    private String area_x;
    private String area_y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_information);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_titletext("审核信息");
        initUI();
        initData();
    }

    private void initUI() {
        slInfo.setOnRefreshListener(this);
    }

    private void initData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String mobile = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "-1");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        LogUtil.e("uid", uid);
        LogUtil.e("mobile", mobile);
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
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(AuditInformationActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        LogUtil.e("response", response.toString());
                        JSONObject list = response.getJSONObject("list");
                        LogUtil.e("list", list.toString());
                        dq_id = list.getString("area_id");
                        wd_id = list.getString("id");
                        wd_mc = list.getString("gname");
                        wd_lx = list.getString("main_items");
                        lxr = list.getString("uname");
                        xxdz = list.getString("address");
                        //yyzz = list.getString("license");
                        yyzz = list.getString("license");
                        tjr_id = list.getString("share_id");
                        shzt = list.getString("examine");
                        shsbyy = list.getString("remark");
                        province = list.getString("pros");
                        city = list.getString("citys");
                        county = list.getString("towns");
                        area_x = list.getString("area_x");
                        area_y = list.getString("area_y");
                        ssx = new StringBuffer().append(list.getString("pros")).append(list.getString("citys")).append(list.getString("towns"));
                        dizhi = new StringBuffer().append(ssx).append("\r\n").append(xxdz);
                        SharedPreferenceUtil.saveString(context, Constant.DQID, dq_id);
                        SharedPreferenceUtil.saveString(context, Constant.WDID, wd_id);
                        setUIData();
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUIData() {
        tvWdmc.setText(wd_mc);
        tvWdlx.setText(wd_lx);
        tvLxr.setText(lxr);
        tvDizhi.setText(dizhi);
        IonUtils.loadImage(context, yyzz, ivYyzz);
        if (!TextUtils.isEmpty(tjr_id) && !"0".equals(tjr_id)) {
            tvTjr.setText(tjr_id);
        }
        switch (shzt) {
            case "0":
                tvShzt.setText("待审核...");
                break;
            case "2":
                tvShzt.setText("审核失败:" + shsbyy);
                bt_skip.setVisibility(View.VISIBLE);
                bt_skip.setEnabled(true);
                showflDialog();
                break;
            case "1":
                goMain();
                break;
            default:
                break;
        }
    }

    private void showflDialog() {

        CommonDialog.showInfoDialog(context, shsbyy, "提示", "取消", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_pos:
                        goReplaceActivity();
                        break;
                }
            }
        });
    }

    private void goReplaceActivity() {
        Intent intent = new Intent(context, ReplceMesActivity.class);
        intent.putExtra("wd_mc", wd_mc);
        intent.putExtra("wd_lx", wd_lx);
        intent.putExtra("lxr", lxr);
        intent.putExtra("ssx", ssx.toString());
        intent.putExtra("xxdz", xxdz);
        intent.putExtra("yyzz", yyzz);
        intent.putExtra("tjr_id", tjr_id);
        intent.putExtra("province", province);
        intent.putExtra("city", city);
        intent.putExtra("county", county);
        startActivity(intent);
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constant.FRISTSHENHE, "恭喜审核通过！");
        startActivityForResult(intent, Constant.SHENHETG);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CommonDialog.showInfoDialog(context, "确认要登出吗？", "提示", "取消", "确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_pos:
                            startActivity(new Intent(context, RegisterAndLoginActivity.class));
                            finish();
                            break;
                    }
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_skip:
                if (!ClickUtil.isFastClick()) {
                    goReplaceActivity();
                }
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                slInfo.onRefreshComplete();
            }
        }, 2000);
    }
}
