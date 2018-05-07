package com.myzyb2.appNYB2.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.AlreadyGetGood;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.adapter.AlreadyExAdapter;
import com.myzyb2.appNYB2.ui.view.DateTimePickDialogUtil;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.widget.CustomDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 个人中心_已取订单
 */
public class TakeOrdersActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

//    @Bind(R.id.bt_left)
//    Button btLeft;
//    @Bind(R.id.bt_center)
//    Button btCenter;
//    @Bind(R.id.bt_right)
//    Button btRight;
    @Bind(R.id.exlist_inpager)
    PullToRefreshExpandableListView exlistInpager;//订单列表
    @Bind(R.id.tv_start_data)
    EditText tvStartData;
    @Bind(R.id.tv_end_data)
    EditText tvEndData;
    @Bind(R.id.bt_query)
    Button btQuery;
    @Bind(R.id.ll_order_history)
    LinearLayout llOrderHistory;//历史订单布局
    private String initStartDateTime; // 初始化时间
    private String startDateTime; // 开始时间用于上传服务器
    private String endDateTime; // 结束时间上传服务器
    private int mPage = 1;
    private List<AlreadyGetGood> sList = new ArrayList<AlreadyGetGood>();
    private ExpandableListView exlist2;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    alreadyExAdapter.setList(sList);
                    alreadyExAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    sendDataToService();
                    exlistInpager.onRefreshComplete();
                    break;
                case 5:
                    sendDataToService();
                    exlistInpager.onRefreshComplete();
                    break;

            }
        }
    };
    private AlreadyExAdapter alreadyExAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_orders);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("已取订单");
        initView();
        initDatePicker();
