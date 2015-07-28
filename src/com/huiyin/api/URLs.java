package com.huiyin.api;
public class URLs {

	// 乐虎二期地址

	//正式站点
//	public static String SERVER_URL = "http://app.lehumall.com/";
//	public static String IMAGE_URL = "http://www.lehumall.com/";

	//仿真环境
//    public static String SERVER_URL = "http://test.lehumall.com/hy/";
//	public static String IMAGE_URL = "http://www.lehumall.com/";

//	 public static String SERVER_URL = "http://192.168.200.47:8080/hy/";
	//public static String SERVER_URL = "http://192.168.200.100:8080/hy/";
	//public static String QINIU_URL = "http://7xjs9u.com1.z0.glb.clouddn.com";
	//测试环境
//	public static String SERVER_URL = "http://d5.java.shovesoft.com/hy/";
//	public static String IMAGE_URL = "http://d5.java.shovesoft.com/hyds/";

//	// 朱念
	public static String SERVER_URL = "http://192.168.3.46:8080/hy/";
	public static String IMAGE_URL = "http://d5.java.shovesoft.com/hyds/";

	// 刘隆煌
//	public static String SERVER_URL = "http://192.168.3.35:8080/hy/";
//	public static String IMAGE_URL = "http://d5.java.shovesoft.com/hyds/";

	// 王磊
//	 public static String SERVER_URL = "http://192.168.3.30:8080/hy/";
//	 public static String IMAGE_URL = "http://d5.java.shovesoft.com/hyds/";
	// 刘作栋
//	 public static String SERVER_URL = "http://172.16.15.101:8080/hy/";
//	 public static String IMAGE_URL = "http://172.16.15.101:8080/hyds";
	
	// added by xieshibin,begin
	 
	// add by zhyao @2015/7/8 七牛云服务地址(用于播放秀场mp3文件，于上传无关)
	public static String QINIU_URL = "http://7xk7kd.com2.z0.glb.qiniucdn.com/";
	// add by zhyao @2015/5/8  添加银行WAP支付地址
	//银行wap支付
	public static final String BANK_WAP_PAY_URL = SERVER_URL + "bank/doPay.do?orderAmount=%1$s&orderNo=%2$s&bankType=%3$s";
	//中行支付完成返回商城回调地址
	public static final String BANK_WAP_CHAIN_CALLBACK_URL = SERVER_URL + "bank/bocCallback.do";
	//支付完成返回商城回调地址
	public static final String BANK_WAP_CONSTRUCTION_CALLBACK_URL = "http://www.lehumall.com/ccbPayAction.do";

	public static final String SHOUYE_URL = SERVER_URL + "appOneCategoryListings.do";
	
	//老接口(用户登录)-废弃
	public static final String APPLOGIN = SERVER_URL + "appLogin.do";
	//新接口(用户登录)-废弃
	public static final String LOGIN = SERVER_URL + "login.do";
	//新接口-去掉了猜你喜欢(用户登录)-废弃
	public static final String LOGINLOAD = SERVER_URL + "loginLoad.do";
	//新接口-去掉了猜你喜欢(用户登录)
	public static final String LOGINOVERLOAD = SERVER_URL + "loginOverLoad.do";
	
	
	//添加登录日志
	public static final String ADDUSERLOGINLOG = SERVER_URL + "addUserLoginLog.do";
	
	
	//老接口(获取用户详情信息)-废弃
	public static final String MYLH = SERVER_URL + "appMyLH.do";
	
	//新接口(获取用户详情信息)-废弃
	public static final String MYLHINFO = SERVER_URL + "myLHInfo.do";
	
	//新接口(获取用户详情信息)
	public static final String MYLHINFOLOAD = SERVER_URL + "myLHInfoLoad.do";
	
	//用户中心猜你喜欢
	public static final String MAYBELIKEBYUSER = SERVER_URL + "mayBeLikeByUser.do";
		
	
	//签到
	public static final String SIGN = SERVER_URL + "sign.do";
	
