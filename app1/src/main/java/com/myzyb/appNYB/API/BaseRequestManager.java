package com.myzyb.appNYB.API;

/**
 * Created by xialv on 2018/2/3.
 */

public class BaseRequestManager {
    public BaseRequest BaseReq;
    private static  BaseRequestManager requestManager =null;
    public static BaseRequestManager shareInstance(){

        if (requestManager ==null){
            requestManager = new BaseRequestManager();
        }
        return requestManager;
    }
    public BaseRequest BuilderBaseRequest(BaseRequest Req){
        BaseReq =Req;
        return BaseReq;
    }
}
