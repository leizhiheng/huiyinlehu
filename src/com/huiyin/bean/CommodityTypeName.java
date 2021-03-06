package com.huiyin.bean;

import java.util.ArrayList;

public class CommodityTypeName extends BaseBean {
	private static final long serialVersionUID = 1L;

	public ArrayList<TypeList> attributeList = new ArrayList<TypeList>();// 商品属性列表

	public class TypeList {
		public String ID;// 1级属性名称
		public ArrayList<TypeName> VALUE_LIST = new ArrayList<TypeName>();// 属性值列表
		public String NAME;// 1级属性id
	}

	public class TypeName {
		public String ID;// 2级属性id
		public String NAME;// 2级属性名称
		private boolean isSelected;

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		@Override
		public String toString() {
			return "TypeName [ID=" + ID + ", NAME=" + NAME + ", isSelected="
					+ isSelected + "]";
		}

	}
}