	//乐虎券列表
	public static final String TICKETDATA = SERVER_URL + "ticketData.do";
	
	//领取乐虎券
	public static final String GETLHTICKET = SERVER_URL + "getLHTicket.do";
	
	
	
	//老接口
	//public static final String REGISTER = SERVER_URL + "appRegister.do";					
	public static final String REGISTER = SERVER_URL + "register.do";
	
	//注册，不带user信息(new)
	public static final String REGISTERLOAD = SERVER_URL + "registerLoad.do";						
	
	public static final String GET_REGEIDT_CODE = SERVER_URL + "appMessageVerify.do";
	public static final String POST_CODE = SERVER_URL + "appFindPasswordCode.do";
	public static final String GET_POINT = SERVER_URL + "appMyIntegralNew.do";
	
	//修改手机号
	public static final String AMENDPHONE = SERVER_URL + "amendPhone.do";
	
	// add by zhyao @2015/5/8  添加红包明细
	public static final String GET_BOUNS = SERVER_URL + "appMyBonus.do";//获取我的红包明细
	public static final String LEHUJUAN_LIST = SERVER_URL + "appMyCoupon.do";
	// 乐虎券删除
	public static final String appMyCouponDelete = SERVER_URL + "appMyCouponDelete.do";
	public static final String COLLECT_LIST = SERVER_URL + "appMyCollect.do";
	public static final String SYSTEMMESSAGE_LIST = SERVER_URL + "appSystemMsg.do";
	public static final String SYSTEMMESSAGE_LIST_S = SERVER_URL + "letterData.do";
	public static final String SYSTEMMESSAGE_STUATS = SERVER_URL + "appLetterReader.do";
	public static final String CANCEL_FOUCS = SERVER_URL + "appCancelCollect.do";
	public static final String ADDR_LIST = SERVER_URL + "appMyShippingAddress.do";
	public static final String ADDR_DELETE = SERVER_URL + "appDeleteShippingAddress.do";
	public static final String ADDR_MODIFY = SERVER_URL + "appAddOrUpdateMyShippingAddress.do";

	public static final String MSG_TYPE = SERVER_URL + "appGuestbookTypeList.do";
	public static final String MAKE_MSG = SERVER_URL + "appAddGuestbook.do";
	public static final String YUYUE_MSG = SERVER_URL + "appOrderBespeakByOrder.do";
	public static final String YUYUE_ADD = SERVER_URL + "appAddReservation.do";
	public static final String appAddBespeak = SERVER_URL + "appAddBespeak.do";
	public static final String FIND_PSW = SERVER_URL + "appFindPasswordSave.do";
//	public static final String ORDERlIST = SERVER_URL + "appAllOrder.do";
	public static final String ORDERlIST = SERVER_URL + "myOrder.do";//订单
	public static final String ONLINE_HELP = SERVER_URL + "appHelp.do";
	public static final String ORDER_DETAIL = SERVER_URL + "orderDetail.do";//订单详情
//	public static final String ORDER_DETAIL = SERVER_URL + "appOrderDetail.do";
	public static final String COMMENT = SERVER_URL + "appCommodityComments.do";
	public static final String ORDER_COMMENT = SERVER_URL + "goodsComments.do";
	public static final String INTGRAL_EXPLAIN = SERVER_URL + "appIntgralexplain.do";
	public static final String BANNER_INTRODUCE = SERVER_URL + "appBannerIntroduce.do";

