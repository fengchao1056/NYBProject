package com.myzyb.appNYB.javabean;

import java.util.List;

/**
 * Created by cuiz on 2016/1/28.
 * 明细列表listview数据
 */
public class PayListBean {


    public List<ListEntity> list;

    @Override
    public String toString() {
        return "PayListBean{" +
                "list=" + list +
                '}';
    }

    public static class ListEntity {

        public String time;
        public String pay_type;

        public String money;


        @Override
        public String toString() {
            return "ListEntity{" +
                    "time='" + time + '\'' +
                    ", pay_type='" + pay_type + '\'' +
                    ", money='" + money + '\'' +
                    '}';
        }
    }

}
