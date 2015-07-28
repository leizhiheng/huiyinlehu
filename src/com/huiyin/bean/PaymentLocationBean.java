package com.huiyin.bean;

import java.util.List;

public class PaymentLocationBean {

    private List<CityListEntity> cityList; //省市列表
    private String curTime;
    private int type;
    private String msg;

    public void setCityList(List<CityListEntity> cityList) {
        this.cityList = cityList;
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

    public List<CityListEntity> getCityList() {
        return cityList;
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

    public class CityListEntity {
        private List<ValueEntity> value; //省对应的市区名称以及编码
        private String key;  //省份

        public void setValue(List<ValueEntity> value) {
            this.value = value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<ValueEntity> getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }

        public class ValueEntity {
            /**
             * NAME : 广州
             * CODE : 123
             */
            private String NAME;
            private String CODE;
            private String MERCHANT_NO;
            private String TERMINAL_NO;

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public void setCODE(String CODE) {
                this.CODE = CODE;
            }

            public String getNAME() {
                return NAME;
            }

            public String getCODE() {
                return CODE;
            }

            public String getMERCHANT_NO() {
                return MERCHANT_NO;
            }

            public void setMERCHANT_NO(String MERCHANT_NO) {
                this.MERCHANT_NO = MERCHANT_NO;
            }

            public String getTERMINAL_NO() {
                return TERMINAL_NO;
            }

            public void setTERMINAL_NO(String TERMINAL_NO) {
                this.TERMINAL_NO = TERMINAL_NO;
            }
        }
    }
}
