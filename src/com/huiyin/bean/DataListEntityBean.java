package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lian on 2015/5/30.
 */
public class DataListEntityBean  extends BaseBean{
    /**
     * ORDER_ID : 12416
     * STORE_NAME : 乐虎自营
     * STORE_LOGO : null
     * ID : 583
     * STORE_ID : 0
     * BESPEAK_CODE : 39155308594600001
     * CREATE_DATE : 2015-05-30 08:59:46
     * STATUS : 25
     * bespeakDetalList : [{"GOODS_IMG":"/attached/image/2014/20141124/201411240916566253.jpg","GOODS_PRICE":3998,"PRIMARY_ID":583,"STORE_ID":0,"GOODS_NO":80779,"GOODS_NAME":"三洋洗衣机DG-L7533BXS 7.5公斤全自动滚筒洗衣机","GOODS_ID":969,"NUM":6,"NORMS_VALUE":"<span>null<\/span>"}]
     * NUM : 1
     * STATUS_NAME : 预约：审核中
     */
    private String ORDER_ID;//订单id
    private String ORDER_CODE;//订单编号
    private String STORE_NAME;//店铺名称
    private String STORE_LOGO;//店铺logo
    private String ID;//预约id
    private String STORE_ID;//店铺id
    private String BESPEAK_CODE;//预约编号
    private String CREATE_DATE;//创建时间
    private String STATUS;//预约状态id
    private String FLAG;// 1退货，2换货，3预约
    private List<BespeakDetalListEntity> bespeakDetalList;
    private String NUM;
    private String STATUS_NAME;//状态名称
    
	public DataListEntityBean(JSONObject obj){
		ORDER_ID = obj.optString("ORDER_ID");//订单id
		setORDER_CODE(obj.optString("ORDER_CODE"));//订单id
		STORE_NAME = obj.optString("STORE_NAME");//店铺名称
		STORE_LOGO = obj.optString("STORE_LOGO");//店铺logo
		ID = obj.optString("ID");//预约id
		STORE_ID = obj.optString("STORE_ID");//店铺id
		BESPEAK_CODE = obj.optString("BESPEAK_CODE");//预约编号
		CREATE_DATE = obj.optString("CREATE_DATE");//创建时间
		STATUS = obj.optString("STATUS");//预约状态id
		FLAG = obj.optString("FLAG");// 1退货，2换货，3预约
		NUM = obj.optString("NUM");
		STATUS_NAME = obj.optString("STATUS_NAME");//状态名称
		bespeakDetalList = new ArrayList<BespeakDetalListEntity>();
		JSONArray array = obj.optJSONArray("bespeakDetalList");
		if(null != array && array.length() > 0){
			for(int i=0; i<array.length(); i++){
				bespeakDetalList.add(new BespeakDetalListEntity(array.optJSONObject(i)));
			}
		}
    }
    
    
	public String getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public String getSTORE_NAME() {
		return STORE_NAME;
	}
	public void setSTORE_NAME(String sTORE_NAME) {
		STORE_NAME = sTORE_NAME;
	}
	public String getSTORE_LOGO() {
		return STORE_LOGO;
	}
	public void setSTORE_LOGO(String sTORE_LOGO) {
		STORE_LOGO = sTORE_LOGO;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSTORE_ID() {
		return STORE_ID;
	}
	public void setSTORE_ID(String sTORE_ID) {
		STORE_ID = sTORE_ID;
	}
	public String getBESPEAK_CODE() {
		return BESPEAK_CODE;
	}
	public void setBESPEAK_CODE(String bESPEAK_CODE) {
		BESPEAK_CODE = bESPEAK_CODE;
	}
	public String getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(String cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public int getSTATUSInt() {
		try{
			return Integer.parseInt(STATUS);
		}catch(Exception e){
			return 0;
		}
	}
	
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getFLAG() {
		return FLAG;
	}
	
	public int getFLAGInt() {
		try{
			return Integer.parseInt(FLAG);
		}catch(Exception e){
			return 0;
		}
	}
	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}
	public List<BespeakDetalListEntity> getBespeakDetalList() {
		return bespeakDetalList;
	}
	public void setBespeakDetalList(List<BespeakDetalListEntity> bespeakDetalList) {
		this.bespeakDetalList = bespeakDetalList;
	}
	public String getNUM() {
		return NUM;
	}
	public void setNUM(String nUM) {
		NUM = nUM;
	}
	public String getSTATUS_NAME() {
		return STATUS_NAME;
	}
	public void setSTATUS_NAME(String sTATUS_NAME) {
		STATUS_NAME = sTATUS_NAME;
	}


	public String getORDER_CODE() {
		return ORDER_CODE;
	}


	public void setORDER_CODE(String oRDER_CODE) {
		ORDER_CODE = oRDER_CODE;
	}
}
