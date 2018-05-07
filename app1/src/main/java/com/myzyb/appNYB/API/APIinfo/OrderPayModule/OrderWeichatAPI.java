package com.myzyb.appNYB.API.APIinfo.OrderPayModule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.myzyb.appNYB.API.BaseRequest;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.util.JsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Created by xialv on 2018/2/2.
 */

public class OrderWeichatAPI extends BaseRequest {



    public OrderWeichatAPI(Object o) {
        super(o);
    }



    @Override
    public RequestMethod RequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    public String RequestBaseURL() {
        return super.RequestBaseURL();
    }

    @Override
    public String requestURL() {
        return UrlConstant.WeChat;
    }


    @Override
    public HashMap requestArgs() {
        OrderBody.ReqInfo args= (OrderBody.ReqInfo) this.requestObject;
        HashMap map = new HashMap();
        map.put("money",args.money);
        map.put ("spbill_create_ip",args.spbill_create_ip);
        map.put("uid",args.uid);
        return map;
    }

    @Override
    public void RequestWillStart() {
        super.RequestWillStart();
    }

    @Override
    public void RequestDidend() {
        super.RequestDidend();
    }

}
