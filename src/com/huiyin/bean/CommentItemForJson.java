package com.huiyin.bean;

/**
 * Created by justme on 15/5/22.
 */
public class CommentItemForJson {
    public String orderDetailId;// 订单详情id
    public String goodsId;// 商品名称
    public String goodsNo;// 商品编号
    public String storeId;// 商品店铺id
    public float evaGrade;// 评分
    public int nice;// 是否加精   1是加精 0是没有加精
    public String content;// 评价内容
    public String picture;//图片（多个以逗号隔开）
    public String label;//总结（多个以逗号隔开）
}