	public static final String BACK_GOOD = SERVER_URL + "appCommodityReturn.do";
	public static final String CHANGE_GOOD = SERVER_URL + "appExchangeCommodity.do";
	public static final String WRITE_LOGISTIC = SERVER_URL + "appCommodityReturnLogistics.do";
	public static final String YUYUE_INIT = SERVER_URL + "appBespeakOrderDetail.do";
	public static final String YUYUE_CANCEL = SERVER_URL + "appBespeakOrderCanal.do";
	public static final String AFTER_DETAIL = SERVER_URL + "appCommodityReturnDetail.do";
	public static final String ORDER_RECORD_DATA = SERVER_URL + "orderRecordData.do";//售后预约记录/退换货记录
	public static final String MODIFY_USERNAME = SERVER_URL + "appUpdateUserName.do";
	public static final String BIND_PHONE = SERVER_URL + "appBindPhone.do";
	public static final String DELETE_ORDER = SERVER_URL + "delOrder.do";
	public static final String CANCEL_ORDER = SERVER_URL + "appCanalExchange.do";
	public static final String RECEIVE_ORDER = SERVER_URL + "appDevliyExchange.do";
	public static final String HELP_TITLE = SERVER_URL + "appHelpTitle.do";
	public static final String SERVICE_AGGREMENT = SERVER_URL + "appServiceCcontract.do";
	public static final String ORDER_CANCEL = SERVER_URL + "canalOrder.do";
//	public static final String ORDER_SURE = SERVER_URL + "appVerifyOrder.do";
	public static final String ORDER_SURE = SERVER_URL + "confirmOrder.do";//确认收货
	public static final String ORDER_BACK_CANCEL = SERVER_URL + "appCanalReturn.do";
	public static final String ORDER_MONEY_RETURN = SERVER_URL + "appDeliverReturn.do";

	//预约信息
	public static final String ORDER_BESPEAK_INFO = SERVER_URL + "orderBespeakInfo.do";
	
	//预约新增
	public static final String ORDER_ADD_BESPEAK = SERVER_URL + "addBespeak.do";
	
	//换货详情
	public static final String ORDER_REPLACEDETAIL = SERVER_URL + "replaceDetail.do";
	
	//修改换货
	public static final String ORDER_AMENDREPLACE = SERVER_URL + "amendReplace.do";
	
	//换货新增
	public static final String ORDER_APPLYREPLACE = SERVER_URL + "applyReplace.do";
	
	//申请退货
	public static final String ORDER_ADDRETURN = SERVER_URL + "addReturn.do";
	
	//修改换货初始化
	public static final String ORDER_AMENDREPLACEINIT = SERVER_URL + "amendReplaceInit.do";
	
	//修改退货初始化
	public static final String ORDER_AMENDRETURNINIT = SERVER_URL + "amendReturnInit.do";
		
	//修改退货
	public static final String ORDER_AMENDRETURN = SERVER_URL + "amendReturn.do";
	
	//退货详情
	public static final String ORDER_RETURNDETAIL = SERVER_URL + "returnDetail.do";
	
	
	
	//查看评价
	public static final String ORDER_COMMENTSVIEW = SERVER_URL + "commentsView.do";
	
	//追加评价
	public static final String ORDER_GOODSCOMMENTSADD = SERVER_URL + "goodsCommentsAdd.do";
		
	
	// 购物车
	public static final String SHOP_LIST = SERVER_URL + "queryShoppingCar.do";
	public static final String SHOP_PAY = SERVER_URL + "payMethodList.do";
	public static final String WRITE_ORDER = SERVER_URL + "queryOrderInit.do";
	public static final String DELETE_SHOPCAR = SERVER_URL + "deleteCart.do";
//	public static final String DELETE_SHOPCAR = SERVER_URL + "deleteShoppingCar.do";
	public static final String MODIFY_ORDER = SERVER_URL + "updateShoppingCarNum.do";
	public static final String SHOP_UNLOGIN = SERVER_URL + "submitOrders.do";
	public static final String IVOICE_INFO = SERVER_URL + "addInvoice.do";//去掉了一个s
//	public static final String CODE_VALIDATE = SERVER_URL + "activationDiscountCouponCode.do";
	public static final String CODE_VALIDATE = SERVER_URL + "activateCoupon.do";
	public static final String COMMIT_ORDER = SERVER_URL + "confirmOrderSubmit.do";

