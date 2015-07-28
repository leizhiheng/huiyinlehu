package com.huiyin.bean;

public class ProductBespeakBean {

	public String PID1;//图片1对应的商品id
	public String IMG1;//图片1
	public String CID1;
	public String GOODS_NO1;//图片1对应的商品编号
	public String STORE_ID1;//图片1对应的店铺id
	public String PID2;//图片2对应的商品id
	public String IMG2;//图片2,
	public String CID2;
    public String GOODS_NO2;//图片1对应的商品编号
    public String STORE_ID2;//图片1对应的店铺id
	public String PID3;//图片3对应的商品id
	public String IMG3;//图片3
	public String CID3;
    public String GOODS_NO3;//图片1对应的商品编号
    public String STORE_ID3;//图片1对应的店铺id
	public String ADVER;//广告语
	public String ANAME;// 新品预约名称


	public String getIMG1() {
		return IMG1;
	}

	public void setIMG1(String iMG1) {
		IMG1 = iMG1;
	}


	public String getIMG2() {
		return IMG2;
	}

	public void setIMG2(String iMG2) {
		IMG2 = iMG2;
	}


	public String getIMG3() {
		return IMG3;
	}

	public void setIMG3(String iMG3) {
		IMG3 = iMG3;
	}


	public String getADVER() {
		return ADVER;
	}

	public void setADVER(String aDVER) {
		ADVER = aDVER;
	}

	public String getANAME() {
		return ANAME;
	}

	public void setANAME(String aNAME) {
		ANAME = aNAME;
	}

	@Override
	public String toString() {
		return "ProductBespeakBean [PID1=" + PID1 + ", IMG1=" + IMG1
				+ ", PID2=" + PID2 + ", IMG2=" + IMG2 + ", PID3=" + PID3
				+ ", IMG3=" + IMG3 + ", ADVER=" + ADVER + ", ANAME=" + ANAME
				+ "]";
	}

}
