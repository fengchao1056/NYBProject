package com.myzyb.appNYB.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.javabean.ChildItem;
import com.myzyb.appNYB.javabean.ParentItem;
import com.myzyb.appNYB.ui.fragment.WaitGetFragment;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;

import java.util.HashMap;
import java.util.List;

import static com.myzyb.appNYB.util.DoubleUtil.doubleFormat;
import static com.myzyb.appNYB.util.DoubleUtil.doubleXintToDouble;

/**
 * 待取货设配器
 * Created by cuiz on 2015/12/7.
 */
public class NoGetExAdapter extends BaseExpandableListAdapter {

    private Context ct;
    private WaitGetFragment fragment;
    private List<ParentItem> pList;
    private Handler mHandler;
    private HashMap<String, Integer> changeHashMap = new HashMap<>();
    private int num_count;
    private double price_count;
    private double weight_count;
    public static HashMap<Integer, ViewHolder> holderHashMap = new HashMap<>();


    public NoGetExAdapter(Context ct, List<ParentItem> pList, Handler mHandler, WaitGetFragment fragment) {
        this.ct = ct;
        this.pList = pList;
        this.mHandler = mHandler;
        this.fragment = fragment;

    }

    @Override
    public int getGroupCount() {
        return pList != null ? pList.size() : 0;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return pList != null && pList.get(groupPosition).getSon() != null ? pList.get(groupPosition).getSon().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        View view;
        view = convertView;
        // 通过getSystemService方法实例化一个视图的填充器
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.exlist1_parence, null);
            viewHolder.pinpai = (TextView) view.findViewById(R.id.tv_pinpai_plist);

