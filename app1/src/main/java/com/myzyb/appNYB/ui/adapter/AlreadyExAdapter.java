package com.myzyb.appNYB.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.javabean.AlreadyGetGood;

import java.util.List;

/**
 * 已取货适配器
 * Created by cuiz on 2015/12/7.
 */
public class AlreadyExAdapter extends BaseExpandableListAdapter {

    private Context ct;
    private ImageView iv_uporown;
    private List<AlreadyGetGood> sList;

    public AlreadyExAdapter(Context ct, List<AlreadyGetGood> sList) {
        this.ct = ct;
        this.sList = sList;
    }

    public void setList(List<AlreadyGetGood> sList) {

        this.sList = sList;

    }

    @Override
    public int getGroupCount() {
        return sList != null ? sList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sList != null && sList.get(groupPosition).getChildren() != null ? sList.get(groupPosition).getChildren().size() + 1 : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return sList.get(groupPosition);
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
        View view = inflater.inflate(R.layout.exlist3_first, null);
        TextView goodnum = (TextView) view.findViewById(R.id.tv_goodnum_flist);
        TextView time = (TextView) view.findViewById(R.id.tv_time_flist);
        iv_uporown = (ImageView) view.findViewById(R.id.iv_uporown_flist);
        goodnum.setText(sList.get(groupPosition).getOrder_num());
        String ctime = sList.get(groupPosition).getCtime();
        time.setText(ctime);

        //判断实例可以展开，如果可以则改变右侧的图标
        if (isExpanded) {
            iv_uporown.setBackgroundResource(R.drawable.down);
        } else {
            iv_uporown.setBackgroundResource(R.drawable.button_up);
        }
        return view;

    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (childPosition == 0) return 2;
        return 1;
    }

    @Override
    public int getChildTypeCount() {

        return 3;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        View view = null;
        //填充视图
        int type = getChildType(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (type == 1) {
            view = inflater.inflate(R.layout.exlist3_scend, null);
            TextView zhongnum = (TextView) view.findViewById(R.id.tv_num_slist);
            TextView cjiaojia = (TextView) view.findViewById(R.id.tv_cjj_slist);
            TextView yugujia = (TextView) view.findViewById(R.id.tv_ygj_slist);
            TextView zhongl = (TextView) view.findViewById(R.id.tv_zl_slist);
            TextView mingc = (TextView) view.findViewById(R.id.tv_pinpai_slist);
            String norms = sList.get(groupPosition).getChildren().get(childPosition - 1).getNorms();
            String sname = sList.get(groupPosition).getChildren().get(childPosition - 1).getSname();
            mingc.setText(sname + norms);
            zhongl.setText("重量:" + sList.get(groupPosition).getChildren().get(childPosition - 1).getWeight() + "kg");
            yugujia.setText("预估价：¥" + sList.get(groupPosition).getChildren().get(childPosition - 1).getAdmin_price());
            cjiaojia.setText("成交价：¥" + sList.get(groupPosition).getChildren().get(childPosition - 1).getTotal_price());
            zhongnum.setText("数量:" + sList.get(groupPosition).getChildren().get(childPosition - 1).getNum() + "块");
        }

        if (type == 2) {
            view = inflater.inflate(R.layout.exlist3__lastitem, null);
            TextView zonglzd = (TextView) view.findViewById(R.id.tv_zlnum_last);
            TextView zjiage = (TextView) view.findViewById(R.id.tv_price_last);
            TextView zonum = (TextView) view.findViewById(R.id.tv_countnum_last);
            TextView jifen = (TextView) view.findViewById(R.id.tv_numjifen_last);
            zonglzd.setText(sList.get(groupPosition).getCount_weight() + "kg");
            zonum.setText(sList.get(groupPosition).getCount_num() + "块");
            zjiage.setText("¥" + sList.get(groupPosition).getTotal_price());
            String count_weight = sList.get(groupPosition).getCount_weight();
            double v = Double.parseDouble(count_weight) * 10;
            jifen.setText(String.valueOf((int) v));


        }


        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
