package com.myzyb2.appNYB2.javabean;

import java.util.List;

/**
 * Created by cuiz on 2016/3/1.
 */
public class Dealer {


    /**
     * message : 操作成功
     * status : 1001
     * list : [{"uid":"12154","id":"12182","img_url":"http://img.myzyb.com/group_img/img_1455868553178.png","gname":"木图测试主网店","phone":"13301395641","address":"上地三街嘉华大厦","uname":"木图"},{"uid":"12157","id":"12185","img_url":"http://img.myzyb.com/default_face.jpg","gname":"郝雪测试","phone":"13321199288","address":"上庄馨悦佳缘","uname":"郝雪"}]
     */

    private String message = "";
    private int status = 0;
    /**
     * uid : 12154
     * id : 12182
     * img_url : http://img.myzyb.com/group_img/img_1455868553178.png
     * gname : 木图测试主网店
     * phone : 13301395641
     * address : 上地三街嘉华大厦
     * uname : 木图
     */

    private List<ListDealer> list;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setList(List<ListDealer> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public List<ListDealer> getList() {
        return list;
    }

    public static class ListDealer {
        private String uid = "";
        private String id = "";
        private String img_url = "";
        private String gname = "";
        private String phone = "";
        private String address = "";
        private String uname = "";

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setUname(String uname) {
            this.uname = uname;
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

        public String getGname() {
            return gname;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getUname() {
            return uname;
        }
    }
}
