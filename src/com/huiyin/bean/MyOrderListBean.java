package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 我的订单列表
 * Created by lian on 2015/5/21.
 */
public class MyOrderListBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<GoodList> orderList;//订单列表
}

