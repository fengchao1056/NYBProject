package com.myzyb.appNYB.ui.activity.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.Battery;
import com.myzyb.appNYB.javabean.Volume;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.adapter.CatograyAdapter;
import com.myzyb.appNYB.ui.adapter.GoodsAdapter;
import com.myzyb.appNYB.util.ClickUtil;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 提交电池
 * cuiz
 */
public class ClickSubmitActivity extends BaseActivity implements Serializable, View.OnClickListener {
    public static ClickSubmitActivity instance = null;
    private ListView listViewVolume, listViewBattery;
    private List<Volume> listVolume = new ArrayList<>();
    private List<Battery> listBattery = new ArrayList<Battery>();
    private Button bt_submit;
    private Map<String, Integer> map = new HashMap<>();
    private String jsonID;
    private CatograyAdapter catograyAdapter;
    private GoodsAdapter goodsAdapter;
    private Context context;
    private ImageView iv_car;//购物车
    private ViewGroup anim_mask_layout;//动画层
    private TextView buyNumView;//购物车上的数量标签
    private int cat_id;
    private String cid;
   // private String Page;
    private int index = 0;
    private int mSum = 0;


    Handler mHandler = new Handler() {
        //用于处理消息的函数，从消息队列中取值执行，一个消息执行一次吧
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    catograyAdapter.setList(listVolume);
                    listViewVolume.setAdapter(catograyAdapter);
                    catograyAdapter.setSelectItem(index);
                    catograyAdapter.notifyDataSetChanged();
                    righrData();
                    break;
                case 2:
                    CommonDialog.closeProgressDialog();
                    goodsAdapter.setListBattery(listBattery);
                    listViewBattery.setAdapter(goodsAdapter);
                    goodsAdapter.notifyDataSetInvalidated();
                    break;
            }

        }
    };
    private TextView tv_zongjiprice;
    private TextView tv_jifen;
    private int integral;//总积分
    private double totalPrice;//总价格


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_submit);
        context = this;
        instance = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("便捷提交");
        initView();
        getData();
        initVolume();

    }

    /**
     * 关联list1，listBattery
     */

    private void openBuyCar() {
        iv_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClickSubmitActivity.this, BuyCarActivity.class);
                //intent.putExtra("list", (Serializable) listBattery2);
                startActivityForResult(intent, Constant.open_buycar);

            }
        });
    }

    /**
     * 初始化list
     */

    private void initVolume() {
        List<Battery> listBattery2 = CommApplication.listBattery2;

        if (listBattery2.size() > 0) {
            iv_car.setClickable(true);
            openBuyCar();
            int count = 0;
            for (Battery battery : listBattery2) {
                count += battery.getCount();
            }
            LogUtil.e("listBat", listBattery2.toString());
            buyNumView.setText(count + "");
            buyNumView.setVisibility(View.VISIBLE);

        }
        catograyAdapter = new CatograyAdapter(context);
        goodsAdapter = new GoodsAdapter(context);
        linkLlist();
    }


    public void TextchangeCallback() {

        mSum = 0;
        integral = 0;
        totalPrice = 0;
        List<Battery> listBattery2 = CommApplication.listBattery2;
        if (listBattery2.size() != 0) {
            for (Battery battery : listBattery2) {
                mSum += battery.getCount();
                totalPrice += battery.getPrice() * battery.getCount();
                integral += battery.getWeight() * battery.getCount();
                buyNumView.setVisibility(View.VISIBLE);
                buyNumView.setText(mSum + "");
                tv_jifen.setText(integral * 10 + "");
                DecimalFormat df = new DecimalFormat("#####0.00");
                String format = df.format(totalPrice);
                tv_zongjiprice.setText("¥" + format);

            }
            openBuyCar();
            bt_submit.setEnabled(true);
        } else {
            iv_car.setClickable(false);
            bt_submit.setEnabled(false);
            buyNumView.setVisibility(View.GONE);
            tv_zongjiprice.setText("");
            tv_jifen.setText("");
        }
    }

    private void linkLlist() {
        listViewVolume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catograyAdapter.setSelectItem(position);
                catograyAdapter.notifyDataSetChanged();
                index = position;
                righrData();
            }
        });
    }


    private void initView() {
        listViewVolume = (ListView) findViewById(R.id.listview_1);
        listViewBattery = (ListView) findViewById(R.id.listview_2);
        iv_car = (ImageView) findViewById(R.id.iv_car_click);
        buyNumView = (TextView) findViewById(R.id.tv_count_click);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        tv_zongjiprice = (TextView) findViewById(R.id.tv_zongjiprice);
        tv_jifen = (TextView) findViewById(R.id.tv_jifen);
        bt_submit.setOnClickListener(this);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v,
                startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        iv_car.getLocationInWindow(endLocation);// shopCart是那个购物车

        // 计算位移
        int endX = 0 - startLocation[0] + 80;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);

            }
        });

    }

    public void getData() {
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        Intent intent = getIntent();
        cat_id = intent.getIntExtra("modleID", 0);
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("cat_id", String.valueOf(cat_id));
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.put("cat_id", cat_id);
        params.add("access_token", token);
        //获取容量
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.BATTERYVOLUME, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    listVolume.clear();
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(ClickSubmitActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if("1001".equals(response.getString("status"))){
                        JSONArray jsonVOLUME = response.getJSONArray("list");
                        //LogUtil.e("jsonVOLUME", jsonVOLUME.toString());
                        for (int i = 0; i < jsonVOLUME.length(); i++) {
                            JSONObject o = (JSONObject) jsonVOLUME.get(i);
                            LogUtil.e("o", o.toString());
                            Volume volume = new Volume();
                            volume.setId(o.getString("id"));
                            volume.setName(o.getString("capacity"));
                            listVolume.add(volume);
                        }
                        mHandler.sendEmptyMessage(1);
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
            }
        });
    }

    private void righrData() {
       /* cid	必须	int	说明（容量id）Page*/
        CommonDialog.showProgressDialog(context);
        cid = listVolume.get(index).getId();
        //Page = "1";
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String area_id = SharedPreferenceUtil.getString(context, Constant.DQID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("cid", cid);
        dictParam.put("area_id", area_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("cid", cid);
        params.add("area_id", area_id);
        params.add("access_token", token);
       // params.add("Page", Page);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.BATTERY, params, dictParam, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        listBattery.clear();
                        LogUtil.e("BATTERY", response.toString());
                        try {
                            if(response.has("data")){
                                String s = response.getString("data");
                                response = AES.desEncrypt(s);
                            }else{
                                response = response.getJSONObject("result");
                            }
                            if("40013".equals(response.getString("status"))){
                                //activity跳转
                                Intent intent = new Intent(ClickSubmitActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else if ("1001".equals(response.getString("status"))) {
                                JSONArray jsonBATTERY = response.getJSONArray("list");
                                // LogUtil.e("jsonVOLUME", jsonBATTERY.toString());
                                for (int i = 0; i < jsonBATTERY.length(); i++) {
                                    JSONObject o = (JSONObject) jsonBATTERY.get(i);
                                    // LogUtil.e("o", o.toString());
                                    Battery battery = new Battery();
                                    battery.setSname(o.getString("sname"));
                                    battery.setPrice(o.getDouble("price"));
                                    battery.setNorms(o.getString("norms"));
                                    battery.setId(o.getInt("id"));
                                    battery.setWeight(o.getDouble("weight"));
                                    listBattery.add(battery);
                                }
                            } else {
                                CommonUtil.StartToast(context, response.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                }

        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                if (!ClickUtil.isFastClick()) {
                    CommonDialog.showProgressDialog(context);
                    submitData();

                }
                break;


        }

    }

    private void submitData() {

        for (Battery battery : CommApplication.listBattery2) {
            map.put(battery.getId() + "", battery.getCount());
        }
        jsonID = JsonUtil.parseMapToJson(map);
        LogUtil.e("jsonID", jsonID);
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("gid", gid);
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("prd", jsonID);
        params.add("gid", gid);
        params.add("uid", uid);
        params.add("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.AddBUYCAR, params, dictParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    CommonDialog.closeProgressDialog();
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(ClickSubmitActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("1001".equals(response.getString("status"))) {
                        goActivity();
                    } else if ("1019".equals(response.getString("status"))) {
                        CommonUtil.StartToast(context, response.getString("message"));
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                    LogUtil.e("submit", response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.back_ClickSb) {
            righrData();
            TextchangeCallback();
        }
    }


    private void goActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", 1);
        startActivityForResult(intent, Constant.JUMP_LIST);
        CommApplication.listBattery2.clear();
        finish();
    }
}

