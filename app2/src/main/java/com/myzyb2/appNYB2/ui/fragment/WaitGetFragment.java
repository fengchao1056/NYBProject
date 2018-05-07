package com.myzyb2.appNYB2.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.Config;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.WaitGetBean;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.login.WelcomeActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.adapter.NoGetExAdapter;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.DoubleUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.StreamUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 物流回收列表待确认页
 */

public class WaitGetFragment extends Fragment {
    private static Button bt_submit;

    private boolean init;
    private Context context;
    private List<WaitGetBean.GroupList> pList;
    private ExpandableListView exlist1;
    private View view;
    public HashMap<Integer, Integer> changeHashMap;
    public HashMap<String, Double> priceHashMap;
    private NoGetExAdapter noGetExAdapter;
    private TextView tv_shuliang;
    private TextView tv_zhongl;
    private TextView tv_zongjia_plist;
    private RelativeLayout rl_countbg;
    private String id;
    private String uid;
    private String area_id;
    private WaitGetBean waitGetBean;
    private AsyncHttpClient client;
    public String start = "";
    public String end = "";

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    CommonDialog.closeProgressDialog();
                    noGetExAdapter.setPlist(pList);
                    exlist1.setAdapter(noGetExAdapter);
                    noGetExAdapter.notifyDataSetChanged();
                    exlist1.setGroupIndicator(null);

                    break;
//                case 4:
//                    getservciesData();
//                    mPullRefreshListView.onRefreshComplete();
//                    break;
//                case 5:
//                    mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
//                    mPullRefreshListView.onRefreshComplete();
//                    break;
            }
        }
    };


    private void sendDataToReciver(String[] str) {

//        gid	必须	int	网点id
//        uid	必须	int	用户id
//        type	必须	string(ems/dealer)	平台
//        status	必须	int(1)	状态
//        ems_id	必须	int	物流的id
//        prd		json	key id value 数量
//        norms		json	key norms例如（6-dzm..） value 价钱
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        CommonDialog.showProgressDialog(context);
        String ems_id = SharedPreferenceUtil.getString(context, Constant.WL_ID, "");
        String loginUid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        RequestParams params = new RequestParams();
        //数量
        if (null != str[0]) {
//            dictParam.put("prd", str[0]);
            params.put("prd", str[0]);
        }
        //价格
        if (null != str[1]) {
//            dictParam.put("norms", str[1]);
            params.put("norms", str[1]);
        }
        dictParam.put("guid", uid);
        dictParam.put("gid", id);
//        dictParam.put("uid", loginUid);
        dictParam.put("ems_id", ems_id);
        dictParam.put("type", "ems");
        dictParam.put("status", "1");
        dictParam.put("access_token", NetUtils.getEncode(token));

        params.add("guid", uid);
        params.add("gid", id);
//        params.add("uid", loginUid);
        params.add("ems_id", ems_id);
        params.add("type", "ems");
        params.add("status", "1");
        params.add("access_token", token);
        LogUtil.e("waiparams", params.toString());
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.replace_agree, params, dictParam, new JsonHttpResponseHandler() {
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
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        bt_submit.setEnabled(false);
                        CommonDialog.closeProgressDialog();
                        CommonUtil.StartToast(context, "确认成功，请等待网点同意");
                        ((RecoverListActivity) context).ReplaceFragment(2);
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
                CommonDialog.showInfoDialogFailure(context);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = getActivity();
        init = true;
        view = inflater.inflate(R.layout.exlist1_pager, container, false);
        initView();
        initData();
        return view;
    }

    private void initData() {
        client = new AsyncHttpClient();
        final String[] s1 = new String[2];
        noGetExAdapter = new NoGetExAdapter(context, mHandler, this);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (changeHashMap != null && !changeHashMap.isEmpty()) {
                    s1[0] = JsonUtil.parseMapToJson(changeHashMap);
                }
                if (priceHashMap != null && !priceHashMap.isEmpty()) {
                    s1[1] = JsonUtil.parseMapToJson(priceHashMap);
                }
                sendDataToReciver(s1);
            }
        });
    }


    private void initCount() {
        tv_zongjia_plist.setText("¥" + waitGetBean.admin_price_count);
        tv_shuliang.setText(waitGetBean.num_count + "块");
        tv_zhongl.setText(waitGetBean.weight_count + "kg");

    }

    private void initView() {
        exlist1 = (ExpandableListView) view.findViewById(R.id.exlist1_inpager);
//        mPullRefreshListView.setOnRefreshListener(this);
//        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
//        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中！");
//        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
//        exlist1 = mPullRefreshListView.getRefreshableView();
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        tv_shuliang = (TextView) view.findViewById(R.id.tv_shuliang);
        tv_zhongl = (TextView) view.findViewById(R.id.tv_zhongl);
        tv_zongjia_plist = (TextView) view.findViewById(R.id.tv_zongjia_plist);
        rl_countbg = (RelativeLayout) view.findViewById(R.id.rl_countbg);
    }