	//商品详情加入购物车
	public static final String addShoppingCart = SERVER_URL + "addCart.do";
	//更新购物车商品的选中
	public static final String ajaxCart = SERVER_URL + "ajaxCart.do";
	//更新数量
	public static final String updateShoppingCartNum = SERVER_URL + "updateCartNum.do";
	//查询购物车
	public static final String shoppingCatData = SERVER_URL + "cartInit.do";
	//修改购物车商品促销方式
	public static final String updatePromotionType = SERVER_URL + "updateCartPromotionType.do";
	//修改购物车商品满赠活动商品
	public static final String updateShopppingCartPromotionGift = SERVER_URL + "updateShopppingCartPromotionGift.do";
	//查询满赠的商品
	public static final String donateData = SERVER_URL + "donateData.do";
	//删除满赠的商品
	public static final String deleteCartPromotionGift = SERVER_URL + "deleteCartPromotionGift.do";
	//购物车提交（进入填写订单界面）
	public static final String submintCart = SERVER_URL + "submitOrderInit.do";

	// added by xieshibin,end

	public static final String appHeadlines = SERVER_URL + "appHeadlines.do";
	// 专区
	public static final String appPrefecture = SERVER_URL + "appPrefecture.do";

	// 秀场喜欢
	public static final String appLike = SERVER_URL + "appLike.do";
	// 秀场添加喜欢
	public static final String appShowLike = SERVER_URL + "appShowLike.do";
	// 秀场评论
	public static final String appAppraise = SERVER_URL + "appAppraise.do";
	// 秀场评论
	public static final String appAddAppraise = SERVER_URL + "appAddAppraise.do";
	// 秀场 1推荐0全部
	public static final String appShow = SERVER_URL + "appShow.do";
	// 秀场 全部搜索
	public static final String appShowSearch = SERVER_URL + "appShowSearch.do";
	// 秀场关注
	public static final String appShowAttention = SERVER_URL + "appShowAttention.do";
	// 秀场添加关注
	public static final String appAttention = SERVER_URL + "appAttention.do";
	// 秀场取消关注
	public static final String appCancelAttention = SERVER_URL + "appCancelAttention.do";
	// 秀场发布
	public static final String appPublish = SERVER_URL + "appPublish.do";
	// add by zhyao @2015/6/29 删除我的秀场
	public static final String delAppShow = SERVER_URL + "delAppShow.do";

	// 智慧管家
	public static final String HOUSE_KEERER_shouye = SERVER_URL + "appQueryWisdom.do";
	public static final String HOUSE_KEERER = SERVER_URL + "appQueryWisdomList.do";
	public static final String HOUSE_KEERER_DETAILS = SERVER_URL + "appQueryWisdomById.do";
	public static final String HOUSE_KEERER_YUYUE = SERVER_URL + "appWisdom.do";
	public static final String HOUSE_YUYUE_TYPE = SERVER_URL + "appWidomJson.do";

	// 分类列表1
	public static final String SORT_LIST_MAIN = SERVER_URL + "appOneCategoryListings.do";
	// 分类里列表2
	public static final String SORT_LIST_LEVEL2 = SERVER_URL + "appTwoCategoryListings.do";

