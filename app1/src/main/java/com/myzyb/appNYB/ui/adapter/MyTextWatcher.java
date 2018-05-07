package com.myzyb.appNYB.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by cuiz on 2016/2/1.
 */
public class MyTextWatcher implements TextWatcher {

    NoGetExAdapter.ViewHolder viewHolder;
    static int groupPosition;

    //修正单价监听事件
    // static int groupPosition;
    private MyTextWatcher(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public static MyTextWatcher myTextWatcher = new MyTextWatcher(groupPosition);

    public static MyTextWatcher getInstance() {
        return myTextWatcher;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        viewHolder = holderHashMap.get(groupPosition);
//        LogUtil.e("pos", groupPosition + "");
//        String mPrice_count = s.toString();
//        double d_xzdj = Double.parseDouble(mPrice_count);
//        LogUtil.e("d_xzdj", d_xzdj + "");
//
//        double d_xzzj = doubleXintToDouble(Double.parseDouble(mPrice_count), pList.get(groupPosition).nums);
//        viewHolder.xzzj.setText("¥" + d_xzzj);
//        pList.get(groupPosition).price = (d_xzdj);
//        pList.get(groupPosition).price_count = (d_xzzj);
//        String norms = pList.get(groupPosition).norms;
//        priceHashMap.put(norms, Double.parseDouble(mPrice_count));
//        fragment.getpriceHashMap(priceHashMap);
//        fragment.setPrice_count();
//        LogUtil.e("plist", pList.get(groupPosition).price + "");

    }
}



