package com.myzyb.appNYB.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ChooseModelActivity extends BaseActivity {

    private Context mContext = this;
    // List<ChooseModle> listModel = new ArrayList<ChooseModle>();
    public static Map<String, Integer> modelMap = new HashMap<String, Integer>();
    private String[] models;//分类名称数组
    private ListView lv_model_choose;
    private TextView tv_model_choose;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);

        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("选择类型");
        initUI();

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("access_token", token);

        if (CommonUtil.hasNetwork(context)) {
            NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.CHOOSEMODLE,params, dictParam, new JsonHttpResponseHandler() {

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
                            Intent intent = new Intent(ChooseModelActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else if("1001".equals(response.getString("status"))){
                            JSONArray list = response.getJSONArray("list");
                            models = new String[list.length()];
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject model = (JSONObject) list.get(i);
                                int cid = model.getInt("cid");
                                String cname = model.getString("cname");
                                models[i] = cname;
                                modelMap.put(cname, cid);
                            }
                            initData();
                        }else{
                            CommonUtil.StartToast(context, response.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    CommonDialog.showInfoDialog(context, getResources().getString(R.string.net_error));

                }
            });


        } else {
            CommonDialog.showInfoDialog(context, getResources().getString(R.string.net_error));
        }

    }

    private void initData() {

        MyAdapter myAdapter = new MyAdapter(mContext);
        lv_model_choose.setAdapter(myAdapter);
        lv_model_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                goActivityForResult(position);


            }
        });
    }

    //把选中的分类Id传递到下个activity
    private void goActivityForResult(int i) {
        Integer modleID = modelMap.get(models[i]);//分类ID
        Intent intent = new Intent(context, ClickSubmitActivity.class);
        intent.putExtra("modleID", modleID);
        startActivityForResult(intent, 1);
    }

    private void initUI() {
        lv_model_choose = (ListView) findViewById(R.id.lv_model_choose);

    }


    class MyAdapter extends BaseAdapter {

        public MyAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return models.length;
        }


        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(ChooseModelActivity.this, R.layout.listview_choosemodel, null);
            }

            tv_model_choose = (TextView) view.findViewById(R.id.tv_model_choose);

            tv_model_choose.setText(models[position]);

            return view;
        }

    }

}