	// 三级分类列表商品详情页
	public static final String SORT_LIST_LEVEL3_GOODS_DETAILS = SERVER_URL + "appSearchCommdoityDataById.do";
	public static final String SORT_LIST_LEVEL3_GOODS_DETAILS_RECOMMENT = SERVER_URL + "appPictures.do?COMMODITY_ID=214";
	public static final String SORT_LIST_LEVEL3_GOODS_DETAILS_SHANDAN = SERVER_URL + "appCommodityReview.do";
	public static final String SORT_LIST_LEVEL3_GOODS_DETAILS_SHANDAN_RECOMMENT = SERVER_URL + "appSearchCommodityReview.do";
	public static final String SORT_LIST_LEVEL3_GOODS_DETAILS_SHOPPING_CAR = SERVER_URL + "addShoppingCar.do";
	public static final String BUY_NOW = SERVER_URL + "buyNow.do";
	public static final String cartPromptlyInit = SERVER_URL + "cartPromptlyInit.do";
	public static final String SORT_LIST_LEVEL3_GOODS_DETAILS_SHOPPING_CAR_add = SERVER_URL + "appSpecList.do?COMMDOITY_ID=262";
	public static final String DISCOUNT_PACKAGE = SERVER_URL + "goodsGroup.do";

	// 四级分类筛选

	public static final String SCTRRNING = SERVER_URL + "appClassifyCommodityType.do?THREE_ID=16";
	public static final String SCTRRNING_VALUE = SERVER_URL + "appClassifyCommodityTypeValue.do?PROPERTYID=492";

	public static final String SELECTTION_URL = SERVER_URL + "categoryAttribute.do";//

	public static final String SCTRRNING_VALUE_finish = SERVER_URL + "appFilter.do?BRAND_ID=21&propertyId=421,493";
	// 分类列表3
	public static final String SORT_LIST_LEVEL3 = SERVER_URL + "threeCategory.do";

	// 分类列表图片模式
//	public static final String SORT_LIST_LEVEL_TUPIAN = SERVER_URL + "appTabulationPicture.do";
	public static final String SORT_LIST_LEVEL_TUPIAN = SERVER_URL + "goodsCategoryList.do";//（新接口）

	// -------------------------------首页------------------------------------------
	public static final String appIndexFirst = SERVER_URL + "appNewIndexFirst.do";//（新接口）
//	public static final String appIndexFirst = SERVER_URL + "appIndexFirst.do";
	public static final String appIndexPoly = SERVER_URL + "appIndexPoly.do";
	public static final String appAllFast = SERVER_URL + "appAllFast.do";
	public static final String appAddFast = SERVER_URL + "appAddFast.do";

	public static final String appOpenPicture = SERVER_URL + "appOpenPicture.do";

	public static final String SALERANK = SERVER_URL + "appSecondaryClassification.do";
	public static final String SALERANK_list = SERVER_URL + "appTopTen.do";

	// 站内搜索
	public static final String SEARCH_HISTROYLIST = SERVER_URL + "appSearchCommodityHistory.do";
	public static final String SEARCH_INIT = SERVER_URL + "appSearchInit.do"; // 初始化url
	public static final String HOT_SEARCH = SERVER_URL + "appHotSearch.do";
	public static final String TN_URL = SERVER_URL + "alipayUnionpayRequest.do";
	public static final String ALIPAY_URL = SERVER_URL + "alipayAfterBlack.do";
	public static final String WXPAY_URL = SERVER_URL + "alipayWeixinNotify.do";
	public static final String ALIPAY_BEFORE = SERVER_URL + "alipayBefore.do";

	// -------------------------------周边商店地图------------------------------------------
	public static final String appNearbyStore = SERVER_URL + "appNearbyStore.do";

	// 微信支付
	public static final String URL_SIGN = SERVER_URL + "alipayWeixinRequest.do";
	public static final String WXPAY_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
	public static final String WXPAY_ORDER_INFO = "https://api.weixin.qq.com/pay/genprepay";

	public static final String GET_VERSION = SERVER_URL + "appUpdate.do";

	// 专区
	public static final String GET_ZHUANQU_DATE = SERVER_URL + "appPrefecture.do";

	// 积分Club
	public static final String GET_CLUB_DATA = SERVER_URL + "appIntegralClub.do";
	public static final String SUBMIT_CLUB_DATA = SERVER_URL + "appDeduct.do";

