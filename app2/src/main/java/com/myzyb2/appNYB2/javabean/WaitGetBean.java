package com.myzyb2.appNYB2.javabean;

import java.util.List;

/**
 * Created by cuiz on 2016/2/1.
 */
public class WaitGetBean {

    public double admin_price_count = 0.00;
    public String message = "";
    public int num_count = 0;
    public String prdstatus = "";
    public double weight_count = 0.00;
    public int status = 0;

//    @Override
//    public String toString() {
//        return "WaitGetBean{" +
//                "admin_price_count=" + admin_price_count +
//                ", message='" + message + '\'' +
//                ", num_count=" + num_count +
//                ", prdstatus='" + prdstatus + '\'' +
//                ", weight_count=" + weight_count +
//                ", status=" + status +
//                ", list=" + list +
//                '}';
//    }

    /**
     * admin_price_count : 4333.56
     * price : 0
     * weight : 646.8
     * price_count : 0
     * nums : 154
     * norms : 6-DZM-10
     * son : [{"uid":"416","id":"2019","num":"22","prd_id":"1616","weight":"4.2","status":"0","norms":"6-DZM-10","gid":"358","sname":"汇源","ems_id":"327"},{"uid":"416","id":"2020","num":"52","prd_id":"1614","weight":"4.2","status":"0","norms":"6-DZM-10","gid":"358","sname":"超威","ems_id":"327"},{"uid":"416","id":"2021","num":"20","prd_id":"1617","weight":"4.2","status":"0","norms":"6-DZM-10","gid":"358","sname":"天能","ems_id":"327"},{"uid":"416","id":"2022","num":"60","prd_id":"1615","weight":"4.2","status":"0","norms":"6-DZM-10","gid":"358","sname":"金超威","ems_id":"327"}]
     * admin_price : 28.14
     */

    public List<GroupList> list;

    public static class GroupList {
        public double admin_price_count = 0.00;
        public double price = 0.00;
        public double weight = 0.00;
        public double price_count = 0.00;
        public int nums = 0;
        public String norms = "";
        public double admin_price = 0.00;

        @Override
        public String toString() {
            return "GroupList{" +
                    "admin_price_count=" + admin_price_count +
                    ", price=" + price +
                    ", weight=" + weight +
                    ", price_count=" + price_count +
                    ", nums=" + nums +
                    ", norms='" + norms + '\'' +
                    ", admin_price=" + admin_price +
                    ", son=" + son +
                    '}';
        }

        /**
         * uid : 416
         * id : 2019
         * num : 22
         * prd_id : 1616
         * weight : 4.2
         * status : 0
         * norms : 6-DZM-10
         * gid : 358
         * sname : 汇源
         * ems_id : 327
         */

        public List<SonList> son;

        public static class SonList {
            public String uid = "";
            public String id = "";
            public int num = 0;
            public String prd_id = "";
            public double weight = 0.00;
            public String status = "";
            public String norms = "";
            public String gid = "";
            public String sname = "";
            public String ems_id = "";


        }
    }
}
