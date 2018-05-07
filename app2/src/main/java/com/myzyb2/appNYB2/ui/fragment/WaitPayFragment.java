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
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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
import com.myzyb2.appNYB2.javabean.ChildItem;
import com.myzyb2.appNYB2.javabean.ParentItem;
import com.myzyb2.appNYB2.ui.activity.bank.BindCardActivity;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.login.WelcomeActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.adapter.WaitAgreeExAdapter;
import com.myzyb2.appNYB2.ui.view.AlertDialog;
import com.myzyb2.appNYB2.ui.view.PasswordDialog;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class WaitPayFragment extends Fragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private boolean init;
    private Context context;
    private List<ParentItem> pList = new ArrayList<ParentItem>();
    private ExpandableListView exlist2;
    private View view;
    private String id;
    private String uid;
    private String area_id;
    private static double price_count;
    private static double weight_count;
    private static int num_count;
    private TextView tv_shuliang;
    private TextView tv_zhongl;
    private TextView tv_zongjia_plist;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    WaitAgreeExAdapter waitAgreeExAdapter = new WaitAgreeExAdapter(context, pList);
                    exlist2.setAdapter(waitAgreeExAdapter);
                    exlist2.setGroupIndicator(null);
                    waitAgreeExAdapter.setStatus(msg.arg1);
                    initCount();
                    CommonDialog.closeProgressDialog();
                    break;
                case 4:
                    getservciesData();
                    mPullRefreshListView.onRefreshComplete();
                    break;
                case 5:
                    mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
                    mPullRefreshListView.onRefreshComplete();
                    break;

            }
        }
    };
    private PullToRefreshExpandableListView mPullRefreshListView;
    private Button bt_submit;
    private PasswordDialog passwordDialog;
    private String stpasswd;
    private AsyncHttpClient client;

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
        view = inflater.inflate(R.layout.exlist2_pager, container, false);
        initView();
        return view;
    }

    private void initView() {
        tv_shuliang = (TextView) view.findViewById(R.id.tv_shuliang);
        tv_zhongl = (TextView) view.findViewById(R.id.tv_zhongl);
        tv_zongjia_plist = (TextView) view.findViewById(R.id.tv_zongjia_plist);
        bt_submit = (Button) view.findViewById(R.id.bt_submit2);
        mPullRefreshListView = (PullToRefreshExpandableListView) view.findViewById(R.id.exlist1_inpager);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中！");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
        exlist2 = mPullRefreshListView.getRefreshableView();
        client = new AsyncHttpClient();
    }

    private void initCount() {
        bt_submit.setOnClickListener(this);
        tv_shuliang.setText(num_count + "块");
        tv_zhongl.setText(weight_count + "kg");
        tv_zongjia_plist.setText("¥" + price_count);
    }

    private void getservciesData() {
        String loginUid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        id = ((RecoverListActivity) context).id;
        uid = ((RecoverListActivity) context).uid;
        area_id = ((RecoverListActivity) context).area_id;
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", loginUid);
        dictParam.put("login_salt", loginSalt);
        dictParam.put("gid", id);
        dictParam.put("guid", uid);
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.SIGN(params, dictParam);
        params.add("uid", loginUid);
        params.add("login_salt", loginSalt);
        params.add("guid", uid);
        params.add("gid", id);
        params.add("area_id", area_id);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());
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
                                }else if ("1001".equals(response.getString("status"))) {
                                    LogUtil.e("输出", response.toString());
                                    serveData(response);
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
//        response = response.getJSONObject("list");
        if (!response.equals("null") && response.length() != 0) {
            pList.clear();
            int prdstatus = response.getInt("prdstatus");

            price_count = response.getDouble("admin_price_count");
            weight_count = response.getDouble("weight_count");
            num_count = response.getInt("num_count");
            JSONArray jsonArray = response.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                LogUtil.e("json", obj.toString());
                ParentItem parentItem = JsonUtil.getSingleBean(obj.toString(), ParentItem.class);
                LogUtil.e("parentItem", parentItem.toString());
                JSONArray son = obj.getJSONArray("son");
                Type listType = new TypeToken<List<ChildItem>>() {
                }.getType();
                List<ChildItem> childItemcts = (List<ChildItem>) JsonUtil.parseJsonToList(son.toString(), listType);
                parentItem.setSon(childItemcts);
                pList.add(parentItem);
            }
            Message message = new Message();
            message.what = 1;
            message.arg1 = prdstatus;
            if (prdstatus == 2) {
                bt_submit.setEnabled(true);
                mHandler.sendMessage(message);
            }
            if (prdstatus == 1) {
                bt_submit.setEnabled(false);
                mHandler.sendMessage(message);
            }
            if (prdstatus == 0) {
                ((RecoverListActivity) context).ReplaceFragment(0);
            }


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


    private void PayDialog() {
        passwordDialog = new PasswordDialog(context);
        passwordDialog.builder()

                .setTitle("请输入支付密码")
                .setMsg("支付金额为： ¥" + price_count)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stpasswd = passwordDialog.getEtString();
                        if (!TextUtils.isEmpty(stpasswd) && stpasswd.length() > 5) {


                            PayToService();
                            CommonDialog.showProgressDialog(context);


                        } else {
                            CommonUtil.StartToast(context, "请输入6位支付密码");
                        }
                    }
                }).show();
    }

    //验证用户是否有支付密码
    private void IsHavePasswdService() {
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
        params.add("uid", uid);
        params.add("access_token", token);
        client.get(context, Config.BASEURL + UrlConstant.HAS_PAWD, params, new JsonHttpResponseHandler() {

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
                        String list = response.getString("list");
                        switch (list.toString()) {
                            case "0":
                                //无密码
                                new AlertDialog(context).builder()
                                        .setTitle("提示")
                                        .setMsg("您还未绑定银行卡请先绑定" + "\n" + "银行卡,以完成交易..." + "\n" + "")
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                               SharedPreferenceUtil.saveBoolean(context, Constant.GO_MAIN, true);
                                                startActivity(new Intent(getActivity(), BindCardActivity.class));
                                            }
                                        }).show();


                                break;
                            case "1":
                                //有密码
                                SharedPreferenceUtil.saveBoolean(context, Constant.ISHAS_PASSWORD, true);
                                PayDialog();
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


            }
        });
    }


    private void PayToService() {

//        gid	必须	int	网点id
//        uid	必须	int	用户id
//        ems_id	必须	int	物流id
//        ems_uid	必须	int	物流用户uid
//        stpasswd	必须	int	物流的id
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WL_ID, "-1");
        String uid2 = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
        String loginUid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        String pwd1 = NetUtils.Md5(stpasswd);
        String pwd2 = NetUtils.Md5(pwd1);
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("login_salt", loginSalt);
        dictParam.put("uid", loginUid);
        dictParam.put("guid", uid);
        dictParam.put("gid", id);
        dictParam.put("ems_id", gid);
        dictParam.put("ems_uid", uid2);
        dictParam.put("stpasswd", pwd2);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params = NetUtils.SIGN(params, dictParam);
        params.add("login_salt", loginSalt);
        params.add("uid", loginUid);
        params.add("guid", uid);
        params.add("gid", id);
        params.add("ems_id", gid);
        params.add("ems_uid", uid2);
        params.add("stpasswd", pwd2);
        params.add("access_token", token);
        LogUtil.e("params", params.toString());

        client.post(context, Config.BASEURL + UrlConstant.pay_agree, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("responsepay", response.toString());
                CommonDialog.closeProgressDialog();
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
                        }else if ("1001".equals(response.getString("status"))) {
                            CommonUtil.StartToast(context, "支付成功");
                            new AlertDialog(context).builder().
                                    setMsg("本次回收电池" + num_count + "块" + "\n" + "总重量" + weight_count + "kg" + "\n" + "已付款¥" + price_count)
                                    .setTitle("付款成功")
                                    .setNegativeButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            EventBus.getDefault().post(((RecoverListActivity) context).carShop);
                                            ((RecoverListActivity) context).finish();
                                        }
                                    }).show();


                        } else {
                            CommonUtil.StartToast(context, response.get("message").toString());
                        }


                    } catch (Exception e) {
                        CommonDialog.closeProgressDialog();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                CommonDialog.closeProgressDialog();
                CommonDialog.showInfoDialogFailure(context);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit2:
                boolean hasPw = SharedPreferenceUtil.getBoolean(context, Constant.ISHAS_PASSWORD, false);
                if (hasPw) {
                    PayDialog();
                } else {
                    IsHavePasswdService();
                }
                break;

        }


    }
}
