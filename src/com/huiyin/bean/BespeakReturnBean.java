package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 预约/退换货 实体类
 * Created by lian on 2015/5/30.
 */
public class BespeakReturnBean extends BaseBean {

    /**
     * dataList : [{"ORDER_ID":12416,"STORE_NAME":"乐虎自营","STORE_LOGO":null,"ID":583,"STORE_ID":0,"BESPEAK_CODE":"39155308594600001","CREATE_DATE":"2015-05-30 08:59:46","STATUS":25,"bespeakDetalList":[{"GOODS_IMG":"/attached/image/2014/20141124/201411240916566253.jpg","GOODS_PRICE":3998,"PRIMARY_ID":583,"STORE_ID":0,"GOODS_NO":80779,"GOODS_NAME":"三洋洗衣机DG-L7533BXS 7.5公斤全自动滚筒洗衣机","GOODS_ID":969,"NUM":6,"NORMS_VALUE":"<span>null<\/span>"}],"NUM":1,"STATUS_NAME":"预约：审核中"},{"ORDER_ID":12750,"STORE_NAME":"乐虎自营","bespeakDetailList":[{"GOODS_IMG":"/upload/image/admin/2015/20150418/201504181528589113_290X290.jpg","GOODS_PRICE":0.01,"PRIMARY_ID":573,"STORE_ID":0,"GOODS_NO":2314,"GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":1,"NORMS_VALUE":"<span>                            <a href=\"wapGoods.do?store_id=0<\/span>"}],"STORE_LOGO":null,"ID":573,"STORE_ID":0,"BESPEAK_CODE":"39155298504100007","CREATE_DATE":"2015-05-29 20:50:41","STATUS":25,"NUM":2,"STATUS_NAME":"预约：审核中"},{"ORDER_ID":12676,"STORE_NAME":"乐虎自营","bespeakDetailList":[{"GOODS_IMG":"/upload/image/admin/2015/20150423/201504231448477604_290X290.png","GOODS_PRICE":43,"PRIMARY_ID":572,"STORE_ID":0,"GOODS_NO":43214432,"GOODS_NAME":"4321【14点准时上架】","GOODS_ID":10422,"NUM":1,"NORMS_VALUE":"<span>null<\/span>"}],"STORE_LOGO":null,"ID":572,"STORE_ID":0,"BESPEAK_CODE":"39155298435500006","CREATE_DATE":"2015-05-29 20:43:55","STATUS":25,"NUM":3,"STATUS_NAME":"预约：审核中"},{"ORDER_ID":13291,"STORE_NAME":"乐虎自营","bespeakDetailList":[{"GOODS_IMG":"/upload/image/admin/2015/20150521/201505211730554539_290X290.png","GOODS_PRICE":2,"PRIMARY_ID":562,"STORE_ID":0,"GOODS_NO":54325,"GOODS_NAME":"wap端运费模板","GOODS_ID":10562,"NUM":1,"NORMS_VALUE":null}],"STORE_LOGO":null,"ID":562,"STORE_ID":0,"BESPEAK_CODE":"3915529830600005","CREATE_DATE":"2015-05-29 20:30:06","STATUS":25,"NUM":4,"STATUS_NAME":"预约：审核中"},{"ORDER_ID":13086,"STORE_NAME":"乐虎自营","bespeakDetailList":[{"GOODS_IMG":"/upload/image/store/2015/20150424/201504240946126404_290X290.jpg","GOODS_PRICE":1999,"PRIMARY_ID":544,"STORE_ID":71,"GOODS_NO":523543,"GOODS_NAME":"OPPO R1C(R8207) 冰晶白 移动4G手机 双卡双待","GOODS_ID":10431,"NUM":1,"NORMS_VALUE":null}],"STORE_LOGO":null,"ID":544,"STORE_ID":0,"BESPEAK_CODE":"3915529636700003","CREATE_DATE":"2015-05-29 18:36:07","STATUS":25,"NUM":5,"STATUS_NAME":"预约：审核中"},{"ORDER_ID":12346,"STORE_NAME":"乐虎自营","bespeakDetailList":[{"GOODS_IMG":"/attached/image/2014/20141127/201411272035092065.jpg","GOODS_PRICE":4599,"PRIMARY_ID":543,"STORE_ID":0,"GOODS_NO":81619,"GOODS_NAME":"美的空调KFR-51LW/DY-PA401(R3) 2匹定频柜机","GOODS_ID":1030,"NUM":1,"NORMS_VALUE":"<span>null<\/span>"},{"GOODS_IMG":"/attached/image/2015/20150327/201503271553577516.jpg","GOODS_PRICE":5350,"PRIMARY_ID":543,"STORE_ID":0,"GOODS_NO":89295,"GOODS_NAME":"苹果(Apple) iPhone 6 灰色 16G（聚星）【运费模板】","GOODS_ID":3502,"NUM":1,"NORMS_VALUE":"<span>null<\/span>"}],"STORE_LOGO":null,"ID":543,"STORE_ID":0,"BESPEAK_CODE":"39155296271200002","CREATE_DATE":"2015-05-29 18:27:12","STATUS":25,"NUM":6,"STATUS_NAME":"预约：审核中"},{"ORDER_ID":12346,"STORE_NAME":"乐虎自营","bespeakDetailList":[{"GOODS_IMG":"/attached/image/2014/20141123/201411231649466811.jpg","GOODS_PRICE":4200,"PRIMARY_ID":542,"STORE_ID":0,"GOODS_NO":80663,"GOODS_NAME":"美的空调KFR-35GW/BP3DN1Y-HB402(A2) 1.5匹变频挂机","GOODS_ID":1395,"NUM":1,"NORMS_VALUE":"<span>nulltype=-1<\/span>"}],"STORE_LOGO":null,"ID":542,"STORE_ID":0,"BESPEAK_CODE":"39155296265800001","CREATE_DATE":"2015-05-29 18:26:58","STATUS":25,"NUM":7,"STATUS_NAME":"预约：审核中"}]
     * totalPageNum : 1
     * curTime : 2015-05-30 15:14:03
     * type : 1
     * msg : 成功
     * pageIndex : 1
     */
    private List<DataListEntityBean> dataList;
    private int totalPageNum;
    private String curTime;
    private int pageIndex;

    
    public BespeakReturnBean(String json){
    	try {
			JSONObject obj = new JSONObject(json);
			totalPageNum = obj.optInt("totalPageNum");
			curTime = obj.optString("curTime");
			type = obj.optInt("type");
			msg = obj.optString("msg");
			pageIndex = obj.optInt("pageIndex");
			
			dataList = new ArrayList<DataListEntityBean>();
			JSONArray array = obj.optJSONArray("dataList");
			if(null != array && array.length() > 0){
				for(int i=0; i<array.length(); i++){
					dataList.add(new DataListEntityBean(array.optJSONObject(i)));
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void setDataList(List<DataListEntityBean> dataList) {
        this.dataList = dataList;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<DataListEntityBean> getDataList() {
        return dataList;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public String getCurTime() {
        return curTime;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public int getPageIndex() {
        return pageIndex;
    }
    
    
    /**
     * 判断是否有更多的数据
     * @return
     */
    public boolean hasMoreData(){
    	if(isEmpty()){
    		return false;
    	}
    	return pageIndex < totalPageNum;
    }
    
    /**
     * 判断是否空数据
     * @return
     */
    public boolean isEmpty(){
    	if(null == dataList || dataList.size() == 0){
    		return true;
    	}
    	return false;
    }
}
