package com.myzyb2.appNYB2.javabean;

import java.util.List;

public class PurchaseBean {


    /**
     * message : 操作成功
     * status : 1001
     * prdstatus : 0
     * list : [{"area_id":"656","phone":"13614511433","uname":"韩志强","gname":"哈尔滨市","img_url":"http://img.myzyb.com/default_face.jpg","address":"哈尔滨市南岗区马端街108号","id":"12185820","uid":"12158945","area_x":39.1439299,"area_y":117.21081309,"countnum":8,"countweight":44.1},{"area_id":"656","phone":"18346161673","uname":"李小荣","gname":"王氏电动车","img_url":"http://img.myzyb.com/default_face.jpg","address":"保国大街保国公寓9号门市","id":"12185826","uid":"12158953","area_x":45.59272842538,"area_y":126.67935649195,"countnum":1,"countweight":4.2},{"area_id":"656","phone":"18946035818","uname":"吕子成","gname":"吕子成","img_url":"http://img.myzyb.com/default_face.jpg","address":"和兴路227号","id":"12185830","uid":"12158957","area_x":45.740223463269,"area_y":126.61731182643,"countnum":9,"countweight":51.25},{"area_id":"656","phone":"13836031882","uname":"于立平","gname":"于立平","img_url":"http://img.myzyb.com/default_face.jpg","address":"和兴路219号","id":"12185831","uid":"12158955","area_x":"","area_y":"","countnum":9,"countweight":51.25},{"area_id":"656","phone":"13936666136","uname":"冯冰","gname":"智能平衡车体验店","img_url":"http://img.myzyb.com/default_face.jpg","address":"和兴路233号","id":"12185832","uid":"12158958","area_x":45.741684363245,"area_y":126.61748687857,"countnum":1,"countweight":8.7},{"area_id":"656","phone":"13796614455","uname":"马春雷","gname":"电动车维修","img_url":"http://img.myzyb.com/default_face.jpg","address":"道里区康安路27号","id":"12185867","uid":"12158998","area_x":45.742591030509,"area_y":126.61314595192,"countnum":4,"countweight":27.4}]
     */

    private String message;
    private int status;
    private int prdstatus;
    private List<ListBean> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrdstatus() {
        return prdstatus;
    }

    public void setPrdstatus(int prdstatus) {
        this.prdstatus = prdstatus;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * area_id : 656
         * phone : 13614511433
         * uname : 韩志强
         * gname : 哈尔滨市
         * img_url : http://img.myzyb.com/default_face.jpg
         * address : 哈尔滨市南岗区马端街108号
         * id : 12185820
         * uid : 12158945
         * area_x : 39.1439299
         * area_y : 117.21081309
         * countnum : 8
         * countweight : 44.1
         */

        private String area_id;
        private String phone;
        private String uname;
        private String gname;
        private String img_url;
        private String address;
        private String id;
        private String uid;
        private double area_x;
        private double area_y;
        private int countnum;
        private double countweight;

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

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

        public int getCountnum() {
            return countnum;
        }

        public void setCountnum(int countnum) {
            this.countnum = countnum;
        }

        public double getCountweight() {
            return countweight;
        }

        public void setCountweight(double countweight) {
            this.countweight = countweight;
        }
    }
}
