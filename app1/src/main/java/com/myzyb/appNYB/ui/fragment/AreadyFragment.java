package com.myzyb.appNYB.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.AlreadyGetGood;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.adapter.AlreadyExAdapter;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/***
 * fragment页面
 *已取货页面
 * @author
 */
public class AreadyFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2 {
    private boolean init;
    private Context context;
    private List<AlreadyGetGood> sList = new ArrayList<AlreadyGetGood>();
    private ExpandableListView exlist2;
    private View view;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = getActivity();
        init = true;
        view = inflater.inflate(R.layout.exlist3_pager, container, false);
        initView();
        return view;
    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshExpandableListView) view.findViewById(R.id.exlist_inpager);
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
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "-1");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", uid);
        dictParam.put("type", "dealer");
        dictParam.put("gid", gid);
        dictParam.put("page", String.valueOf(mPage));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.POST_SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
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
                                //activity跳转
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }else if ("1001".equals(response.getString("status"))) {
                                if ("null".equals(response.getString("list"))) {
                                    mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据!");
                                } else {

                                }
                                JSONArray alreadyGetList = response.getJSONArray("list");
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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (getUserVisibleHint()) {
            getdata();
        }
    }

    private void getdata() {
        if (init) {
//			init=false;
            //从服务器加载数据
            getservciesData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            getdata();
        }
    }
}
