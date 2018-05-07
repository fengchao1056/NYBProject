package com.myzyb2.appNYB2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.javabean.ChildItem;
import com.myzyb2.appNYB2.javabean.ParentItem;

import java.util.List;

/**
 * Created by cuiz on 2015/12/7.
 */
public class WaitAgreeExAdapter extends BaseExpandableListAdapter {

    private Context ct;
    private ImageView iv_uporown;
    private List<ParentItem> pList;
    private int status;
    public WaitAgreeExAdapter(Context ct, List<ParentItem> pList) {

        this.ct = ct;
        this.pList = pList;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public int getGroupCount() {
        return pList.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return pList.get(groupPosition).getSon().size();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        // 通过getSystemService方法实例化一个视图的填充器
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exlist2_parence, null);
        TextView pinpai = (TextView) view.findViewById(R.id.tv_pinpai_p2list);
        TextView count = (TextView) view.findViewById(R.id.tv_count_p2list);
        TextView yugudanjia = (TextView) view.findViewById(R.id.tv_yugudanjia_p2list);
        TextView zongl = (TextView) view.findViewById(R.id.tv_zhongl_p2list);
        TextView yuguzongjia = (TextView) view.findViewById(R.id.tv_yuguzongjia_p2list);
        TextView tv_xzdj_p2list = (TextView) view.findViewById(R.id.tv_xzdj_p2list);
        TextView tv_xzzh_p2list = (TextView) view.findViewById(R.id.tv_xzzh_p2list);
        iv_uporown = (ImageView) view.findViewById(R.id.iv_uporown_p2list);
        TextView tv_state_p2list = (TextView) view.findViewById(R.id.tv_state_p2list);
        pinpai.setText(pList.get(groupPosition).getNorms());
        count.setText(pList.get(groupPosition).getNums() + "块");
        zongl.setText(pList.get(groupPosition).getWeight() + "kg");
        yugudanjia.setText(pList.get(groupPosition).getAdmin_price() + "元/块");
        yuguzongjia.setText(pList.get(groupPosition).getAdmin_price_count() + "");
        tv_xzdj_p2list.setText(pList.get(groupPosition).getPrice() + "元/块");
        tv_xzzh_p2list.setText(pList.get(groupPosition).getPrice_count() + "");


        //判断实例可以展开，如果可以则改变右侧的图标
        if (!isExpanded) {
            iv_uporown.setBackgroundResource(R.drawable.down);
        } else {
            iv_uporown.setBackgroundResource(R.drawable.button_up);
        }

        switch (status) {
            case 1:
                tv_state_p2list.setText("待确认");
                break;
            case 2:
                tv_state_p2list.setText("待付款");
                break;
            default:
                tv_state_p2list.setText("待确认");
                break;
        }


        return view;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        //填充视图
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exlist2_child, null);
        TextView mingc = (TextView) view.findViewById(R.id.tv_pinpai_child2);
        TextView tvnum = (TextView) view.findViewById(R.id.tv_num_child2);
        ChildItem childItem = pList.get(groupPosition).getSon().get(childPosition);
        String sname = childItem.getSname();
        String norms = childItem.getNorms();
        int num = childItem.getNum();
        mingc.setText(sname + norms);
        tvnum.setText(num + "");
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