	// 二维码搜索
	public static final String scanningCode = SERVER_URL + "scanningCode.do";
	// 商品收藏与取消（新接口）
	public static final String appCollect = SERVER_URL + "addFocus.do";
	public static final String appCancelCollect = SERVER_URL + "cancelFocus.do";
//	public static final String appCollect = SERVER_URL + "appCollect.do";
//	public static final String appCancelCollect = SERVER_URL + "appCancelCollect.do";

	// 数据统计
	public static final String SEND_MOBILE_INFO = SERVER_URL + "appStatisticsInstall.do";

	// 运费
	public static final String FREESHIPPING = SERVER_URL + "appFreight.do";

	// 订单折扣
	public static final String ORDER_DISCOUNT = SERVER_URL + "appOrderDiscount.do";

	// 融云的请求接口
	public static final String RONGCLOUD_TOKEN = SERVER_URL + "appRongCloud.do";
	// 融云的debug key
	public static final String rongIoKey = "6tnym1brn8g57";
	// 融云的debug 客服用户Id
	public static final String customerServiceId = "KEFU1416397431684";

	// 物流查询的列表
	public static final String appQueryLogistics = SERVER_URL + "appQueryLogistics.do";
	// 物流公司
	public static final String appLogisticsInfo = SERVER_URL + "apll.do";
	// 物流车辆位置
	public static final String connected = SERVER_URL + "connected.do";
	// 便民生活卡消费记录
	public static final String appPrepaidCardsList = SERVER_URL + "appPrepaidCardsList.do";
	// 绑定服务卡
	public static final String appBindCardInfo = SERVER_URL + "appBindCardInfo.do";
	// 服务卡修改密码
	public static final String appUpdateCardPwd = SERVER_URL + "appUpdateCardPwd.do";
	// 申请服务卡
	public static final String appApplyCard = SERVER_URL + "appApplyCard.do";
	// 卡号验证
	public static final String appValidationCardInfo = SERVER_URL + "appValidationCardInfo.do";
	// 充值
	public static final String appAddRechargeOrder = SERVER_URL + "appAddRechargeOrder.do";
	// 解除绑定服务卡
	public static final String appRelieveBindCard = SERVER_URL + "appRelieveBindCard.do";
	// 用服务卡支付
	public static final String appCardRedeem = SERVER_URL + "appCardRedeem.do";
	// 充值优惠活动列表
	public static final String appCardPromotionsList = SERVER_URL + "appCardPromotionsList.do";

	// 验证是否能申请售后
	public static final String verifyAfter = SERVER_URL + "verifyAfter.do";

	// 自提门店列表
	public static final String appNearShop = SERVER_URL + "appNearShop.do";

	// 微信登陆
	public static final String appOpenIDOAuthPhone = SERVER_URL + "appOpenIDOAuthPhone.do";
	// 秒杀标题
//	public static final String seckillLateralTitle = SERVER_URL + "seckillLateralTitle.do";
	public static final String seckillLateralTitle = SERVER_URL + "seckillTitle.do";
	// 秒杀列表
	public static final String allSecKill = SERVER_URL + "allSecKill.do";
	// 秒杀列表 新的接口名
	public static final String seckillData = SERVER_URL + "seckillData.do";
	// 闪购列表
	public static final String flashSaleActive = SERVER_URL + "flashSaleActive.do";
	// 闪购专区
	public static final String flashSaleRegion = SERVER_URL + "flashSaleRegion.do";
	
	// 闪购专区(new)
	public static final String flashGoods = SERVER_URL + "flashGoods.do";
	
