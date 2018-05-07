package com.myzyb.appNYB.javabean;

import java.util.List;

/**
 * Created by cuiz on 2015/12/7.
 */
public class ParentItem {


    private String norms;
    private int nums;
    private double weight;
    private double admin_price;
    private double admin_price_count;
    private List<ChildItem> son;


    public String getNorms() {
        return norms;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAdmin_price() {
        return admin_price;
    }

    public void setAdmin_price(double admin_price) {
        this.admin_price = admin_price;
    }

    public double getAdmin_price_count() {
        return admin_price_count;
    }

    public void setAdmin_price_count(double admin_price_count) {
        this.admin_price_count = admin_price_count;
    }

    public List<ChildItem> getSon() {
        return son;
    }

    public void setSon(List<ChildItem> son) {
        this.son = son;
    }

    @Override
    public String toString() {
        return "ParentItem{" +
                "norms='" + norms + '\'' +
                ", nums='" + nums + '\'' +
                ", weight='" + weight + '\'' +

                ", admin_price='" + admin_price + '\'' +
                ", admin_price_count='" + admin_price_count + '\'' +
                ", son=" + son +
                '}';
    }
}
