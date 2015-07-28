package com.huiyin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2015/6/14.
 */
public class VIPInfoBean  implements Serializable{

    /**
     * categoryData : [{"NAME":"1243555555555555555534444444444444444444444444444444444444444444444","ID":61},{"NAME":"手机","ID":51},{"NAME":"水果544466666666666666666","ID":53},{"NAME":"冰箱","ID":52}]
     * WORD : 再累计5000成长值即可升级到终极会员111
     * recordData : [{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-12 17:00:47","NUM":1},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-12 16:50:48","NUM":2},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-12 16:42:35","NUM":3},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-12 16:37:06","NUM":4},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"2312","USER_NAME":"admin","CREATETIME":"2015-06-11 18:25:33","NUM":5},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"2312","USER_NAME":"admin","CREATETIME":"2015-06-11 18:25:21","NUM":6},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"2312","USER_NAME":"admin","CREATETIME":"2015-06-11 18:24:59","NUM":7},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"2312","USER_NAME":"admin","CREATETIME":"2015-06-11 18:24:28","NUM":8},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"非推荐","USER_NAME":"ceshi","CREATETIME":"2015-06-11 14:39:54","NUM":9},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-11 14:21:50","NUM":10},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-11 14:15:45","NUM":11},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-11 14:15:42","NUM":12},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-11 14:15:39","NUM":13},{"PHONE":"13265483619","USER_URL":"attached/user21153/image/2015/20150513/201505131444599884.jpg","GIFT_NAME":"23452354623","USER_NAME":"ceshi","CREATETIME":"2015-06-11 14:15:36","NUM":14},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"乐虎券 单品促销","USER_NAME":"admin","CREATETIME":"2015-06-08 19:18:19","NUM":15},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"乐虎券 单品促销","USER_NAME":"admin","CREATETIME":"2015-06-08 19:18:06","NUM":16},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"这是虚拟商品","USER_NAME":"admin","CREATETIME":"2015-05-31 16:16:22","NUM":17},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"这是虚拟商品","USER_NAME":"admin","CREATETIME":"2015-05-31 16:13:37","NUM":18},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"这是虚拟商品","USER_NAME":"admin","CREATETIME":"2015-05-31 16:13:15","NUM":19},{"PHONE":"18271632291","USER_URL":null,"GIFT_NAME":"这是虚拟商品","USER_NAME":"admin","CREATETIME":"2015-05-31 16:13:07","NUM":20}]
     * curTime : 2015-06-14 17:26:40
     * GRADE_EXPLAN :
     * GROWTH_VALUE : 0
     * NEXT_GROWTH_VALUE : 5000
     * type : 1
     * msg : 成功
     */
    private List<CategoryDataEntity> categoryData;
    private String WORD;
    private List<RecordDataEntity> recordData;
    private String curTime;
    private String GRADE_EXPLAN;
    private int GROWTH_VALUE; //用户目前有的成长值
    private int NEXT_GROWTH_VALUE;//达到下一个等级需要的成长值
    private String NEXT_GROWTH_NEED;//升级到下一级需要的成长值
    private String NEXT_GROWTH_NAME; //下一级等级的等级名称
    private int type;
    private String msg;

    public String getNEXT_GROWTH_NEED() {
        return NEXT_GROWTH_NEED;
    }

    public void setNEXT_GROWTH_NEED(String NEXT_GROWTH_NEED) {
        this.NEXT_GROWTH_NEED = NEXT_GROWTH_NEED;
    }

    public String getNEXT_GROWTH_NAME() {
        return NEXT_GROWTH_NAME;
    }

    public void setNEXT_GROWTH_NAME(String NEXT_GROWTH_NAME) {
        this.NEXT_GROWTH_NAME = NEXT_GROWTH_NAME;
    }

    public void setCategoryData(List<CategoryDataEntity> categoryData) {
        this.categoryData = categoryData;
    }

    public void setWORD(String WORD) {
        this.WORD = WORD;
    }

    public void setRecordData(List<RecordDataEntity> recordData) {
        this.recordData = recordData;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setGRADE_EXPLAN(String GRADE_EXPLAN) {
        this.GRADE_EXPLAN = GRADE_EXPLAN;
    }

    public void setGROWTH_VALUE(int GROWTH_VALUE) {
        this.GROWTH_VALUE = GROWTH_VALUE;
    }

    public void setNEXT_GROWTH_VALUE(int NEXT_GROWTH_VALUE) {
        this.NEXT_GROWTH_VALUE = NEXT_GROWTH_VALUE;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CategoryDataEntity> getCategoryData() {
        return categoryData;
    }

    public String getWORD() {
        return WORD;
    }

    public List<RecordDataEntity> getRecordData() {
        return recordData;
    }

    public String getCurTime() {
        return curTime;
    }

    public String getGRADE_EXPLAN() {
        return GRADE_EXPLAN;
    }

    public int getGROWTH_VALUE() {
        return GROWTH_VALUE;
    }

    public int getNEXT_GROWTH_VALUE() {
        return NEXT_GROWTH_VALUE;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public class CategoryDataEntity {
        /**
         * NAME : 1243555555555555555534444444444444444444444444444444444444444444444
         * ID : 61
         */
        private String NAME;
        private int ID;

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getNAME() {
            return NAME;
        }

        public int getID() {
            return ID;
        }
    }

    public class RecordDataEntity  implements Serializable {
        /**
         * PHONE : 13265483619
         * USER_URL : attached/user21153/image/2015/20150513/201505131444599884.jpg
         * GIFT_NAME : 23452354623
         * USER_NAME : ceshi
         * CREATETIME : 2015-06-12 17:00:47
         * NUM : 1
         */
        private String PHONE;
        private String USER_URL;
        private String GIFT_NAME;
        private String USER_NAME;
        private String CREATETIME;
        private int NUM;

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public void setUSER_URL(String USER_URL) {
            this.USER_URL = USER_URL;
        }

        public void setGIFT_NAME(String GIFT_NAME) {
            this.GIFT_NAME = GIFT_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public void setCREATETIME(String CREATETIME) {
            this.CREATETIME = CREATETIME;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public String getPHONE() {
            return PHONE;
        }

        public String getUSER_URL() {
            return USER_URL;
        }

        public String getGIFT_NAME() {
            return GIFT_NAME;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public String getCREATETIME() {
            return CREATETIME;
        }

        public int getNUM() {
            return NUM;
        }
    }
}