	// 新品预约分类
	public static final String bespeakType = SERVER_URL + "appBespeakType.do";
	// 新品预约列表（新接口）
	public static final String bespeak = SERVER_URL + "newGoodsList.do";
//	// 新品预约列表
//	public static final String bespeak = SERVER_URL + "appBespeak.do";
//	// 新品预约结果
//	public static final String bespeakRecord = SERVER_URL + "appBespeakRecord.do";
	// 新品预约结果（新接口）
	public static final String bespeakRecord = SERVER_URL + "newGoodsAddRecord.do";
	// 我的预约列表
	public static final String myNewGoodsData = SERVER_URL + "myNewGoodsData.do";
	// 介绍的统一接口
	public static final String introduceId = SERVER_URL + "appIntroduce.do";
	// 新的商品详情 从121开始（新接口）
	public static final String appCommdoityDataById = SERVER_URL + "goodsDetail.do";
	
	// 商品详情，只未取商品信息，包装参数，售后服务单独添加的新接口
	public static final String goodsContent = SERVER_URL + "goodsContent.do";
	
	
//	// 新的商品详情 从121开始
//	public static final String appCommdoityDataById = SERVER_URL + "appCommdoityDataById.do";
	// 购物车新接口
	public static final String queryShoppingCarNew = SERVER_URL + "queryShoppingCarNew.do";

	// 判断秒杀
	public static final String judgeSeckill = SERVER_URL + "judgeSeckill.do";
	// 发票详情
	public static final String invoiceInfo = SERVER_URL + "invoiceInfo.do";
	// 图片上传
	public static final String appImgUpload = SERVER_URL + "appImgUpload.do";
	// 会员等级
	public static final String appUserRank = SERVER_URL + "appUserRank.do";
	// 通过订单ID查询价格
	public static final String orderPrice = SERVER_URL + "orderPrice.do";
	// 乐虎红包申请记录
	public static final String queryTicketPacketList = SERVER_URL + "queryTicketPacketList.do";
	// 乐虎红包申请类型
	public static final String addTicketPacketInit = SERVER_URL + "addTicketPacketInit.do";
	// 乐虎红包申请
	public static final String addTicketPacket = SERVER_URL + "addTicketPacket.do";

	// 店铺主页
	public static final String storeIndex = SERVER_URL + "storeIndex.do";
	// 店铺详情
	public static final String storeDetail = SERVER_URL + "storeDetail.do";
	// 店铺商品分类
	public static final String storeCategory = SERVER_URL + "storeCategory.do";
	// 店铺商品列表
	public static final String goodsCategoryList = SERVER_URL + "goodsCategoryList.do";
	// 店铺促销列表
	public static final String storePromotion = SERVER_URL + "storePromotion.do";
	// 查询执照
	public static final String BLInfo = SERVER_URL + "BLInfo.do";
	// 品牌介绍
	public static final String storeNavigation = SERVER_URL + "storeNavigation.do";
	// 店铺关注
	public static final String addFocus = SERVER_URL + "addFocus.do";
	// 店铺取消关注
	public static final String cancelFocus = SERVER_URL + "cancelFocus.do";
	// 我的关注
	public static final String myFocus = SERVER_URL + "myFocus.do";
	// 激活虎券
	public static final String activateCoupon = SERVER_URL + "activateCoupon.do";
    //当前购物车的可用虎券
	public static final String couponData = SERVER_URL + "couponData.do";
    //提交订单
	public static final String sumbitOrder = SERVER_URL + "sumbitOrder.do";
    //退换货添加物流
	public static final String afterLogistics = SERVER_URL + "afterLogistics.do";
    //修改收货地址时更新的运费
    public static final String freightByAddres = SERVER_URL + "freightByAddres.do";
    //查询已有增值税发票
    public static final String vat = SERVER_URL + "vat.do";

