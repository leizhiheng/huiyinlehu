package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lian on 2015/5/30.
 */
public class OrderRecordBean extends BaseBean{

    /**
     * goodsList : [{"GOODS_IMG":"upload/image/admin/2015/20150521/201505211730554539_200X200.png","GOODS_PRICE":2,"ORDER_DETAIL_ID":18701,"GOODS_NO":54325,"STORE_ID":0,"GOODS_NAME":"wap端运费模板","GOODS_ID":10562,"QUANTITY":1,"NORMS_VALUE":null}]
     * flag : 3
     * curTime : 2015-05-30 16:30:34
     * returnRate : 0.0
     * type : 1
     * returnList : [{"NAME":"不适合","ID":131},{"NAME":"兴趣爱好","ID":143},{"NAME":"医疗行业","ID":142},{"NAME":"质量问题","ID":81}]
     * msg : 成功
     * replaceList : [{"NAME":"大法师","ID":381}]
     */
    private List<GoodsListEntity> goodsList;//商品列表
    private int flag;//1退货 2换货 3退换货都可以 -1都不可以,
    private String curTime;//当前服务器时间
    private String returnRate;//退货折损率
    private List<ReturnListEntity> returnList;//退货理由
    private List<ReplaceListEntity> replaceList;//换货理由

    public void setGoodsList(List<GoodsListEntity> goodsList) {
        this.goodsList = goodsList;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setReturnRate(String returnRate) {
        this.returnRate = returnRate;
    }


    public void setReturnList(List<ReturnListEntity> returnList) {
        this.returnList = returnList;
    }


    public void setReplaceList(List<ReplaceListEntity> replaceList) {
        this.replaceList = replaceList;
    }

    public List<GoodsListEntity> getGoodsList() {
        return goodsList;
    }

    public int getFlag() {
        return flag;
    }

    /**
     * 可以退换货
     * @return
     */
    public boolean canReplace(){
    	//1退货 2换货 3退换货都可以 -1都不可以,
    	return flag != (-1);
    }
    
    public String getCurTime() {
        return curTime;
    }

    public String getReturnRate() {
        return returnRate;
    }


    public List<ReturnListEntity> getReturnList() {
    	
    	//过滤空数据
    	for(int i=0; i<returnList.size(); i++){
    		if(returnList.get(i).isEmpty()){
    			returnList.remove(i);
    			i--;
    		}
    	}
        return returnList;
    }

    public List<ReplaceListEntity> getReplaceList() {
    	
    	//过滤空数据
    	for(int i=0; i<replaceList.size(); i++){
    		if(replaceList.get(i).isEmpty()){
    			replaceList.remove(i);
    			i--;
    		}
    	}
        return replaceList;
    }

    /**
     * 获取售后类型
     * @return
     */
	public List<ReservationType> getReservationType() {
		
		List<ReservationType> dataList = new ArrayList<ReservationType>();
		
		//1退货 2换货 3退换货都可以 -1都不可以,
		switch (flag) {
		case 1:
			dataList.add(new ReservationType("1", "我要退货"));
			break;

		case 2:
			dataList.add(new ReservationType("2", "我要换货(只能换同类同款商品)"));
			break;
		case 3:
			dataList.add(new ReservationType("1", "我要退货"));
			dataList.add(new ReservationType("2", "我要换货(只能换同类同款商品)"));
			break;
		}
		return dataList;
	}
}
