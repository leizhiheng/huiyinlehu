package com.huiyin.bean;

import java.util.ArrayList;

/**
 * Created by kuangyong on 2015/6/5.
 */
public class GDBCommon extends BaseBean {

    public String PRICE;//价格
    public String BASK_NUMBER;//晒单数量
    public String FLAG_SECILL;//优惠套餐：1存在，0不存在,
    public String DISCOUNT_PRICE;//折扣价格
    public String CASH;//赠送现金金额
    public String IS_CASH;// 1有赠送现金，0没有
    public String GOODS_IMG_LIST;//图片集合,
    public String INTEGRAL;//赠送积分
    public String QQ;//店铺的通讯
    public String ID;//商品id
    public String COUNT_FOCUS_GOODS;//商品关注数量
    public String IS_TICKET;//是否赠送虎券：1赠送，0不赠送
    public String NUM;//
    public String COUNT_FOCUS_STORE;//店铺关注数量
    public String GOODS_NO;// 货品id 货品号库存：当规格参数集合为-1的时候，取的就是这里的值
    public String GOODS_PRICE;//商品价格
    public String WANGWANG;//店铺通讯
    public String FLAG_DONATE;//满赠:1存在满赠，0不存在
    public String FLAG_SKU;//单品促销：1存在，0不存在
    public String GOODS_NAME;//商品名称
    public String STORE_ID;//店铺id
    public String STORE_LOGO;//店铺logo
    public String PROMOTION_TYPE;//促销方式 1、折扣 2、直降 3、折后降
    public String FLAG_GROUP;//优惠套餐：1存在，0不存在,
    public String AD;//广告语,
    public String STORE_NAME;//店铺名称
    public String FLAG_GIFT;//赠品 1存在，0不存在,
    public String TICKET_NAME;//乐虎券名称
    public String SERVE;//服务
    public String IS_INTEGRAL;//1有赠送，0不存在
    public float STORE_SCORE;//店铺评分
    public String FLAG_FOCUS_GOODS;//用户是否收藏该商品：0没有收藏，1收藏了
    public String FLAG_FOCUS_STORE;//用户是否收藏该店铺：0没有收藏，1收藏了
    public String REVIEW_NUMBER;//评论总人数
    public String REVIEW_PERCENT;//好评率
    public String SHOPPING_CAR_NUM;// 购物车数量（未做）
    public String FLAG_ACTIVITY;//0不存在任何活动,1新品预约，2团购，3秒杀，4单品，5闪购（倒计时/0,4无倒计时、折扣价/0不存在折扣价）
    public String FLAG_NEWGOODS;//1新品预约，0不存在
    public String FLAG_REDUCE;//,满减：1存在满减，0不存在
    public String FLAG_BUY; //是否还可以继续购买：0不可以继续购买
    public String NEW_GOODS_ID;//新品预约id
    public String END_TIME;//活动结束时间
    public String START_TIME;// 活动开始时间
    public String DISCOUNT;// 折扣
    public String GOODS_IMG;// 产品图片
    public String FLAG_DISCOUNT;//1存在折扣，0不存在折扣
    public String FLAG_BESPEAK ;//0该用户没有预约过，>0预约过了
    public String GOODS_STATUS ;//0、已保存1、已出售2、已下架3、违规下架
    public String GOODS_STOCK;//库存
    public String info_content_map;//商品信息
    public String packinglist_content_map;//包装参数
    public String afterservice_content_map;//售后服务
    public String EXAMINE_STATUS;//0、未审核 1、新品审核中 2、下架审核中 3、上架审核中 4、删除审核中 5、新品审核不通过 6、上架审核不通过 7、下架审核不通过 8、删除审核不通过
    public ArrayList<GDBItem> MAYBE_LIKE = new ArrayList<GDBItem>();// 也许喜欢
    public ArrayList<DEFAULT_NORM> DEFAULT_NORMS=new ArrayList<DEFAULT_NORM>();//当前规格
    public ArrayList<GOODS_NORM> GOODS_NORMS_LIST=new ArrayList<GOODS_NORM>();//规格集合
    public ArrayList<HINT> PROMOTION_REDUCE=new ArrayList<HINT>();//满减
    public ArrayList<GOODS> PROMOTION_DONATE=new ArrayList<GOODS>();//满赠列表
    public ArrayList<EVA_GOODS> EVA_GOODS_LIST=new ArrayList<EVA_GOODS>();//晒单评价列表
    public ArrayList<PROMOTION> PROMOTION_GIFT=new ArrayList<PROMOTION>();//赠品列表
    public String QUOTA_ONE;//是否开启单品限购 0开启 1不开启
    public String MOST_QUANTITY;//最多购买数量 <=0是不限制数量
    public String LEAST_QUANTITY;//最少购买数量
    public String PROMOTION_ID;//活动ID FLAG_ACTIVITY >2 时从这里取活动ID
    // add by zhyao @2015/5/8  添加商品可用红包金额
    public String BONUS;//可用红包金额
    public String COUNT_RECOMMEND;//推荐配件:>0有推荐配件，<=0不存在推荐配件
    public String COUNT_POPULAR;//人气组合:>0有人气组合，<=0不存在人气组合
    public String FLAG_FLASH;//1存在闪购 0不存在闪购（如果存在闪购需要显示倒计时，折后价，字段和其它打折促销一样）
    public String THIRD_GOODSCATEGORY_ID;
    /**
     * 图文详情
     * @return
     */
	public String getInfo_content_map() {
		return info_content_map;
	}

