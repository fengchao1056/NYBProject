package com.myzyb2.appNYB2.ui.activity.my;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.Config;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.AlreadyGetGood;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.login.WelcomeActivity;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.adapter.AlreadyExAdapter;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DlearGetActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {
    private static String tag = "areadyFragment";
    private boolean init;
    private Context context;
    private List<AlreadyGetGood> sList = new ArrayList<AlreadyGetGood>();
    private ExpandableListView exlist2;
    private int mPage = 1;
    private String gid;
    private AlreadyExAdapter alreadyExAdapter;
    private PullToRefreshExpandableListView mPullRefreshListView;
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
                    getservciesData();
                    mPullRefreshListView.onRefreshComplete();
                    break;
                case 5:
                    getservciesData();
                    mPullRefreshListView.onRefreshComplete();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlear_get);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("已取订单");
        getservciesData();
        initView();

    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshExpandableListView) findViewById(R.id.exlist_inpager);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");
        exlist2 = mPullRefreshListView.getRefreshableView();
        alreadyExAdapter = new AlreadyExAdapter(context, sList);
        exlist2.setAdapter(alreadyExAdapter);
        exlist2.setGroupIndicator(null);
    }

    private void getservciesData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        gid = getIntent().getStringExtra("gid");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", uid);
        dictParam.put("type", "dealer");
        dictParam.put("gid", gid);
        dictParam.put("page", String.valueOf(mPage));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params  = NetUtils.SIGN(params, dictParam);
        params.put("login_salt", loginSalt);
        params.put("uid", uid);
        params.put("type", "dealer");//网点已取货
        params.put("gid", gid);
        params.put("page", mPage);
        params.put("access_token", token);
        LogUtil.e("params", params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, Config.BASEURL + UrlConstant.AREADYCLAIMGOODS, params, new JsonHttpResponseHandler() {
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
                                Intent intent = new Intent(DlearGetActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else if ("1001".equals(response.getString("status"))) {
                                LogUtil.e("params", response.toString());
                                JSONArray alreadyGetList = response.getJSONArray("list");
                                if ("null".equals(response)) {
                                    mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据!");
                                } else {
                                    mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("下拉加载...");
                                }
                                if (alreadyGetList != null && alreadyGetList.length() > 0) {
                                    for (int i = 0; i < alreadyGetList.length(); i++) {
                                        JSONObject alreadyGetJsonObject = alreadyGetList.getJSONObject(i);
                                        AlreadyGetGood alreadyGetGood = JsonUtil.getSingleBean(alreadyGetJsonObject.toString(), AlreadyGetGood.class);
                                        sList.add(alreadyGetGood);
                                    }
                                    mHandler.sendEmptyMessage(1);

                                }
                            } else {
                                CommonUtil.StartToast(context, response.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
        );


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mPage = 1;
        if (sList != null && sList.size() > 0) {
            sList.clear();
        }
        mHandler.sendEmptyMessageDelayed(4, 2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mPage++;
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("下拉加载...");
        mHandler.sendEmptyMessageDelayed(5, 1000);

    }
}