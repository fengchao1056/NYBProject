package com.myzyb.appNYB.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.javabean.Child2Item;
import com.myzyb.appNYB.javabean.Parent2Item;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 待确认适配器
 * Created by cuiz on 2015/12/7.
 */
public class WaitAgreeExAdapter extends BaseExpandableListAdapter {

    private Context ct;
    private List<Parent2Item> p2List;
    private int status;

    public WaitAgreeExAdapter(Context ct, List<Parent2Item> pList) {

        this.ct = ct;
        this.p2List = pList;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getGroupCount() {
        return p2List != null ? p2List.size() : 0;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return p2List != null && p2List.get(groupPosition).getSon() != null ? p2List.get(groupPosition).getSon().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return p2List.get(groupPosition);
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
        ViewHolder viewHolder;
        View view = convertView;
        if (convertView == null) {
            // 通过getSystemService方法实例化一个视图的填充器
            LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exlist2_parence, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvCountP2list.setText(p2List.get(groupPosition).getNums() + "块");
        viewHolder.tvPinpaiP2list.setText(p2List.get(groupPosition).getNorms());
        viewHolder.tvZhonglP2list.setText(p2List.get(groupPosition).getWeight() + "kg");
        viewHolder.tvYugudanjiaP2list.setText(p2List.get(groupPosition).getAdmin_price() + "元/块");
        viewHolder.tvYuguzongjiaP2list.setText(p2List.get(groupPosition).getAdmin_price_count() + "元");
        viewHolder.tvXzdjP2list.setText(p2List.get(groupPosition).getPrice() + "元/块");
        viewHolder.tvXzzhP2list.setText(p2List.get(groupPosition).getPrice_count() + "元");
        switch (status) {
            case 0:
                viewHolder.tvStateP2list.setText("待取货");
                break;
            case 1:
                viewHolder.tvStateP2list.setText("待确认");
                break;
            case 2:
                viewHolder.tvStateP2list.setText("待付款");
                break;
            default:
                viewHolder.tvStateP2list.setText("待取货");
                break;
        }
        //判断实例可以展开，如果可以则改变右侧的图标
        if (!isExpanded) {
            viewHolder.ivUporownP2list.setBackgroundResource(R.drawable.down);
        } else {
            viewHolder.ivUporownP2list.setBackgroundResource(R.drawable.button_up);
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
        Child2Item child2Item = p2List.get(groupPosition).getSon().get(childPosition);
        String sname = child2Item.getSname();
        String norms = child2Item.getNorms();
        int num = child2Item.getNum();
        mingc.setText(sname + norms + "");
        tvnum.setText(num + "");
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'exlist2_parence.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_pinpai_p2list)
        TextView tvPinpaiP2list;
        @Bind(R.id.tv_state_p2list)
        TextView tvStateP2list;
        @Bind(R.id.iv_uporown_p2list)
        ImageView ivUporownP2list;
        @Bind(R.id.tv_count_p2list)
        TextView tvCountP2list;
        @Bind(R.id.tv_yugudanjia_p2list)
        TextView tvYugudanjiaP2list;
        @Bind(R.id.tv_xztext_p2list)
        TextView tvXztextP2list;
        @Bind(R.id.tv_xzdj_p2list)
        TextView tvXzdjP2list;
        @Bind(R.id.ll_leftitem_p2list)
        LinearLayout llLeftitemP2list;
        @Bind(R.id.tv_zhongl_p2list)
        TextView tvZhonglP2list;
        @Bind(R.id.tv_tjg_p2list)
        TextView tvTjgP2list;
        @Bind(R.id.tv_yuguzongjia_p2list)
        TextView tvYuguzongjiaP2list;
        @Bind(R.id.tv_xzztext_p2list)
        TextView tvXzztextP2list;
        @Bind(R.id.tv_xzzh_p2list)
        TextView tvXzzhP2list;
        @Bind(R.id.ll_weight)
        LinearLayout llWeight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
