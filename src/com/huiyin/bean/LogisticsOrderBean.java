package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 退换货订单物流
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-10
 */
public class LogisticsOrderBean extends BaseBean {
	
	private static final long serialVersionUID = 1L;
	
	public String COMPANY_NO;       //快递公司编号
    public String DELIVERY_CODE;    //如果是第三方快递就是快递单号，如果是乐虎快递就是联系电话。
    public String COMPANY_NAME;     //快递公司名称
    public String context;          //乐虎快递的物流跟踪信息
    public String ftime;            //乐虎快递的物流时间
    
    public Returnval RETURNVAL;		//物流信息
    

    
    public class Returnval implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public String status;		//监控中
    	 public String message;		//消息
    	 public String state;		//已签收
    	 public String com;			//天天快递
    	 public String nu;			//886143084825(订单编号)
    	 
    	 public List<DataEntity> data;   //第三方快递的物流跟踪信息
    	 
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
    
    public class DataEntity implements Serializable{
		private static final long serialVersionUID = 1L;
		public String time;         //第三方物流跟踪时间 e.g： 2015-06-11 13:29:38
        public String areaName;     //第三方物流跟踪城市
        public String areaCode;     //第三方物流跟踪城市编号
        public String status;       //第三方物流状态
        public String context;      //第三方物流跟踪详细内容
        public String ftime;        //第三方物流跟踪时间 e.g： 2015-06-11 13:29:38
        
    }

}
