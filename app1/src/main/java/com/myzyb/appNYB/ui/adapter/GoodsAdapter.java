package com.myzyb.appNYB.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.javabean.Battery;
import com.myzyb.appNYB.ui.activity.main.ClickSubmitActivity;
import com.myzyb.appNYB.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *电池右列表
 */
public class GoodsAdapter extends BaseAdapter {

    private List<Battery> list = new ArrayList<>();
    private Context context;
    private int int_count = 0;
    private int mSum = 0;
    private String sCount;
    private HashMap<Integer, Battery> hashMap = new HashMap<Integer, Battery>();
    private List<Battery> listVelue = new ArrayList<>();

    private Integer index = -1;

    public GoodsAdapter(Context context) {
        this.context = context;
        list.clear();
    }

    public void setListBattery(List<Battery> listBattery) {
        this.list = listBattery;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final int pos = position;
        final Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.right_listview, null);
            viewholder = new Viewholder();
            viewholder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewholder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            viewholder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewholder.iv_add = (Button) view.findViewById(R.id.iv_add);
            viewholder.iv_remove = (Button) view.findViewById(R.id.iv_remove);
            viewholder.et_acount = (EditText) view.findViewById(R.id.et_count);
            view.setTag(viewholder);
            viewholder.et_acount.setTag(position);
            viewholder.et_acount.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = (Integer) v.getTag();
                    }
                    return false;
                }
            });

        } else {
            viewholder = (Viewholder) view.getTag();
            viewholder.et_acount.setTag(position);
            //viewholder.et_acount.setText("");

        }

        viewholder.tv_name.setText(list.get(pos).getSname());
        viewholder.tv_desc.setText(list.get(pos).getNorms());
        viewholder.tv_price.setText(list.get(pos).getPrice() + "");

        int tag = (int) viewholder.et_acount.getTag();
        hasBattery(list, tag);

        LogUtil.e("test", tag + "==" + list.get(tag).getCount());
        if (list.get(tag).getCount() == 0 || "".equals(viewholder.et_acount.getText())) {

            viewholder.et_acount.setText("");
        } else {

            viewholder.et_acount.setText(list.get(tag).getCount() + "");
            viewholder.iv_remove.setEnabled(true);
        }

        //初始电池数量


        viewholder.et_acount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                sCount = s.toString().trim();
                int position = (Integer) viewholder.et_acount.getTag();
                if (!TextUtils.isEmpty(sCount)) {
                    int_count = Integer.valueOf(sCount).intValue();
                    list.get(position).setCount(int_count);
                    if (int_count > 0) {
                        viewholder.iv_remove.setEnabled(true);
                        Battery battery = list.get(position);
                        addBatteryList(CommApplication.listBattery2, battery);
                        viewholder.et_acount.setTextColor(Color.parseColor("#fd6103"));

                    } else {
                        Battery battery = list.get(position);
                        RemoveBatteryList(CommApplication.listBattery2, battery);
                        viewholder.iv_remove.setEnabled(false);
                        viewholder.et_acount.setTextColor(Color.GRAY);
                    }
                    ((ClickSubmitActivity) context).TextchangeCallback();

                } else {
                    viewholder.et_acount.setHint("0");
                    return;
                }

            }
        });
        viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) viewholder.et_acount.getTag();

                int count = list.get(position).getCount() + 1;
                list.get(position).setCount(count);
                LogUtil.e("position", list.get(position).toString());
                viewholder.et_acount.setText(list.get(position).getCount() + "");
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(context);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                ball.setImageResource(R.drawable.ball);// 设置buyImg的图片
                ((ClickSubmitActivity) context).setAnim(ball, startLocation);//开始执行动画
            }
        });

        viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) viewholder.et_acount.getTag();
                int count = list.get(position).getCount();
                count--;
                if (count >= 0) {
                    list.get(position).setCount(count);
                    viewholder.et_acount.setText(list.get(position).getCount() + "");
                }
            }
        });


        return view;
    }

    private void RemoveBatteryList(List<Battery> list, Battery battery) {
        int id1 = battery.getId();
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            if (id1 == id) {
                list.remove(i);
            }
        }
    }

    /**
     * 根据电池ID判断结果电池集合是否有相同电池
     * 把结果电池集合里的电池数量赋予初始电池集合里的相同电池
     */

    private void hasBattery(List<Battery> list, int position) {
        int id = list.get(position).getId();
        List<Battery> listBattery = CommApplication.listBattery2;
        if (!listBattery.isEmpty()) {
            for (int i = 0; i < listBattery.size(); i++) {
                int id2 = listBattery.get(i).getId();
                if (id == id2) {
                    list.get(position).setCount(listBattery.get(i).getCount());
                }
            }
        }
        LogUtil.e("list", list.toString());

    }

    private void addBatteryList(List<Battery> list, Battery battery) {
        int id1 = battery.getId();
        int count = battery.getCount();
        boolean flag = true;

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i).getId();
                if (id1 == (id)) {
                    list.get(i).setCount(count);
                    flag = false;
                }
            }
            if (flag) {
                list.add(battery);
            }
        } else {
            list.add(battery);
        }
    }


    class Viewholder {
        TextView tv_name;
        TextView tv_desc;
        TextView tv_price;
        Button iv_add, iv_remove;
        EditText et_acount;
    }

}
