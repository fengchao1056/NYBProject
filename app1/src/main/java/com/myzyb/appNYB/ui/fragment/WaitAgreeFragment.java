package com.myzyb.appNYB.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.myzyb.appNYB.javabean.Child2Item;
import com.myzyb.appNYB.javabean.Parent2Item;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.ui.activity.additive.WaitEmsActivity;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.adapter.WaitAgreeExAdapter;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
/**
 *待确认
 */


public class WaitAgreeFragment extends Fragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private boolean init;
    private Context context;
    private List<Parent2Item> p2List;
    private ExpandableListView exlist2;
    private View view;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    WaitAgreeExAdapter waitAgreeExAdapter = new WaitAgreeExAdapter(context, p2List);
                    exlist2.setAdapter(waitAgreeExAdapter);
                    exlist2.setGroupIndicator(null);
                    waitAgreeExAdapter.setStatus(msg.arg1);
                    CommonDialog.closeProgressDialog();
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
    private PullToRefreshExpandableListView mPullRefreshListView;
    private AsyncHttpClient client;
    private Button bt_tongyi;
    private Button bt_bohui;
    private RelativeLayout rl_bo_to;
    private TextView tv_payremind;

    private double price_count;
    private double weight_count;
    private int num_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = getActivity();
        view = inflater.inflate(R.layout.exlist2_pager, container, false);
        initView();
        init = true;
        return view;
    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshExpandableListView) view.findViewById(R.id.exlist2_inpager);
        rl_bo_to = (RelativeLayout) view.findViewById(R.id.rl_bo_to);
        bt_tongyi = (Button) view.findViewById(R.id.bt_tongyi);
        bt_bohui = (Button) view.findViewById(R.id.bt_bohui);
        tv_payremind = (TextView) view.findViewById(R.id.tv_payremind);
        bt_tongyi.setOnClickListener(this);
        bt_bohui.setOnClickListener(this);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中！");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
        exlist2 = mPullRefreshListView.getRefreshableView();
    }

    private void getservciesData() {

        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "-1");
        String area_id = SharedPreferenceUtil.getString(context, Constant.DQID, "-1");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "-1");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");

        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("guid", uid);
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", uid);
        dictParam.put("gid", gid);
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.POST_SIGN(params, dictParam);
        params.put("guid", uid);
        params.add("login_salt", loginSalt);
        params.put("uid", uid);
        params.put("gid", gid);
        params.put("area_id", area_id);
        params.put("access_token", token);
        LogUtil.e("params", params.toString());
        client = new AsyncHttpClient();
        client.post(context, Config.BASEURL + UrlConstant.WAITAGREE, params, new JsonHttpResponseHandler() {
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
                        if (200 == statusCode) {
                            FragmentManager fm1 = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction1 = fm1.beginTransaction();
                            int prdstatus = response.getInt("prdstatus");
                            LogUtil.e("agreeresponse", response.toString());
                            if (prdstatus == 0) {
                                tv_payremind.setText("等待物流确认");
                                initEXList(response, prdstatus);
                                bt_tongyi.setVisibility(View.GONE);
                                bt_bohui.setVisibility(View.GONE);
                            }

                            if (prdstatus == 1) {
                                initEXList(response, prdstatus);
                                bt_tongyi.setVisibility(View.VISIBLE);
                                bt_bohui.setVisibility(View.VISIBLE);
                                tv_payremind.setText("取货员确认收货" + weight_count + "kg，总计需付款" + price_count + "元");
                                tv_payremind.setVisibility(View.VISIBLE);


                            }

                            if (prdstatus == 2) {
                                initEXList(response, prdstatus);
                                tv_payremind.setText("请等待物流付款！");
                                bt_tongyi.setVisibility(View.GONE);
                                bt_bohui.setVisibility(View.GONE);
                            }

                            if (prdstatus == 3) {

                                if (transaction1 != null) {

                                    CommonUtil.StartToast(context, "交易完成");
                                    AreadyFragment areadyFragment = new AreadyFragment();
                                    transaction1.replace(R.id.frgment_base, areadyFragment);
                                    transaction1.commit();
                                }
                            }
                            if (prdstatus == 4) {
                                if (transaction1 != null) {
                                    CommonUtil.StartToast(context, "物流终止交易");
                                    WaitGetFragment waitGetFragment = new WaitGetFragment();
                                    transaction1.replace(R.id.frgment_base, waitGetFragment);
                                    transaction1.commit();
                                }
                            }
                        } else {
                            CommonUtil.StartToast(context, "连接失败");
                        }
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 初始化expandlistview数据
     *
     * @param response
     * @param prdstatus
     * @throws JSONException
     */
    private void initEXList(JSONObject response, int prdstatus) throws JSONException {
        String list = response.getString("list");

        if (!TextUtils.isEmpty(list) && !list.equals("null") && list.length() != 0) {
//            waitAgreeBean = JsonUtil.getSingleBean(response.toString(), WaitAgreeBean.class);
//            if (waitAgreeBean != null) {
//                p2List = waitAgreeBean.list;
//                LogUtil.e("p2List", p2List.toString());
            if (p2List != null) {
                p2List.clear();
            } else {
                p2List = new ArrayList<Parent2Item>();
            }
            price_count = response.getDouble("admin_price_count");
            weight_count = response.getDouble("weight_count");
            num_count = response.getInt("num_count");
            JSONArray jsonArray = response.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                LogUtil.e("json", obj.toString());
                Parent2Item parent2Item = JsonUtil.getSingleBean(obj.toString(), Parent2Item.class);
                LogUtil.e("parentItem", parent2Item.toString());
                JSONArray son = obj.getJSONArray("son");
                Type listType = new TypeToken<List<Child2Item>>() {
                }.getType();
                List<Child2Item> child2Itemcts = (List<Child2Item>) JsonUtil.parseJsonToList(son.toString(), listType);
                parent2Item.setSon(child2Itemcts);
                p2List.add(parent2Item);

            }
            Message message = new Message();
            message.what = 1;
            message.arg1 = prdstatus;
            mHandler.sendMessage(message);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bohui:

                sendToServe(0);
                bt_tongyi.setVisibility(View.GONE);
                bt_bohui.setVisibility(View.GONE);


                break;
            case R.id.bt_tongyi:
                sendToServe(2);
                bt_tongyi.setVisibility(View.GONE);
                bt_bohui.setVisibility(View.GONE);
                break;

        }


    }

    private void sendToServe(final int status) {
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "-1");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "-1");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("guid", uid);
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", uid);
        dictParam.put("gid", gid);
        dictParam.put("type", Constant.type);
        dictParam.put("status", String.valueOf(status));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.POST_SIGN(params, dictParam);
        params.add("guid", uid);
        params.add("login_salt", loginSalt);
        params.put("uid", uid);
        params.put("gid", gid);
        params.put("type", Constant.type);
        params.put("status", status);
        params.put("access_token", token);

        client.post(context, Config.BASEURL + UrlConstant.AGREEORNOT, params, new JsonHttpResponseHandler() {
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
                    int jsonStatus = response.getInt("status");
                    if(40013 == jsonStatus){
                        //activity跳转
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else if (jsonStatus == 1001) {
                        if (status == 0) {
                            CommonUtil.StartToast(context, "驳回成功");

                        } else if (status == 2) {
                            CommonUtil.StartToast(context, "您已同意，等待物流付款");
                            startActivity(new Intent(getActivity(), WaitEmsActivity.class));
                        }
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


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
