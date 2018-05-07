package com.myzyb.appNYB.API.APIinfo.OrderPayModule;

import com.myzyb.appNYB.API.BaseRequest;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.util.JsonUtil;

import java.util.HashMap;

/**
 * Created by xialv on 2018/2/3.
 */

public class OrderAlipayAPi extends BaseRequest {
    public OrderAlipayAPi(Object o) {
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
        return UrlConstant.alipay;
    }


    @Override
    public HashMap requestArgs() {
        OrderBody.ReqInfo args= (OrderBody.ReqInfo) this.requestObject;
        HashMap map = new HashMap();
        map.put("money",args.money);
        map.put("uid",args.uid);
        return  map;


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
