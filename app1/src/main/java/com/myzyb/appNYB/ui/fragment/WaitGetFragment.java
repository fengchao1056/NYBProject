package com.myzyb.appNYB.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.C;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.ChildItem;
import com.myzyb.appNYB.javabean.ParentItem;
import com.myzyb.appNYB.service.FragmentListener;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.adapter.NoGetExAdapter;
import com.myzyb.appNYB.ui.view.AlertDialog;
import com.myzyb.appNYB.ui.view.GrabDialog;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.DoubleUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;
import com.myzyb.appNYB.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 *待取货
 */

public class WaitGetFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2 {
    private static Button bt_submit;

    private boolean init;
    private Context context;
    private List<ParentItem> pList = new ArrayList<ParentItem>();
    private ExpandableListView exlist1;
    private View view;
    public HashMap<Integer, Integer> changeHashMap;
    private NoGetExAdapter noGetExAdapter;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    CommonDialog.closeProgressDialog();
                    initCount();
                    exlist1.setAdapter(noGetExAdapter);
                    noGetExAdapter.notifyDataSetInvalidated();
                    exlist1.setGroupIndicator(null);

                    break;
                case 2:
                    String json = (String) msg.obj;
                    sendRemove(json);
                    break;
                case 3:
                    String json2 = (String) msg.obj;
                    sendRemove(json2);
                    break;
                case 4:
                    mPullRefreshListView.onRefreshComplete();
                    getservciesData();
                    break;
                case 5:
                    mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
                    mPullRefreshListView.onRefreshComplete();
                    break;
            }
        }
    };
    private TextView tv_shuliang;
    private TextView tv_zhongl;
    private TextView tv_zongjia_plist;
    private static double price_count;
    private static double weight_count;
    private static int jifen;
    private static int num_count;
    private LinearLayout rl_countbg;
    private TextView tv_tishi;
    private TextView tv_jifen_plist;

    private PullToRefreshExpandableListView mPullRefreshListView;
    private FragmentListener listener;
    private Fragment fragment;

    private void sendRemove(String json) {
        CommonDialog.showProgressDialog(context);
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        LogUtil.e("ces", "text");
        HashMap<String, String> dictParam = new HashMap<String, String>();
//        dictParam.put("prd", json);
        dictParam.put("gid", gid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.put("prd", json);
        params.put("gid", gid);
        params.put("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.DELBATTERY, params, dictParam, new JsonHttpResponseHandler() {

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
                    }else if ("1001".equals(response.getString("status"))) {
                        getservciesData();
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }
        });

    }


    private void sendChange(String json) {
        CommonDialog.showProgressDialog(context);
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
//        dictParam.put("prd", json);
        dictParam.put("gid", gid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.put("prd", json);
        params.put("gid", gid);
        params.add("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.UpdateBATTERY, params, dictParam, new JsonHttpResponseHandler() {

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
                        getservciesData();
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
                CommonUtil.StartToast(context, "连接失败");
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        context = getActivity();

        view = inflater.inflate(R.layout.exlist1_pager, container, false);
        initView();
        initData();
        init = true;
        return view;
    }

    private void initData() {
        noGetExAdapter = new NoGetExAdapter(context, pList, mHandler, this);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = JsonUtil.parseMapToJson(changeHashMap);
                sendChange(s);

            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FragmentListener) activity;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void initCount() {
        tv_shuliang.setText(num_count + "块");
        tv_zhongl.setText(weight_count + "kg");
        tv_zongjia_plist.setText("¥" + price_count);
        jifen = (int) DoubleUtil.doubleXintToDouble(weight_count, 10);
        tv_jifen_plist.setText(jifen + "");
    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshExpandableListView) view.findViewById(R.id.exlist1_inpager);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中！");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
        exlist1 = mPullRefreshListView.getRefreshableView();
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        tv_shuliang = (TextView) view.findViewById(R.id.tv_shuliang);
        tv_zhongl = (TextView) view.findViewById(R.id.tv_zhongl);
        tv_zongjia_plist = (TextView) view.findViewById(R.id.tv_zongjia_plist);
        tv_jifen_plist = (TextView) view.findViewById(R.id.tv_jifen_plist);
        tv_tishi = (TextView) view.findViewById(R.id.tv_tishi);
        rl_countbg = (LinearLayout) view.findViewById(R.id.rl_countbg);
    }


    public void getMap(HashMap map) {
        changeHashMap = map;

        if (!changeHashMap.isEmpty()) {
            bt_submit.setEnabled(true);

        } else {
            bt_submit.setEnabled(false);

        }
    }

    public double getPrice_count() {
        return price_count;
    }

    public double getWeight_count() {
        return weight_count;
    }


    public int getNum_count() {
        return num_count;
    }

    public void setPrice_count(int num_count, double price_count, double weight_count) {
        WaitGetFragment.price_count = price_count;
        WaitGetFragment.weight_count = weight_count;
        WaitGetFragment.num_count = num_count;
        initCount();
    }
    private void getservciesData() {
        if (changeHashMap != null) {
            changeHashMap.clear();
        }
        bt_submit.setEnabled(false);
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "-1");
        String area_id = SharedPreferenceUtil.getString(context, Constant.DQID, "-1");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "-1");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", uid);
        dictParam.put("gid", gid);
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.POST_SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
        params.put("uid", uid);
        params.put("gid", gid);
        params.put("area_id", area_id);
        params.put("access_token", token);
        LogUtil.e("params", params.toString());
        AsyncHttpClient Client = new AsyncHttpClient();
        Client.post(context, Config.BASEURL + UrlConstant.WAITCLAIMGOODS, params, new JsonHttpResponseHandler() {
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
                                int prdstatus = response.getInt("prdstatus");
                                listener.onFragmentClickListener(prdstatus, noGetExAdapter);
                                if(response.has("ems_phone")) {
                                    long tel = response.getLong("ems_phone");
                                    if (tel != 0) {
                                        ((MainActivity) context).tel = String.valueOf(tel);
                                        if (((MainActivity) context).getSelectPosition() == 1) {
                                            ((MainActivity) context).showDialog(((MainActivity) context).tel);
                                        }
                                    } else {
                                        ((MainActivity) context).tel = "";
                                    }
                                }else{
                                    ((MainActivity) context).tel = "";
                                }
                                switch (prdstatus) {
                                    case 0:
                                        setEXList(response);
                                        break;
                                    case 1:
                                        //跳转到待确认
                                        FragmentManager fm1 = getActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction1 = fm1.beginTransaction();

                                        if (transaction1 != null) {
                                            WaitAgreeFragment waitAgreeFragment = new WaitAgreeFragment();
                                            transaction1.replace(R.id.frgment_base, waitAgreeFragment);
                                            transaction1.commit();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                CommonUtil.StartToast(context, response.getString("message"));
                            }
                        } catch (
                                Exception e
                                )

                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        CommonUtil.StartToast(context, "连接失败");
                    }
                }


        );

    }

    private void setEXList(JSONObject response) throws JSONException {

        String list = response.getString("list");
        if (!list.equals("null") && list.length() != 0) {
            pList.clear();
            price_count = response.getDouble("admin_price_count");
            weight_count = response.getDouble("weight_count");
            num_count = response.getInt("num_count");
            JSONArray jsonArray = response.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                ParentItem parentItem = JsonUtil.getSingleBean(obj.toString(), ParentItem.class);
                JSONArray son = obj.getJSONArray("son");
                Type listType = new TypeToken<List<ChildItem>>() {
                }.getType();
                List<ChildItem> childItemcts = (List<ChildItem>) JsonUtil.parseJsonToList(son.toString(), listType);
                parentItem.setSon(childItemcts);
                pList.add(parentItem);
            }
            LogUtil.e("pList", pList.toString());
            mHandler.sendEmptyMessage(1);
        } else {
            exlist1.setVisibility(View.GONE);
            rl_countbg.setVisibility(View.GONE);
            tv_tishi.setGravity(Gravity.CENTER);
            tv_tishi.setVisibility(View.VISIBLE);
            CommonDialog.closeProgressDialog();
        }


    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        LogUtil.e("isReplaceccccccconResume", "");
        super.onResume();
        if (getUserVisibleHint()) {
            getdata();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e("isReplaceccccccconDestroyView", "");

    }

    @Override
    public void onStop() {
        super.onStop();

        LogUtil.e("isReplaceccccccconStop", "");
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtil.e("isReplaceccccccconPause", "");
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




    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(4, 2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(5, 2000);
    }

}
