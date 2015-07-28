package com.huiyin.bean;

import com.huiyin.utils.MathUtil;
import com.huiyin.utils.Utils;

/**
 * 商品
 * 新增换货，修改换货商品数据Bean对象
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-3
 */
public class GoodsListEntity extends BaseBean{

   
    private String GOODS_IMG;				// 商品图片
    
    private String GOODS_PRICE;				// 商品价格
    
    private String ORDER_DETAIL_ID;			// 订单详情id
    
    private String QTY;						// 可以选择的商品数量上限
    
    private String ID;						// 换货详情ID

    private String GOODS_NAME;				// 商品名称
    
    private String STORE_ID;				// 店铺id
    
    private String GOODS_NO;				// 商品编号
    
    private String GOODS_ID;				// 商品id
    
    private String QUANTITY;				// 数量
    
    private String NORMS_VALUE;				// 规格

    
    public void setGOODS_IMG(String GOODS_IMG) {
        this.GOODS_IMG = GOODS_IMG;
    }

    public void setGOODS_PRICE(String GOODS_PRICE) {
        this.GOODS_PRICE = GOODS_PRICE;
    }

    public void setORDER_DETAIL_ID(String ORDER_DETAIL_ID) {
        this.ORDER_DETAIL_ID = ORDER_DETAIL_ID;
    }

    public void setGOODS_NO(String GOODS_NO) {
        this.GOODS_NO = GOODS_NO;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public void setGOODS_NAME(String GOODS_NAME) {
        this.GOODS_NAME = GOODS_NAME;
    }

    public void setGOODS_ID(String GOODS_ID) {
        this.GOODS_ID = GOODS_ID;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    public void setNORMS_VALUE(String NORMS_VALUE) {
        this.NORMS_VALUE = NORMS_VALUE;
    }

    public String getGOODS_IMG() {
        return GOODS_IMG;
    }

    public String getGOODS_PRICE() {
        return GOODS_PRICE;
    }

    public String getORDER_DETAIL_ID() {
        return ORDER_DETAIL_ID;
    }

    public String getGOODS_NO() {
        return GOODS_NO;
    }

    public String getSTORE_ID() {
        return STORE_ID;
    }

    public String getGOODS_NAME() {
        return GOODS_NAME;
    }

    public String getGOODS_ID() {
        return GOODS_ID;
    }

    public String getQUANTITY() {
        return QUANTITY;
    }

    public String getNORMS_VALUE() {
        return NORMS_VALUE;
    }
    
	public String getQTY() {
		return QTY;
	}

	public void setQTY(String qTY) {
		QTY = qTY;
	}

	public String getID() {
		if(null != ID){
			return ID;
		}
		return "";
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	
	//-------------------------换货修改-商品数量加减-逻辑  start-----------------------------
    private int replaceUpdateNumber = 0;	//换货数量
	public boolean addReplaceUpdate(int num){
		
		if(0 == replaceUpdateNumber){
			//说明是第一次过去换货数量，默认返回上一次的购买数量
			replaceUpdateNumber = Utils.anInt(QUANTITY);
		}
		
		
		//换货上限数量
		int totalNum = MathUtil.stringToInt(QTY);
		if((replaceUpdateNumber+num) > totalNum){
			//不能大于totalNum
			return false;
		}else if((replaceUpdateNumber+num) < 1){
			//不能小于1
			return false;
		}
		replaceUpdateNumber += num;
		return true;
	}
	
	public int getReplaceUpdateNumber(){
		if(0 == replaceUpdateNumber){
			//说明是第一次过去换货数量，默认返回上一次的购买数量
			replaceUpdateNumber = Utils.anInt(QUANTITY);
		}
		return this.replaceUpdateNumber;
	}
	//-------------------------换货修改-商品数量加减-逻辑  逻辑 end-----------------------------
	
	
	//===========================================================================
	
	//-------------------------换货新增-商品数量加减-逻辑  start-----------------------------
    private int replaceAddNumber = 1;	//换货数量
	public boolean addReplaceAdd(int num){
		
		//换货上限数量
		int totalNum = MathUtil.stringToInt(QUANTITY);
		if((replaceAddNumber+num) > totalNum){
			//不能大于totalNum
			return false;
		}else if((replaceAddNumber+num) < 1){
			//不能小于1
			return false;
		}
		replaceAddNumber += num;
		return true;
	}
	
	public int getReplaceAddNumber(){
		return this.replaceAddNumber;
	}
	//-------------------------换货新增-商品数量加减-逻辑  逻辑 end-----------------------------
}