//获取数量改变的电池集合
    public void getChangeHashMap(HashMap map) {
        changeHashMap = map;
    }
    //获取修正价改变的电池集合
    public void getpriceHashMap(HashMap map) {
        priceHashMap = map;
    }

    public void setPrice_count() {
        double price_count = 0;
        double weight = 0;
        int number = 0;
        for (WaitGetBean.GroupList parentItem : pList) {
            double price_count1 = parentItem.price_count;
            double admin_price_count = parentItem.admin_price_count;
            weight += parentItem.weight;
            number += parentItem.nums;

            if (price_count1 != 0) {
                price_count += price_count1;
            } else {
                price_count += admin_price_count;
            }
        }
        tv_zongjia_plist.setText("¥ " + DoubleUtil.doubleFormat(price_count));
        tv_shuliang.setText(number + "块");
        tv_zhongl.setText(DoubleUtil.doubleFormat(weight) + "kg");

    }

    //获取修正价范围系数
    public void getPriceRatio() {
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String loginUid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
//        dictParam.put("guid", uid);
        dictParam.put("uid", loginUid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
//        params.add("guid", uid);
        params.add("uid", loginUid);
        params.add("access_token", token);
        client.post(context, Config.BASEURL + UrlConstant.FIXED_PRICE, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (200 == statusCode) {
                            try {
                                if(response.has("data")){
                                    String s = response.getString("data");
                                    response = AES.desEncrypt(s);
                                }else{
                                    response = response.getJSONObject("result");
                                }
                                String status = response.getString("status");
                                if("40013".equals(status)){
                                    //activity跳转
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                } else if ("1001".equals(status)) {
                                    JSONObject jsonList = response.getJSONObject("list");
                                    start = jsonList.getString("start");
                                    end = jsonList.getString("end");
                                    if (!TextUtils.isEmpty(start) || !TextUtils.isEmpty(end)) {
                                        mHandler.sendEmptyMessage(1);
                                    } else {
                                        CommonUtil.StartToast(context, "与服务器连接失败");
                                    }
                                } else {
                                    CommonUtil.StartToast(context, response.getString("message"));
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

    private void getservciesData() {
        id = ((RecoverListActivity) context).id;
        uid = ((RecoverListActivity) context).uid;
        area_id = ((RecoverListActivity) context).area_id;
        String loginUid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("guid", uid);
        dictParam.put("uid", loginUid);
        dictParam.put("gid", id);
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
        params.add("guid", uid);
        params.add("uid", loginUid);
        params.add("gid", id);
        params.add("area_id", area_id);
        params.add("access_token", token);
        LogUtil.e("getwaiparams", params.toString());
        client.post(context, Config.BASEURL + UrlConstant.WAITAGREE, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
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
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                } else if ("1001".equals(response.getString("status"))) {
//                                    response = response.getJSONObject("list");
                                    int prdstatus = response.getInt("prdstatus");
                                    switch (prdstatus) {
                                        case 0:
                                            serveData(response);
                                            bt_submit.setEnabled(true);
                                            break;
                                        case 1:
                                            //切换到付款
                                            ((RecoverListActivity) context).ReplaceFragment(1);
                                            break;
                                        case 2:
                                            //切换到付款
                                            ((RecoverListActivity) context).ReplaceFragment(1);
                                            break;
                                        default:
                                            CommonUtil.StartToast(context, response.getString("message"));
                                            break;
                                    }

                                } else {
                                    CommonUtil.StartToast(context, response.getString("message"));
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

    private void serveData(JSONObject response) throws JSONException {
//        String list = response.getString("list");
        if (!response.equals("null") && response.length() != 0) {
            waitGetBean = JsonUtil.getSingleBean(response.toString(), WaitGetBean.class);
            pList = waitGetBean.list;
            initCount();
            getPriceRatio();//修正价系数

        } else {
            bt_submit.setEnabled(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            getdata();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void getdata() {
        if (init) {
//			init=false;
            //从服务器加载数据
            if (changeHashMap != null) {
                changeHashMap.clear();
            }
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


//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        mHandler.sendEmptyMessageDelayed(4, 2000);
//    }
//
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        mHandler.sendEmptyMessageDelayed(5, 2000);
//    }
}
