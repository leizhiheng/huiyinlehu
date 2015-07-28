package com.huiyin.bean;

/**
 * 预约类型
 * 
 * @author 刘远祺
 * 
 */
public class ReservationType {
	
	public String RESERVATIONTYP_ID; 	// 类型id
	
	public String RESERVATIONTYP_NAME; 	// 类型名称
	
	public ReservationType(){}
	
	public ReservationType(String id, String name){
		this.RESERVATIONTYP_ID = id;
		this.RESERVATIONTYP_NAME = name;
	};
}
