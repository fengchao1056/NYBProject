package com.myzyb.appNYB.javabean;

import java.util.List;

public class ShopBean {
    private String message;
    private int status;
    private int prdstatus;
    private List<List> list;
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setPrdstatus(int prdstatus) {
        this.prdstatus = prdstatus;
    }
    public int getPrdstatus() {
        return prdstatus;
    }

    public void setList(List<List> list) {
        this.list = list;
    }
    public List<List> getList() {
        return list;
    }
}
