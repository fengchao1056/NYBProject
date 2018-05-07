package com.myzyb2.appNYB2.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.CommApplication;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.Config;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.PersonalCenter;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.activity.main.MainActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.activity.pay.PayPriceActivity;
import com.myzyb2.appNYB2.ui.view.CircleImageView;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.FontsUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.tv_weight)
    TextView tvWeight;
    @Bind(R.id.tv_shopnum)
    TextView tvShopnum;
    @Bind(R.id.tv_gomain)
    TextView tvGomain;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_gopay)
    TextView tvGopay;
    @Bind(R.id.tv_version)
    TextView tvVersion;
//    @Bind(R.id.imageview)
//    CircleImageView imageview;
    @Bind(R.id.scl_scrollView)
    PullToRefreshScrollView sclScrollView;

    @Bind(R.id.DZM12)
    TextView tx12;
    @Bind(R.id.DZM20)
    TextView tx20;
    @Bind(R.id.DZM30)
    TextView tx30;

    @Bind(R.id.baojia)
    TextView txBj;

    @Bind(R.id.back_line)
    LinearLayout backout;




    private Context context;
    private PersonalCenter personalCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        context = this;
        initView();
        String versionName = CommonUtil.getVersionName(getApplicationContext());
        if (versionName != null && !TextUtils.isEmpty(versionName)) {
            tvVersion.setText("版本：V" + versionName);
        }
