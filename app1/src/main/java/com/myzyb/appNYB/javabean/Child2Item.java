package com.myzyb.appNYB.javabean;

/**
 * Created by cuiz on 2015/12/7.
 */
public class Child2Item {


    private String id;
    private String prd_id;
    private int num;//shuliang
    private String uid;
    private String gid;
    private String area_id;
    private String norms; //6-dz
    private String sname;//pinpai
    private double weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrd_id() {
        return prd_id;
    }

    public void setPrd_id(String prd_id) {
        this.prd_id = prd_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getNorms() {
        return norms;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "ChildItem{" +
                "id='" + id + '\'' +
                ", prd_id='" + prd_id + '\'' +
                ", num='" + num + '\'' +
                ", uid='" + uid + '\'' +
                ", gid='" + gid + '\'' +
                ", area_id='" + area_id + '\'' +
                ", norms='" + norms + '\'' +
                ", sname='" + sname + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
