package com.myzyb2.appNYB2.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.ElectrombileShop;
import com.myzyb2.appNYB2.javabean.ElectrombileShop.carShop;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.RecoverListActivity;
import com.myzyb2.appNYB2.ui.view.CircleImageView;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.DoubleUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by cuiz on 2016/1/14.
 */
public class GrabListAdapter extends BaseAdapter {
    private List<carShop> list;
    private Context context;

    public GrabListAdapter(Context context) {
        this.context = context;
    }

    public void setLIst(List<carShop> list) {
        this.list = list;
    }

    public List<carShop> getList() {
        return list;
    }

    @Override
    public int getCount() {

        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grab, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.item_carshop, null);

        String gname = list.get(position).getGname();
        String address = list.get(position).getAddress();
        int countnum = list.get(position).getCountnum();
        double countweight = DoubleUtil.doubleFormat(list.get(position).getCountweight());
        viewHolder.tvWeightNum.setText(String.valueOf(countweight));
        viewHolder.tvName.setText(gname);
        viewHolder.rl_grab.setVisibility(View.VISIBLE);
        viewHolder.btn_grab.setBackgroundResource(R.drawable.btn_circle_green);
        viewHolder.btn_grab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    String ems_id = SharedPreferenceUtil.getString(context, Constant.WL_ID, "");
                    String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
                    HashMap<String, String> dictParam = new HashMap<String, String>();
                    dictParam.put("gid", list.get(position).getId());
                    dictParam.put("ems_id",ems_id);
                    dictParam.put("access_token", NetUtils.getEncode(token));
                    RequestParams params = new RequestParams();
                    params.add("gid", list.get(position).getId());
                    params.add("ems_id", ems_id);
                    params.add("access_token", token);
                    LogUtil.e("params", params.toString());
                    NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.click_grab, params, dictParam, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                if(response.has("data")){
                                    String s = response.getString("data");
                                    response = AES.desEncrypt(s);
                                }else{
                                    response = response.getJSONObject("result");
                                }
                                if("40013".equals(response.getString("status"))){
                                    //activity跳转
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                }else if("1001".equals(response.getString("status"))){
                                    ToastUtil.showToast(response.getString("message"));
                                    list.remove(position);
                                    notifyDataSetChanged();
                                }else {
                                    CommonUtil.StartToast(context, response.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            CommonDialog.showInfoDialogFailure(context);

                        }
                    });
                }
            }
        });
        viewHolder.tvAdrees.setText(address);
        viewHolder.tvNum.setText(String.valueOf(countnum));
        Ion.with(context).load(list.get(position).getImg_url()).withBitmap()
                .placeholder(R.drawable.moren_photo)
                .error(R.drawable.moren_photo).intoImageView(viewHolder.ivHeadPhoto);

        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_carshop.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_head_photo)
        CircleImageView ivHeadPhoto;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_weight_num)
        TextView tvWeightNum;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_adrees)
        TextView tvAdrees;
        @Bind(R.id.rl_grab)
        RelativeLayout rl_grab;
        @Bind(R.id.btn_grab)
        Button btn_grab;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
