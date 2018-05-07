package com.myzyb2.appNYB2.javabean;


import java.util.List;

/**
 * Created by cuiz on 2016/1/20.
 */
public class AlreadyGetGood {


    /**
     * id : 2131
     * count_weight : 30
     * order_num : 201602249437425
     * gid : 12182
     * children : [{"id":"2134","num":"1","weight":"4.2","norms":"6-DZM-10","purpose":"","admin_price":"28.14","sname":"超威","total_price":"1"},{"id":"2132","num":"1","weight":"7.2","norms":"8-DZM-16","purpose":"","admin_price":"48.24","sname":"超威","total_price":"1"},{"id":"2135","num":"1","weight":"7.2","norms":"8-DZM-16","purpose":"","admin_price":"48.24","sname":"汇源","total_price":"1"},{"id":"2133","num":"1","weight":"7.2","norms":"8-DZM-16","purpose":"","admin_price":"48.24","sname":"金超威","total_price":"1"},{"id":"2136","num":"1","weight":"4.2","norms":"6-DZM-10","purpose":"","admin_price":"28.14","sname":"金超威","total_price":"1"}]
     * ems_id : 12183
     * total_price : 5
     * count_num : 5
     * ctime : 2016-02-24 14:12:54
     */

    private String id = "";
    private String count_weight = "";
    private String order_num = "";
    private String gid = "";
    private String ems_id = "";
    private String total_price = "";
    private String count_num = "";
    private String ctime = "";
    /**
     * id : 2134
     * num : 1
     * weight : 4.2
     * norms : 6-DZM-10
     * purpose :
     * admin_price : 28.14
     * sname : 超威
     * total_price : 1
     */

    private List<ChildrenEntity> children;

    public void setId(String id) {
        this.id = id;
    }

    public void setCount_weight(String count_weight) {
        this.count_weight = count_weight;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setEms_id(String ems_id) {
        this.ems_id = ems_id;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setCount_num(String count_num) {
        this.count_num = count_num;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public void setChildren(List<ChildrenEntity> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public String getCount_weight() {
        return count_weight;
    }

    public String getOrder_num() {
        return order_num;
    }

    public String getGid() {
        return gid;
    }

    public String getEms_id() {
        return ems_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getCount_num() {
        return count_num;
    }

    public String getCtime() {
        return ctime;
    }

    public List<ChildrenEntity> getChildren() {
        return children;
    }

    public static class ChildrenEntity {
        private String id = "";
        private String num = "";
        private String weight = "";
        private String norms = "";
        private String purpose = "";
        private String admin_price = "";
        private String sname = "";
        private String total_price = "";

        public void setId(String id) {
            this.id = id;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public void setNorms(String norms) {
            this.norms = norms;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public void setAdmin_price(String admin_price) {
            this.admin_price = admin_price;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getId() {
            return id;
        }

        public String getNum() {
            return num;
        }

        public String getWeight() {
            return weight;
        }

        public String getNorms() {
            return norms;
        }

        public String getPurpose() {
            return purpose;
        }

        public String getAdmin_price() {
            return admin_price;
        }

        public String getSname() {
            return sname;
        }

        public String getTotal_price() {
            return total_price;
        }

        @Override
        public String toString() {
            return "ChildrenEntity{" +
                    "id='" + id + '\'' +
                    ", num='" + num + '\'' +
                    ", weight='" + weight + '\'' +
                    ", norms='" + norms + '\'' +
                    ", purpose='" + purpose + '\'' +
                    ", admin_price='" + admin_price + '\'' +
                    ", sname='" + sname + '\'' +
                    ", total_price='" + total_price + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AlreadyGetGood{" +
                "id='" + id + '\'' +
                ", count_weight=" + count_weight +
                ", order_num='" + order_num + '\'' +
                ", gid='" + gid + '\'' +
                ", ems_id='" + ems_id + '\'' +
                ", total_price=" + total_price +
                ", count_num=" + count_num +
                ", ctime='" + ctime + '\'' +
                ", children=" + children +
                '}';
    }
}
