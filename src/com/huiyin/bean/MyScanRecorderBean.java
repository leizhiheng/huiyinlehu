package com.huiyin.bean;

import java.util.List;

/**
 * Created by Mike on 2015/6/11.
 */
public class MyScanRecorderBean {

    /**
     * historyList : [{"GOODS_IMG":"upload/image/admin/2015/20150604/201506041003038521_200X200.jpg","STORE_ID":0,"GOODS_NO":147258,"GOODS_NAME":"1汇银测试洗衣机按件","GOODS_ID":10831,"NUM":1},{"GOODS_IMG":"upload/image/admin/2015/20150603/201506031615101209_200X200.jpg","STORE_ID":0,"GOODS_NO":258,"GOODS_NAME":"蜘蛛侠","GOODS_ID":10834,"NUM":2},{"GOODS_IMG":"upload/image/admin/2015/20150511/201505111634143004_200X200.jpg","STORE_ID":0,"GOODS_NO":54213523,"GOODS_NAME":"测试wap端商品名称","GOODS_ID":10481,"NUM":3},{"GOODS_IMG":"upload/image/store/2015/20150424/201504241052027933_200X200.jpg","STORE_ID":71,"GOODS_NO":65687,"GOODS_NAME":"OPPO运费模板【按件数】【飞虎队】","GOODS_ID":10444,"NUM":4},{"GOODS_IMG":"upload/image/admin/2015/20150605/201506050947583653_200X200.png","STORE_ID":0,"GOODS_NO":64325325,"GOODS_NAME":"平台没有运费","GOODS_ID":10891,"NUM":5},{"GOODS_IMG":"upload/image/admin/2015/20150527/201505271729001935_200X200.png","STORE_ID":0,"GOODS_NO":52435432,"GOODS_NAME":"【按重量】运费模板【飞虎队】0.5kg","GOODS_ID":10641,"NUM":6},{"GOODS_IMG":"upload/image/admin/2015/20150527/201505271732546032_200X200.png","STORE_ID":0,"GOODS_NO":555435423,"GOODS_NAME":"【按重量】运费模板【EMS】1kg","GOODS_ID":10644,"NUM":7},{"GOODS_IMG":"/attached/image/2015/20150404/201504041539034684.jpg","STORE_ID":0,"GOODS_NO":950000197,"GOODS_NAME":"【阿珂姆/ACOME】户外魔术骑行头巾围脖口罩多用无缝头巾AA151Z0803","GOODS_ID":3823,"NUM":8},{"GOODS_IMG":"upload/image/admin/2015/20150527/201505271731202709_200X200.png","STORE_ID":0,"GOODS_NO":6326,"GOODS_NAME":"【按重量】运费模板【圆通】1kg","GOODS_ID":10643,"NUM":9},{"GOODS_IMG":"upload/image/admin/2015/20150527/201505271527143407_200X200.png","STORE_ID":0,"GOODS_NO":7654623543,"GOODS_NAME":"运费模板按件数【天天快递】","GOODS_ID":10615,"NUM":10}]
     * totalPageNum : 13
     * curTime : 2015-06-11 16:50:38
     * type : 1
     * msg : 成功
     * pageIndex : 1
     */
    private List<HistoryListEntity> historyList;
    private int totalPageNum;
    private String curTime;
    private int type;
    private String msg;
    private int pageIndex;

    public void setHistoryList(List<HistoryListEntity> historyList) {
        this.historyList = historyList;
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

    public List<HistoryListEntity> getHistoryList() {
        return historyList;
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

    public class HistoryListEntity {
        /**
         * GOODS_IMG : upload/image/admin/2015/20150604/201506041003038521_200X200.jpg
         * STORE_ID : 0
         * GOODS_NO : 147258
         * GOODS_NAME : 1汇银测试洗衣机按件
         * GOODS_ID : 10831
         * NUM : 1
         */
        private String GOODS_STATUS;
        private String GOODS_IMG;
        private String STORE_ID;
        private String GOODS_NO;
        private String GOODS_NAME;
        private String GOODS_ID;
        private String GOODS_PRICE;
        private int NUM;

        public String getGOODS_STATUS() {
            return GOODS_STATUS;
        }

        public void setGOODS_STATUS(String GOODS_STATUS) {
            this.GOODS_STATUS = GOODS_STATUS;
        }

        public String getGOODS_PRICE() {
            return GOODS_PRICE;
        }

        public void setGOODS_PRICE(String GOODS_PRICE) {
            this.GOODS_PRICE = GOODS_PRICE;
        }

        public void setGOODS_IMG(String GOODS_IMG) {
            this.GOODS_IMG = GOODS_IMG;
        }

        public void setSTORE_ID(String STORE_ID) {
            this.STORE_ID = STORE_ID;
        }

        public void setGOODS_NO(String GOODS_NO) {
            this.GOODS_NO = GOODS_NO;
        }

        public void setGOODS_NAME(String GOODS_NAME) {
            this.GOODS_NAME = GOODS_NAME;
        }

        public void setGOODS_ID(String GOODS_ID) {
            this.GOODS_ID = GOODS_ID;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public String getGOODS_IMG() {
            return GOODS_IMG;
        }

        public String getSTORE_ID() {
            return STORE_ID;
        }

        public String getGOODS_NO() {
            return GOODS_NO;
        }

        public String getGOODS_NAME() {
            return GOODS_NAME;
        }

        public String getGOODS_ID() {
            return GOODS_ID;
        }

        public int getNUM() {
            return NUM;
        }
    }
}
