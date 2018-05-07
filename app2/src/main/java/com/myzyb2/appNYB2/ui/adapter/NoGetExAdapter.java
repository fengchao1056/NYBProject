package com.myzyb2.appNYB2.ui.adapter;

import android.content.Context;
import android.os.Handler;
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

import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.javabean.WaitGetBean.GroupList;
import com.myzyb2.appNYB2.ui.fragment.WaitGetFragment;
import com.myzyb2.appNYB2.ui.view.EdittextDialog;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.DoubleUtil;
import com.myzyb2.appNYB2.util.LogUtil;

import java.util.HashMap;
import java.util.List;

import static com.myzyb2.appNYB2.util.DoubleUtil.doubleFormat;
import static com.myzyb2.appNYB2.util.DoubleUtil.doubleXintToDouble;

/**
 * Created by cuiz on 2015/12/7.
 */
public class NoGetExAdapter extends BaseExpandableListAdapter {

    private Context ct;
    private WaitGetFragment fragment;
    private List<GroupList> pList;
    private Handler mHandler;
    private HashMap<String, Integer> changeHashMap = new HashMap<>();
    private HashMap<Integer, ViewHolder> holderHashMap = new HashMap<>();
    private HashMap<String, Double> priceHashMap = new HashMap<>();
    private EdittextDialog edittextDialog;


    public NoGetExAdapter(Context ct, Handler mHandler, WaitGetFragment fragment) {
        this.ct = ct;
        this.mHandler = mHandler;
        this.fragment = fragment;

    }

    public void setPlist(List<GroupList> pList) {
        this.pList = pList;
    }

    @Override
    public int getGroupCount() {

        return pList != null ? pList.size() : 0;
    }


