package com.myzyb2.appNYB2.ui.activity.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.Config;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.ElectrombileShop.carShop;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.adapter.MyFragmentPagerAdapter;
import com.myzyb2.appNYB2.ui.fragment.AreadyFragment;
import com.myzyb2.appNYB2.ui.fragment.WaitGetFragment;
import com.myzyb2.appNYB2.ui.fragment.WaitPayFragment;
import com.myzyb2.appNYB2.ui.view.CircleImageView;
import com.myzyb2.appNYB2.ui.view.MyViewPager;
import com.myzyb2.appNYB2.ui.view.PagerSlidingTabStrip;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * Created by cuiz on 2016/1/15.
 */
public class RecoverListActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.iv_headphoto)
    CircleImageView ivHeadphoto;
    @Bind(R.id.tv_gname)
    TextView tvGname;
    @Bind(R.id.tv_adress)
    TextView tvAdress;
    @Bind(R.id.tv_uname)
    TextView tvUname;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    private PagerSlidingTabStrip tabs;
    private MyViewPager pager;
    private static int state = 0;
    private ArrayList<Fragment> pages = new ArrayList<>();
    private Context context;
    public TextView txt_title;
    public ImageButton imgbtn_left;
    private WaitGetFragment waitGetFragment;
    private WaitPayFragment waitPayFragment;
    private AreadyFragment areadyFragment;
    private MyFragmentPagerAdapter fp;
    public carShop carShop;
    public String uid;
    public String id;
    public String address;
    public String gname;
    public String phone;
    public String uname;
    public String img_url;
    public String area_id;
    private boolean callback = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_recoverlist);
        ButterKnife.bind(this);
        setTitle();
        ReplaceFragment(state);
        sendData();
    }

    private void sendData() {
        Intent intent = getIntent();
        carShop = (carShop) intent.getSerializableExtra("carShop");
        id = carShop.getId();
        uid = carShop.getUid();
        area_id = carShop.getArea_id();
        address = carShop.getAddress();
        gname = carShop.getGname();
        phone = carShop.getPhone();
        uname = carShop.getUname();
        img_url = carShop.getImg_url();
        tvPhone.setText(phone);
        tvAdress.setText(address);
        tvGname.setText(gname);
        tvUname.setText(uname);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                RecoverListActivity.this.startActivity(intent);
            }
        });
        Ion.with(context).load(img_url).withBitmap()
                .placeholder(R.drawable.madehua)
                .error(R.drawable.madehua).intoImageView(ivHeadphoto);
    }

    private void setTitle() {
        imgbtn_left = (ImageButton) findViewById(R.id.imgbtn_left);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("回收列表");
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setImageResource(R.drawable.back_button);
        imgbtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog.showInfoDialog(context, "是否终止交易", "提示", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_pos:
                                sendWlBack();
                                break;
                        }
                    }
                });
            }
        });
    }

    private void sendWlBack() {

//        gid	必须	int	网点id
//        uid	必须	int	用户id
//        ems_id	必须	int	物流id
        String loginUid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String ems_id = SharedPreferenceUtil.getString(context, Constant.WL_ID, "-1");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("guid", uid);
        dictParam.put("gid", id);
        dictParam.put("uid", loginUid);
        dictParam.put("ems_id", ems_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
        params.add("guid", uid);
        params.add("gid", id);
        params.add("uid", loginUid);
        params.add("ems_id", ems_id);
        params.add("access_token", token);
        AsyncHttpClient Client = new AsyncHttpClient();
        Client.post(context, Config.BASEURL + UrlConstant.back_wl, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        LogUtil.e("response", response.toString());
                        if (200 == statusCode) {
                            try {
                                if(response.has("data")){
                                    String s = response.getString("data");
                                    response = AES.desEncrypt(s);
                                }else{
                                    response = response.getJSONObject("result");
                                }
                                if("40013".equals(response.getString("status"))){
                                    //activity跳转
                                    Intent intent = new Intent(RecoverListActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }else if ("1001".equals(response.getString("status"))) {
                                    callback = true;
                                    finish();
                                } else {
                                    CommonUtil.StartToast(context, "message");
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        CommonDialog.showInfoDialogFailure(context);
                    }
                }

        );
    }

    public void ReplaceFragment(int state) {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.fragment_tabs);
        tabs.setWidth(getscrren()[0]);
        pager = (MyViewPager) findViewById(R.id.fragment_viewpager);
        String[] titls = {"待取货", "已取货"};
        fp = new MyFragmentPagerAdapter(getSupportFragmentManager(), titls);
        waitGetFragment = new WaitGetFragment();
        waitPayFragment = new WaitPayFragment();
        areadyFragment = new AreadyFragment();
        pages.clear();
        pager.removeAllViewsInLayout();
        if (state == 0) {
            pages.add(waitGetFragment);
            pages.add(areadyFragment);
        } else {
            pages.add(waitPayFragment);
            pages.add(areadyFragment);
        }
        fp.setList(pages);
        pager.setAdapter(fp);
        fp.notifyDataSetChanged();
        initPagerData();
    }

    // 初始化页面数据
    private void initPagerData() {
        double v = CommonUtil.getscreenHeightScale(context);
        // 页面适配器
        tabs.setOnPageChangeListener(this);
        tabs.setViewPager(pager);
        tabs.setBackgroundColor(Color.WHITE);
        // 指示器颜色
        tabs.setIndicatorColor(Color.rgb(8, 207, 78));
        // 指示器的高度
        tabs.setIndicatorHeight((int) (10 * v));
        // 字体大小
        tabs.setTextSize((int) (30 * v));
        // 设置底线的高度
        tabs.setUnderlineHeight((1));
        // 设置底线的颜色
        tabs.setUnderlineColor(Color.rgb(8, 207, 78));
        // 适配时指定的高度
        tabs.getLayoutParams().height = (int) (100 * v);
    }

    // 获取屏幕宽高
    private int[] getscrren() {
        int[] w_h = new int[2];
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        w_h[0] = width;
        w_h[1] = height;
        return w_h;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {


            CommonDialog.showInfoDialog(context, "是否终止交易", "提示", "取消", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_pos:
                            sendWlBack();
                            break;
                    }
                }
            });

            if (callback) {
                return true;
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
