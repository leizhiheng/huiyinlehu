package com.huiyin.bean;

/**
 * 举报类型所包含的举报主题
 * @author leizhiheng
 *
 */
public class ReportBaseInfoTitle {
		private String titleName;
		private int titleId;
		private int belongTypeId;
		
		public ReportBaseInfoTitle(String titleName,int titleId,int typeId){
			this.titleName = titleName;
			this.titleId = titleId;
			this.belongTypeId = typeId;
		}

		public String getTitleName() {
			return titleName;
		}

		public void setTitleName(String titleName) {
			this.titleName = titleName;
		}

		public int getTitleId() {
			return titleId;
		}

		public void setTitleId(int titleId) {
			this.titleId = titleId;
		}

		public int getBelongTypeId() {
			return belongTypeId;
		}

		public void setBelongTypeId(int belongTypeId) {
			this.belongTypeId = belongTypeId;
		}
	}
