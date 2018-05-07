package com.myzyb2.appNYB2.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.ElectrombileShop;
import com.myzyb2.appNYB2.javabean.PurchaseBean;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.adapter.GrabListAdapter;
import com.myzyb2.appNYB2.ui.adapter.ShopListAdapter;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by panjichang on 17/12/25.
 */
public class GrabFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2 {


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    shopListAdapter.setLIst(ElectrombileShopList);
                    refreshListView.setAdapter(shopListAdapter);
                    shopListAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    Intent intent = new Intent(context, RecoverListActivity.class);
                    intent.putExtra("carShop", ElectrombileShopList.get(0));
                    startActivity(intent);
                    break;
                case 4:
                    requestData();
                    refreshListView.onRefreshComplete();
                    break;
                case 5:
                    refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
                    refreshListView.onRefreshComplete();
                    break;
            }
        }
    };
    private List<ElectrombileShop.carShop> ElectrombileShopList;//提交电池的网店
    private PullToRefreshListView refreshListView;
    private View view;
    private GrabListAdapter shopListAdapter;
    private String ems_id;
    private int prdstatus;
    private ElectrombileShop electrombileShop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_grab_list, null);
        initRefreshListView(inflater);
        ButterKnife.bind(this, view);
        return view;
    }






    private void initRefreshListView(LayoutInflater inflater) {

        refreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_carshop);
        shopListAdapter = new GrabListAdapter(context);
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);//两边都可以拉
        //1.设置刷新监听
        refreshListView.setOnRefreshListener(this);
        refreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中！");
        refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
//        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
//                final String gid = ElectrombileShopList.get(position - 1).getId();
//                HashMap<String, String> dictParam = new HashMap<String, String>();
//                dictParam.put("gid", gid);
//                dictParam.put("ems_gid", ems_id);
//                dictParam.put("access_token", NetUtils.getEncode(token));
//                RequestParams params = new RequestParams();
//                params.add("gid", gid);
//                params.add("ems_gid", ems_id);
//                params.add("access_token", token);
//                LogUtil.e("params", params.toString());
//                netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.show_grablist, params, dictParam, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//
//                        try {
//                            if(response.has("data")){
//                                String s = response.getString("data");
//                                response = AES.desEncrypt(s);
//                            }else{
//                                response = response.getJSONObject("result");
//                            }
//                            if("40013".equals(response.getString("status"))){
//                                //activity跳转
//                                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                startActivity(intent);
//                            }else if ("1001".equals(response.getString("status"))) {
//                                LogUtil.e("click_catshop", response.toString());
//                                Intent intent = new Intent(context, RecoverListActivity.class);
//                                ElectrombileShop.carShop carShop = ElectrombileShopList.get(position - 1);
//                                carShop.getId();
//                                intent.putExtra("carShop", ElectrombileShopList.get(position - 1));
//                                startActivity(intent);
//
//                            } else {
//
//                                CommonUtil.StartToast(context, response.getString("message"));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        super.onFailure(statusCode, headers, responseString, throwable);
//                        CommonDialog.showInfoDialogFailure(context);
//
//                    }
//                });
//            }
//        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(4, 2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mHandler.sendEmptyMessageDelayed(5, 2000);
    }


    protected void requestData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        ems_id = SharedPreferenceUtil.getString(context, Constant.WL_ID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("ems_id", ems_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("ems_id", ems_id);
        params.add("access_token", token);
        LogUtil.e("mutu", params.toString());
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.show_grablist, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("responsexx", response.toString());
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");

                    }


//                    electrombileShop = JsonUtil.getSingleBean(response.toString(), ElectrombileShop.class);
                    electrombileShop = JsonUtil.getSingleBean(response.toString(), ElectrombileShop.class);
//                    PurchaseBean purchaseBean = JsonUtil.getSingleBean(response.toString(), PurchaseBean.class);
//                    Gson gson= new Gson();
//                    ElectrombileShop electrombileShop = gson.fromJson(response.toString(), ElectrombileShop.class);

                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        ElectrombileShopList = new ArrayList<>();
                        CommonDialog.closeProgressDialog();
                        prdstatus = response.getInt("prdstatus");
                        if (!ElectrombileShopList.isEmpty()) {
                            ElectrombileShopList.clear();
                        }
                        //TODO list判定
//                        ElectrombileShop electrombileShop = JsonUtil.getSingleBean(data.toString(), ElectrombileShop.class);

                        if (electrombileShop!=null){
                            ElectrombileShopList = electrombileShop.getList();
                        }else {
                            JSONArray list = response.getJSONArray("list");
                            int length = list.length();
//                            ElectrombileShopList.add();



                        }
                        switch (prdstatus) {
                            case 1:
                                //TODO 自动进入回收列表
                                mHandler.sendEmptyMessage(1);
                                break;
                            case 0:
                                mHandler.sendEmptyMessage(0);
                                break;
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
                CommonDialog.showInfoDialogFailure(context);
            }
        });
    }



    private boolean mIsVisibleToUser=false;
    private  boolean mIsPause=false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser=isVisibleToUser;
        if (isVisibleToUser) {
            CommonDialog.showProgressDialog(context);
            requestData();
        }
    }




    @Override
    public void onPause() {
        super.onPause();
        mIsPause=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mIsVisibleToUser&&mIsPause){
            requestData();
        }
        mIsPause=false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
