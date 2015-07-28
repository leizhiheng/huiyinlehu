package com.huiyin.bean;

import java.util.ArrayList;

public class ReportBaseInfoType {
	public String typeName;//举报类型名称
	public int typeId;//举报类型id
	private String typeDescription;//举报类型描述
	public ArrayList<ReportBaseInfoTitle> titleList;
	
	public ReportBaseInfoType(){}

	public ReportBaseInfoType(String name,int id){
		this.typeName = name;
		this.typeId = id;
		titleList = new ArrayList<ReportBaseInfoTitle>();
	}

	public void addTitle(ReportBaseInfoTitle title){
		titleList.add(title);
	}
	
	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public int getTypeId() {
		return typeId;
	}


	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public ArrayList<ReportBaseInfoTitle> getTitleList() {
		return titleList;
	}

	public void setTitleList(ArrayList<ReportBaseInfoTitle> titleList) {
		this.titleList = titleList;
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	
	

}
