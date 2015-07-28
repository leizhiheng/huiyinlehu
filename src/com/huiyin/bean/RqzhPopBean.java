package com.huiyin.bean;

import java.util.List;

/**
 * Created by Mike on 2015/6/28.
 */
public class RqzhPopBean {

    /**
     * goodsRecommend : [{"GOODS_IMG":"upload/image/store/2015/20150519/201505191806563573_200X200.jpg","GOODS_PRICE":"0.01","GOODS_NAME":"OPPO店铺固定运费5元","GOODS_ID":10533,"NUM":1},{"GOODS_IMG":"upload/image/admin/2015/20150527/201505271759181744_200X200.png","GOODS_PRICE":"234.00","GOODS_NAME":"卖家承担运费","GOODS_ID":10651,"NUM":2},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":3},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":4},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":5},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":6},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":7},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":8},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":9},{"GOODS_IMG":"upload/image/admin/2015/20150418/201504181505069899_200X200.png","GOODS_PRICE":"0.01","GOODS_NAME":"测试数据","GOODS_ID":3811,"NUM":10}]
     * curTime : 2015-06-28 14:38:46
     * type : 1
     * msg : 成功
     */
    public List<GoodsRecommendEntity> goodsRecommend;
    public String curTime;
    public int type;
    public String msg;

    public class GoodsRecommendEntity {
        /**
         * GOODS_IMG : upload/image/store/2015/20150519/201505191806563573_200X200.jpg
         * GOODS_PRICE : 0.01
         * GOODS_NAME : OPPO店铺固定运费5元
         * GOODS_ID : 10533
         * NUM : 1
         */
        public String GOODS_IMG;
        public String GOODS_PRICE;
        public String GOODS_NAME;
        public int GOODS_ID;
        public int NUM;
    }
}
