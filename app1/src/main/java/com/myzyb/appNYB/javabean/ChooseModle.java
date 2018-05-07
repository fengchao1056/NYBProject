package com.myzyb.appNYB.javabean;

import java.util.List;

/**
 * Created by cuiz on 2015/12/19.
 */
public class ChooseModle {

    private String cid ;
    private String cname ;
    private static List<ChooseModle> list;
    public ChooseModle() {

    }

    public List<ChooseModle> getList() {
        return list;
    }

    public void setList(List<ChooseModle> list) {
        this.list = list;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
