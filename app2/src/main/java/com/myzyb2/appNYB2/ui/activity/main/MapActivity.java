package com.myzyb2.appNYB2.ui.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.ElectrombileShop;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.ToastUtil;
import com.myzyb2.appNYB2.util.ViewToBitmap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by panjichang on 17/12/27.
 */
public class MapActivity extends FragmentActivity implements BaiduMap.OnMarkerClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getgoods);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        txt_title.setText("地图");
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setImageResource(R.drawable.back_button);
        initMap();
        requestData();
    }


    @Bind(R.id.txt_title)
    TextView txt_title;

    @Bind(R.id.imgbtn_left)
    ImageButton imgbtn_left;

    @OnClick(R.id.imgbtn_left)
    public void goBack(){
        finish();
    }

    @Bind(R.id.map_view)
    TextureMapView mMapView;

    private List<ElectrombileShop.carShop> mElectrombileShopList=new ArrayList<>();
    //地图
    BaiduMap mBaiduMap;


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private void startLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();

        option.setCoorType("bd09ll");

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位


        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMarkerClickListener(this);
        startLocation();
    }

    //显示覆盖物
    private void showMarker(List<ElectrombileShop.carShop> list,int status) {
        for(ElectrombileShop.carShop mcarShop:list){
            if(!mElectrombileShopList.contains(mcarShop)){
                mElectrombileShopList.add(mcarShop);
            }
            //定义Maker坐标点
            LatLng point = new LatLng(mcarShop.getArea_x(), mcarShop.getArea_y());
            if(status!=-1){
                mcarShop.setStatus(status);
            }
            Bitmap tempBitmap = ViewToBitmap.ViewToBitmap(this, R.layout.view_marker_drawable,mcarShop.getGname() , mcarShop.getCountweight() + "kg",mcarShop.getStatus());

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(tempBitmap);
            Bundle mBundle=new Bundle();
            mBundle.putSerializable("mcarShop",mcarShop);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap).extraInfo(mBundle);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }

    private void grab(final ElectrombileShop.carShop mCarShop){
        {
            String ems_id = SharedPreferenceUtil.getString(this, Constant.WL_ID, "");
            String token = SharedPreferenceUtil.getString(this, Constant.TOKEN, "");
            HashMap<String, String> dictParam = new HashMap<String, String>();
            dictParam.put("gid",mCarShop.getId());
            dictParam.put("ems_id",ems_id);
            dictParam.put("access_token", NetUtils.getEncode(token));
            RequestParams params = new RequestParams();
            params.add("gid",mCarShop.getId());
            params.add("ems_id", ems_id);
            params.add("access_token", token);
            LogUtil.e("params", params.toString());
            NetUtils.newInstance().putReturnJson(this, NetUtils.POST, UrlConstant.click_grab, params, dictParam, new JsonHttpResponseHandler() {
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
                            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else if("1001".equals(response.getString("status"))){
                            ToastUtil.showToast(response.getString("message"));
                            mCarShop.setStatus(1);
                            mBaiduMap.clear();
                            showMarker(mElectrombileShopList,-1);
                        }else {
                            CommonUtil.StartToast(MapActivity.this, response.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    CommonDialog.showInfoDialogFailure(MapActivity.this);

                }
            });
        }
    }

    private void goRecoverList(final ElectrombileShop.carShop mCarShop){
            String ems_id = SharedPreferenceUtil.getString(this, Constant.WL_ID, "");
            String token = SharedPreferenceUtil.getString(MapActivity.this, Constant.TOKEN, "");
            final String gid = mCarShop.getId();
            HashMap<String, String> dictParam = new HashMap<String, String>();
            dictParam.put("gid", gid);
            dictParam.put("ems_gid", ems_id);
            dictParam.put("access_token", NetUtils.getEncode(token));
            RequestParams params = new RequestParams();
            params.add("gid", gid);
            params.add("ems_gid", ems_id);
            params.add("access_token", token);
            LogUtil.e("params", params.toString());
            NetUtils.newInstance().putReturnJson(MapActivity.this, NetUtils.POST, UrlConstant.click_catshop, params, dictParam, new JsonHttpResponseHandler() {
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
                            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else if ("1001".equals(response.getString("status"))) {
                            LogUtil.e("click_catshop", response.toString());
                            Intent intent = new Intent(MapActivity.this, RecoverListActivity.class);
                            ElectrombileShop.carShop carShop = mCarShop;
                            intent.putExtra("carShop", carShop);
                            startActivity(intent);
                        } else {
                            CommonUtil.StartToast(MapActivity.this, response.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    CommonDialog.showInfoDialogFailure(MapActivity.this);

                }
            });
    }



    private void requestCollect(){
        String token = SharedPreferenceUtil.getString(this, Constant.TOKEN, "");
        String ems_id = SharedPreferenceUtil.getString(this, Constant.WL_ID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("ems_id", ems_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("ems_id", ems_id);
        params.add("access_token", token);
        LogUtil.e("mutu", params.toString());
        NetUtils.newInstance().putReturnJson(this, NetUtils.POST, UrlConstant.show_catshop, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("responsexx", response.toString());
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    ElectrombileShop electrombileShop = JsonUtil.getSingleBean(response.toString(), ElectrombileShop.class);
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        CommonDialog.closeProgressDialog();
                        int prdstatus = response.getInt("prdstatus");
                        if(prdstatus==0){
                            if(electrombileShop.getList()!=null&&electrombileShop.getList().size()>0) {
                                showMarker(electrombileShop.getList(),1);
                            }
                        }else if(prdstatus==1) {
                            isRequestData=true;
                            Intent intent = new Intent(MapActivity.this, RecoverListActivity.class);
                            intent.putExtra("carShop", electrombileShop.getList().get(0));
                            startActivity(intent);
                        }
                    } else {
                        CommonUtil.StartToast(MapActivity.this, response.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                CommonDialog.showInfoDialogFailure(MapActivity.this);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isRequestData) {
            isRequestData=false;
            mBaiduMap.clear();
            mElectrombileShopList.clear();
            requestData();
        }
    }

    private void requestGrab(){
        String token = SharedPreferenceUtil.getString(this, Constant.TOKEN, "");
        String ems_id = SharedPreferenceUtil.getString(this, Constant.WL_ID, "");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("ems_id", ems_id);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("ems_id", ems_id);
        params.add("access_token", token);
        LogUtil.e("mutu", params.toString());
        NetUtils.newInstance().putReturnJson(this, NetUtils.POST, UrlConstant.show_grablist, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.e("responsexx", response.toString());
                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    ElectrombileShop electrombileShop = JsonUtil.getSingleBean(response.toString(), ElectrombileShop.class);
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        CommonDialog.closeProgressDialog();
                        if(electrombileShop.getList()!=null&&electrombileShop.getList().size()>0) {
                            showMarker(electrombileShop.getList(),0);
                        }
                    } else {
                        CommonUtil.StartToast(MapActivity.this, response.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                CommonDialog.showInfoDialogFailure(MapActivity.this);
            }
        });
    }

    protected void requestData() {
        requestGrab();
        requestCollect();
    }




    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }


    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(ElectrombileShop.carShop mcarShop){
        if(mcarShop!=null){
            for(ElectrombileShop.carShop carShop:mElectrombileShopList){
                if(carShop.getId().equals(mcarShop.getId())){
                    mElectrombileShopList.remove(carShop);
                    mBaiduMap.clear();
                    showMarker(mElectrombileShopList,-1);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ElectrombileShop.carShop mcarShop= (ElectrombileShop.carShop) marker.getExtraInfo().getSerializable("mcarShop");
        if(mcarShop!=null){
            if(mcarShop.getStatus()==0){
                grab(mcarShop);
            }else{
                goRecoverList(mcarShop);
            }
        }
        return false;
    }

    private boolean isRequestData=false;

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            Log.d("onReceiveLocation",location.getLatitude()+":"+location.getLongitude());

            int errorCode = location.getLocType();
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

// 设置定位数据
            mBaiduMap.setMyLocationData(locData);

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfiguration(config);

            LatLng cenpt = new LatLng(location.getLatitude(), location.getLongitude());
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }
    }
}
