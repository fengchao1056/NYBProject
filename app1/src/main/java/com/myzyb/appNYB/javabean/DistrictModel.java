package com.myzyb.appNYB.javabean;

public class DistrictModel {
	//县
	private String name;
	private String id;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", id=" + id + "]";
	}

}
