package com.myzyb.appNYB.API.APIinfo.OrderPayModule;

import com.myzyb.appNYB.util.JsonUtil;

import java.util.HashMap;

/**
 * Created by xialv on 2018/2/2.
 */

public class OrderBody {
    public ReqInfo reqInfo; //微信订单相应字符串
    public RepInfo repInfo; //微信订单相应字符串
    public AlipayRepInfo PayRepInfo;
    //请求对象


    public static class ReqInfo{
        public String uid;
        public String money;
        public String tradeType;
        public String spbill_create_ip;
        public String Type;

        public ReqInfo setType(String type) {
            Type = type;
            return this;
        }

        public ReqInfo setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public ReqInfo setMoney(String money) {
            this.money = money;
            return this;
        }

        public ReqInfo setTradeType(String tradeType) {
            this.tradeType = tradeType;
            return this;
        }

        public ReqInfo setSpbill_create_ip(String spbill_create_ip) {
            this.spbill_create_ip = spbill_create_ip;
            return this;
        }
    }
    public  RepInfo builderRepInfo(){
        return  new RepInfo();
    }

    public  ReqInfo builderReqInfo(){
        this.reqInfo =new ReqInfo();
        return this.reqInfo ;
    }



    // 微信
    public static class RepInfo{
        public String return_code;
        public String return_msg;
        public String appid;
        public String mch_id;
        public String device_info;
        public String nonce_str;
        public String sign;
        public String result_code;
        public String err_code;
        public String err_code_des;
        public String trade_type;
        public String prepay_id;
        public String order_no;
        public String money;
        public String timeStamp;
    }
    //阿里
    public static class  AlipayRepInfo{
        public String sign;
        public String orderNO;

    }


    public HashMap reqInfoMap(){
        return JsonUtil.ObjectToJson(this.reqInfo);
    }



}
