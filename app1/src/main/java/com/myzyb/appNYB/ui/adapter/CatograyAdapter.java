package com.myzyb.appNYB.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.javabean.Volume;

import java.util.List;
/**
 *展示电池12am列表
 */

public class CatograyAdapter extends BaseAdapter {

    private Context context;
    private List<Volume> list;
    private int selectItem = -1;

    public CatograyAdapter(Context context) {
        this.context = context;

    }

    public void setList(List<Volume> list) {
        this.list = list;
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


    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = LayoutInflater.from(context).inflate(R.layout.left_listview, null);

        tv_catogray = (TextView) view.findViewById(R.id.tv_catogray);


        if (position == selectItem) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.parseColor("#f0f0f0"));
        }

        tv_catogray.setText(list.get(position).getName());

        return view;
    }


    TextView tv_catogray;


}
