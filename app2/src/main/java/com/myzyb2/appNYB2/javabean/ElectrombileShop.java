package com.myzyb2.appNYB2.javabean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by cuiz on 2015/12/5.
 * 网点信息
 */
public class ElectrombileShop implements Serializable{
    @Override
    public String toString() {
        return "ElectrombileShop{" +
                "prdstatus=" + 0 +
                ", list=" + list +
                '}';
    }

    private int status = 0;
    private String message = "";
    private int prdstatus = 0;
    private List<carShop> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPrdstatus(int prdstatus) {
        this.prdstatus = prdstatus;
    }

    public void setList(List<carShop> list) {
        this.list = list;
    }

    public int getPrdstatus() {
        return prdstatus;
    }

    public List<carShop> getList() {
        return list;
    }

    public static class carShop implements Serializable {
        @Override
        public String toString() {
            return "carShop{" +
                    "uid='" + uid + '\'' +
                    ", id='" + id + '\'' +
                    ", img_url='" + img_url + '\'' +
                    ", phone='" + phone + '\'' +
                    ", area_id='" + area_id + '\'' +
                    ", address='" + address + '\'' +
                    ", countnum=" + countnum +
                    ", uname='" + uname + '\'' +
                    ", gname='" + gname + '\'' +
                    ", countweight=" + countweight +
                    '}';
        }

        private String uid = "";//用户id
        private String id = "";//网点id
        private String img_url = "";
        private String phone = "";
        private String area_id = "";
        private String address = "";
        private int countnum = 0;
        private double area_x = 0.00;
        private double area_y = 0.00;
        private String uname = "";
        private String gname = "";

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private double countweight = 0.00;
        private int status=0;

        public double getArea_x() {
            return area_x;
        }

        public void setArea_x(double area_x) {
            this.area_x = area_x;
        }

        public double getArea_y() {
            return area_y;
        }

        public void setArea_y(double area_y) {
            this.area_y = area_y;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCountnum(int countnum) {
            this.countnum = countnum;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public void setCountweight(double countweight) {
            this.countweight = countweight;
        }

        public String getUid() {
            return uid;
        }

        public String getId() {
            return id;
        }

        public String getImg_url() {
            return img_url;
        }

        public String getPhone() {
            return phone;
        }

        public String getArea_id() {
            return area_id;
        }

        public String getAddress() {
            return address;
        }

        public int getCountnum() {
            return countnum;
        }

        public String getUname() {
            return uname;
        }

        public String getGname() {
            return gname;
        }

        public double getCountweight() {
            return countweight;
        }
    }
}
