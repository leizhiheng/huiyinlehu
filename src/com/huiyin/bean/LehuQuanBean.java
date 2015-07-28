package com.huiyin.bean;

import java.util.List;

/**
 * Created by Mike on 2015/5/31.
 */
public class LehuQuanBean {

    /**
     * ticketList : [{"LH_END_TIME":"2015-06-06 00:00:00","TICKET_CODE":"LH235250874300000","CONDITION1":10,"CONDITION2":2,"RANGES":5,"ID":55653,"LH_START_TIME":"2015-05-25 00:00:00","NUM":1,"HQ_TYPE":1,"HQ_NAME":"全场满减"}]
     * totalPageNum : 1
     * curTime : 2015-05-31 17:26:18
     * type : 1
     * msg : 成功
     * pageIndex : 1
     */
    private List<TicketListEntity> ticketList;
    private int totalPageNum;
    private String curTime;
    private int type;
    private String msg;
    private int pageIndex;

    public void setTicketList(List<TicketListEntity> ticketList) {
        this.ticketList = ticketList;
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

    public List<TicketListEntity> getTicketList() {
        return ticketList;
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

    public class TicketListEntity {
        /**
         * LH_END_TIME : 2015-06-06 00:00:00
         * TICKET_CODE : LH235250874300000
         * CONDITION1 : 10
         * CONDITION2 : 2
         * RANGES : 5
         * ID : 55653
         * LH_START_TIME : 2015-05-25 00:00:00
         * NUM : 1
         * HQ_TYPE : 1
         * HQ_NAME : 全场满减
         */
        private String LH_END_TIME;
        private String TICKET_CODE;
        private int CONDITION1;
        private int CONDITION2;
        private int RANGES;
        private int ID;
        private String LH_START_TIME;
        private int NUM;
        private int HQ_TYPE;
        private String HQ_NAME;

        public void setLH_END_TIME(String LH_END_TIME) {
            this.LH_END_TIME = LH_END_TIME;
        }

        public void setTICKET_CODE(String TICKET_CODE) {
            this.TICKET_CODE = TICKET_CODE;
        }

        public void setCONDITION1(int CONDITION1) {
            this.CONDITION1 = CONDITION1;
        }

        public void setCONDITION2(int CONDITION2) {
            this.CONDITION2 = CONDITION2;
        }

        public void setRANGES(int RANGES) {
            this.RANGES = RANGES;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setLH_START_TIME(String LH_START_TIME) {
            this.LH_START_TIME = LH_START_TIME;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public void setHQ_TYPE(int HQ_TYPE) {
            this.HQ_TYPE = HQ_TYPE;
        }

        public void setHQ_NAME(String HQ_NAME) {
            this.HQ_NAME = HQ_NAME;
        }

        public String getLH_END_TIME() {
            return LH_END_TIME;
        }

        public String getTICKET_CODE() {
            return TICKET_CODE;
        }

        public int getCONDITION1() {
            return CONDITION1;
        }

        public int getCONDITION2() {
            return CONDITION2;
        }

        public int getRANGES() {
            return RANGES;
        }

        public int getID() {
            return ID;
        }

        public String getLH_START_TIME() {
            return LH_START_TIME;
        }

        public int getNUM() {
            return NUM;
        }

        public int getHQ_TYPE() {
            return HQ_TYPE;
        }

        public String getHQ_NAME() {
            return HQ_NAME;
        }
    }
}
