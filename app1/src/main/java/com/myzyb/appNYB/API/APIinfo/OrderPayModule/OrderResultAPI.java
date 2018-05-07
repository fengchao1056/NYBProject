package com.myzyb.appNYB.API.APIinfo.OrderPayModule;

import com.myzyb.appNYB.API.BaseRequest;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.http.UrlConstant;

import java.util.HashMap;

/**
 * Created by xialv on 2018/2/2.
 */

public class OrderResultAPI extends BaseRequest {
    public OrderResultAPI(Object o) {
        super(o);
    }

    @Override
    public RequestMethod RequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    public String RequestBaseURL() {
        return Config.appPublic;
    }

    @Override
    public String requestURL() {
        return UrlConstant.PayResult;
    }


    @Override
    public HashMap requestArgs() {
        OrderResult.ReqInfo args= (OrderResult.ReqInfo) this.requestObject;
        HashMap map = new HashMap();
        map.put("order_no",args.orderNo);
        map.put("come",args.come);
        map.put("sendType",args.sendType);
        map.put("version",args.version);
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
