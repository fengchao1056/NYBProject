package com.myzyb.appNYB.javabean;


import java.util.List;

/**
 * Created by cuiz on 2016/1/20.
 */
public class AreeadyGetGood {

    public List<FatherItem> list;

    public static class FatherItem {
        public String id;
        public String order_num;
        public String gid;
        public String ems_id;
        public String ctime;
        public int count_num;
        public double count_weight;
        public double total_price;
        public List<SonItem> children;

        public static class SonItem {
            public String id;
            public String num;
            public String weight;
            public String total_price;
            public String admin_price;
            public String sname;
            public String purpose;
            public String norms;
        }
    }
}