    @Override
    public int getChildrenCount(int groupPosition) {


        return pList != null && pList.get(groupPosition).son != null ? pList.get(groupPosition).son.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return pList.get(groupPosition).son.get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final int pos = groupPosition;
        ViewHolder viewHolder = new ViewHolder();
        View view = convertView;
        // 通过getSystemService方法实例化一个视图的填充器
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (view == null) {
//            viewHolder = new ViewHolder();
        view = inflater.inflate(R.layout.exlist1_parence, null);
        viewHolder.pinpai = (TextView) view.findViewById(R.id.tv_pinpai_plist);
        viewHolder.f_count = (TextView) view.findViewById(R.id.tv_count_plist);
        viewHolder.f_zongjia = (TextView) view.findViewById(R.id.tv_zongjia_plist);
        viewHolder.f_zongl = (TextView) view.findViewById(R.id.tv_zongl_plist);
        viewHolder.danjia = (TextView) view.findViewById(R.id.tv_danjia_plist);
        viewHolder.xzdj = (TextView) view.findViewById(R.id.tv_xzdj_plist);
        viewHolder.xzzj = (TextView) view.findViewById(R.id.tv_xzzj_plist);
        viewHolder.iv_uporown = (ImageView) view.findViewById(R.id.iv_uporown_plist);
        viewHolder.xzdj.setTag(pos);
        view.setTag(viewHolder);


        holderHashMap.put(pos, viewHolder);
        viewHolder.pinpai.setText(pList.get(pos).norms);
        viewHolder.f_count.setText(pList.get(pos).nums + "块");
        viewHolder.f_zongjia.setText("¥" + pList.get(pos).admin_price_count);
        viewHolder.danjia.setText("¥" + pList.get(pos).admin_price);
        viewHolder.f_zongl.setText(pList.get(pos).weight + "kg");
        viewHolder.xzzj.setText(pList.get(pos).price_count + "元");
        viewHolder.xzdj.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(fragment.start) && !TextUtils.isEmpty(fragment.end)) {
                    edittextDialog = new EdittextDialog(ct);
                    edittextDialog.builder().setTitle("输入修正单价")
                            .setPositiveButton("确定", new PriceEdittextListener(pos))
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                } else {


                }
            }
        });
        viewHolder.xzdj.addTextChangedListener(new MyTextWatcher(pos));
        int tag = (int) viewHolder.xzdj.getTag();
        LogUtil.e("tag", tag + "");
        viewHolder.xzdj.setText(String.valueOf(pList.get(tag).price));


        //判断实例可以展开，如果可以则改变右侧的图标
        if (!isExpanded) {
            viewHolder.iv_uporown.setBackgroundResource(R.drawable.down);
        } else {
            viewHolder.iv_uporown.setBackgroundResource(R.drawable.button_up);
        }

        return view;

    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, final ViewGroup parent) {
        //填充视图
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exlist1_child, null);
        TextView mingc = (TextView) view.findViewById(R.id.tv_mingc_child);
        final EditText et_count = (EditText) view.findViewById(R.id.et_count_child);
        Button iv_add = (Button) view.findViewById(R.id.iv_add_child);
        final Button iv_remove = (Button) view.findViewById(R.id.iv_remove_child);
        final GroupList.SonList sonList = pList.get(groupPosition).son.get(childPosition);
        String sname = sonList.sname;
        String norms = sonList.norms;
        int num = sonList.num;
        mingc.setText(sname + norms);
        et_count.setText(String.valueOf(num));
        et_count.setTag(groupPosition);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addNum = sonList.num + 1;
                sonList.num = addNum;
                et_count.setText(addNum + "");
                String id = sonList.id;
                changeChildIrem(id, addNum);
            }
        });

        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reduceNum = sonList.num - 1;
                String id = sonList.id;
                if (reduceNum != 0) {
                    sonList.num = reduceNum;
                    et_count.setText(reduceNum + "");

                } else {

                    //iv_remove.setEnabled(false);
                }
                changeChildIrem(id, reduceNum);
            }
        });

        et_count.addTextChangedListener(new TextWatcher() {
            int before;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s)) {
                    before = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s) && Integer.valueOf(s.toString()) > 0) {
                    Integer integer = Integer.valueOf(s.toString());
                    pList.get(groupPosition).son.get(childPosition).num = integer;
                    pList.get(groupPosition).nums += (integer - before);
                    initCount();
                    String id = sonList.id;
                    // changeHashMap.put(id, integer);
                    changeChildIrem(id, integer);
                    if (integer > 1) {
                        iv_remove.setEnabled(true);
                    } else {
                        iv_remove.setEnabled(false);
                    }

                } else if (!TextUtils.isEmpty(s) && Integer.valueOf(s.toString()) == 0) {
                    CommonUtil.StartToast(ct, "电池不能少于1块");
                    et_count.setText(pList.get(groupPosition).son.get(childPosition).num + "");
                    //pList.get(groupPosition).son.get(childPosition).num = 1;
                    //iv_remove.setEnabled(false);
                    // String id = sonList.id;
                } else {
                    if (!et_count.isFocusable()) {
                        CommonUtil.StartToast(ct, "电池不能少于1块");
                        et_count.setText(pList.get(groupPosition).son.get(childPosition).num + "");
                    }

                }


            }

            private void initCount() {
                //单个电池重量，单价，
                double weight = pList.get(groupPosition).son.get(childPosition).weight;
                //父订单电池总重量，数量价格
                double admin_price = pList.get(groupPosition).admin_price;

                int nums = pList.get(groupPosition).nums;
                pList.get(groupPosition).admin_price_count = DoubleUtil.doubleXintToDouble(admin_price, nums);
                pList.get(groupPosition).weight = DoubleUtil.doubleXintToDouble(weight, nums);
                ViewHolder viewHolder = holderHashMap.get(groupPosition);
                viewHolder.f_count.setText(nums + "块");
                viewHolder.f_zongjia.setText("¥" + doubleFormat(pList.get(groupPosition).admin_price_count));
                viewHolder.f_zongl.setText(doubleFormat(pList.get(groupPosition).weight) + "kg");
                double price1 = doubleXintToDouble(pList.get(groupPosition).price, pList.get(groupPosition).nums);
                pList.get(groupPosition).price_count = (price1);
                viewHolder.xzzj.setText("¥" + price1);

                fragment.setPrice_count();


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
        ImageView iv_uporown;
        TextView xzdj;
        TextView xzzj;
    }


    private void changeChildIrem(String id, int reduceNum) {

        if (reduceNum != 0) {
            changeHashMap.put(id, reduceNum);
            fragment.getChangeHashMap(changeHashMap);
        }
    }

    //修正单价text监听事件
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class MyTextWatcher implements TextWatcher {

        int groupPosition;

        public MyTextWatcher(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ViewHolder viewHolder = holderHashMap.get(groupPosition);
            LogUtil.e("pos", groupPosition + "");
            String mPrice_count = s.toString();
            double d_xzdj = Double.parseDouble(mPrice_count);
            double d_xzzj = doubleXintToDouble(Double.parseDouble(mPrice_count), pList.get(groupPosition).nums);
            viewHolder.xzzj.setText("¥" + d_xzzj);
            pList.get(groupPosition).price = (d_xzdj);
            pList.get(groupPosition).price_count = (d_xzzj);
            String norms = pList.get(groupPosition).norms;
            //当修正单价不为0时提交电池到map
            if (Double.parseDouble(mPrice_count) != 0) {
                priceHashMap.put(norms, Double.parseDouble(mPrice_count));
                fragment.getpriceHashMap(priceHashMap);
                fragment.setPrice_count();
            }
        }
    }

    //修正单价dialog输入框监听
    private class PriceEdittextListener implements View.OnClickListener {
        int groupPosition;

        public PriceEdittextListener(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void onClick(View v) {
            ViewHolder viewHolder = holderHashMap.get(groupPosition);
            LogUtil.e("viewHolder", holderHashMap.toString());
            String s = edittextDialog.getEtString();
            double admin_price = pList.get(groupPosition).admin_price;

            if (!TextUtils.isEmpty(s)) {
                double minDouble = DoubleUtil.doubleFormat(admin_price * Double.parseDouble(fragment.start));//修正单价最小值
                double maxDouble = DoubleUtil.doubleFormat(admin_price * Double.parseDouble(fragment.end));//修正单价最大值

                if (Double.parseDouble(s) < minDouble || Double.parseDouble(s) > maxDouble) {
                    CommonDialog.showInfoDialog(ct, "此款电池的修正价格范围为" + minDouble + "~" + maxDouble + "元之间");
                    return;
                }
                if (Double.parseDouble(s) == 0) {
                    CommonUtil.StartToast(ct, "修正价不能为0");
                    return;
                }
                if (s.contains(".")) {
                    int i = s.indexOf(".");
                    if (s.length() - i - 1 > 2) {
                        CommonUtil.StartToast(ct, "小数后不能超过两位");
                    } else {
                        viewHolder.xzdj.setText(s);
                        pList.get(groupPosition).price = (Double.parseDouble(s));
                    }

                } else {
                    viewHolder.xzdj.setText(s);
                    pList.get(groupPosition).price = (Double.parseDouble(s));
                }

            } else {
                CommonUtil.StartToast(ct, "修正价不能为空");
            }
        }
    }
}