            viewHolder.f_count = (TextView) view.findViewById(R.id.tv_count_plist);
            viewHolder.f_zongjia = (TextView) view.findViewById(R.id.tv_zongjia_plist);
            viewHolder.f_zongl = (TextView) view.findViewById(R.id.tv_zongl_plist);
            viewHolder.danjia = (TextView) view.findViewById(R.id.tv_danjia_plist);
            viewHolder.bt_shanchu = (Button) view.findViewById(R.id.bt_shanchu);
            viewHolder.iv_uporown = (ImageView) view.findViewById(R.id.iv_uporown_plist);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();

        }
        holderHashMap.put(groupPosition, viewHolder);
        viewHolder.bt_shanchu.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                CommonDialog.showInfoDialog(ct, "确定删除该电池", "提示", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_pos:
                                removeParentItem(groupPosition);
                                break;
                        }
                    }
                });

            }
        });
        viewHolder.pinpai.setText(pList.get(groupPosition).getNorms());
        viewHolder.f_count.setText(pList.get(groupPosition).getNums() + "块");
        viewHolder.f_zongjia.setText("¥" + pList.get(groupPosition).getAdmin_price_count());
        viewHolder.danjia.setText("¥" + pList.get(groupPosition).getAdmin_price());
        viewHolder.f_zongl.setText(pList.get(groupPosition).getWeight() + "kg");


        //判断实例可以展开，如果可以则改变右侧的图标
        if (!isExpanded) {
            viewHolder.iv_uporown.setBackgroundResource(R.drawable.down);
        } else {
            viewHolder.iv_uporown.setBackgroundResource(R.drawable.button_up);
        }
        return view;

    }

    private void removeParentItem(int groupPosition) {

        List<ChildItem> son = pList.get(groupPosition).getSon();
        HashMap<Integer, String> integerStringHashMap = new HashMap<>();
        int i = -1;
        for (ChildItem childItem : son) {
            i++;
            String id = childItem.getId();
            integerStringHashMap.put(i, id);
        }
        String s = JsonUtil.parseMapToJson(integerStringHashMap);
        LogUtil.e("s", s);

        Message message = mHandler.obtainMessage();
        message.what = 2;
        message.obj = s;
        mHandler.sendMessage(message);

    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        //填充视图
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exlist1_child, null);
        TextView mingc = (TextView) view.findViewById(R.id.tv_mingc);
        final EditText et_count = (EditText) view.findViewById(R.id.et_count);
        Button iv_add = (Button) view.findViewById(R.id.iv_add);
        Button iv_remove = (Button) view.findViewById(R.id.iv_remove);
        final ChildItem childItem = pList.get(groupPosition).getSon().get(childPosition);
        String sname = childItem.getSname();
        String norms = childItem.getNorms();
        int num = childItem.getNum();
        mingc.setText(sname + norms);
        et_count.setText(num + "");
        et_count.setTag(groupPosition);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addNum = childItem.getNum() + 1;
                childItem.setNum(addNum);
                et_count.setText(addNum + "");
                String id = childItem.getId();
                changeChildIrem(id, addNum);
            }
        });

        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reduceNum = childItem.getNum() - 1;
                String id = childItem.getId();
                if (reduceNum != 0) {
                    childItem.setNum(reduceNum);
                    et_count.setText(reduceNum + "");

                } else {
                    removeDialog(groupPosition, childPosition);
                    LogUtil.e("groupPosition1", groupPosition + "");
                }
                changeChildIrem(id, reduceNum);
            }
        });

        et_count.addTextChangedListener(new TextWatcher() {

            private Integer beforecount;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s)) {
                    beforecount = Integer.valueOf(s.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s)) {
                    if (!"0".equals(s.toString())) {
                        Integer integer = Integer.valueOf(s.toString());
                        initCount(integer - beforecount);
                        LogUtil.e("integer", integer + "||||" + beforecount);
                        pList.get(groupPosition).getSon().get(childPosition).setNum(integer);
                        String id = childItem.getId();
                        changeHashMap.put(id, integer);

                    } else {
                        removeDialog(groupPosition, childPosition);
                    }
                } else {
                    removeDialog(groupPosition, childPosition);
                }

            }

            private void initCount(Integer integer) {
                num_count = fragment.getNum_count();
                price_count = fragment.getPrice_count();
                weight_count = fragment.getWeight_count();
                //单个电池重量，单价，integer增加或减少的电池数量
                double weight = pList.get(groupPosition).getSon().get(childPosition).getWeight();
                double price = pList.get(groupPosition).getAdmin_price();
                double c_weight = doubleXintToDouble(weight, integer);
                double c_price = doubleXintToDouble(price, integer);

                //父订单电池总重量，数量价格
                double weightParent = pList.get(groupPosition).getWeight();
                int nums = pList.get(groupPosition).getNums();
                double admin_price_count = pList.get(groupPosition).getAdmin_price_count();

                //处理父订单总计

                pList.get(groupPosition).setNums(nums + integer);
                pList.get(groupPosition).setAdmin_price_count(doubleFormat(c_price + admin_price_count));
                pList.get(groupPosition).setWeight(doubleFormat(weightParent + c_weight));
                ViewHolder viewHolder = holderHashMap.get(groupPosition);
                viewHolder.f_count.setText(nums + integer + "块");
                viewHolder.f_zongjia.setText("¥" + doubleFormat(c_price + admin_price_count));
                viewHolder.f_zongl.setText(doubleFormat(weightParent + c_weight) + "kg");

                // 所有电池数量，价格，重量
                fragment.setPrice_count((num_count + integer), doubleFormat(price_count + c_price), doubleFormat(c_weight + weight_count));


            }
        });

        return view;


    }

    class ViewHolder {
        TextView f_count;
        TextView f_zongjia;
        TextView f_zongl;
        TextView pinpai;
        TextView danjia;
        Button bt_shanchu;
        ImageView iv_uporown;

    }


    private void removeDialog(final int gPosition, final int cPosition) {
        CommonDialog.showInfoDialog(ct, "确定删除该电池", "提示", "取消", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_pos:
                        removeChildItem(gPosition, cPosition);
                        break;
                }
            }
        });

    }

    private void changeChildIrem(String id, int reduceNum) {

        if (reduceNum != 0) {
            changeHashMap.put(id, reduceNum);
            fragment.getMap(changeHashMap);
        } else {
            if (changeHashMap.containsKey(id)) {
                changeHashMap.remove(id);
                fragment.getMap(changeHashMap);
            }
        }
    }


    private void removeChildItem(int groupPosition, int childPosition) {
        ChildItem childItem1 = pList.get(groupPosition).getSon().get(childPosition);
        String id = childItem1.getId();
        HashMap<Integer, String> integerStringHashMap = new HashMap<>();
        integerStringHashMap.put(1, id);
        String s = JsonUtil.parseMapToJson(integerStringHashMap);
        pList.get(groupPosition).getSon().remove(childPosition);
        Message message = mHandler.obtainMessage(3, s);
        mHandler.sendMessage(message);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
