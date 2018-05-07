package com.myzyb.appNYB.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.PayListBean;
import com.myzyb.appNYB.javabean.PayListBean.ListEntity;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 明细列表
 * cuiz
 */

public class SpecificationActivity extends BaseActivity {
    private List<ListEntity> payList = new ArrayList<ListEntity>();
    private Context context;
    private ListView list_sption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specification);
        context = this;
        initTitleBar();
        setTitleBar_left_bn(R.drawable.back_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBar_titletext("明细列表");
        initView();
        getData();


    }


    private void getData() {
        CommonDialog.showProgressDialog(context);
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        RequestParams params = new RequestParams();
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        if (!TextUtils.isEmpty(gid)) {
            dictParam.put("gid", gid);
            dictParam.put("access_token", NetUtils.getEncode(token));
            params.add("gid", gid);
            params.add("access_token", token);
            netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.pay_list, params, dictParam, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        CommonDialog.closeProgressDialog();
                        if (200 == statusCode) {
                            if(response.has("data")){
                                String s = response.getString("data");
                                response = AES.desEncrypt(s);
                            }else{
                                response = response.getJSONObject("result");
                            }
                            if("40013".equals(response.getString("status"))){
                                //activity跳转
                                Intent intent = new Intent(SpecificationActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else if ("1001".equals(response.getString("status"))) {
                                String list = response.getString("list");
                                if (!TextUtils.isEmpty(list) && !list.equals("null") && list.length() != 0) {
                                    PayListBean payListBean = JsonUtil.getSingleBean(response.toString(), PayListBean.class);
                                    payList = payListBean.list;
                                    initData();
                                }
                            }
                        } else {
                            CommonUtil.StartToast(context, "连接失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonDialog.closeProgressDialog();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                    CommonUtil.StartToast(context, "连接失败");
                }
            });
        }

    }

    private void initData() {
        CommonDialog.closeProgressDialog();
        list_sption.setAdapter(new MyAdapter());

    }


    private void initView() {
        list_sption = (ListView) findViewById(R.id.list_sption);


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return payList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final Viewholder viewholder;
            if (view == null) {
                viewholder = new Viewholder();
                view = LayoutInflater.from(SpecificationActivity.this).inflate(R.layout.list_specification, null);
                viewholder.tv_money = (TextView) view.findViewById(R.id.tv_money_sption);
                viewholder.tv_setgandget = (TextView) view.findViewById(R.id.tv_setgandget_sption);
                viewholder.tv_time = (TextView) view.findViewById(R.id.tv_time_sption);

                view.setTag(viewholder);
            } else
                viewholder = (Viewholder) view.getTag();

            viewholder.tv_setgandget.setText(payList.get(position).pay_type);
            viewholder.tv_time.setText(payList.get(position).time);
            viewholder.tv_money.setText(payList.get(position).money);


            return view;
        }
    }

    class Viewholder {
        TextView tv_money;
        TextView tv_setgandget;
        TextView tv_time;


    }
}