	/**
	 * 包装规格
	 * @return
	 */
	public String getPackinglist_content_map() {
		return packinglist_content_map;
	}

	/**
	 * 售后服务
	 * @return
	 */
	public String getAfterservice_content_map() {
		return afterservice_content_map;
	}

	public int getFLAG_ACTIVITY() {
		try{
			return Integer.parseInt(FLAG_ACTIVITY);
		}catch(Exception e){
			return 0;
		}
	}

	public int getQUOTA_ONE() {
		try{
			return Integer.parseInt(QUOTA_ONE);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_FOCUS_GOODS() {
		try{
			return Integer.parseInt(FLAG_FOCUS_GOODS);
		}catch(Exception e){
			return 0;
		}
	}

	public int getNEW_GOODS_ID() {
		try{
			return Integer.parseInt(NEW_GOODS_ID);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_BUY() {
		try{
			return Integer.parseInt(FLAG_BUY);
		}catch(Exception e){
			return 0;
		}
	}

	public int getGOODS_STOCK() {
		try{
			return Integer.parseInt(GOODS_STOCK);
		}catch(Exception e){
			return 0;
		}
	}

	public int getCOUNT_POPULAR() {
		try{
			return Integer.parseInt(COUNT_POPULAR);
		}catch(Exception e){
			return 0;
		}
	}

	public int getCOUNT_RECOMMEND() {
		try{
			return Integer.parseInt(COUNT_RECOMMEND);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_GROUP() {
		try{
			return Integer.parseInt(FLAG_GROUP);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_FOCUS_STORE() {
		try{
			return Integer.parseInt(FLAG_FOCUS_STORE);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_GIFT() {
		try{
			return Integer.parseInt(FLAG_GIFT);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_REDUCE() {
		try{
			return Integer.parseInt(FLAG_REDUCE);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_DONATE() {
		try{
			return Integer.parseInt(FLAG_DONATE);
		}catch(Exception e){
			return 0;
		}
	}

	public int getIS_TICKET() {
		try{
			return Integer.parseInt(IS_TICKET);
		}catch(Exception e){
			return 0;
		}
	}

	public int getIS_CASH() {
		try{
			return Integer.parseInt(IS_CASH);
		}catch(Exception e){
			return 0;
		}
	}

	public int getIS_INTEGRAL() {
		try{
			return Integer.parseInt(IS_INTEGRAL);
		}catch(Exception e){
			return 0;
		}
	}

	public int getREVIEW_NUMBER() {
		try{
			return Integer.parseInt(REVIEW_NUMBER);
		}catch(Exception e){
			return 0;
		}
	}

	public int getGOODS_STATUS() {
		try{
			return Integer.parseInt(GOODS_STATUS);
		}catch(Exception e){
			return 0;
		}
	}

	public int getMOST_QUANTITY() {
		try{
			return Integer.parseInt(MOST_QUANTITY);
		}catch(Exception e){
			return 0;
		}
	}

	public int getLEAST_QUANTITY() {
		try{
			return Integer.parseInt(LEAST_QUANTITY);
		}catch(Exception e){
			return 0;
		}
	}

	public int getFLAG_DISCOUNT() {
		try{
			return Integer.parseInt(FLAG_DISCOUNT);
		}catch(Exception e){
			return 0;
		}
	}

}
