package com.huiyin.bean;

/**
 * Created by Mike on 2015/6/2.
 */
public class InvoiceAddBean {

    /**
     * curTime : 2015-06-02 16:01:34
     * vat : {"BANK":"1","INVOICE_TITLE":"我我我","COLLECTOR_ADDRESS":"1","COLLECTOR_PHONE":"1","REGISTERED_ADDRESS":"1","COMPANY_NAME":"","ID":"3117","IDENTIFICATION_NUMBER":"1","COLLECTOR_NAME":"1","INVOICE_CONTENT":"","ACCOUNT":"1","REGISTERED_PHONE":"1"}
     * type : 1
     * msg : 成功
     */
    private String curTime;
    private VatEntity vat;
    private int type;
    private String msg;

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setVat(VatEntity vat) {
        this.vat = vat;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurTime() {
        return curTime;
    }

    public VatEntity getVat() {
        return vat;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public class VatEntity {
        /**
         * BANK : 1
         * INVOICE_TITLE : 我我我
         * COLLECTOR_ADDRESS : 1
         * COLLECTOR_PHONE : 1
         * REGISTERED_ADDRESS : 1
         * COMPANY_NAME :
         * ID : 3117
         * IDENTIFICATION_NUMBER : 1
         * COLLECTOR_NAME : 1
         * INVOICE_CONTENT :
         * ACCOUNT : 1
         * REGISTERED_PHONE : 1
         */
        private String BANK;
        private String INVOICE_TITLE;
        private String COLLECTOR_ADDRESS;
        private String COLLECTOR_PHONE;
        private String REGISTERED_ADDRESS;
        private String COMPANY_NAME;
        private String ID;
        private String IDENTIFICATION_NUMBER;
        private String COLLECTOR_NAME;
        private String INVOICE_CONTENT;
        private String ACCOUNT;
        private String REGISTERED_PHONE;
        private String IMG;

        public void setBANK(String BANK) {
            this.BANK = BANK;
        }

        public void setINVOICE_TITLE(String INVOICE_TITLE) {
            this.INVOICE_TITLE = INVOICE_TITLE;
        }

        public void setCOLLECTOR_ADDRESS(String COLLECTOR_ADDRESS) {
            this.COLLECTOR_ADDRESS = COLLECTOR_ADDRESS;
        }

        public void setCOLLECTOR_PHONE(String COLLECTOR_PHONE) {
            this.COLLECTOR_PHONE = COLLECTOR_PHONE;
        }

        public void setREGISTERED_ADDRESS(String REGISTERED_ADDRESS) {
            this.REGISTERED_ADDRESS = REGISTERED_ADDRESS;
        }

        public void setCOMPANY_NAME(String COMPANY_NAME) {
            this.COMPANY_NAME = COMPANY_NAME;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public void setIDENTIFICATION_NUMBER(String IDENTIFICATION_NUMBER) {
            this.IDENTIFICATION_NUMBER = IDENTIFICATION_NUMBER;
        }

        public void setCOLLECTOR_NAME(String COLLECTOR_NAME) {
            this.COLLECTOR_NAME = COLLECTOR_NAME;
        }

        public void setINVOICE_CONTENT(String INVOICE_CONTENT) {
            this.INVOICE_CONTENT = INVOICE_CONTENT;
        }

        public void setACCOUNT(String ACCOUNT) {
            this.ACCOUNT = ACCOUNT;
        }

        public void setREGISTERED_PHONE(String REGISTERED_PHONE) {
            this.REGISTERED_PHONE = REGISTERED_PHONE;
        }

        public String getBANK() {
            return BANK;
        }

        public String getINVOICE_TITLE() {
            return INVOICE_TITLE;
        }

        public String getCOLLECTOR_ADDRESS() {
            return COLLECTOR_ADDRESS;
        }

        public String getCOLLECTOR_PHONE() {
            return COLLECTOR_PHONE;
        }

        public String getREGISTERED_ADDRESS() {
            return REGISTERED_ADDRESS;
        }

        public String getCOMPANY_NAME() {
            return COMPANY_NAME;
        }

        public String getID() {
            return ID;
        }

        public String getIDENTIFICATION_NUMBER() {
            return IDENTIFICATION_NUMBER;
        }

        public String getCOLLECTOR_NAME() {
            return COLLECTOR_NAME;
        }

        public String getINVOICE_CONTENT() {
            return INVOICE_CONTENT;
        }

        public String getACCOUNT() {
            return ACCOUNT;
        }

        public String getREGISTERED_PHONE() {
            return REGISTERED_PHONE;
        }

        public String getIMG() {
            return IMG;
        }

        public void setIMG(String IMG) {
            this.IMG = IMG;
        }
    }
}
