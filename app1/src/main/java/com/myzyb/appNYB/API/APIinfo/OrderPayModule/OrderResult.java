package com.myzyb.appNYB.API.APIinfo.OrderPayModule;

/**
 * Created by xialv on 2018/2/2.
 */

public class OrderResult {
    public OrderResult.ReqInfo reqInfo;
    public OrderResult.RepInfo repInfo;
    public class ReqInfo{
        public String orderNo;
        public String come;
        public String sendType;
        public String version;
        public String uid;
        public ReqInfo setOrderNo(String orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public ReqInfo setCome(String come){
            this.come = come;
            return this;
        }

        public ReqInfo setSendType(String sendType){
            this.sendType = sendType;
            return this;
        }

        public ReqInfo setVersion(String version){
            this.version = version;
            return this;
        }

        public ReqInfo setUid(String uid){
            this.uid = uid;
            return this;
        }

    }
    public class RepInfo{
        public String orderNoStatue;
        public String detailUrl;

        public RepInfo setOrderNoStatue(String orderNoStatue) {
            this.orderNoStatue = orderNoStatue;
            return this;
        }

        public RepInfo setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
            return this;
        }
    }
    public  ReqInfo builderReq(){
        this.reqInfo = new ReqInfo();
        return this.reqInfo;
    }
    public RepInfo builderRep(){
        this.repInfo = new RepInfo();
        return this.repInfo;
    }
}
