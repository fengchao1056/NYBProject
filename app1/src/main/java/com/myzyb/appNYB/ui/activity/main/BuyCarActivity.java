package com.myzyb.appNYB.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.Battery;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.util.ClickUtil;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.DoubleUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 购物车
 * cuiz
 */

public class BuyCarActivity extends BaseActivity implements View.OnClickListener, Serializable {

    private ListView lsv_buycar;
    private LinearLayout ll_blowcar;
    private RelativeLayout exit_layout;
    private Button bt_submit;
    private Context context;
    private Map<String, Integer> map = new HashMap<>();
    private String jsonID;
    private ViewGroup anim_mask_layout;//动画层
    private TextView tv_time;
    private MyAdapter myAdapter;
    private ImageView iv_car_buycar;
    private TextView tv_count_buycar;//计数
    private int integral;//总积分
    private double totalPrice;//总价格
    private int mSum = 0;
    private int c = 0;
    private TextView tv_zongjia;
    private TextView tv_jifen;
    private List<Battery> list;
    Handler mHandler = new Handler() {
        //用于处理消息的函数，从消息队列中取值执行，一个消息执行一次吧
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    c++;

                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_car);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        context = this;
        initView();
        initData();

    }

    private void initView() {
        bt_submit = (Button) findViewById(R.id.bt_submit_buycar);
        tv_count_buycar = (TextView) findViewById(R.id.tv_count_buycar);
        iv_car_buycar = (ImageView) findViewById(R.id.iv_car_buycar);
        ll_blowcar = (LinearLayout) findViewById(R.id.ll_blowcar);
        exit_layout = (RelativeLayout) findViewById(R.id.exit_layout);
        lsv_buycar = (ListView) findViewById(R.id.lsv_buycar);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_zongjia = (TextView) findViewById(R.id.tv_zongjia);
        tv_jifen = (TextView) findViewById(R.id.tv_jifen);
        exit_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });

    }

    @Override

    public void finish() {

        setResult(Constant.back_ClickSb);

        super.finish();

    }

    private void initData() {
        list = CommApplication.listBattery2;
        myAdapter = new MyAdapter();
        lsv_buycar.setAdapter(myAdapter);
        String stringDate = CommonUtil.getStringDate();
        tv_time.setText("订单时间：" + stringDate);
        initPrice();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit_buycar:
                if (!ClickUtil.isFastClick()) {
                    submitData();
                }
                break;
            case R.id.bt_clear:

                clearBuycar();

                break;
        }
    }

    private void clearBuycar() {

        CommonDialog.showInfoDialog(context, "确认清空回收车？", "提示", "取消", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_pos:
                        CommApplication.listBattery2.clear();
                        initPrice();
                        myAdapter.notifyDataSetInvalidated();
                        bt_submit.setEnabled(false);
                        break;
                }
            }


        });
    }


    class MyAdapter extends BaseAdapter {
        private MyTextWatcher myTextWatcher;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position) != null ? list.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            final Viewholder viewholder;
            View view;
            view = convertView;
            if (view != null) {
                viewholder = (Viewholder) view.getTag();
                viewholder.et_count.setTag(position);
            } else {
                viewholder = new Viewholder();
                view = LayoutInflater.from(BuyCarActivity.this).inflate(R.layout.list_item_buycar, null);
                viewholder.pinpai = (TextView) view.findViewById(R.id.tv_pinpai_buycar);
                viewholder.xinghao = (TextView) view.findViewById(R.id.tv_xinghao_buycar);
                viewholder.jiage = (TextView) view.findViewById(R.id.tv_jiage_buycar);
                viewholder.zhongnum = (TextView) view.findViewById(R.id.tv_zhongnum_buycar);
                viewholder.et_count = (EditText) view.findViewById(R.id.et_count);
                viewholder.iv_add = (Button) view.findViewById(R.id.iv_add);
                viewholder.iv_remove = (Button) view.findViewById(R.id.iv_remove);
                viewholder.et_count.setTag(position);
                view.setTag(viewholder);

            }
            int tag = (int) viewholder.et_count.getTag();
            viewholder.et_count.setText(list.get(tag).getCount() + "");
            viewholder.pinpai.setText(list.get(position).getSname());
            viewholder.xinghao.setText(list.get(position).getNorms());
            String s = DoubleUtil.doubleXint(list.get(position).getPrice(), list.get(position).getCount());
            String s1 = DoubleUtil.doubleXint(list.get(position).getWeight(), list.get(position).getCount());
            viewholder.jiage.setText("¥" + s);
            viewholder.zhongnum.setText(s1 + "kg");


            viewholder.iv_add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int count = list.get(position).getCount() + 1;
                    list.get(position).setCount(count);
                    viewholder.et_count.setText(list.get(position).getCount() + "");
                    int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                    v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                    ImageView ball = new ImageView(context);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                    ball.setImageResource(R.drawable.ball);// 设置buyImg的图片
                    setAnim(ball, startLocation);//开始执行动画

                }
            });
            viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // int position = (Integer) viewholder.et_count.getTag();
                    if (list.size() != 0) {
                        int count = list.get(position).getCount() - 1;
                        if (count > 0) {
                            list.get(position).setCount(count);
                            viewholder.et_count.setText(list.get(position).getCount() + "");
                        } else {
                            viewholder.et_count.setText("0");
                            Battery battery = list.get(position);
                            list = RemoveBatteryList(list, battery);
                            notifyDataSetChanged();
                        }

                    } else {
                        notifyDataSetChanged();
                    }
                }
            });

            myTextWatcher = new MyTextWatcher(viewholder);
            viewholder.et_count.removeTextChangedListener(myTextWatcher);
            viewholder.et_count.addTextChangedListener(myTextWatcher);


            return view;
        }


        private class Viewholder {
            TextView pinpai;
            TextView xinghao;
            TextView jiage;
            TextView zhongnum;
            EditText et_count;
            Button iv_add;
            Button iv_remove;
            //MyTextWatcher myTextWatcher;

        }

        class MyTextWatcher implements TextWatcher {
            private Viewholder viewholder;
            int i = 0;

            public MyTextWatcher(Viewholder viewholder) {
                this.viewholder = viewholder;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                i++;


                LogUtil.e("i", i + "");
                String sCount = s.toString().trim();
                int position = (Integer) viewholder.et_count.getTag();
                if (!list.isEmpty()) {
                    if (!TextUtils.isEmpty(sCount)) {
                        int int_count = Integer.valueOf(sCount).intValue();

                        if (int_count > 0) {
                            list.get(position).setCount(int_count);
                            viewholder.iv_remove.setEnabled(true);
                            viewholder.et_count.setTextColor(Color.parseColor("#fd6103"));
                            String s2 = DoubleUtil.doubleXint(list.get(position).getPrice(), list.get(position).getCount());
                            String s3 = DoubleUtil.doubleXint(list.get(position).getWeight(), list.get(position).getCount());
                            viewholder.jiage.setText("¥" + s2);
                            viewholder.zhongnum.setText(s3 + "kg");
                        } else {
//                            Battery battery = list.get(position);
//                            list = RemoveBatteryList(list, battery);
//                            notifyDataSetChanged();
                        }
                    } else {
//                        Battery battery = list.get(position);
//                        list = RemoveBatteryList(list, battery);
//                        notifyDataSetChanged();
                    }

                }
                initPrice();
            }
        }
    }


    private List<Battery> RemoveBatteryList(List<Battery> list, Battery battery) {
        int id1 = battery.getId();
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            if (id1 == id) {
                list.remove(i);
            }
        }
        return list;
    }


    private void submitData() {

        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        for (Battery battery : list) {
            map.put(battery.getId() + "", battery.getCount());
        }
        jsonID = JsonUtil.parseMapToJson(map);
        LogUtil.e("jsonID", jsonID);
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
                        Intent intent = new Intent(BuyCarActivity.this, LoginActivity.class);
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

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
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
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
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
        tv_count_buycar.getLocationInWindow(endLocation);// shopCart是那个购物车

        // 计算位移
        int endX = 0 - startLocation[0] + 85;// 动画位移的X坐标
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
        set.setDuration(600);// 动画的执行时间
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


    public void initPrice() {
        mSum = 0;
        integral = 0;
        totalPrice = 0;
        if (list.size() != 0) {
            for (Battery battery : list) {
                mSum += battery.getCount();
                totalPrice += battery.getPrice() * battery.getCount();
                integral += battery.getWeight() * battery.getCount();
                // new java.text.DecimalFormat("#.00").format(totalPrice);
                tv_count_buycar.setVisibility(View.VISIBLE);
                tv_count_buycar.setText(mSum + "");
                tv_jifen.setText(integral * 10 + "");
                DecimalFormat df = new DecimalFormat("#####0.00");
                String format = df.format(totalPrice);
                tv_zongjia.setText("¥" + format);
            }

            bt_submit.setEnabled(true);
        } else {
            bt_submit.setEnabled(false);
            tv_count_buycar.setVisibility(View.GONE);
            tv_zongjia.setText("");
            tv_jifen.setText("");
        }
    }

    public void onBackPressed() {
        finish();
        return;
    }

    private void goActivity() {

        CommApplication.listBattery2.clear();
        ClickSubmitActivity.instance.finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", 1);
        startActivityForResult(intent, Constant.JUMP_LIST);
        finish();
    }

}
