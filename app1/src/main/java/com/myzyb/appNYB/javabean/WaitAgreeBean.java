package com.myzyb.appNYB.javabean;


import java.util.List;

/**
 * Created by cuiz on 2015/12/7.
 */
public class WaitAgreeBean {

    public String message;
    public int status;
    public String prdstatus;
    public int num_count;
    public double weight_count;
    public double admin_price_count;
    public List<GroupEntity> list;

    @Override

    public String toString() {
        return "WaitAgreeBean{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", prdstatus='" + prdstatus + '\'' +
                ", num_count=" + num_count +
                ", weight_count=" + weight_count +
                ", admin_price_count=" + admin_price_count +
                ", list=" + list +
                '}';
    }

    public static class GroupEntity {
        public String norms;
        public String nums;
        public double weight;
        public double admin_price;
        public double admin_price_count;
        public String price;
        public int price_count;
        public List<SonEntity> son;

        @Override
        public String toString() {
            return "GroupEntity{" +
                    "norms='" + norms + '\'' +
                    ", nums='" + nums + '\'' +
                    ", weight=" + weight +
                    ", admin_price=" + admin_price +
                    ", admin_price_count=" + admin_price_count +
                    ", price='" + price + '\'' +
                    ", price_count=" + price_count +
                    ", son=" + son +
                    '}';
        }

        public static class SonEntity {
            @Override
            public String toString() {
                return "SonEntity{" +
                        "id='" + id + '\'' +
                        ", prd_id='" + prd_id + '\'' +
                        ", num='" + num + '\'' +
                        ", uid='" + uid + '\'' +
                        ", gid='" + gid + '\'' +
                        ", status='" + status + '\'' +
                        ", ems_id='" + ems_id + '\'' +
                        ", sname='" + sname + '\'' +
                        ", norms='" + norms + '\'' +
                        ", weight='" + weight + '\'' +
                        '}';
            }

            public String id;
            public String prd_id;
            public String num;
            public String uid;
            public String gid;
            public String status;
            public String ems_id;
            public String sname;
            public String norms;
            public String weight;






        }
    }
}
