package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * Created by Mike on 2015/7/2.
 */
public class LogisticsBean extends BaseBean {
    
	private static final long serialVersionUID = 1L;

	/**物流信息**/
    public List<OrderLogisticsEntity> orderLogistics;

    /**one节点信息**/
    public OtherEntity one;
    
    /**two节点信息**/
    public OtherEntity two;
    
    /**three节点信息**/
    public OtherEntity three;
    
    
    /**物流实体类**/
    public class OrderLogisticsEntity implements Serializable{

        public String COMPANY_NO;       //快递公司编号
        public String DELIVERY_CODE;    //如果是第三方快递就是快递单号，如果是乐虎快递就是联系电话。
        public String COMPANY_NAME;     //快递公司名称
        public String context;          //乐虎快递的物流跟踪信息
        public String ftime;            //乐虎快递的物流时间
        public List<DataEntity> data;   //第三方快递的物流跟踪信息

        public OtherEntity one,two,three;

		public List<DataEntity> getLogisticList() {
			if(null != data && data.size() > 0){
				
				//非自营
				return data;
			}else{
				
				//自营,返回物流公司的信息
				List<DataEntity> list = new ArrayList<DataEntity>();
				DataEntity entity = new DataEntity();
				
				entity.context = context;
				entity.ftime = ftime;
				entity.areaName = COMPANY_NAME;
				
				list.add(entity);
				return list;
			}
		}
    }
    
    // one, two, three 节点对应的类
	public class OtherEntity {
		public String time;
		public String desc;
		
		public boolean isEmpty(){
			return TextUtils.isEmpty(time) || TextUtils.isEmpty(desc);
		}
	}

	/**
	 * 设置one two three节点数据(订单物流状态)
	 */
	public void setOneTwoThree() {
		
		OrderLogisticsEntity logistic = null;
		for(int i=0; i<orderLogistics.size(); i++){
			
			logistic = orderLogistics.get(i);
			if(null != one && !one.isEmpty()){
				logistic.one = one;
			}
			if(null != two && !two.isEmpty()){
				logistic.two = two;
			}
			if(null != three && !three.isEmpty()){
				logistic.three = three;
			}
		}
	}
}
