package com.huiyin.bean;

import java.io.Serializable;

/**
 * Created by Mike on 2015/5/30.
 */
public class FillOrderBean extends BaseBean {

	private String is_consumption;
	private String shoppingType;
	private String shoppingId;
	private ShoppingCarDataBaseBeanNew.ShoppingCarDataReturnMap cart;
	private BaseInfoEntity baseInfo;

	public void setCart(ShoppingCarDataBaseBeanNew.ShoppingCarDataReturnMap cart) {
		this.cart = cart;
	}

	public void setBaseInfo(BaseInfoEntity baseInfo) {
		this.baseInfo = baseInfo;
	}

	public ShoppingCarDataBaseBeanNew.ShoppingCarDataReturnMap getCart() {
		return cart;
	}

	public BaseInfoEntity getBaseInfo() {
		return baseInfo;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIs_consumption() {
		return is_consumption;
	}

	public void setIs_consumption(String is_consumption) {
		this.is_consumption = is_consumption;
	}

	public String getShoppingType() {
		return shoppingType;
	}

	public void setShoppingType(String shoppingType) {
		this.shoppingType = shoppingType;
	}

	public String getShoppingId() {
		return shoppingId;
	}

	public void setShoppingId(String shoppingId) {
		this.shoppingId = shoppingId;
	}

	public class BaseInfoEntity implements Serializable {

		private String FLAG_VAT;                // >0有增值税发票，需要请求后台接口
		private String VAT;// >0表示可以开增值税发票,否则不可以（是否可以增值税发票）先判断是否可以开增值税发票，如果可以，再判断有误增值税发票
		private String deliveryTime;            // 送货时间
		private String INTEGRAL;
		private String ORDER_CASH;
		private String INTEGRAL_UPPER_LIMIT;
		private String MAX_UPPER_LIMIT;  //按比例最多可以使用积分抵扣的金额,单位元
		private String TICKET_NUM;
		private String USER_INTEGRAL;
		private String CASH;
		private String FLAG_INTEGRAL_DEDUCTION;
		private String goodsSumfreightStr;   //总运费
		private AddressMapEntity addressMap;
		// add by zhyao @2015/5/8 添加当前用户红包总额
		private String USER_BONUS;// 用户红包总额
		// TODO 调整了数据结构
		// add by zhyao @2015/5/8 添加当前订单最大使用红包
		private String totalBonus;// 当前订单可最大使用红包

		public String getUSER_BONUS() {
			return USER_BONUS;
		}

		public void setUSER_BONUS(String uSER_BONUS) {
			USER_BONUS = uSER_BONUS;
		}

		public String getVAT() {
			return VAT;
		}

		public void setVAT(String VAT) {
			this.VAT = VAT;
		}

		public void setFLAG_VAT(String FLAG_VAT) {
			this.FLAG_VAT = FLAG_VAT;
		}

		public void setDeliveryTime(String deliveryTime) {
			this.deliveryTime = deliveryTime;
		}

		public void setINTEGRAL(String INTEGRAL) {
			this.INTEGRAL = INTEGRAL;
		}

		public void setORDER_CASH(String ORDER_CASH) {
			this.ORDER_CASH = ORDER_CASH;
		}

		public void setINTEGRAL_UPPER_LIMIT(String INTEGRAL_UPPER_LIMIT) {
			this.INTEGRAL_UPPER_LIMIT = INTEGRAL_UPPER_LIMIT;
		}

		public void setTICKET_NUM(String TICKET_NUM) {
			this.TICKET_NUM = TICKET_NUM;
		}

		public void setUSER_INTEGRAL(String USER_INTEGRAL) {
			this.USER_INTEGRAL = USER_INTEGRAL;
		}

		public void setCASH(String CASH) {
			this.CASH = CASH;
		}

		public void setFLAG_INTEGRAL_DEDUCTION(String FLAG_INTEGRAL_DEDUCTION) {
			this.FLAG_INTEGRAL_DEDUCTION = FLAG_INTEGRAL_DEDUCTION;
		}

		public void setAddressMap(AddressMapEntity addressMap) {
			this.addressMap = addressMap;
		}

		public String getFLAG_VAT() {
			return FLAG_VAT;
		}

		public String getDeliveryTime() {
			return deliveryTime;
		}

		public String getINTEGRAL() {
			return INTEGRAL;
		}

		public String getORDER_CASH() {
			return ORDER_CASH;
		}

		public String getINTEGRAL_UPPER_LIMIT() {
			return INTEGRAL_UPPER_LIMIT;
		}

		public String getTICKET_NUM() {
			return TICKET_NUM;
		}

		public String getUSER_INTEGRAL() {
			return USER_INTEGRAL;
		}

		public String getCASH() {
			return CASH;
		}

		public String getFLAG_INTEGRAL_DEDUCTION() {
			return FLAG_INTEGRAL_DEDUCTION;
		}

		public AddressMapEntity getAddressMap() {
			return addressMap;
		}

		public String getTotalBonus() {
			return totalBonus;
		}

		public void setTotalBonus(String totalBonus) {
			this.totalBonus = totalBonus;
		}

        public String getGoodsSumfreightStr() {
            return goodsSumfreightStr;
        }

        public void setGoodsSumfreightStr(String goodsSumfreightStr) {
            this.goodsSumfreightStr = goodsSumfreightStr;
        }

        public String getMAX_UPPER_LIMIT() {
            return MAX_UPPER_LIMIT;
        }

        public void setMAX_UPPER_LIMIT(String MAX_UPPER_LIMIT) {
            this.MAX_UPPER_LIMIT = MAX_UPPER_LIMIT;
        }

        public class AddressMapEntity implements Serializable {
			/**
			 * PHONE : 18589055417 NAME : 发卡机构和 ADDRESS : 安徽安庆大观区任天野 ID : 5713
			 */
			private String PHONE;
			private String NAME;
			private String ADDRESS;
			private String ID;

			public void setPHONE(String PHONE) {
				this.PHONE = PHONE;
			}

			public void setNAME(String NAME) {
				this.NAME = NAME;
			}

			public void setADDRESS(String ADDRESS) {
				this.ADDRESS = ADDRESS;
			}

			public void setID(String ID) {
				this.ID = ID;
			}

			public String getPHONE() {
				return PHONE;
			}

			public String getNAME() {
				return NAME;
			}

			public String getADDRESS() {
				return ADDRESS;
			}

			public String getID() {
				return ID;
			}
		}
	}
}