    //提交举报
  	public static final String submitReport = SERVER_URL + "addReport.do";
  	//获取举报基本信息
  	public static final String loadReportInfo = SERVER_URL + "reportbaseInfo.do";
  	//获取举报列表
  	public static final String loadReportList = SERVER_URL + "reportData.do";
  	//获取举报详情
  	public static final String loadReportDetails = SERVER_URL + "reportDetailInfo.do";
  	//获取投诉列表页
  	public static final String complainData = SERVER_URL + "complainData.do";
  	//获取投诉类型列表
  	public static final String complainType = SERVER_URL + "complainType.do";
  	//发起投诉
  	public static final String addComplain = SERVER_URL + "addComplain.do";
  	//投诉详情
  	public static final String complainDetail = SERVER_URL + "complainDetail.do";
  	//投诉解决
  	public static final String solveComplain = SERVER_URL + "solveComplain.do";
  	//继续留言
  	public static final String complainContinue = SERVER_URL + "complainContinue.do";
  	//取消投诉
  	public static final String cancelComplain = SERVER_URL + "cancelComplain.do";
    //浏览记录
  	public static final String historyData = SERVER_URL + "historyData.do";
  	//百度云推送绑定
  	public static final String baiduPushInfo = SERVER_URL + "baiduPushInfo.do";
    //取消预约
  	public static final String cancelNewGoods = SERVER_URL + "cancelNewGoods.do";
  	//删除浏览记录
    public static final String cancelHistory= SERVER_URL + "cancelHistory.do";
    //查询会员信息
    public static final String NClub= SERVER_URL + "NClub.do";
    //查询会员商品信息
    public static final String clubGoods= SERVER_URL + "clubGoods.do";
  	
  	//验证密码是否正确
  	public static final String checkPasswork = SERVER_URL + "verifyPsw.do";
  	//发送验证邮件
  	public static final String sendCheckEmail = SERVER_URL + "sendEmail.do";
  	//登录日志
  	public static final String loginLog = SERVER_URL + "loginLog.do";
  	//会员等级
  	public static final String memberRank = SERVER_URL + "userRank.do";
  	//修改性别
  	public static final String changeSex = SERVER_URL + "changeSex.do";
  	//修改生日
  	public static final String changeBirthday = SERVER_URL + "changeBirthday.do";
    //完成邮箱验证
  	public static final String verifyEmialCheckResult = SERVER_URL + "verifyEmail.do";
  	//会员兑换商品详情
  	public static final String giftDetail = SERVER_URL + "giftDetail.do";
  	//提交兑换商品
  	public static final String convertGift = SERVER_URL + "convertGift.do";
  	//查询水电煤缴费的地区
  	public static final String cityList = SERVER_URL + "cityList.do";
  	//查询水电煤缴费情况
  	public static final String shareBill = SERVER_URL + "shareBill.do";
  	//查询水电煤缴费记录
  	public static final String shareBillData = SERVER_URL + "shareBillData.do";
  	//关于
  	public static final String about = SERVER_URL + "about.do";
  	//取账户安全信息
  	public static final String accountSecurity = SERVER_URL + "accountSecurity.do";

    //add by zhyao @2015/6/12 添加查询消费卷
  	public static final String queryCoupons = SERVER_URL + "coupons.do";
  	//add by zhyao @2015/6/12 添加转赠红包
  	public static final String userGiveBonus = SERVER_URL + "userGiveBonus.do";
  	//add by zhyao @2015/6/24 添加秀场赠送红包
  	public static final String appGiveBonus = SERVER_URL + "appGiveBonus.do";
    //add by zhyao @2015/6/12 添加查询七牛云存储uptoken
  	public static final String queryQiNiuUptoken = SERVER_URL + "qiniuAction.do";
  	
    //查询商品相关的人气组合
  	public static final String popularGroup = SERVER_URL + "popularGroup.do";
  	//查询商品相关的推荐搭配
  	public static final String goodsRecommend = SERVER_URL + "goodsRecommend.do";
    //修改密码
    public static final String changePwd = SERVER_URL + "changePwd.do";
    //订单物流
    public static final String orderLogistics = SERVER_URL + "orderLogistics.do";
    //加入多个商品到购物车
    public static final String addCarts = SERVER_URL + "addCarts.do";
    //商品详情中的猜你喜欢
    public static final String mayBeLike = SERVER_URL + "mayBeLike.do";
}
