package com.huiyin.bean;

import java.util.List;

/**
 * Created by Mike on 2015/6/28.
 */
public class TjdpPopBean {

    /**
     * goodsRecommend : [{"NAME":"OPPO推荐一","ID":262,"list":[{"GOODS_IMG":"upload/image/store/2015/20150603/201506031155511245_200X200.jpg","GOODS_PRICE":2543,"ID":262,"GOODS_NO":45,"STORE_ID":71,"GOODS_NAME":"店铺新品","GOODS_ID":10761}],"size":1},{"NAME":"OPPO推荐二","ID":264,"list":[{"GOODS_IMG":"upload/image/store/2015/20150424/201504241052027933_200X200.jpg","GOODS_PRICE":111,"ID":264,"GOODS_NO":65687,"STORE_ID":71,"GOODS_NAME":"OPPO运费模板【按件数】【飞虎队】","GOODS_ID":10444},{"GOODS_IMG":"upload/image/store/2015/20150505/201505050949234168_200X200.jpg","GOODS_PRICE":55,"ID":263,"GOODS_NO":5432476,"STORE_ID":71,"GOODS_NAME":"有没有在平台后台显示","GOODS_ID":10471}],"size":2}]
     * curTime : 2015-06-28 15:58:35
     * type : 1
     * msg : 成功
     */
    public List<GoodsRecommendEntity> goodsRecommend;
    public String curTime;
    public int type;
    public String msg;

    public class GoodsRecommendEntity {
        /**
         * NAME : OPPO推荐一
         * ID : 262
         * list : [{"GOODS_IMG":"upload/image/store/2015/20150603/201506031155511245_200X200.jpg","GOODS_PRICE":2543,"ID":262,"GOODS_NO":45,"STORE_ID":71,"GOODS_NAME":"店铺新品","GOODS_ID":10761}]
         * size : 1
         */
        public String NAME;
        public int ID;
        public List<ListEntity> list;
        public int size;

        public class ListEntity {
            /**
             * GOODS_IMG : upload/image/store/2015/20150603/201506031155511245_200X200.jpg
             * GOODS_PRICE : 2543
             * ID : 262
             * GOODS_NO : 45
             * STORE_ID : 71
             * GOODS_NAME : 店铺新品
             * GOODS_ID : 10761
             */
            public String GOODS_IMG;
            public String GOODS_PRICE;
            public int ID;
            public String GOODS_NO;
            public int STORE_ID;
            public String GOODS_NAME;
            public int GOODS_ID;
            public int GOODS_STOCK;
        }
    }
}
