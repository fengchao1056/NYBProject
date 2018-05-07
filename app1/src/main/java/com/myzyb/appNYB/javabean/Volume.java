package com.myzyb.appNYB.javabean;

import java.util.List;

public class Volume {
    private String name;
    private List<Battery> list;
    private String id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Battery> getList() {
        return list;
    }

    public void setList(List<Battery> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
