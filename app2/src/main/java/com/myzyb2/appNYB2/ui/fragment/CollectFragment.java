package com.myzyb2.appNYB2.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.ElectrombileShop;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.adapter.ShopListAdapter;
import com.myzyb2.appNYB2.ui.view.springview.container.DefaultFooter;
import com.myzyb2.appNYB2.ui.view.springview.container.DefaultHeader;
import com.myzyb2.appNYB2.ui.view.springview.widget.SpringView;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by panjichang on 17/12/25.
 */
public class CollectFragment extends BaseFragment{


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
                    mSpringView.onFinishFreshAndLoad();
                    break;
                case 5:
                    mSpringView.onFinishFreshAndLoad();
                    break;
            }
        }
    };
    private List<ElectrombileShop.carShop> ElectrombileShopList;//提交电池的网店
    private SwipeMenuListView refreshListView;
    private View view;
    private ShopListAdapter shopListAdapter;
    private String ems_id;
    private int prdstatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_goods_list, null);
        ButterKnife.bind(this, view);
        initRefreshListView(inflater);
        return view;
    }



    private SpringView mSpringView;


    private void initRefreshListView(LayoutInflater inflater) {

        refreshListView = (SwipeMenuListView) view.findViewById(R.id.lv_carshop);
        mSpringView = (SpringView) view.findViewById(R.id.mSpringView);
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new DefaultHeader(context));
        mSpringView.setFooter(new DefaultFooter(context));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(4, 2000);
            }
            @Override
            public void onLoadmore() {
                mHandler.sendEmptyMessageDelayed(5, 2000);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        context);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                openItem.setWidth(CommonUtil.dip2px(context,90));
                // set item title
                openItem.setTitle("取消");
                // set item title fontsize
                openItem.setTitleSize(15);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
// set creator
        refreshListView.setMenuCreator(creator);

        refreshListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 取消
                    {
                        String ems_id = SharedPreferenceUtil.getString(context, Constant.WL_ID, "");
                        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
                        HashMap<String, String> dictParam = new HashMap<String, String>();
                        dictParam.put("gid", ElectrombileShopList.get(position).getId());
                        dictParam.put("ems_id",ems_id);
                        dictParam.put("access_token", NetUtils.getEncode(token));
                        RequestParams params = new RequestParams();
                        params.add("gid", ElectrombileShopList.get(position).getId());
                        params.add("ems_id", ems_id);
                        params.add("access_token", token);
                        LogUtil.e("params", params.toString());
                        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.cancle_grab, params, dictParam, new JsonHttpResponseHandler() {
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
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                    }else if("1001".equals(response.getString("status"))){
                                        ToastUtil.showToast(response.getString("message"));
                                        ElectrombileShopList.remove(position);
                                        shopListAdapter.notifyDataSetChanged();
                                    }else {
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
                    break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        shopListAdapter = new ShopListAdapter(context);
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
                final String gid = ElectrombileShopList.get(position).getId();
                HashMap<String, String> dictParam = new HashMap<String, String>();
                dictParam.put("gid", gid);
                dictParam.put("ems_gid", ems_id);
                dictParam.put("access_token", NetUtils.getEncode(token));
                RequestParams params = new RequestParams();
                params.add("gid", gid);
                params.add("ems_gid", ems_id);
                params.add("access_token", token);
                LogUtil.e("params", params.toString());
                netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.click_catshop, params, dictParam, new JsonHttpResponseHandler() {
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
                                LogUtil.e("click_catshop", response.toString());
                                Intent intent = new Intent(context, RecoverListActivity.class);
                                ElectrombileShop.carShop carShop = ElectrombileShopList.get(position);
                                intent.putExtra("carShop",carShop);
                                startActivity(intent);

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
        });
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
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.show_catshop, params, dictParam, new JsonHttpResponseHandler() {
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
                    ElectrombileShop electrombileShop = JsonUtil.getSingleBean(response.toString(), ElectrombileShop.class);
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
                        ElectrombileShopList = electrombileShop.getList();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
