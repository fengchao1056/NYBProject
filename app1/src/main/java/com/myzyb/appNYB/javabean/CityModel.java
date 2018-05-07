package com.myzyb.appNYB.javabean;

import java.util.List;

public class CityModel {
    private String name;



    private String id;
    private List<DistrictModel> districtList;

    public CityModel() {
        super();
    }

    public CityModel(String name, List<DistrictModel> districtList, String id) {
        super();
        this.name = name;
        this.districtList = districtList;
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictModel> districtList) {
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList
                + "]";
    }

}
