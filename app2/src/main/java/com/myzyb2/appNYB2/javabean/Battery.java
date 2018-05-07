package com.myzyb2.appNYB2.javabean;


import java.io.Serializable;

public class Battery implements Serializable {
    private String sname = "";
    private double price = 0.00;
    private String norms = "";
    private double weight = 0.00;
    private int count = 0;
    private int id = 0;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNorms() {
        return norms;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    @Override
    public String toString() {
        return "Battery{" +
                "sname='" + sname + '\'' +
                ", price=" + price +
                ", norms='" + norms + '\'' +
                ", weight=" + weight +
                ", count=" + count +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Battery battery = (Battery) o;

        if (Double.compare(battery.price, price) != 0) return false;
        if (Double.compare(battery.weight, weight) != 0) return false;
        if (count != battery.count) return false;
        if (id != battery.id) return false;
        if (sname != null ? !sname.equals(battery.sname) : battery.sname != null) return false;
        return !(norms != null ? !norms.equals(battery.norms) : battery.norms != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = sname != null ? sname.hashCode() : 0;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (norms != null ? norms.hashCode() : 0);
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + count;
        result = 31 * result + id;
        return result;
    }
}