//        showTodayOrder();
    }

    private void initView() {
//        btLeft.setOnClickListener(this);
//        btCenter.setOnClickListener(this);
//        btRight.setOnClickListener(this);
        tvStartData.setOnClickListener(this);
        tvEndData.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        isVisibility(false);
        initStartDateTime = CommonUtil.formatDateToDay();
        exlistInpager.setOnRefreshListener(this);
        exlistInpager.setMode(PullToRefreshBase.Mode.BOTH);
        exlistInpager.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
        exlistInpager.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        exlistInpager.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");
        exlist2 = exlistInpager.getRefreshableView();
        alreadyExAdapter = new AlreadyExAdapter(context, sList);
        exlist2.setAdapter(alreadyExAdapter);
        exlist2.setGroupIndicator(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //今日订单
            case R.id.bt_left:
                showTodayOrder();
                break;
            //最近三日
            case R.id.bt_center:
                isVisibility(true);
//                initTextBg(2);
                clearList();
                try {
                    Date[] dates = CommonUtil.formatDateOneDay(null, false);
                    LogUtil.e("11", String.valueOf(dates[0].getTime()));
                    LogUtil.e("11", String.valueOf(dates[1].getTime()));
                    startDateTime = String.valueOf(dates[0].getTime() / 1000 - (2 * 24 * 60 * 60));
                    endDateTime = String.valueOf(dates[1].getTime() / 1000);
                    mPage = 1;
                    sendDataToService();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            //历史订单
            case R.id.bt_right:
                isVisibility(false);
//                initTextBg(3);
                clearList();
                break;
            //开始日期
            case R.id.tv_start_data:
//                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//                        this, initStartDateTime);
//                dateTimePicKDialog.dateTimePicKDialog(tvStartData);
                customDatePicker1.show(tvStartData.getText().toString());
                break;
            //结束日期
            case R.id.tv_end_data:
//                DateTimePickDialogUtil dateTimePicKDialog2 = new DateTimePickDialogUtil(
//                        this, initStartDateTime);
//                dateTimePicKDialog2.dateTimePicKDialog(tvEndData);
                customDatePicker2.show(tvEndData.getText().toString());
                break;
            //查询按钮
            case R.id.bt_query:
                String startDate = tvStartData.getText().toString().trim();
                String endDate = tvEndData.getText().toString().trim();
                if ("请选择开始日期".equals(startDate)) {
                    CommonUtil.StartToast(context, "请选择开始日期");
                    return;
                }
                if ("请选择结束日期".equals(endDate)) {
                    CommonUtil.StartToast(context, "请选择结束日期");
                    return;
                }
                try {
                    Date[] dates1 = CommonUtil.formatDate(startDate, true);
                    long mlong1 = dates1[0].getTime() / 1000;
                    Date[] dates2 = CommonUtil.formatDate(endDate, true);
                    long mlong2 = dates2[1].getTime() / 1000;
                    if (mlong2 < mlong1) {
                        CommonUtil.StartToast(context, "结束日期不能小于开始日期");
                    } else {
                        startDateTime = String.valueOf(mlong1);
                        endDateTime = String.valueOf(mlong2);
                        mPage = 1;
                        sendDataToService();
                        isVisibility(true);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;

        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvStartData.setText(now.split(" ")[0]);
        tvEndData.setText(now.split(" ")[0]);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvStartData.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndData.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 不显示时和分
        customDatePicker2.setIsLoop(false); // 不允许循环滚动
    }

    private void isVisibility(boolean b) {

        if (b) {
            exlistInpager.setVisibility(View.VISIBLE);
            llOrderHistory.setVisibility(View.GONE);
        } else {
            exlistInpager.setVisibility(View.GONE);
            llOrderHistory.setVisibility(View.VISIBLE);
        }

    }

    private void showTodayOrder() {
        isVisibility(true);
//        initTextBg(1);
        clearList();
        try {
            Date[] dates = CommonUtil.formatDateOneDay(null, false);
            LogUtil.e("11", String.valueOf(dates[0].getTime()));
            LogUtil.e("11", String.valueOf(dates[1].getTime()));
            startDateTime = String.valueOf(dates[0].getTime() / 1000);
            endDateTime = String.valueOf(dates[1].getTime() / 1000);
            mPage = 1;
            sendDataToService();


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

//    private void initTextBg(int stata) {
//        switch (stata) {
//            case 1:
//                btLeft.setBackgroundResource(R.drawable.today_white);
//                btLeft.setTextColor(Color.parseColor("#08cf4e"));
//                btCenter.setBackgroundColor(Color.parseColor("#ebebeb"));
//                btCenter.setTextColor(Color.parseColor("#636363"));
//                btRight.setBackgroundResource(R.drawable.histoy_gray);
//                btRight.setTextColor(Color.parseColor("#636363"));
//
//                break;
//            case 2:
//                btLeft.setBackgroundResource(R.drawable.today_gray);
//                btLeft.setTextColor(Color.parseColor("#636363"));
//                btCenter.setBackgroundColor(Color.parseColor("#ffffff"));
//                btCenter.setTextColor(Color.parseColor("#08cf4e"));
//                btRight.setBackgroundResource(R.drawable.histoy_gray);
//                btRight.setTextColor(Color.parseColor("#636363"));
//                break;
//            case 3:
//                btLeft.setBackgroundResource(R.drawable.today_gray);
//                btLeft.setTextColor(Color.parseColor("#636363"));
//                btCenter.setBackgroundColor(Color.parseColor("#ebebeb"));
//                btCenter.setTextColor(Color.parseColor("#636363"));
//                btRight.setBackgroundResource(R.drawable.history_white);
//                btRight.setTextColor(Color.parseColor("#08cf4e"));
//                break;
//        }
//    }


    private void sendDataToService() {

        //ems_gid
        //start_time
        //end_time
        //page
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        final String ems_gid = SharedPreferenceUtil.getString(context, Constant.WL_ID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("ems_gid", ems_gid);
        dictParam.put("start_time", startDateTime);
        dictParam.put("end_time", endDateTime);
        dictParam.put("page", String.valueOf(mPage));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("ems_gid", ems_gid);
        params.add("start_time", startDateTime);
        params.add("end_time", endDateTime);
        params.add("page", String.valueOf(mPage));
        params.add("access_token", token);
        // UrlConstant.Time_Already_get

        LogUtil.e("params", params.toString());
        netUtils.putReturnJson(context, NetUtils.GET, UrlConstant.Time_Already_get, params, dictParam,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("response", response.toString());
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(TakeOrdersActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        JSONArray alreadyGetList = response.getJSONArray("list");
                        if (alreadyGetList != null && alreadyGetList.length() > 0) {
                            for (int i = 0; i < alreadyGetList.length(); i++) {
                                JSONObject alreadyGetJsonObject = alreadyGetList.getJSONObject(i);
                                AlreadyGetGood alreadyGetGood = JsonUtil.getSingleBean(alreadyGetJsonObject.toString(), AlreadyGetGood.class);
                                sList.add(alreadyGetGood);
                            }
                            mHandler.sendEmptyMessage(1);
                        } else {
                            CommonUtil.StartToast(context, "没有数据了");
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
            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mPage = 1;
        clearList();
        mHandler.sendEmptyMessageDelayed(4, 2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mPage++;
        exlistInpager.getLoadingLayoutProxy(false, true).setPullLabel("下拉加载...");
        mHandler.sendEmptyMessageDelayed(5, 1000);

    }

    public void clearList() {
        if (sList != null && sList.size() > 0) {
            sList.clear();
        }
    }

}
