package com.myzyb.appNYB.util;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.Volume;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by cuiz on 2015/12/27.
 */
public class loadJson {
    private Context context;
    private RequestParams params;
    List<Volume> listVolume;
    public loadJson(Context context,RequestParams params) {
       this.context =context;
       this.params =params; }

    public List<Volume> getData() {
        //获取容量
//        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.BATTERYVOLUME, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                try {
//                    listVolume = new ArrayList<Volume>();
//                    LogUtil.e("capacitycode", response.toString());
//                    JSONArray jsonVOLUME = response.getJSONArray("list");
//                    LogUtil.e("jsonVOLUME", jsonVOLUME.toString());
//                    for (int i = 0; i < jsonVOLUME.length(); i++) {
//                        JSONObject o = (JSONObject) jsonVOLUME.get(i);
//                        LogUtil.e("o", o.toString());
//                        Volume volume = new Volume();
//                        volume.setId(o.getString("id"));
//                        volume.setName(o.getString("capacity"));
//                        LogUtil.e("vo", o.getString("id") + o.getString("capacity"));
//                        listVolume.add(volume);
//
//
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        });

        return listVolume;
    }




}