//        Drawable d=Drawable.createFromPath();


    }

    private void initView() {
//        String versionName = CommonUtil.getVersionName(context);
//        if (versionName != null && !TextUtils.isEmpty(versionName)) {
//            tvVersion.setText(versionName);
//        }
        sclScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getData();
                        sclScrollView.onRefreshComplete();
                    }
                }, 1000);


            }
        });

    }


    private void initData() {


        if (personalCenter != null) {

            int v = (int) ((personalCenter.countweight / 1000) + 0.5);
            tvWeight.setText(String.valueOf(v));
        }


        FontsUtil.FontsUtil(tvWeight, context);
        tvShopnum.setText(String.valueOf(personalCenter.countnum));
        tvPrice.setText(String.valueOf(personalCenter.money));
        FontsUtil.FontsUtilYUe(tvPrice, context);
//        Ion.with(context).load(personalCenter.img_url).withBitmap()
//                .placeholder(R.drawable.madehua)
//                .error(R.drawable.madehua).intoImageView(imageview);
        tvGomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personalCenter.money != null && !TextUtils.isEmpty(personalCenter.money)) {
                    double i = Double.parseDouble(personalCenter.money);
                    if (i < Constant.min_money) {
                        CommonUtil.StartToast(context, "余额小于" + Constant.min_money + "，请充值");
                    } else {
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                }else {
                    CommonUtil.StartToast(context,"网络异常，请下拉刷新本页面");
                }
            }
        });

        tvGopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PayPriceActivity.class));


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }






    private void setTVColor(String str , String str1 ,String str2, String color , TextView tv){
        int a = 0; //从字符ch1的下标开始
        int b = str.indexOf(str1)+1; //到字符ch2的下标+1结束,因为SpannableStringBuilder的setSpan方法中区间为[ a,b )

        SpannableStringBuilder builder = new SpannableStringBuilder(str1);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.insert(0,str2);
//        SpannableStringBuilder builder = new SpannableStringBuilder(str2);
//        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }
    public void setSize(String str , char ch1 , char ch2 , String color , TextView tv,char size1, char size2 ){
        int a = str.indexOf(ch1); //从字符ch1的下标开始
        int b = str.indexOf(ch2); //到字符ch2的下标+1结束,因为SpannableStringBuilder的setSpan方法中区间为[ a,b )左闭右开
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int pre  = str.indexOf(size1); //从字符ch1的下标开始
        int last  = str.indexOf(size2)+1;
        builder.setSpan(new AbsoluteSizeSpan(12, true), pre, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }
    private void initDataPrice() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String area_id = SharedPreferenceUtil.getString(context, Constant.DQID, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", NetUtils.getEncode(uid));
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.put("area_id", area_id);
        params.put("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.GET, UrlConstant.RecoveryPrice, params, dictParam, new JsonHttpResponseHandler() {

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
//                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        JSONObject list = response.getJSONObject("list");

                        String price = list.getString("prize");
                        String price1 =list.getString("prize1");
                        String price2 =list.getString("prize2");
                        String price3 =list.getString("prize3");
                        String norms1 = list.getString("norms1");
                        String norms2 = list.getString("norms2");
                        String norms3 = list.getString("norms3");
                        if (txBj != null && tx12 != null) {
//                            tx12.setText(norms1+"   " +price1);
//                            tx20.setText(norms2+"   " +price2);
//                            tx30.setText(norms3+"   " +price3);
                            setSize("今日回收报价(废旧电池) "+price +"元／kg",' ','元',"#FE6026",txBj,'(',')');

                            int count = (norms1+"   " + price1).length()-1;
                            String str1 = norms1+"   " + price1;
                            setTVColor(str1,"   " + price1,norms1,"#FE6026",tx12);


                            int count1 = (norms2+"   " +price2).length()-1;
                            String str2 = norms2+"   " +price2;
                            setTVColor(str2,"   " +price2,norms2,"#FE6026",tx20);

                            int count2 = (norms3+"   " +price3).length()-1;
                            String str3 = norms3+"   " +price3;
                            setTVColor(str3,"   " +price3,norms3,"#FE6026",tx30);
//                            FontsUtil.FontsUtilYUe(txBj, context);
//                            FontsUtil.FontsUtilYUe(tx12, context);
//                            FontsUtil.FontsUtilYUe(tx20, context);
//                            FontsUtil.FontsUtilYUe(tx30, context);
//                            StringInterceptionChangeRed(txBj,price,price,"今日回收报价(废旧电池）"+price +"元／kg" );

//                                tvNum.setText(prize);
//                                FontsUtil.FontsUtil(tvNum, context);
//                                tvXh1.setText("6-DZM-12  " + prize1);
//                                tvXh2.setText("6-DZM-20  " + prize2);
                        }

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

                LogUtil.e("responseString", responseString);
            }
        });


    }


    private void getData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String mobile = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "-1");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("mobile", mobile);
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("mobile", mobile);
        params.add("uid", uid);
        params.add("access_token", token);
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.SELLERURL, params, dictParam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        LogUtil.e("response", response.toString());
                        try {
                            CommonDialog.closeProgressDialog();
                            String s = response.getString("data");
                            response = AES.desEncrypt(s);
                            String status = response.getString("status");
//                            String list = response.getString("list");
                            switch (status) {
                                case "40013":
                                    //activity跳转
                                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "1001":
                                    if (response != null && !TextUtils.isEmpty(response.toString()) && !"null".equals(response)) {
                                        personalCenter = JsonUtil.getSingleBean(response.getString("list"), PersonalCenter.class);
//                                        personalCenter = JsonUtil.getSingleBean(list, PersonalCenter.class);
                                        if (personalCenter.bc_ids != null) {
                                            SharedPreferenceUtil.saveString(context, Constant.Bank_card, personalCenter.bc_ids);
                                            SharedPreferenceUtil.saveString(context, Constant.Bank_zd, personalCenter.bc_id);
                                        } else {
                                            SharedPreferenceUtil.saveString(context, Constant.Bank_card, "");
                                            SharedPreferenceUtil.saveString(context, Constant.Bank_zd, "");

                                        }
                                        //初始化控件内容;
                                        initData();
                                        initDataPrice();
                                    }
                                    break;
                                default:
                                    CommonUtil.StartToast(context, response.getString("message"));
                                    break;
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }

        );


    }

}
