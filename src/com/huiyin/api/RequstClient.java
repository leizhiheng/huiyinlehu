package com.huiyin.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.huiyin.AppContext;
import com.huiyin.bean.GoodBean;
import com.huiyin.bean.GoodsListEntity;
import com.huiyin.http.AsyncHttpClient;
import com.huiyin.http.AsyncHttpResponseHandler;
import com.huiyin.http.RequestParams;
import com.huiyin.pay.wxpay.Constants;
import com.huiyin.utils.CryptionUtil;
import com.huiyin.utils.Des3;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class RequstClient {

    /**
     * 定义一个异步网络客户端 默认超时未10秒 当超过，默认重连次数为5次 默认最大连接数为10个
     */
    private static final int TIMEOUT_SECOND = 10000;
    private static final String TAG = "RequstClient";
    private static AsyncHttpClient mClient = null;

    static {
        /* 设置连接和响应超时时间 */
        mClient = new AsyncHttpClient();
        mClient.setTimeout(TIMEOUT_SECOND);
    }

    /**
     * 取消该Context下所有的请求
     * @param mContext 上下文
     */
    public static void cancelRequests(Context mContext) {
        mClient.cancelRequests(mContext, true);
    }
    /**
     * @param url      API 地址
     * @param mHandler 数据加载状态回调
     */
    public static void post(String url, AsyncHttpResponseHandler mHandler) {

        post(url, null, mHandler);
    }

    /**
     * post 请求
     *
     * @param url     API 地址
     * @param params  请求的参数
     * @param handler 数据加载状态回调
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler mHandler) {
        /* 将参数顺序传递 */
        if (params != null) {
            LogUtil.i(TAG, "发起post请求:" + url + "?" + params.toString());
        } else {
            LogUtil.i(TAG, "发起post请求:" + url);
        }
        mClient.post(url, params, mHandler);
    }

    /**
     * 登录(废弃)
     *
     * @param userName
     * @param passWord
     * @param mHandler
     */
    @SuppressLint("SimpleDateFormat")
    public static void appLogin(String userName, String password, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", userName);
            String mPassword = CryptionUtil.md5(password.trim());
            map.put("password", mPassword);
            String pwd3des = Des3.encode(password.trim());
            map.put("password3des", pwd3des);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            map.put("channelId", AppContext.getInstance().channelIdBai);
            map.put("userId", AppContext.getInstance().userIdBai);
            map.put("nearbyId", AppContext.getInstance().getNearbyId());
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userName", userName);
            params.put("password", mPassword);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            post(URLs.APPLOGIN, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 登录
     *
     * @param userName
     * @param passWord
     * @param mHandler
     */
    @SuppressLint("SimpleDateFormat")
	public static void doLogin(String userName, String password, int index, AsyncHttpResponseHandler mHandler) {
        try {
        	
        	String mPassword = CryptionUtil.md5(password.trim());
            String deviceId = DeviceUtils.getDeviceId(AppContext.getInstance());
        	
            RequestParams params = new RequestParams();
            params.put("userName", userName);
            params.put("password", mPassword);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("origin", "3");
            params.put("index", String.valueOf(index));
            params.put("phoneToken", deviceId);
            params.put("mKey", CryptionUtil.getSignData(params));
            
            post(URLs.LOGIN, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 登录
     *
     * @param userName
     * @param passWord
     * @param mHandler
     */
    @SuppressLint("SimpleDateFormat")
	public static void doLogin(String userName, String password, AsyncHttpResponseHandler mHandler) {
        try {
        	
        	String mPassword = CryptionUtil.md5(password.trim());
            String deviceId = DeviceUtils.getDeviceId(AppContext.getInstance());
        	
            RequestParams params = new RequestParams();
            params.put("userName", userName);
            params.put("password", mPassword);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("origin", "3");
            params.put("phoneToken", deviceId);
            Encryption.encryptSingple(params);
            
            post(URLs.LOGIN, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 登录-不带猜你喜欢
     *
     * @param userName
     * @param passWord
     * @param mHandler
     */
    @SuppressLint("SimpleDateFormat")
	public static void loginLoad(String userName, String password, AsyncHttpResponseHandler mHandler) {
        try {
        	
        	String mPassword = CryptionUtil.md5(password.trim());
            String deviceId = DeviceUtils.getDeviceId(AppContext.getInstance());
        	
            RequestParams params = new RequestParams();
            params.put("userName", userName);
            params.put("password", mPassword);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("origin", "3");
            params.put("phoneToken", deviceId);
            Encryption.encryptSingple(params);
            
            post(URLs.LOGINLOAD, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 登录-不带猜你喜欢-new
     *
     * @param userName
     * @param passWord
     * @param mHandler
     */
    @SuppressLint("SimpleDateFormat")
	public static void loginOverLoad(String userName, String password, AsyncHttpResponseHandler mHandler) {
        try {
        	
        	String mPassword = CryptionUtil.md5(password.trim());
            String deviceId = DeviceUtils.getDeviceId(AppContext.getInstance());
        	
            RequestParams params = new RequestParams();
            params.put("userName", userName);
            params.put("password", mPassword);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            params.put("origin", "3");
            params.put("phoneToken", deviceId);
            Encryption.encryptSingple(params);
            //params.put("mKey", CryptionUtil.getSignData(params));
            
            post(URLs.LOGINOVERLOAD, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 添加登录日志
     *
     * @param userName
     * @param passWord
     * @param mHandler
     */
	public static void addUserLoginLog(String userId, AsyncHttpResponseHandler mHandler) {
        try {
        	
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("origin", "3");
            Encryption.encryptSingple(params);
            
            post(URLs.ADDUSERLOGINLOG, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 我的乐虎-已废弃
     *
     * @param mHandler
     */
    public static void doMyLH(AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", AppContext.getInstance().userId);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", AppContext.getInstance().userId);
            params.put("mKey", paramStr);
            post(URLs.MYLH, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 我的乐虎-废弃
     *
     * @param mHandler
     */
    public static void myLHInfo(AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", AppContext.userId);
            Encryption.encryptSingple(params);
            post(URLs.MYLHINFO, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 我的乐虎-新接口
     *
     * @param mHandler
     */
    public static void myLHInfoLoad(AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", AppContext.userId);
            Encryption.encryptSingple(params);
            post(URLs.MYLHINFOLOAD, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 用户中心猜你喜欢
     *
     * @param mHandler
     */
    public static void mayBeLikeByUser(AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", AppContext.userId);
            Encryption.encryptSingple(params);
            post(URLs.MAYBELIKEBYUSER, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 签到
     *
     * @param mHandler
     */
    public static void sign(AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", AppContext.userId);
            Encryption.encryptSingple(params);
            post(URLs.SIGN, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 乐虎券列表
     * @param pageIndex 分页参数-页码
     * @param status 状态   1，全部（未使用），2即将过期，3待领取的
     * @param mHandler
     */
    public static void ticketData(int pageIndex, int status, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", AppContext.userId);
            params.put("pageIndex", pageIndex+"");
            params.put("status", status+"");
//            params.put("mKey", CryptionUtil.getSignData(params));
            Encryption.encryptSingple(params);
            post(URLs.TICKETDATA, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 领取乐虎券
     * @param acitveId 虎券活动id
     * @param mHandler
     */
    public static void getLHTicket(String acitveId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", AppContext.userId);
            params.put("acitveId", acitveId+"");
//            params.put("mKey", CryptionUtil.getSignData(params));
            Encryption.encryptSingple(params);
            post(URLs.GETLHTICKET, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付方式
     */
    public static void doPayWay(AsyncHttpResponseHandler mHandler) {
        try {

            post(URLs.SHOP_PAY, null, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 在线帮助
     */
    public static void onLineHelp(AsyncHttpResponseHandler mHandler) {
        try {

            post(URLs.HELP_TITLE, null, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取运费
     */
    public static void appFreight(float totalPrice, String cityId, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalPrice", Des3.encode(totalPrice + ""));
            map.put("cityId", Des3.encode(cityId));
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("totalPrice", Des3.encode(totalPrice + ""));
            params.put("cityId", Des3.encode(cityId));
            params.put("mKey", paramStr);
            post(URLs.FREESHIPPING, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取订单折扣
     */
    public static void appOrderDiscount(float totalPrice, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalPrice", Des3.encode(totalPrice + ""));
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("totalPrice", Des3.encode(totalPrice + ""));
            params.put("mKey", paramStr);
            post(URLs.ORDER_DISCOUNT, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 填写订单初始化
     */
    public static void writeOrderInit(String userId, String shopId, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("shopId", shopId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("shopId", shopId);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.WRITE_ORDER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 删除订单
     *
     * @param orderId
     * @param mHandler
     */
    @SuppressLint("SimpleDateFormat")
    public static void deleteOrder(String orderId, int flag, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("orderId", orderId);
            params.put("flag", String.valueOf(flag));
            Encryption.encryptSingple(params);
            post(URLs.DELETE_ORDER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * 取消换货订单
     *
     * @param replaceId 换货ID
     * @param mHandler
     */
    public static void cancelChangeOrder(String replaceId, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("replaceId", replaceId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("replaceId", replaceId);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.CANCEL_ORDER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    
    

    /**
     * 确认收货
     *
     * @param orderId
     * @param mHandler
     */
    public static void receiveOrder(String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("replaceId", orderId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("replaceId", orderId);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.RECEIVE_ORDER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * 未登录时提交订单
     *
     * @param consignee_name
     * @param consignee_phone
     * @param postal_code
     * @param consignee_address
     * @param shipping_method
     * @param payment_method
     * @param receipt_time
     * @param invoicing_method
     * @param invoice_title
     * @param invoice_content
     * @param company_name
     * @param identification_number
     * @param registered_address
     * @param registered_phone
     * @param bank
     * @param account
     * @param collector_name
     * @param collector_phone
     * @param collector_address
     * @param commodity
     * @param delivery_remark
     * @param province_id
     * @param city_id
     * @param area_id
     * @param freight
     * @param mHandler
     */
    public static void commitOrderUnLogin(String consignee_name, String consignee_phone, String postal_code, String consignee_address, String shipping_method,
                                          String payment_method, String receipt_time, String invoicing_method, String invoice_title, String invoice_content, String company_name, String identification_number,
                                          String registered_address, String registered_phone, String bank, String account, String collector_name, String collector_phone, String collector_address,
                                          String commodity, String delivery_remark, String province_id, String city_id, String area_id, String freight, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("consignee_name", consignee_name);
            map.put("consignee_phone", consignee_phone);
            map.put("postal_code", postal_code);
            map.put("consignee_address", consignee_address);
            map.put("shipping_method", shipping_method);
            map.put("payment_method", payment_method);
            map.put("receipt_time", receipt_time);
            map.put("invoicing_method", invoicing_method);
            map.put("invoice_title", invoice_title);
            map.put("invoice_content", invoice_content);
            map.put("company_name", company_name);
            map.put("identification_number", identification_number);
            map.put("registered_address", registered_address);
            map.put("registered_phone", registered_phone);
            map.put("bank", bank);
            map.put("account", account);
            map.put("collector_name", collector_name);
            map.put("collector_phone", collector_phone);
            map.put("collector_address", collector_address);
            map.put("commodity", commodity);
            map.put("delivery_remark", delivery_remark);
            map.put("province_id", province_id);
            map.put("city_id", city_id);
            map.put("area_id", area_id);
            map.put("freight", freight);
            map.put("channelId", AppContext.getInstance().channelIdBai);
            map.put("userId", AppContext.getInstance().userIdBai);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            map.put("nearbyId", AppContext.getInstance().getNearbyId());
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("consignee_name", consignee_name);
            params.put("consignee_phone", consignee_phone);
            params.put("postal_code", postal_code);
            params.put("consignee_address", consignee_address);
            params.put("shipping_method", shipping_method);
            params.put("payment_method", payment_method);
            params.put("receipt_time", receipt_time);
            params.put("invoicing_method", invoicing_method);
            params.put("invoice_title", invoice_title);
            params.put("invoice_content", invoice_content);
            params.put("company_name", company_name);
            params.put("identification_number", identification_number);
            params.put("registered_address", registered_address);
            params.put("registered_phone", registered_phone);
            params.put("bank", bank);
            params.put("account", account);
            params.put("collector_name", collector_name);
            params.put("collector_phone", collector_phone);
            params.put("collector_address", collector_address);
            params.put("commodity", commodity);
            params.put("delivery_remark", delivery_remark);
            params.put("province_id", province_id);
            params.put("city_id", city_id);
            params.put("area_id", area_id);
            params.put("freight", freight);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            post(URLs.SHOP_UNLOGIN, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 乐虎劵验证
     */
    public static void postCodeValidate(String userId, String code, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("userId", userId);
            map.put("ticketCode", code);
            map.put("total",  Des3.encode("-1"));
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);		//
            params.put("ticketCode", code);		//
            params.put("total", Des3.encode("-1"));			//
            params.put("mKey", paramStr);
            post(URLs.CODE_VALIDATE, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交订单
     *
     * @param userId
     * @param shoppingId           购物车id（多个用“，”隔开）
     * @param freight              运费（默认为0）
     * @param integral_d           积分抵扣金额
     * @param integral             使用的积分
     * @param delivery_remark      备注信息
     * @param address_id           地址id
     * @param default_paly_id      支付方式
     * @param default_receipt_time 收货时间
     * @param invoices_id          发票ID
     * @param lh_id                乐虎券id
     * @param mHandler
     */

    public static void postCommitInfo(String userId, String shoppingId, String freight, String integral_d, String integral, String delivery_remark, String address_id,
                                      String default_paly_id, String default_receipt_time, String invoices_id, String lh_id, String sendId, int nearby_mention, String buyMessage,
                                      AsyncHttpResponseHandler mHandler) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", userId);
            map.put("shoppingId", shoppingId);
            map.put("freight", freight);
            map.put("integral_d", integral_d);
            map.put("integral", integral);
            map.put("delivery_remark", delivery_remark);
            map.put("address_id", address_id);
            map.put("default_paly_id", default_paly_id);
            map.put("default_receipt_time", default_receipt_time);
            map.put("invoices_id", invoices_id);
            map.put("lh_id", lh_id);
            map.put("shipping_method", sendId);
            if (sendId.equals("-1"))
                map.put("nearby_mention", String.valueOf(nearby_mention));
            if (!StringUtils.isBlank(buyMessage)) {
                map.put("buyMessage", buyMessage);
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            map.put("nearbyId", AppContext.getInstance().getNearbyId());
            map.put("origin", "1");
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("user_id", userId);
            params.put("shoppingId", shoppingId);
            params.put("freight", freight);
            params.put("integral_d", integral_d);
            params.put("integral", integral);
            params.put("delivery_remark", delivery_remark);
            params.put("address_id", address_id);
            params.put("default_paly_id", default_paly_id);
            params.put("default_receipt_time", default_receipt_time);
            params.put("invoices_id", invoices_id);
            params.put("lh_id", lh_id);
            params.put("shipping_method", sendId);
            if (sendId.equals("-1"))
                params.put("nearby_mention", String.valueOf(nearby_mention));
            if (!StringUtils.isBlank(buyMessage)) {
                params.put("buyMessage", buyMessage);
            }
            params.put("time", str_time);
            params.put("origin", "1");
            params.put("mKey", paramStr);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            post(URLs.COMMIT_ORDER, params, mHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增值发票信息
     *
     * @param userId
     * @param invoicing_method      0 普通发票 1 增值税发票
     * @param invoice_content       发票明细
     * @param company_name          增值税发票的单位名称
     * @param identification_number 纳税人识别号
     * @param registered_address    注册地址
     * @param registered_phone      注册电话
     * @param bank                  开户银行
     * @param account               银行账户
     * @param collector_name        收货人名称
     * @param collector_phone       收货人手机
     * @param collector_address     收货人地址
     * @param img
     * @param invoiceId             修改发票需要发的id
     * @param mHandler
     */
    public static void postAddedInvoiceInfo(String userId, String invoicing_method, String invoice_content, String company_name, String identification_number,
                                            String registered_address, String registered_phone, String bank, String account, String collector_name, String collector_phone, String collector_address,
                                            String img,String invoiceId ,AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("invoicing_method", invoicing_method);
            map.put("invoice_content", invoice_content);
            map.put("company_name", company_name);
            map.put("identification_number", identification_number);
            map.put("registered_address", registered_address);
            map.put("registered_phone", registered_phone);
            map.put("bank", bank);
            map.put("account", account);
            map.put("collector_name", collector_name);
            map.put("collector_phone", collector_phone);
            map.put("collector_address", collector_address);
            map.put("img", img);
            map.put("invoiceId", invoiceId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("invoicing_method", invoicing_method);
            params.put("invoice_content", invoice_content);
            params.put("company_name", company_name);
            params.put("identification_number", identification_number);
            params.put("registered_address", registered_address);
            params.put("registered_phone", registered_phone);
            params.put("bank", bank);
            params.put("account", account);
            params.put("collector_name", collector_name);
            params.put("collector_phone", collector_phone);
            params.put("time", str_time);
            params.put("collector_address", collector_address);
            params.put("img", img);
            params.put("invoiceId", invoiceId);
            params.put("mKey", paramStr);
            post(URLs.IVOICE_INFO, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通发票信息
     *
     * @param userId
     * @param invoicing_method   0 普通发票 1 增值税发票
     * @param invoice_title      发票抬头 :收件人/收货单位名字
     * @param invoice_content    发票明细
     * @param invoice_title_type 发票抬头类型
     * @param mHandler
     */
    public static void postNormalInvoiceInfo(String userId, String invoicing_method, String invoice_title, String invoice_content, String invoice_title_type,
                                             AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("invoicing_method", invoicing_method);
            map.put("invoice_title", invoice_title);
            map.put("invoice_content", invoice_content);
            map.put("invoice_title_type", invoice_title_type);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("invoicing_method", invoicing_method);
            params.put("invoice_title", invoice_title);
            params.put("invoice_content", invoice_content);
            params.put("invoice_title_type", invoice_title_type);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.IVOICE_INFO, params, mHandler);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 购物车列表
     *
     * @param userId
     * @param type
     * @param mHandler
     */
    public static void doShopList(String userId, String type, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("type", type);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("type", type);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.SHOP_LIST, params, mHandler);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 购物车新接口
     *
     * @param userId
     * @param type
     * @param mHandler
     */
    public static void doShopListNew(String userId, String type, AsyncHttpResponseHandler mHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("type", type);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("type", type);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.queryShoppingCarNew, params, mHandler);
    }

    /**
     * 删除订单
     *
     * @param deleteIds    需删除购物车集合（逗号分隔）
     * @param userId       用户ID
     *
     */
    public static void doDeleteOrder(String deleteIds, String userId,AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("deleteIds", deleteIds);
        params.put("userId", userId);
        Encryption.encryptSingple(params);
        post(URLs.DELETE_SHOPCAR, params, mHandler);
    }

    /**
     * 修改订单数量
     *
     * @param id
     * @param num
     * @param mHandler
     */
    public static void doModifyOrder(String id, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("id", id);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.MODIFY_ORDER, params, mHandler);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 修改用户名
     */
    public static void doModifyUsername(String userId, String userName, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("userName", userName);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("userName", userName);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.MODIFY_USERNAME, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 绑定手机号
     */
    public static void bindPhone(String userId, String phone, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("phone", phone);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("phone", phone);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.BIND_PHONE, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 第三方账户绑定
     *
     * @param password          密码
     * @param phone             手机
     * @param openId            第三方返回用户ID
     * @param messageVerifyCode 短信验证码
     * @param mHandler          句柄
     */
    public static void bindPhone(String password, String phone, String openId, String messageVerifyCode, int flag, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String mPassword = CryptionUtil.md5(password.trim());
            map.put("password", mPassword);
            String pwd3des;
            pwd3des = Des3.encode(password.trim());
            map.put("password3des", pwd3des);
            map.put("phone", phone);
            map.put("openId", openId);
            if (flag != 0)
                map.put("flag", String.valueOf(flag));
            if (!StringUtils.isBlank(messageVerifyCode))
                map.put("messageVerifyCode", messageVerifyCode);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("password", mPassword);
            params.put("password3des", pwd3des);
            params.put("phone", phone);
            params.put("openId", openId);
            if (flag != 0)
                params.put("flag", String.valueOf(flag));
            if (!StringUtils.isBlank(messageVerifyCode))
                params.put("messageVerifyCode", messageVerifyCode);
            params.put("mKey", paramStr);
            post(URLs.BIND_PHONE, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订单列表
     *
     * @param userId
     * @param pageindex
     * @param flag        0：商品订单 1：退货订单 2：预约订单
     * @param remark      1：待支付 收货2：待评价 3：退换货中
     * @param status      订单所有状态
     * @param startTime
     * @param endTime
     * @param searchOrder
     * @param mHandler
     */
    public static void getOrderList(String userId, String pageindex, String flag, String remark, String status, String startTime, String endTime, String searchOrder,
                                    AsyncHttpResponseHandler mHandler) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("pageindex", pageindex);
            map.put("flag", flag);
            if (!StringUtils.isBlank(remark))
                map.put("remark", remark);
            if (!StringUtils.isBlank(status))
                map.put("status", status);
            if (!StringUtils.isBlank(startTime))
                map.put("startTime", startTime);
            if (!StringUtils.isBlank(endTime))
                map.put("endTime", endTime);
            if (!StringUtils.isBlank(searchOrder))
                map.put("searchOrder", searchOrder);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageindex", pageindex);
            params.put("flag", flag);
            if (!StringUtils.isBlank(remark))
                params.put("remark", remark);
            if (!StringUtils.isBlank(status))
                params.put("status", status);
            if (!StringUtils.isBlank(startTime))
                params.put("startTime", startTime);
            if (!StringUtils.isBlank(endTime))
                params.put("endTime", endTime);
            if (!StringUtils.isBlank(searchOrder))
                params.put("searchOrder", searchOrder);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.ORDERlIST, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订单信息
     *
     * @param userId    用户id
     * @param mark      标志：：1全部订单,2待付款,3待收货,4待评价，5预约/退换货
     * @param searchKey 搜索关键词：商品名称/商品编号/订单编号（当mark=5时）
     * @param pageIndex 当前页
     * @param mHandler
     */
    public static void getOrderListNew(String userId, int mark, int pageIndex,String searchKey, AsyncHttpResponseHandler mHandler) {
        try {

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageIndex", pageIndex + "");
            params.put("mark", mark + "");
            if(5==mark){//预约/退换货
                params.put("searchKey", searchKey + "");
            }
            Encryption.encryptSingple(params);
            post(URLs.ORDERlIST, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订单详情
     */
    public static void getOrderDetail(String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("orderId", orderId);
            Encryption.encryptSingple(params);
            post(URLs.ORDER_DETAIL, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * *
     * 取消订单
     *
     * @param orderId
     * @param mHandler
     */
    public static void cancelOrder(String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("orderId", orderId);
            Encryption.encryptSingple(params);
            post(URLs.ORDER_CANCEL, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * *
     * 取消退款
     *
     * @param orderId
     * @param mHandler
     */
    public static void cancelBackOrder(String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("returnId", orderId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("returnId", orderId);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.ORDER_BACK_CANCEL, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * *
     * 确认到账
     *
     * @param orderId
     * @param mHandler
     */
    public static void sureReceiveMoney(String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("returnId", orderId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("returnId", orderId);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.ORDER_MONEY_RETURN, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 确认收货
     *
     * @param orderId
     * @param userId
     * @param mHandler
     */
    public static void makeSureOrder(String userId, String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("orderId", orderId);
            Encryption.encryptSingple(params);
            post(URLs.ORDER_SURE, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 退货
     *
     * @param orderId
     * @param returnType   退款方式
     * @param reason       退货原因
     * @param bank         银行
     * @param bankBranch   银行帐号分行
     * @param bankRealName 开户银行真实姓名
     * @param bankAccount  银行账户
     * @param phone        电话号码
     * @param userId
     * @param number       退货商品数量
     * @param commodityId  退货商品数量
     * @param mHandler
     */

    public static void backGood(String orderId, String returnType, String reason, String bank, String bankBranch, String bankRealName, String bankAccount, String phone,
                                String userId, String number, String commodityId, String imgs, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", orderId);
            map.put("returnType", returnType);
            map.put("reason", reason);
            map.put("bank", bank);
            map.put("bankBranch", bankBranch);
            map.put("bankRealName", bankRealName);
            map.put("bankAccount", bankAccount);
            map.put("phone", phone);
            map.put("userId", userId);
            map.put("number", number);
            map.put("commodityId", commodityId);
            map.put("imgs", imgs);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("orderId", orderId);
            params.put("returnType", returnType);
            params.put("reason", reason);
            params.put("bank", bank);
            params.put("bankBranch", bankBranch);
            params.put("bankRealName", bankRealName);
            params.put("bankAccount", bankAccount);
            params.put("phone", phone);
            params.put("userId", userId);
            params.put("number", number);
            params.put("commodityId", commodityId);
            params.put("imgs", imgs);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.BACK_GOOD, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 换货
     *
     * @param orderId               订单id
     * @param commodityId           商品id
     * @param reason退货原因
     * @param consigneeName收件人姓名
     * @param consigneePhone收件人电话号码
     * @param imgs                  图片url
     * @param number                换货数量
     * @param consigneeAddress收件人地址
     * @param mHandler
     */

    public static void changeGood(String userId, String orderId, String commodityId, String reason, String consigneeName, String consigneePhone, String imgs, String number,
                                  String consigneeAddress, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("orderId", orderId);
            map.put("commodityId", commodityId);
            map.put("imgs", imgs);
            map.put("number", number);
            map.put("reason", reason);
            map.put("consigneeName", consigneeName);
            map.put("consigneePhone", consigneePhone);
            map.put("consigneeAddress", consigneeAddress);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("orderId", orderId);
            params.put("reason", reason);
            params.put("commodityId", commodityId);
            params.put("imgs", imgs);
            params.put("number", number);
            params.put("consigneeName", consigneeName);
            params.put("consigneePhone", consigneePhone);
            params.put("consigneeAddress", consigneeAddress);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.CHANGE_GOOD, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 物流信息
     *
     * @param logisticsName
     * @param logisticsNo
     * @param commodityReturnId
     * @param mHandler
     */
    public static void writeLogistic(String logisticsName, String logisticsNo, String returnId, String commdotiyId, String proofImg, String flag, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("logisticsName", logisticsName);
            params.put("logisticsNo", logisticsNo);
            params.put("returnId", returnId);
            params.put("commodityId", commdotiyId);
            params.put("proofImg", proofImg);
            params.put("flag", flag);

            post(URLs.WRITE_LOGISTIC, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 发表评论商品评论
     */
    public static void postCommentInfo(String userId, String commdotiyId, String score, String content, String order_id, String image, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("orderId", order_id);
            map.put("commdotiyId", commdotiyId);
            map.put("score", score);
            map.put("content", content);
            map.put("img", image);
            map.put("origin", String.valueOf(2));
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("orderId", order_id);
            params.put("commdotiyId", commdotiyId);
            params.put("score", score);
            params.put("content", content);
            params.put("img", image);
            params.put("time", str_time);
            params.put("origin", String.valueOf(2));
            params.put("mKey", paramStr);
            post(URLs.COMMENT, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 售后预约记录/退换货记录
     *  userId 用户id
        pageIndex 当前页码
        flag 1安装维修预约服务历史记录，2退换货记录
     */

    public static void getBespeakRecordData(String userId, int  pageIndex,String flag, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageIndex", pageIndex+"");
            params.put("flag", flag);
            Encryption.encryptSingple(params);
            post(URLs.ORDER_RECORD_DATA, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 售后详情状态及数据
     */

    public static void postAfterInfo(String userId, String returnId, String commodityId, String flag, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("returnId", returnId);
            params.put("commodityId", commodityId);
            params.put("flag", flag);
            post(URLs.AFTER_DETAIL, params, mHandler);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 发表订单评论
     *
     * @param userId   用户ID
     * @param orderId  订单ID
     * @param storeId  店铺ID
     * @param isGuest  是否匿名评价：1是，0否
     * @param pack     包装满意度
     * @param speed    送货速度满意度
     * @param server   服务满意度
     * @param origin   1 PC端 2 安卓端 3 IOS端 4 WAP端 5 微信端
     * @param evaData  评价内容 这是一个json字符串
     * @param mHandler 句柄
     */
    public static void postTotalComment(String userId, String orderId, int storeId, int isGuest, String pack, String speed, String server, int origin, String evaData, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("orderId", orderId);
            map.put("storeId", String.valueOf(storeId));
            map.put("isGuest", String.valueOf(isGuest));
            map.put("pack", pack);
            map.put("speed", speed);
            map.put("server", server);
            map.put("origin", String.valueOf(origin));
            map.put("evaData", evaData);
//            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String str_time = sf.format(new Date());
//            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("orderId", orderId);
            params.put("storeId", String.valueOf(storeId));
            params.put("isGuest", String.valueOf(isGuest));
            params.put("pack", pack);
            params.put("speed", speed);
            params.put("server", server);
            params.put("origin", String.valueOf(origin));
            params.put("evaData", evaData);
//            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.ORDER_COMMENT, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约信息
     *
     * @param orderCode
     * @param mHandler
     */
    public static void postYuYueInfo(String orderCode, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", orderCode);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            map.put("userId", AppContext.userId);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("orderCode", orderCode);
            params.put("time", str_time);
            params.put("userId", AppContext.userId);
            params.put("mKey", paramStr);
            post(URLs.YUYUE_MSG, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 预约初始化信息
     *
     * @param orderCode
     * @param mHandler
     */
    public static void postYuYueInit(String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("ORDER_ID", orderId);
            post(URLs.YUYUE_INIT, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 预约取消
     *
     * @param orderCode
     * @param mHandler
     */
    public static void postYuYueCancel(String ORDER_ID, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ORDER_ID", ORDER_ID);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("ORDER_ID", ORDER_ID);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.YUYUE_CANCEL, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 提交预约信息
     *
     * @param order_id      订单id
     * @param type          预约类型id
     * @param phone         预约信息：手机号码
     * @param name          预约信息：联系人
     * @param address       预约信息：地址
     * @param remarks       预约信息：备注信息
     * @param userId        关联用户
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param partId        所需配件
     * @param commodity_id  商品id
     * @param commodity_num 商品数量
     * @param mHandler
     */
    public static void postCommitYuYue(String order_id, String type, String phone, String name, String address, String remarks, String userId, String startTime, String endTime,
                                       String partId, String commodity_id, String commodity_num, AsyncHttpResponseHandler mHandler) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ORDER_ID", order_id);
            map.put("RESERVATION_TYPE_ID", type);
            map.put("PHONE", phone);
            map.put("NAME", name);
            map.put("ADDRESS", address);
            map.put("REMARKS", remarks);
            map.put("STARTTIME", startTime);
            map.put("ENDTIME", endTime);
            map.put("USER_ID", userId);
            map.put("PARTS_ID", partId);
            map.put("COMMODITY_ID", commodity_id);
            map.put("COMMODITY_NUM", commodity_num);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("ORDER_ID", order_id);
            params.put("RESERVATION_TYPE_ID", type);
            params.put("PHONE", phone);
            params.put("NAME", name);
            params.put("ADDRESS", address);
            params.put("REMARKS", remarks);
            params.put("USER_ID", userId);
            params.put("STARTTIME", startTime);
            params.put("ENDTIME", endTime);
            params.put("PARTS_ID", partId);
            params.put("COMMODITY_ID", commodity_id);
            params.put("COMMODITY_NUM", commodity_num);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.YUYUE_ADD, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 提交预约信息
     *
     * @param order_id  订单id
     * @param type      预约类型id
     * @param phone     预约信息：手机号码
     * @param name      预约信息：联系人
     * @param address   预约信息：地址
     * @param remarks   预约信息：备注信息
     * @param userId    关联用户
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param commodity
     * @param mHandler
     */
    public static void appAddBespeak(String order_id, String type, String phone, String name, String address, String remarks, String userId, String startTime, String endTime,
                                     String commodity, AsyncHttpResponseHandler mHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ORDER_ID", order_id);
        map.put("RESERVATION_TYPE_ID", type);
        map.put("PHONE", phone);
        map.put("NAME", name);
        map.put("ADDRESS", address);
        map.put("REMARKS", remarks);
        map.put("STARTTIME", startTime);
        map.put("ENDTIME", endTime);
        map.put("USER_ID", userId);
        map.put("commodity", commodity);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("ORDER_ID", order_id);
        params.put("RESERVATION_TYPE_ID", type);
        params.put("PHONE", phone);
        params.put("NAME", name);
        params.put("ADDRESS", address);
        params.put("REMARKS", remarks);
        params.put("USER_ID", userId);
        params.put("STARTTIME", startTime);
        params.put("ENDTIME", endTime);
        params.put("commodity", commodity);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.appAddBespeak, params, mHandler);
    }

    // add by zhyao @2015/6/24 添加是不是全部消费卷参数allConsumption
    /**
     * 获取地址列表
     *
     * @param userId
     * @param mHandler
     */
    public static void getAddrList(String userId, String pageindex, boolean isAllConsumption, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageindex", pageindex);
            // add by zhyao @2015/6/24 添加是不是全部消费卷参数allConsumption
            if(isAllConsumption) {
            	params.put("allConsumption", "1");
            }
            else {
            	params.put("allConsumption", "0");
            }
            post(URLs.ADDR_LIST, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 修改地址
     *
     * @param userId
     * @param CITY_ID
     * @param CONSIGNEE_PHONE
     * @param ADDRESS
     * @param CONSIGNEE_NAME
     * @param POSTAL_CODE
     * @param AREA_ID
     * @param PROVINCE_ID
     * @param ADDRESSID
     * @param IS_DEFAULT
     * @param mHandler
     */

    public static void postModifyAddress(String userId, String CITY_ID, String CONSIGNEE_PHONE, String ADDRESS, String CONSIGNEE_NAME, String POSTAL_CODE, String AREA_ID,
                                         String PROVINCE_ID, String ADDRESSID, String IS_DEFAULT, String levelAddr, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("CITY_ID", CITY_ID);
            params.put("CONSIGNEE_PHONE", CONSIGNEE_PHONE);
            params.put("ADDRESS", ADDRESS);
            params.put("CONSIGNEE_NAME", CONSIGNEE_NAME);
            params.put("POSTAL_CODE", POSTAL_CODE);
            params.put("AREA_ID", AREA_ID);
            params.put("PROVINCE_ID", PROVINCE_ID);
            params.put("ADDRESSID", ADDRESSID);
            params.put("IS_DEFAULT", IS_DEFAULT);
            params.put("levelAddr", levelAddr);

            post(URLs.ADDR_MODIFY, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 删除地址
     *
     * @param userId
     * @param addressId
     * @param mHandler
     */
    public static void postDeleteAddress(String userId, String addressId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("addressId", addressId);
            post(URLs.ADDR_DELETE, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取留言类型列表
     *
     * @param mHandler
     */
    public static void getMsgTypeList(AsyncHttpResponseHandler mHandler) {
        try {

            post(URLs.MSG_TYPE, null, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取帮助信息
     *
     * @param flag
     * @param mHandler
     */
    public static void getHelpInfo(String flag, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            if (flag.equals("server")) { // 满足这个条件是服务协议接口
                post(URLs.SERVICE_AGGREMENT, null, mHandler);
            } else if (flag.equals("4")) {
                post(URLs.INTGRAL_EXPLAIN, null, mHandler);
            } else {
                params.put("flag", flag);
                post(URLs.ONLINE_HELP, params, mHandler);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取活动介绍
     *
     * @param flag
     * @param mHandler
     */
    public static void getBannerIntroduce(String flag, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("bannerId", flag);
            post(URLs.BANNER_INTRODUCE, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 留言
     *
     * @param userId
     * @param mHandler
     */
    public static void postMsg(String userId, String content, String name, String type_id, String phone, String mail, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("CONTENT", content);
            params.put("NAME", name);
            params.put("TYPE_ID", type_id);
            params.put("PHONE", phone);
            params.put("MAILBOX", mail);
            post(URLs.MAKE_MSG, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取收藏列表
     *
     * @param userId    用户名
     * @param pageIndex 页数
     * @param type      关注类型： 1 ：商品 2 ：店铺
     * @param mHandler  句柄
     */
    public static void getCollectList(String userId, String pageIndex, int type, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pageIndex", pageIndex);
        params.put("type", String.valueOf(type));
        post(URLs.myFocus, params, mHandler);
    }

    /**
     * 取消收藏
     *
     * @param userId
     * @param commodityId 商品id
     * @param mHandler
     */
    public static void cancelFoucs(String userId, String commodityId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("USER_ID", userId);
        params.put("COMMODITY_ID", commodityId);
        post(URLs.CANCEL_FOUCS, params, mHandler);

    }

    /**
     * 获取乐虎劵列表
     *
     * @param userId
     * @param mHandler
     */
    public static void getLeHuList(String userId, String flag, String pageIndex, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("flag", flag);
            map.put("pageindex", pageIndex);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("flag", flag);
            params.put("pageindex", pageIndex);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.LEHUJUAN_LIST, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取积分
     *
     * @param userId
     * @param mHandler
     */
    public static void getPoint(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("pageindex", pageindex);
            map.put("pagesize", "10");
            // SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            // String str_time = sf.format(new Date());
            // map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageindex", pageindex);
            params.put("pagesize", "10");
            // params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.GET_POINT, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    // add by zhyao @2015/5/6  添加获取红包明细
    /**
     * 获取红包明细
     *
     * @param userId
     * @param mHandler
     */
    public static void getHongbao(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("pageindex", pageindex);
            map.put("pagesize", "10");
            // SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            // String str_time = sf.format(new Date());
            // map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageindex", pageindex);
            params.put("pagesize", "10");
            // params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.GET_BOUNS, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 获取验证码
     * @param registerPhone 手机号码
     * @param flag 填 3
     * @param mHandler
     */
    public static void getRegeditCode(String registerPhone, String flag, AsyncHttpResponseHandler mHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", registerPhone);
        map.put("flag", flag);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("phone", registerPhone);
        params.put("flag", flag);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.GET_REGEIDT_CODE, params, mHandler);
    }

    /**
     * 验证验证码
     *
     * @param phone					手机号码/邮箱
     * @param messageVerifyCode   	验证码
     * @param flag 					0是手机验证码，1是邮箱验证码		
     * @param mHandler
     */
    public static void postCode(String phone, String messageVerifyCode, String flag, AsyncHttpResponseHandler mHandler) {

    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    	String str_time = sf.format(new Date());
    	
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("phone", phone);
//        map.put("messageVerifyCode", verifyCode);
//        map.put("time", str_time);
//        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("messageVerifyCode", messageVerifyCode);
        params.put("flag", flag);
        params.put("time", str_time);
        params.put("mKey", CryptionUtil.getSignData(params));
        post(URLs.POST_CODE, params, mHandler);
    }

    /**
     * 找回密码
     *
     * @param phone
     * @param code
     * @param mHandler
     */
    public static void postFindPsw(String phone, String password, String pswSfae, AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("phone", phone);
            String mPassword = CryptionUtil.md5(password.trim());
            map.put("password", mPassword);
            map.put("pswSfae", pswSfae);
            String pwd3des;
            pwd3des = Des3.encode(password.trim());
            map.put("password3des", pwd3des);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("phone", phone);
            params.put("password", mPassword);
            params.put("pswSfae", pswSfae);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.FIND_PSW, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 找回密码
     *
     * @param phone
     * @param code
     * @param mHandler
     */
    public static void amendPhone(String userId, String phone , String messageVerifyCode , AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("phone", phone);
            params.put("messageVerifyCode", messageVerifyCode);
//            params.put("mKey", CryptionUtil.getSignData(params));
            Encryption.encryptSingple(params);
            post(URLs.AMENDPHONE, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    /**
     * 注册
     * @param registerPhone 		手机号码
     * @param password				密码
     * @param pswSfae				密码强度  1弱  2中  3强
     * @param sendCode				校验码
     * @param access_token			token
     * @param openid
     * @param flag
     * @param userIfoJson
     * @param mHandler
     */
    public static void doRegeditOld(String registerPhone, String password, String pswSfae, String sendCode, String access_token, String openid, int flag, String userIfoJson,
                                 AsyncHttpResponseHandler mHandler) {
        try {
            String mPassword = CryptionUtil.md5(password.trim());
            String pwd3des = Des3.encode(password.trim());
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            
            RequestParams params = new RequestParams();
            params.put("phone", registerPhone);
            params.put("messageVerifyCode", sendCode);
            params.put("password", mPassword);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            if (!StringUtils.isBlank(access_token))
                params.put("access_token", access_token);
            if (!StringUtils.isBlank(openid))
                params.put("openid", openid);
            if (flag != 0)
                params.put("flag", String.valueOf(flag));
            if (!StringUtils.isBlank(userIfoJson))
                params.put("userIfoJson", userIfoJson);
            params.put("mKey", CryptionUtil.getSignData(params));
            post(URLs.REGISTER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    /**
     * 注册
     * @param registerPhone 		手机号码
     * @param password				密码
     * @param pswSfae				密码强度  1弱  2中  3强
     * @param sendCode				校验码
     * @param access_token			token
     * @param openid
     * @param flag
     * @param userIfoJson
     * @param mHandler
     */
    public static void register(String registerPhone, String password, String pswSfae, String sendCode, String access_token, String openid, int flag, String userIfoJson,
                                 AsyncHttpResponseHandler mHandler) {
        try {
            String mPassword = CryptionUtil.md5(password.trim());
            String pwd3des = Des3.encode(password.trim());
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            
            RequestParams params = new RequestParams();
            params.put("phone", registerPhone);
            params.put("messageVerifyCode", sendCode);
            params.put("password", mPassword);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            params.put("origin", "3");
            if (!StringUtils.isBlank(access_token))
                params.put("access_token", access_token);
            if (!StringUtils.isBlank(openid))
                params.put("openid", openid);
            if (flag != 0)
                params.put("flag", String.valueOf(flag));
            if (!StringUtils.isBlank(userIfoJson))
                params.put("userIfoJson", userIfoJson);
//            params.put("mKey", CryptionUtil.getSignData(params));
            Encryption.encryptSingple(params);
            post(URLs.REGISTER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    /**
     * 注册
     * @param registerPhone 		手机号码
     * @param password				密码
     * @param pswSfae				密码强度  1弱  2中  3强
     * @param sendCode				校验码
     * @param access_token			token
     * @param openid
     * @param flag
     * @param userIfoJson
     * @param mHandler
     */
    public static void registerLoad(String registerPhone, String password, String pswSfae, String sendCode, String access_token, String openid, int flag, String userIfoJson,
                                 AsyncHttpResponseHandler mHandler) {
        try {
            String mPassword = CryptionUtil.md5(password.trim());
            String pwd3des = Des3.encode(password.trim());
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            
            RequestParams params = new RequestParams();
            params.put("phone", registerPhone);
            params.put("messageVerifyCode", sendCode);
            params.put("password", mPassword);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("channelId", AppContext.getInstance().channelIdBai);
            params.put("userId", AppContext.getInstance().userIdBai);
            params.put("nearbyId", AppContext.getInstance().getNearbyId());
            params.put("origin", "3");
            if (!StringUtils.isBlank(access_token))
                params.put("access_token", access_token);
            if (!StringUtils.isBlank(openid))
                params.put("openid", openid);
            if (flag != 0)
                params.put("flag", String.valueOf(flag));
            if (!StringUtils.isBlank(userIfoJson))
                params.put("userIfoJson", userIfoJson);
//            params.put("mKey", CryptionUtil.getSignData(params));
            Encryption.encryptSingple(params);
            post(URLs.REGISTERLOAD, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 快捷登录
     *
     * @param uid      微博/QQ登录Id
     * @param nickName
     * @param type     1:微博登录 2:QQ登录
     * @param mHandler
     */
    public static void suserLogin(String uid, String nickName, String type, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("nickName", nickName);
        params.put("type", type);
        // mPost(URLs.SUSER_LOGIN, params, mHandler);
    }

    // 今日头条
    public static void appHeadlines(AsyncHttpResponseHandler mHandler) {
        post(URLs.appHeadlines, mHandler);
    }

    // 今日头条
    public static void appHeadlines2(AsyncHttpResponseHandler mHandler, String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        post(URLs.appHeadlines, params, mHandler);
    }

    // 专区
    public static void appPrefecture(AsyncHttpResponseHandler mHandler, String id, String pageSize, String pageIndex) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        post(URLs.appPrefecture, params, mHandler);
    }

    // 秀场喜欢
    public static void appLike(AsyncHttpResponseHandler mHandler, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("mKey", paramStr);
        params.put("time", str_time);
        post(URLs.appLike, params, mHandler);
    }

    // 秀场喜欢添加
    public static void appShowLike(AsyncHttpResponseHandler mHandler, String userId, String spotlightId) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("spotlightId", spotlightId);
        post(URLs.appShowLike, params, mHandler);
    }

    // 秀场评论
    public static void appAppraise(AsyncHttpResponseHandler mHandler, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        map.put("userId", AppContext.userId);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("time", str_time);
        params.put("userId", AppContext.userId);
        params.put("mKey", paramStr);
        post(URLs.appAppraise, params, mHandler);
    }

    // 秀场评论
    public static void appAddAppraise(AsyncHttpResponseHandler mHandler, String spotlight_id, String user_id, String reply_id, String content) {
        RequestParams params = new RequestParams();
        params.put("spotlight_id", spotlight_id);
        params.put("user_id", user_id);
        params.put("reply_id", reply_id);
        params.put("content", content);
        post(URLs.appAddAppraise, params, mHandler);
    }

    // 未登录情况下的推荐
    // add by zhyao @2015/6/29 添加isMine参数：1-自己的，0-全部
    public static void appNoShow(AsyncHttpResponseHandler mHandler, String recommend, String isMine, String use_id, String pageSize, String pageIndex) {
        RequestParams params = new RequestParams();
        params.put("recommend", recommend);
        params.put("isMine", isMine);
        params.put("user_id", use_id);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        post(URLs.appShow, params, mHandler);
    }
    
    // 秀场 1推荐0全部
    public static void appShow(AsyncHttpResponseHandler mHandler, String recommend, String pageSize, String pageIndex) {
        RequestParams params = new RequestParams();
        params.put("recommend", recommend);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        post(URLs.appShow, params, mHandler);
    }

    // 秀场 全部搜索
    public static void appShowSearch(AsyncHttpResponseHandler mHandler, String keyword, String flag) {
        RequestParams params = new RequestParams();
        params.put("keyword", keyword);
        params.put("flag", flag);
        if (AppContext.userId != null) {
            params.put("user_id", AppContext.userId);
        }
        post(URLs.appShowSearch, params, mHandler);
    }

    // 秀场 关注
    public static void appShowAttention(AsyncHttpResponseHandler mHandler, String id, String pageSize, String pageIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("USER_ID", id);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("USER_ID", id);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.appShowAttention, params, mHandler);
    }

    // 秀场 添加关注
    public static void appAttention(AsyncHttpResponseHandler mHandler, String id, String spotlightId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("USER_ID", id);
        map.put("ATTENTION_ID", spotlightId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("USER_ID", id);
        params.put("ATTENTION_ID", spotlightId);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.appAttention, params, mHandler);
    }

    // 秀场 取消关注
    public static void appCancelAttention(AsyncHttpResponseHandler mHandler, String id, String spotlightId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("USER_ID", id);
        map.put("ATTENTION_ID", spotlightId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("USER_ID", id);
        params.put("ATTENTION_ID", spotlightId);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.appCancelAttention, params, mHandler);
    }

    // 秀场发布(用户的ID和评论内容上传到服务器)
    public static void appPublish(AsyncHttpResponseHandler mHandler, String user_id, String content, String spo_img) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", user_id);
        map.put("content", content);
        map.put("spo_img", spo_img);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);

        String paramStr = CryptionUtil.getSignData(map);
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("content", content);
        params.put("spo_img", spo_img);
        params.put("time", str_time);

        params.put("mKey", paramStr);
        post(URLs.appPublish, params, mHandler);
    }

    /**
     * 获取一级分类列表的数据
     *
     * @param url
     * @param mHandler
     */
    public static void getClassLists(String url, AsyncHttpResponseHandler mHandler) {
        post(url, mHandler);
    }

    /**
     * 智慧管家首页
     *
     * @param housekeeperId
     * @param housekeeperIMG
     * @param housekeeperTitle
     */
    public static void getTitle(AsyncHttpResponseHandler mHandler) {

        post(URLs.HOUSE_KEERER_shouye, mHandler);

    }

    /**
     * 智慧管家预约类型
     */

    public static void getType(AsyncHttpResponseHandler mHandler) {

        try {

            post(URLs.HOUSE_YUYUE_TYPE, null, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 智慧管家列表
     *
     * @param pageindex
     * @param mHandler
     */
    public static void houseKeeper(String pageindex, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("pageindex", pageindex);
            post(URLs.HOUSE_KEERER, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 智慧管家详情
     *
     * @param id
     * @param mHandler
     */
    public static void houseKeeperDeatils(String id, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("id", id);
            post(URLs.HOUSE_KEERER_DETAILS, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 智慧管家预约
     * <p/>
     * wisdom_id, 预约id name,预约人姓名 phone,电话 address,地址 invoice_img,发票图片
     * nameplate_img,铭牌图片 Remarks评论
     */
    public static void houseKeepMakePointment(String userId, String wisdom_id, String name, String phone, String address, String nameplate_img, String invoice_img, String remarks,
                                              AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("wisdom_id", wisdom_id);
            map.put("name", name);
            map.put("phone", phone);
            map.put("address", address);

            map.put("invoice_img", invoice_img);
            map.put("nameplate_img", nameplate_img);
            map.put("remarks", remarks);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("wisdom_id", wisdom_id);
            params.put("name", name);
            params.put("phone", phone);
            params.put("address", address);

            params.put("invoice_img", invoice_img);
            params.put("nameplate_img", nameplate_img);
            params.put("remarks", remarks);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.HOUSE_KEERER_YUYUE, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /***
    // * 三级分类列表商品详情页
    // *
    // * @param bannerList
    // * banner图区域数据
    // * @param fastList
    // * 快捷服务区域数据
    // * @param prommotionLayout
    // * 促销活动区域数据 chartpositionList 销售排行榜
    // */
    // public static void ShouYeHP(String bannerList, String fastList, String
    // prommotionLayout, String chartpositionList,
    // AsyncHttpResponseHandler mHandler) {
    //
    // try {
    // RequestParams params = new RequestParams();
    // params.put("bannerList", bannerList);
    // params.put("fastList", fastList);
    // params.put("prommotionLayout", prommotionLayout);
    // params.put("chartpositionList", chartpositionList);
    //
    // post(URLs.SERVER_URL1, params, mHandler);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // }

    /**
     * 商品详情的加入购物车 1
     *
     * @param userId ：用户名id commodityId：商品id goods_codes：货品id num：购买数量
     *               type：购物车类型（1，app购买，2tv购买）
     *               purchase：购物类型（（1，加入购物车，2，立即结算，3组合商品））
     *               commodityIds：组合商品id（多个用“，”隔开）
     */

    public static void goodsDetailsShoppingCar(String userId, String commodityId, String goods_codes, String num, String type, String purchase, String commodityIds,
                                               String specValue, AsyncHttpResponseHandler mHandler) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("commodityId", commodityId);
            map.put("goods_codes", goods_codes);
            map.put("num", num);
            map.put("type", type);
            map.put("purchase", purchase);
            map.put("commodityIds", commodityIds);
            map.put("specValue", specValue);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());
            map.put("time", str_time);
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("commodityId", commodityId);
            params.put("goods_codes", goods_codes);
            params.put("num", num);
            params.put("type", type);
            params.put("purchase", purchase);
            params.put("commodityIds", commodityIds);
            params.put("specValue", specValue);
            params.put("time", str_time);
            params.put("mKey", paramStr);
            post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHOPPING_CAR, params, mHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *马上结算(旧)
     * @param userId 用户id
     * @param type 类型 -1、普通订单 5、团购 6、秒杀
     * @param num 商品数量
     * @param goodsId 商品id
     * @param goodsNo 商品编号
     * @param mHandler
     */
    public static void buyNow(String userId, String type, int num, String goodsId, String goodsNo, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("type", type);
            params.put("num", num+"");
            params.put("goodsId", goodsId);
            params.put("goodsNo", goodsNo);
            Encryption.encryptSingple(params);
            post(URLs.BUY_NOW, params, mHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *马上结算(新)
     * @param userId 用户id
     * @param shoppingType //-1、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
     * @param num 商品数量
     * @param goodsId 商品id
     * @param goodsNo 商品编号
     * @param promotion_id 礼品id,实物兑换需要提交的
     * @param mHandler
     */
    public static void cartPromptlyInit(String userId, String shoppingType, int num, String goodsId, String goodsNo,String promotion_id, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("shoppingType", shoppingType);
            params.put("num", num+"");
            params.put("goodsId", goodsId);
            params.put("goodsNo", goodsNo);
            params.put("promotion_id", promotion_id);
            Encryption.encryptSingple(params);
            post(URLs.cartPromptlyInit, params, mHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *马上结算(新)
     * @param userId 用户id
     * @param shoppingType //-1、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
     * @param num 商品数量
     * @param goodsId 商品id
     * @param goodsNo 商品编号
     * @param mHandler
     */
    public static void cartPromptlyInit(String userId, String shoppingType, int num, String goodsId, String goodsNo, AsyncHttpResponseHandler mHandler) {
        cartPromptlyInit(userId,shoppingType,num,goodsId,goodsNo,"-1",mHandler);
    }

    /**
     logisticsId 物流公司
     kdCode      物流单号
     id          退/换货id
     userId      用户id
     flag        1退款，2换货
     */
    public static void commitLogistics(String userId, String kdCode, String id, String logisticsId, int flag, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("kdCode", kdCode);
            params.put("id", id);
            params.put("logisticsId", logisticsId);
            params.put("flag", flag+"");
            Encryption.encryptSingple(params);
            post(URLs.afterLogistics, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品详情的加入购物车 2
     *
     * @param TV_PRICE TV_PRICE": 11, 规格值对应的商品价格 "SPEC_VALUE": 规格值名称,
     *                 "SPEC_VALUE_ID": 规格值id
     *                 <p/>
     *                 "SPEC_NAME": 规格名称, "SPEC_NAME_ID": 规格名id
     */
    public static void goodsDetailsAddcar(String commodityId, String tv_price, String spec_value, String spec_value_id, String spec_name, String spec_name_id,
                                          AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("commodityId", commodityId);
            params.put("tv_price", tv_price);
            params.put("spec_value", spec_value);
            params.put("spec_value_id", spec_value_id);
            params.put("spec_name", spec_name);
            params.put("spec_name_id", spec_name_id);
            post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHOPPING_CAR_add, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 三级分类列表商品详情页
     *
     * @param PRICE 乐虎价格, COMMODITY_NAME 商品名称, COMMODITY_ID 商品id, COMMODITY_AD
     *              商品广告语,
     *              <p/>
     *              NUM COMMODITY_NAME
     */
    public static void goodsDetailsHttp(String userId, String commodity_id, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            if (userId != null) {
                params.put("userId", userId);
            }
            params.put("COMMDOITY_ID", commodity_id);
            post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // -----------------------------------------启动页------------------------------------
    public static void appOpenPicture(AsyncHttpResponseHandler mHandler) {
        post(URLs.appOpenPicture, mHandler);
    }

    public static void goodsDetailsHttpNew(String userId, String commodity_id, String storeId, String goodsNo, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            if (userId != null) {
                params.put("userId", userId);
            }
//            params.put("goodsId", 10786+"");
//            params.put("storeId", 0+"");
//            params.put("goodsNo", 5415321+"");
            params.put("goodsId", commodity_id);
            params.put("storeId", storeId);
            params.put("goodsNo", goodsNo);
            post(URLs.appCommdoityDataById, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 商品内容
     * @param goodsId
     * @param mHandler
     */
    public static void goodsContent(String goodsId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("goodsId", goodsId);
            post(URLs.goodsContent, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // -----------------------------------------首页------------------------------------

    /**
     * 首页
     *
     * @param mHandler
     */
    public static void appIndexFirst(AsyncHttpResponseHandler mHandler) {
        post(URLs.appIndexFirst, null, mHandler);
    }
    

    public static void appIndexPoly(AsyncHttpResponseHandler mHandler) {
        post(URLs.appIndexPoly, null, mHandler);
    }

    /**
     * 获取二级分类列表的数据
     *
     * @param url
     * @param mHandler
     */
    public static void getClassListLevel2(String url, String id, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("TWO_ID", id);
        post(url, params, mHandler);
    }

    /**
     * 获取三级分类列表的数据
     *
     * @param url
     * @param mHandler
     */
    public static void getClassListLevel3(String url, String id, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("THREEE_ID", id);
        post(url, params, mHandler);
    }

    public static void appAllFast(AsyncHttpResponseHandler mHandler) {
        post(URLs.appAllFast, null, mHandler);
    }

    public static void appAddFast(AsyncHttpResponseHandler mHandler, String userId, String fastIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("fastIds", fastIds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("fastIds", fastIds);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.appAddFast, null, mHandler);
    }

    // -----------------------------------------首页-结束-----------------------------------

    /**
     * 三级分类列表商品详情点评晒单
     *
     * @param PHONE
     *            "15980847932",用户手机号（如果用户名为空，则用户名用手机号替代）, FACE_IMAGE_PATH 用户头像,
     *            CONTENT "商品不错，真的好喜欢",评价内容, SCORE 5 分数, CREATE_TIME
     *            "2014-08-14 08:08:36",创建时间, USER_NAME 用户名, SIZEDIMENSION "1",
     *            大小, COLOR "红色",颜色 COMMODITY_IMAGE_PATH 晒图, NUM : 1, LEVEL_NAME
     *            : 用户等级 reviewScore 好评率,
     * @param
     */

    /**
     * 三级分类列表商品详情商品图片
     *
     * @param COMMODITY_IMAGE_LIST
     */

    public static void recommentSunSingle(String commodity_id, String commodity_image_list, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("commodity_id", commodity_id);
            params.put("commodity_image_list", commodity_image_list);
            post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_RECOMMENT, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 四级分类列表商品筛选 1， 类别属性
     *
     * @param PROPERTYNAME PROPERTYID
     */

    public static void selecteAttributes(String attributeId, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("THREE_ID", attributeId);
            post(URLs.SELECTTION_URL, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 四级分类列表商品筛选 2， 类别属性值
     *
     * @param PROPERTYNAME 属性名, PROPERTYID 属性名对应id BRAND_ID 品牌id, BRAND_NAME 品牌名称
     */

    public static void ScreeningAttributeValue(String attributeValueId, String propertyName, String propertyId, String brandId, String bandName, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("attributeValueId", attributeValueId);
            params.put("propertyName", propertyName);
            params.put("propertyId", propertyId);
            params.put("brandId", brandId);
            params.put("bandName", bandName);
            post(URLs.SCTRRNING_VALUE, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 商品点评晒单评论数
     *
     * @param commodity_id 商品id
     */
    public static void getDianPingShu(String commodity_id, AsyncHttpResponseHandler mHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put("COMMDOITY_ID", commodity_id);
            post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHANDAN, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 商品点评晒单评论内容
     *
     * @param commodity_id 商品id
     * @param pageSize     每页大小
     * @param pageIndex    当前页
     * @param Flag         0全部，1好评，2中评, 3差评
     */
    public static void sunSingle(String pageSize, String pageIndex, String commodity_id, String Flag, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("pageSize", pageSize);
            params.put("pageIndex", pageIndex);
            params.put("COMMDOITY_ID", commodity_id);
            params.put("flag", Flag);

            post(URLs.SORT_LIST_LEVEL3_GOODS_DETAILS_SHANDAN_RECOMMENT, params, mHandler);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    // 分类列表图片模式

    public static void getClassListTuPianMode(String url, String id, String flag, String sort, String pageIndex, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("category_id", id);
        params.put("flag", flag);
        params.put("sort", sort);
        params.put("pageIndex", pageIndex);
        post(url, params, mHandler);
    }

    /**
     * 四级商品列表
     *
     * @param sales_volume 1销量2价格3评价4人气
     * @param pageSize     每页大小 10
     * @param pageIndex    当前页 1
     * @param category_id  三级类目id
     * @param propertyId   属性id,多个属性以逗号隔开
     * @param BRAND_ID     品牌id
     * @param mark         4表示，是通过筛选
     * @param keyword      站内搜索关键词
     * @param sort         1,升序，2降序
     * @param storeId      店铺ID
     * @param mHandler
     */

    public static void getFourClassList(String sales_volume, String pageSize, String pageIndex, String category_id, String propertyId, String BRAND_ID,
                                        String mark, String keyword, String sort, String storeId,AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("sortMode", sales_volume);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        params.put("sortType", sort);
        params.put("mark", mark);
        if (null != mark && "1".equals(mark)) {//关键字
            params.put("keyword", keyword);
            if(!TextUtils.isEmpty(storeId)) {
                params.put("storeId", storeId);
            }
        } else if (null != mark && "2".equals(mark) || null != mark && "3".equals(mark)) {//全部和二级分分类
            params.put("categoryId", category_id);
        } else if (null != mark && "4".equals(mark) || null != mark && "3".equals(mark)) {//筛选
            if (null != BRAND_ID && !"".equals(BRAND_ID)) {
                params.put("brandIds", BRAND_ID);
            }
            params.put("attributeIds", propertyId);
        } else if(null != mark&&"5".equals(mark)) {
            if(!TextUtils.isEmpty(storeId)) {
                params.put("storeId", storeId);
            }
        }
        post(URLs.SORT_LIST_LEVEL_TUPIAN, params, mHandler);
    }

    /**
     * 银联支付
     *
     * @param url
     * @param orderId
     * @param mHandler
     */
    public static void payForUP(String url, String orderId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        post(url, params, mHandler);
    }

    /**
     * 支付宝支付
     *
     * @param url
     * @param orderId
     * @param mHandler
     */
    public static void payForAlipay(String url, String orderId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            Map<String, Object> map = new HashMap<String, Object>();
            String mOrderId;
            mOrderId = Des3.encode(orderId);
            map.put("orderId", mOrderId);
            params.put("orderId", mOrderId);
            String paramStr = CryptionUtil.getSignData(map);
            params.put("mKey", paramStr);
            post(url, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信支付，获取后台签名和相关数据
     *
     * @param url
     * @param orderId
     * @param mHandler
     */
    public static void payForWX(String url, String orderId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        post(url, params, mHandler);
    }

    /**
     * 微信支付，获取access_token
     *
     * @param url        https://api.weixin.qq.com/cgi-bin/token
     * @param grant_type 获取access_toke，此处填写client_credential
     * @param appid      APP唯一凭证
     * @param secret     应用密钥，在微信开放平台提交应用审核通过后获得
     * @param mHandler
     */
    public static void getAccess_token(String appid, String secret, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("grant_type", "client_credential");
        params.put("appid", appid);
        params.put("secret", secret);
        post(URLs.WXPAY_ACCESS_TOKEN, params, mHandler);
    }

    /**
     * 微信支付，生成预支付订单
     *
     * @param url           https://api.weixin.qq.com/pay/genprepay
     * @param access_token  getAccess_token方法获取的ACCESS_TOKEN
     * @param appid         应用唯一标识，在微信开放平台提交应用审核通过后获得
     * @param traceid       商家对用户的唯一标识,如果用微信SSO，此处建议填写授权用户的openid
     * @param noncestr      32位内的随机串，防重发
     * @param packagename   订单详情（具体生成方法见后文）
     * @param timestamp     时间戳，为1970年1月1日00:00到请求发起时间的秒数
     * @param app_signature 签名（具体生成方法见后文）
     * @param sign_method   加密方式，默认为sha1
     * @param mHandler
     */
    public static void getOrderInfo(String access_token, String appid, String traceid, String noncestr, String packagename, String timestamp, String app_signature,
                                    String sign_method, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", access_token);
        params.put("appid", appid);
        params.put("traceid", traceid);
        params.put("noncestr", noncestr);
        params.put("package", packagename);
        params.put("timestamp", timestamp);
        params.put("app_signature", app_signature);
        params.put("sign_method", sign_method);
        post(URLs.WXPAY_ORDER_INFO, params, mHandler);
    }

    /**
     * 支付回调
     *
     * @param type     支付类型 1.银联支付 2.支付宝3.微信支付
     * @param orderId  订单号
     * @param tn       流水号
     * @param mHandler
     */
    public static void toRequestWebResult(int type, String orderId, String tn, AsyncHttpResponseHandler mHandler) {
        StringBuffer url = new StringBuffer();
        url.append(URLs.SERVER_URL);
        Map<String, Object> map = new HashMap<String, Object>();
        RequestParams params = new RequestParams();
        if (1 == type) {// 银联支付
            map.put("orderNumber", orderId);
            map.put("tn", tn);
            params.put("orderNumber", orderId);
            params.put("tn", tn);
            url.append("alipayUnionpayAfter.do");
        } else if (2 == type) {
            System.out.println("加了orderid------" + orderId);
            map.put("orderId", orderId);
            params.put("orderId", orderId);
            url.append("alipayAfter.do");
        } else if (3 == type) {
            System.out.println("加了orderid------" + orderId);
            map.put("orderId", orderId);
            params.put("orderId", orderId);
            url.append("alipayAfter1.do");
        }
        String paramStr = CryptionUtil.getSignData(map);
        params.put("mKey", paramStr);
        System.out.println(url + "");
        post(url.toString(), params, mHandler);
    }

    /**
     * 筛选点击完成
     *
     * @param "PRICE": 商品价格, "COMMODITY_IMAGE_PATH": 商品图片, COMMODITY_NAME": 商品名称
     *                 PROPERTYID:属性名对应id（多个id以逗号隔开） BRAND_ID 品牌id priceStart 价格起始值
     *                 priceEnd价格结束值
     */

    public static void FinishValue(String brand_id, String propertyid, String priceStart, String priceEnd, String price, String commodity_image_path, String commodity_name,
                                   AsyncHttpResponseHandler mHandler) {

        try {

            RequestParams params = new RequestParams();
            params.put("brand_id", propertyid);
            params.put("propertyid", propertyid);
            params.put("priceStart", priceStart);
            params.put("priceEnd", priceEnd);
            params.put("price", price);
            params.put("commodity_image_path", commodity_image_path);
            params.put("commodity_name", commodity_name);
            post(URLs.SCTRRNING_VALUE_finish, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 热销排行商品名称接口

    public static void getHLVHttp(AsyncHttpResponseHandler mHandler) {
        try {
            post(URLs.SALERANK, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 热销排行商品列表接口

	/*
     * @two_parent_class pageSize pageNum
	 */

    public static void getVlvHttp(String id, String pageIndex, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("two_parent_class", id);
            params.put("pageNum", pageIndex);
            post(URLs.SALERANK_list, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 站内搜索初始化

    /**
     * USER_ID 用户Id KEYWORD 搜索关键字
     */

    public static void SearchInit(AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            post(URLs.SEARCH_INIT, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 免运费
     */

    public static void FreeShipping(AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            post(URLs.FREESHIPPING, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 站内搜索下拉框

    /**
     * @param url
     * @param mHandler
     */

    public static void searchHistroyList(String key_word, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("keyword", key_word);
            post(URLs.SEARCH_HISTROYLIST, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 周边商店
     *
     * @param cityName
     * @param pageIndex
     * @param longitude 经度
     * @param latitude  纬度
     * @param mHandler
     */
    public static void appNearbyStore(int cityId, String cityName, int pageIndex, double longitude, double latitude, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        // params.put("cityId", String.valueOf(cityId));
        params.put("cityName", cityName);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude", String.valueOf(latitude));
        post(URLs.appNearbyStore, params, mHandler);
    }

    /**
     * 获取版本
     *
     * @param mHandler
     */
    public static void getVersion(AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("t", "1");
        params.put("i", AppContext.getInstance().getNearbyId());
        post(URLs.GET_VERSION, params, mHandler);
    }

    // 获取专区一和专区二的数据
    public static void getZhuanQu(String url, String id, String pageIndex, String flag, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("pageIndex", pageIndex);
        params.put("flag", flag);
        post(url, params, mHandler);
    }

    /**
     * 获取积分Club的数据
     */
    public static void getClub(String userId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        post(URLs.GET_CLUB_DATA, params, mHandler);
    }

    /**
     * 提交积分Club的数据
     */
    public static void submitClub(String userId, String id, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("luckId", id);
        post(URLs.SUBMIT_CLUB_DATA, params, mHandler);
    }

    /**
     * 通过二维码或者一维码获取商品ID
     *
     * @param code
     * @param mHandler
     */
    public static void scanningCode(String code, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("code", code);
        post(URLs.scanningCode, params, mHandler);
    }

    /**
     * 商品详情收藏商品
     *
     * @param USER_ID
     * @param COMMODITY_ID
     * @param mHandler
     */
    public static void appCollect(String USER_ID, String COMMODITY_ID, String goodsNo, String storeId, int type, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", String.valueOf(USER_ID));
        params.put("goodsId", COMMODITY_ID);
        params.put("goodsNo", goodsNo);
        params.put("storeId", storeId);
        params.put("type", type + "");
        post(URLs.appCollect, params, mHandler);
    }

    /**
     * 商品详情取消收藏
     *
     * @param USER_ID
     * @param COMMODITY_ID
     * @param mHandler
     */
    public static void appCancelCollect(String USER_ID, String COMMODITY_ID, String goodsNo, String storeId, int type, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", String.valueOf(USER_ID));
        params.put("goodsId", COMMODITY_ID);
        params.put("goodsNo", goodsNo);
        params.put("storeId", storeId);
        params.put("type", type + "");
        post(URLs.appCancelCollect, params, mHandler);
    }

    /**
     * 数据统计，统计用户量
     *
     * @param imei
     * @param verision
     * @param mHandler
     */
    public static void sendMobileInfo(String imei, String version, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("imei", imei);
        params.put("model", android.os.Build.MODEL);
        params.put("sysVersion", android.os.Build.VERSION.RELEASE);
        params.put("version", version);
        params.put("type", 1 + "");// 1代表安卓，2代表iOS
        params.put("nearbyId", AppContext.getInstance().getNearbyId());
        post(URLs.SEND_MOBILE_INFO, params, mHandler);
    }

    /**
     * 融云
     *
     * @param mHandler
     */
    public static void sendRongCloud(AsyncHttpResponseHandler mHandler) {
        post(URLs.RONGCLOUD_TOKEN, mHandler);
    }

    /**
     * 物流列表查询
     *
     * @param mHandler
     * @param userId
     */
    public static void appQueryLogistics(AsyncHttpResponseHandler mHandler, String userId, String orderId) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        if (!StringUtils.isBlank(orderId))
            params.put("orderId", orderId);
        post(URLs.appQueryLogistics, params, mHandler);
    }

    /**
     * 物流公司
     * @param mHandler
     */
    public static void getLogisticsInfo(AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();

        post(URLs.appLogisticsInfo, params, mHandler);
    }

    /**
     * 车辆定位
     *
     * @param mHandler
     * @param id
     */
    public static void carLocattion(AsyncHttpResponseHandler mHandler, int id) {
        RequestParams params = new RequestParams();
        params.put("id", String.valueOf(id));
        post(URLs.connected, params, mHandler);
    }

    /**
     * @param mHandler
     * @param type      （类型，默认不传，查询全部，1：充值 2：消费 3：退款）
     * @param startTime
     * @param endTime
     * @param userId
     * @param token     （令牌，没有传空）
     * @param cardNum
     * @param curPage   （当前页）
     */
    public static void appPrepaidCardsList(AsyncHttpResponseHandler mHandler, int type, String startTime, String endTime, String userId, String token, String cardNum, int curPage) {
        RequestParams params = new RequestParams();
        if (type != 0)
            params.put("type", String.valueOf(type));
        if (!StringUtils.isBlank(startTime))
            params.put("startTime", startTime);
        if (!StringUtils.isBlank(endTime))
            params.put("endTime", endTime);
        params.put("userId", userId);
        if (!StringUtils.isBlank(token))
            params.put("token", token);
        if (!StringUtils.isBlank(cardNum))
            params.put("cardNum", cardNum);
        params.put("curPage", String.valueOf(curPage));
        String val = cardNum + 123 + curPage + 123 + userId + 123 + type;
        val = CryptionUtil.md5(val);
        params.put("val", val);
        post(URLs.appPrepaidCardsList, params, mHandler);
    }

    /**
     * 获取系统消息
     *
     * @param userId
     * @param mHandler
     */
    public static void getSystemMsg(String userId, String pageindex, AsyncHttpResponseHandler mHandler) {
        try {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("userId", userId);
//            map.put("pageIndex", pageindex);
//            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String str_time = sf.format(new Date());
//            map.put("time", str_time);
//            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("pageIndex", pageindex);
//            params.put("time", str_time);
//            params.put("mKey", paramStr);
//            post(URLs.SYSTEMMESSAGE_LIST, params, mHandler);
            post(URLs.SYSTEMMESSAGE_LIST_S, params, mHandler);//接口变更
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改系统消息状态
     *
     * @param letterId 消息ID
     */
    public static void appLetterReader(String letterId, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("letterId", letterId);
            post(URLs.SYSTEMMESSAGE_STUATS, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定生活服务卡
     *
     * @param userId
     * @param cardNum
     * @param password
     * @param mHandler
     * @throws Exception
     */
    public static void appBindCardInfo(String userId, String cardNum, String password, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("cardNum", cardNum);
            String mPassword;
            mPassword = Des3.encode(password);
            params.put("password", mPassword);
            String val = cardNum + 123 + mPassword + 123 + userId;
            val = CryptionUtil.md5(val);
            params.put("val", val);
            post(URLs.appBindCardInfo, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生活服务卡修改密码
     *
     * @param userId
     * @param cardNum
     * @param oldPwd
     * @param newPwd
     * @param mHandler
     */
    public static void appUpdateCardPwd(String userId, String cardNum, String oldPwd, String newPwd, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("cardNum", cardNum);
            oldPwd = Des3.encode(oldPwd);
            params.put("oldPwd", oldPwd);
            newPwd = Des3.encode(newPwd);
            params.put("newPwd", newPwd);
            String val = cardNum + 123 + oldPwd + 123 + newPwd + 123 + userId;
            val = CryptionUtil.md5(val);
            params.put("val", val);
            post(URLs.appUpdateCardPwd, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 申请生活服务卡
     *
     * @param userId   用户ID
     * @param tel      电话
     * @param vCard
     * @param userName 用户名
     * @param sCard
     * @param address  用户地址
     * @param mHandler 句柄
     */
    public static void appApplyCard(String userId, String tel, String vCard, String userName, String sCard, String address, AsyncHttpResponseHandler mHandler) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("tel", tel);
            params.put("vCard", vCard);
            userName = Des3.encode(userName);
            params.put("userName", userName);
            params.put("sCard", sCard);
            address = Des3.encode(address);
            params.put("address", address);
            String val = userId + 123 + tel + 123 + vCard + 123 + userName + 123 + address;
            val = CryptionUtil.md5(val);
            params.put("val", val);
            post(URLs.appApplyCard, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证生活服务卡号
     *
     * @param userId
     * @param tel
     * @param vCard
     * @param userName
     * @param sCard
     * @param address
     * @param mHandler
     */
    public static void appValidationCardInfo(String cardNum, String token, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("cardNum", cardNum);
        params.put("token", token);
        String val = CryptionUtil.md5(cardNum);
        params.put("val", val);
        post(URLs.appValidationCardInfo, params, mHandler);
    }

    /**
     * 充值生活服务卡
     *
     * @param userId
     * @param cardNum
     * @param money
     * @param token
     * @param mHandler
     */
    public static void appAddRechargeOrder(String userId, String cardNum, String money, String token, String type, String pid, AsyncHttpResponseHandler mHandler) {
        try {
            String val = userId + "123" + money + "123" + cardNum + "123" + type + "123" + pid;
            val = CryptionUtil.md5(val);
            RequestParams params = new RequestParams();
            params.put("id", userId);
            params.put("cardNum", cardNum);
            params.put("money", money);
            params.put("token", token);
            params.put("type", type);
            params.put("pid", pid);
            params.put("val", val);
            post(URLs.appAddRechargeOrder, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除服务卡绑定
     *
     * @param mHandler
     * @param userId
     * @param cardNum
     * @param password
     */
    public static void appRelieveBindCard(AsyncHttpResponseHandler mHandler, String userId, String cardNum, String password) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("cardNum", cardNum);
            password = Des3.encode(password);
            params.put("password", password);
            String val = cardNum + 123 + userId + 123 + password;
            val = CryptionUtil.md5(val);
            params.put("val", val);
            post(URLs.appRelieveBindCard, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务卡支付
     *
     * @param mHandler
     * @param userId
     * @param cardNum
     * @param password
     * @param money
     * @param orderId
     * @param token
     */
    public static void appCardRedeem(AsyncHttpResponseHandler mHandler, String userId, String cardNum, String password, String money, String orderId, String token, String id) {
        try {
            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("cardNum", cardNum);
            password = Des3.encode(password);
            params.put("password", password);
            params.put("money", money);
            params.put("orderId", orderId);
            params.put("token", token);
            params.put("id", id);
            String val = cardNum + 123 + userId + 123 + password + 123 + id;
            val = CryptionUtil.md5(val);
            params.put("val", val);
            post(URLs.appCardRedeem, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 充值优惠活动列表
     *
     * @param mHandler
     */
    public static void appCardPromotionsList(AsyncHttpResponseHandler mHandler) {
        post(URLs.appCardPromotionsList, mHandler);
    }

    /**
     * 验证是否能申请售后(老接口，不用了)
     *
     * @param orderId
     * @param commodityId
     * @param mHandler
     */
    public static void appVerifyChangeAndReturn(String orderId, String commodityId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        map.put("commodityId", commodityId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        params.put("orderId", orderId);
        params.put("commodityId", commodityId);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.verifyAfter, params, mHandler);
    }

    /**
     * 退/换货验证
     *
     * @param orderId 订单id
     * @param mHandler
     */
    public static void appVerifyAfter(String orderId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        Encryption.encryptSingple(params);
        post(URLs.verifyAfter, params, mHandler);
    }

    public static void getWeChatToken(String code, AsyncHttpResponseHandler mHandler) {
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%1s&secret=%2s&code=%3s&grant_type=authorization_code", Constants.APP_ID,
                Constants.APP_SECRET, code);
        LogUtil.d("WXEntryActivity", "get access token, url = " + url);
        mClient.get(url, mHandler);
    }

    /**
     * 自提门店列表
     *
     * @param cityId    城市ID
     * @param longitude 经度
     * @param latitude  纬度
     * @param mHandler
     */
    public static void appNearShop(int cityId, double longitude, double latitude, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("cityId", String.valueOf(cityId));
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude", String.valueOf(latitude));
        post(URLs.appNearShop, params, mHandler);
    }

    /**
     * 微信检测账户接口
     *
     * @param openid
     * @param mHandler
     */
    public static void appOpenIDOAuthPhone(String openid, int flag, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("openid", openid);
        if (flag != 0)
            params.put("flag", String.valueOf(flag));
        post(URLs.appOpenIDOAuthPhone, params, mHandler);
    }

    /**
     * 秒杀标题
     *
     * @param mHandler
     */
    public static void seckillLateralTitle(AsyncHttpResponseHandler mHandler) {
        post(URLs.seckillLateralTitle, mHandler);
    }

    /**
     * 秒杀列表  中途修改接口名  原接口名是URLs.allSecKill
     *
     * @param seckillId
     * @param mHandler
     */
    public static void allSecKill(int seckillId, int pageIndex, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("seckillId", String.valueOf(seckillId));
        params.put("pageIndex", String.valueOf(pageIndex));
        if (!StringUtils.isBlank(AppContext.getInstance().userId))
            params.put("userId", AppContext.getInstance().userId);
        post(URLs.seckillData, params, mHandler);
    }

    /**
     * 闪购列表
     *
     * @param pageIndex
     * @param mHandler
     */
    public static void flashSaleActive(int pageIndex, int type, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("type", String.valueOf(type));
        post(URLs.flashSaleActive, params, mHandler);
    }

    /**
     * 闪购专区
     *
     * @param pageIndex
     * @param id
     * @param mHandler
     */
    public static void flashSaleRegion(int pageIndex, int id, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("id", String.valueOf(id));
        post(URLs.flashSaleRegion, params, mHandler);
    }
    
    /**
     * 闪购专区
     *
     * @param pageIndex
     * @param id
     * @param mHandler
     */
    public static void flashGoods(int pageIndex, int id, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("id", String.valueOf(id));
        post(URLs.flashGoods, params, mHandler);
    }

    /**
     * 新品预约分类
     *
     * @param mHandler
     */
    public static void bespeakType(AsyncHttpResponseHandler mHandler) {
        post(URLs.bespeakType, mHandler);
    }

    /**
     * 新品预约列表 type 新品预约分类ID (不传 或传-1 表示 全部)
     *
     * @param mHandler
     */
    public static void bespeak(int type, int pageIndex, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("type", String.valueOf(type));
        params.put("pageIndex", String.valueOf(pageIndex));
        if (AppContext.userId != null) {
            params.put("userId", AppContext.userId);
        }
        post(URLs.bespeak, params, mHandler);
    }

    /**
     * 新品预约结果
     *
     * @param mHandler
     */
    public static void bespeakRecord(int Id, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("Id", String.valueOf(Id));
        params.put("userId", AppContext.userId);
        Encryption.encryptSingple(params);
        post(URLs.bespeakRecord, params, mHandler);
    }

    /**
     * 我的预约列表
     *
     * @param mHandler
     */
    public static void bespeakRecordList(int pageIndex, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", AppContext.userId);
        params.put("pageIndex", String.valueOf(pageIndex));
        Encryption.encryptSingple(params);
        post(URLs.myNewGoodsData, params, mHandler);
    }

    /**
     * 介绍的统一
     *
     * @param introduceId
     * @param mHandler
     */
    public static void introduceId(int introduceId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("introduceId", String.valueOf(introduceId));
        post(URLs.introduceId, params, mHandler);
    }

    /**
     * judgeSeckill
     *
     * @param seckillId
     * @param mHandler
     */
    public static void judgeSeckill(int seckillId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("seckillId", String.valueOf(seckillId));
        post(URLs.judgeSeckill, params, mHandler);
    }

    /**
     * 发票信息
     *
     * @param invoiceId
     * @param mHandler
     */
    public static void invoiceInfo(int invoiceId, AsyncHttpResponseHandler mHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("invoiceId", String.valueOf(invoiceId));
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("invoiceId", String.valueOf(invoiceId));
        params.put("mKey", paramStr);
        post(URLs.invoiceInfo, params, mHandler);
    }

    /**
     * 热搜商品
     */
    public static void hotSearch(int pageIndex, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(pageIndex));
        post(URLs.HOT_SEARCH, params, mHandler);
    }

    /**
     * 乐虎券删除
     *
     * @param mHandler
     * @param id
     */
    public static void appMyCouponDelete(AsyncHttpResponseHandler mHandler, int id) {
        RequestParams params = new RequestParams();
        params.put("Id", String.valueOf(id));
        post(URLs.appMyCouponDelete, params, mHandler);
    }

    /**
     * 图片上传
     *
     * @param handler
     * @param data
     * @param fileType
     */
    public static void imgUpload(AsyncHttpResponseHandler handler, String data, String fileType, String userId, int flag) {
        RequestParams params = new RequestParams();
        params.put("data", data);
        params.put("fileType", fileType);
        params.put("userId", userId);
        params.put("flag", String.valueOf(flag));
        post(URLs.appImgUpload, params, handler);
    }

    /**
     * 会员等级
     *
     * @param handler
     * @param userId
     */
    public static void appUserRank(AsyncHttpResponseHandler handler, String userId) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        post(URLs.appUserRank, params, handler);
    }

    /**
     * 根据ID查询订单价格
     *
     * @param handler
     * @param orderId
     */
    public static void orderPrice(AsyncHttpResponseHandler handler, String orderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        params.put("mKey", paramStr);
        post(URLs.orderPrice, params, handler);
    }

    /**
     * 乐虎红包申请记录
     *
     * @param mHandler
     * @param userId
     * @param pageIndex
     */
    public static void queryTicketPacketList(AsyncHttpResponseHandler mHandler, String userId, int pageIndex) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pageIndex", String.valueOf(pageIndex));
        post(URLs.queryTicketPacketList, params, mHandler);
    }

    /**
     * 乐虎红包类型
     *
     * @param mHandler
     */
    public static void addTicketPacketInit(AsyncHttpResponseHandler mHandler) {
        post(URLs.addTicketPacketInit, mHandler);
    }

    /**
     * 申请乐虎红包
     *
     * @param mHandler 句柄
     * @param userId   用户ID
     * @param type
     * @param typeName
     * @param price
     * @param imgList
     */
    public static void addTicketPacket(AsyncHttpResponseHandler mHandler, String userId, int type, String typeName, String price, String imgList) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("type", String.valueOf(type));
        params.put("typeName", typeName);
        params.put("price", price);
        params.put("imgList", imgList);
        post(URLs.addTicketPacket, params, mHandler);
    }

    /**
     * 店铺首页
     *
     * @param mHandler 句柄
     * @param storeId  店铺ID
     */
    public static void storeIndex(AsyncHttpResponseHandler mHandler, int storeId) {
        RequestParams params = new RequestParams();
        if (!StringUtils.isBlank(AppContext.getInstance().userId))
            params.put("userId", AppContext.getInstance().userId);
        params.put("storeId", String.valueOf(storeId));
        post(URLs.storeIndex, params, mHandler);
    }

    /**
     * 店铺详情
     *
     * @param mHandler 句柄
     * @param storeId  店铺ID
     */
    public static void storeDetail(AsyncHttpResponseHandler mHandler, int storeId) {
        RequestParams params = new RequestParams();
        if (!StringUtils.isBlank(AppContext.getInstance().userId))
            params.put("userId", AppContext.getInstance().userId);
        params.put("storeId", String.valueOf(storeId));
        post(URLs.storeDetail, params, mHandler);
    }

    /**
     * 店铺商品分类
     *
     * @param mHandler 句柄
     * @param storeId  店铺ID
     */
    public static void storeCategory(AsyncHttpResponseHandler mHandler, int storeId) {
        RequestParams params = new RequestParams();
        params.put("storeId", String.valueOf(storeId));
        post(URLs.storeCategory, params, mHandler);
    }

    /**
     * 商品列表
     *
     * @param mHandler     句柄
     * @param storeId      店铺id
     * @param keyword      搜索关键字
     * @param categoryId   分类id  分类ID没有二级分类传一级分类的ID
     * @param brandIds     品牌ids（以逗号隔开）
     * @param attributeIds 根据属性筛选（以逗号隔开）
     * @param sortMode     排序方式 1销量，2价格，3评价，4人气，5时间
     * @param sortType     排序类型： 1,升序，2降序
     * @param mark         1根据关键字搜索，2根据二级分类搜索，3根据三级分类搜索，4筛，5店铺全部商品，6店铺产品推荐，7店铺新品
     * @param pageIndex    当前页码
     */
    public static void goodsCategoryList(AsyncHttpResponseHandler mHandler, int storeId, String keyword, int categoryId, String brandIds, String attributeIds, int sortMode, int sortType, int mark, int pageIndex) {
        RequestParams params = new RequestParams();
        params.put("storeId", String.valueOf(storeId));
        params.put("keyword", keyword);
        if (categoryId != 0)
            params.put("categoryId", String.valueOf(categoryId));
        params.put("brandIds", brandIds);
        params.put("attributeIds", attributeIds);
        if (sortMode != 0)
            params.put("sortMode", String.valueOf(sortMode));
        params.put("sortType", String.valueOf(sortType));
        params.put("mark", String.valueOf(mark));
        params.put("pageIndex", String.valueOf(pageIndex));
        post(URLs.goodsCategoryList, params, mHandler);
    }

    /**
     * 获取优惠套餐数据
     *
     * @param mHandler 回调
     * @param goodsNo  商品编号
     * @param storeId  店铺id
     */
    public static void getDiscountPackageData(String goodsNo, String storeId, AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("goodsNo", goodsNo);
        params.put("storeId", storeId);
        post(URLs.DISCOUNT_PACKAGE, params, mHandler);
    }


    /**
     * 商店促销商品列表
     *
     * @param mHandler  句柄
     * @param storeId   店铺ID
     * @param pageIndex 页数
     */
    public static void storePromotion(AsyncHttpResponseHandler mHandler, int storeId, int pageIndex) {
        RequestParams params = new RequestParams();
        params.put("storeId", String.valueOf(storeId));
        params.put("pageIndex", String.valueOf(pageIndex));
        post(URLs.storePromotion, params, mHandler);
    }

    /**
     * 查询执照
     *
     * @param mHandler 句柄
     * @param storeId  店铺ID
     */
    public static void BLInfo(AsyncHttpResponseHandler mHandler, int storeId) {
        RequestParams params = new RequestParams();
        params.put("storeId", String.valueOf(storeId));
        post(URLs.BLInfo, params, mHandler);
    }

    /**
     * 店铺品牌介绍
     *
     * @param mHandler 句柄
     * @param storeId  店铺ID
     */
    public static void storeNavigation(AsyncHttpResponseHandler mHandler, int storeId) {
        RequestParams params = new RequestParams();
        params.put("storeId", String.valueOf(storeId));
        post(URLs.storeNavigation, params, mHandler);
    }

    /**
     * 商品和店铺关注
     *
     * @param mHandler 句柄
     * @param userId   用户id
     * @param storeId  店铺ID 商品关注时传小于0的数进来
     * @param goodsNo  商品编号
     * @param goodsId  商品id 店铺关注时传小于0的数进来
     * @param type     关注类型： 1 ：商品 2 ：店铺
     */
    public static void addFocus(AsyncHttpResponseHandler mHandler, String userId, int storeId, String goodsNo, int goodsId, int type) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        if (storeId > 0)
            params.put("storeId", String.valueOf(storeId));
        if (!StringUtils.isBlank(goodsNo))
            params.put("goodsNo", goodsNo);
        if (goodsId > 0)
            params.put("goodsId", String.valueOf(goodsId));
        params.put("type", String.valueOf(type));
        post(URLs.addFocus, params, mHandler);
    }

    /**
     * 取消商品和店铺关注
     *
     * @param mHandler 句柄
     * @param userId   用户id
     * @param storeId  店铺ID 商品关注时传小于0的数进来
     * @param goodsNo  商品编号
     * @param goodsId  商品id 店铺关注时传小于0的数进来
     * @param type     关注类型： 1 ：商品 2 ：店铺
     */
    public static void cancelFocus(AsyncHttpResponseHandler mHandler, String userId, int storeId, String goodsNo, int goodsId, int type) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        if (storeId > 0)
            params.put("storeId", String.valueOf(storeId));
        if (!StringUtils.isBlank(goodsNo))
            params.put("goodsNo", goodsNo);
        if (goodsId > 0)
            params.put("goodsId", String.valueOf(goodsId));
        params.put("type", String.valueOf(type));
        post(URLs.cancelFocus, params, mHandler);
    }

    /**
     * 商品详情加入购物车
     *
     * @param mHandler              句柄
     * @param goodsNo               商品规格
     * @param goodsIds              商品ID
     * @param userId                用户ID
     * @param promotionId           活动ID  没有加进去 先传－1
     * @param num                   数量
     * @param shoppingType          类型  -1、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
     *
     */
    public static void addShoppingCart(AsyncHttpResponseHandler mHandler, String goodsNo, String goodsIds, String userId,int promotionId, int num,int shoppingType) {
        RequestParams params = new RequestParams();
        params.put("goods_no", goodsNo);
        params.put("goods_id", goodsIds);
        params.put("userId", userId);
        params.put("num", String.valueOf(num));
        params.put("promotion_id", String.valueOf(promotionId));
        params.put("shoppingType", String.valueOf(shoppingType));
        Encryption.encryptSingple(params);
        post(URLs.addShoppingCart, params, mHandler);
    }

    /**
     *
     * @param userId     用户ID
     * @param selectIds  当前选中的shoppingId集合
     *
     */
    public static void ajaxCart(AsyncHttpResponseHandler mHandler, String userId, String selectIds) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("selectIds", selectIds);
        Encryption.encryptSingple(params);
        post(URLs.ajaxCart, params, mHandler);
    }

    /**
     * 更新购物车中单一商品的购买数量
     *
     * @param mHandler 句柄
     * @param goodsNo  商品规格
     * @param goodsId  商品ID
     * @param userId   用户ID
     * @param num      修改的数量
     */
    public static void updateShoppingCartNum(AsyncHttpResponseHandler mHandler, String goodsNo,
                                             int goodsId, String userId, int num,long promotionId,
                                             int shoppingType,String selectIds) {
        RequestParams params = new RequestParams();
        params.put("goodsNo", goodsNo);
        params.put("goodsId", String.valueOf(goodsId));
        params.put("userId", userId);
        params.put("num", String.valueOf(num));
        params.put("promotionId", String.valueOf(promotionId));
        params.put("shoppingType", String.valueOf(shoppingType));
        params.put("selectIds", selectIds);
        Encryption.encryptSingple(params);
        post(URLs.updateShoppingCartNum, params, mHandler);
    }

    /**
     * 查询购物车数据
     *
     * @param mHandler 句柄
     * @param userId   用户ID
     */
    public static void shoppingCatData(AsyncHttpResponseHandler mHandler, String userId) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        Encryption.encryptSingple(params);
        post(URLs.shoppingCatData, params, mHandler);
    }

    /**
     * 修改购物车商品促销方式
     *
     * @param mHandler      句柄
     * @param goodsNo       商品规格(只有当满赠促销的时候才必传)非必传时传空
     * @param goodsId       商品ID(只有当满赠促销的时候才必传) 非必传小于0
     * @param promotionId   活动id
     * @param promotionType 类型  0、普通商品 1、单品 2、套餐 3、赠品 4、满减 5、满赠  6、秒杀 7、礼品兑换 8、团购
     *                      9、天天低价 10、分销 11、闪购 12、推荐配件
     * @param userId        用户ID
     * @param ids           操作项ShoppingID promotionType为满赠时不用传
     * @param selectIds     已经选择的ShoppingID
     */
    public static void updatePromotionType(AsyncHttpResponseHandler mHandler, String goodsNo,
                                           int goodsId, long promotionId, int promotionType,
                                           String userId,int ids,String selectIds) {
        RequestParams params = new RequestParams();
        params.put("promotionId", String.valueOf(promotionId));
        params.put("userId", userId);
        params.put("promotionType", String.valueOf(promotionType));
        params.put("selectIds", selectIds);
        if (!TextUtils.isEmpty(goodsNo)) {
            params.put("goodsNo", goodsNo);
        } else {
            params.put("ids", String.valueOf(ids));
        }
        if (goodsId >= 0)
            params.put("goodsId", String.valueOf(goodsId));
        Encryption.encryptSingple(params);
        post(URLs.updatePromotionType, params, mHandler);
    }

    /**
     * 修改购物车商品满赠活动的商品
     *
     * @param mHandler      句柄
     * @param goods_no       商品规格(只有当满赠促销的时候才必传)非必传时传空
     * @param goods_id       商品ID(只有当满赠促销的时候才必传) 非必传小于0
     * @param promotion_id   活动id
     * @param promotion_type 类型  0、普通商品 1、单品 2、套餐 3、赠品 4、满减 5、满赠  6、秒杀 7、礼品兑换 8、团购
     *                      9、天天低价 10、分销 11、闪购 12、推荐配件
     * @param userId        用户ID
     * @param ids           操作项ShoppingID promotionType为满赠时不用传
     * @param selectIds     已经选择的ShoppingID
     */
    public static void updateShopppingCartPromotionGift(AsyncHttpResponseHandler mHandler, String goods_no,
                                           int goods_id, long promotion_id, int promotion_type,
                                           String userId,int ids,String selectIds) {
        RequestParams params = new RequestParams();
        params.put("promotion_id", String.valueOf(promotion_id));
        params.put("userId", userId);
        params.put("promotion_type", String.valueOf(promotion_type));
        params.put("selectIds", selectIds);
        if (!TextUtils.isEmpty(goods_no)) {
            params.put("goods_no", goods_no);
        } else {
            params.put("ids", String.valueOf(ids));
        }
        if (goods_id >= 0)
            params.put("goods_id", String.valueOf(goods_id));
        Encryption.encryptSingple(params);
        post(URLs.updateShopppingCartPromotionGift, params, mHandler);
    }

    /**
     * 删除赠品
     *
     * @param promotionId   活动id
     * @param userId        用户ID
     * @param selectIds     已经选择的ShoppingID
     */
    public static void deleteCartPromotionGift(AsyncHttpResponseHandler mHandler, long promotionId,
                                               String userId, String selectIds) {
        RequestParams params = new RequestParams();
        params.put("promotionId", String.valueOf(promotionId));
        params.put("userId", userId);
        params.put("selectIds", selectIds);
        Encryption.encryptSingple(params);
        post(URLs.deleteCartPromotionGift, params, mHandler);
    }

    /**
     * 查询满赠的赠品
     *
     * @param mHandler      句柄
     * @param promotionId   活动ID
     * @param price         当前选中的同店铺的商品总额
     */
    public static void donateData(AsyncHttpResponseHandler mHandler, int promotionId,double price) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("promotionId", String.valueOf(promotionId));		//
            map.put("price", Des3.encode(String.valueOf(price)));			//
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("promotionId", String.valueOf(promotionId));		//
            params.put("price", Des3.encode(String.valueOf(price)));			//
            params.put("mKey", paramStr);
            post(URLs.donateData, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 购物车提交（进入填写订单界面）
     *
     * @param mHandler 句柄
     * @param shoppingIds  购物车ids （多个以逗号隔开）
     * @param userId   用户id
     */
    public static void submintCart(AsyncHttpResponseHandler mHandler, String shoppingIds,String userId) {
        RequestParams params = new RequestParams();
        params.put("shoppingIds", shoppingIds);
        params.put("userId", userId);
        Encryption.encryptSingple(params);
        post(URLs.submintCart, params, mHandler);
    }

    /**
     * 预约信息
     *
     * @param mHandler      句柄
     * @param orderInfo     订单id/编号
     * @param flag       	1订单id，2订单编号
     * @param userId        用户ID
     */
    public static void orderBespeakInfo(AsyncHttpResponseHandler mHandler, String orderInfo, int flag, String userId) {
        
    	RequestParams params = new RequestParams();
       
        params.put("orderInfo", orderInfo);
        params.put("userId", userId);
        params.put("flag", String.valueOf(flag));
        Encryption.encryptSingple(params);
        post(URLs.ORDER_BESPEAK_INFO, params, mHandler);
    }
    
  
    /**
     * 预约新增
     * 
     * @param mHandler		句柄
     * @param orderId		订单ID
     * @param typeId		申请类型		
     * @param userId		用户ID
     * @param phone			手机号码
     * @param name			姓名
     * @param address		地址
     * @param remarks		备注
     * @param goods			商品
     * @param origin		用户来源 1: web 2: pc 3:android, 4:ios 5:wp
     */
    public static void addBespeak(AsyncHttpResponseHandler mHandler, String orderId, String typeId, String userId, 
    		String phone, String name, String address, String remarks, List<GoodBean> goods, String origin) {
        RequestParams params = new RequestParams();
        
        //将商品集合转化成json字符串
        String goodsJson = JSONParseUtils.get_addBespeak_goods_JSON(goods);
        
        params.put("orderId", orderId);	// 订单id
        params.put("typeId", typeId);	// 申请类型
        params.put("userId", userId);	// 申请用户id
        params.put("phone", phone);		// 手机号码
        params.put("name", name);		// 姓名
        params.put("address", address);	// 地址
        params.put("remarks", remarks);	// 备注
        params.put("goodsJson", goodsJson);	// 商品
        params.put("origin", origin);	// 用户来源1: web 2: pc 3:android, 4:ios 5:wp
        Encryption.encryptSingple(params);
        post(URLs.ORDER_ADD_BESPEAK, params, mHandler);
    }
    
    
    /**
     * 换货详情
     * 
     * @param mHandler		句柄
     * @param replaceId		换货ID
     */
    public static void replaceDetail(AsyncHttpResponseHandler mHandler, String replaceId) {
        RequestParams params = new RequestParams();
        params.put("replaceId", replaceId);	// 换货ID
        Encryption.encryptSingple(params);
        post(URLs.ORDER_REPLACEDETAIL, params, mHandler);
    }
    
    /**
     * 退货详情
     * 
     * @param mHandler		句柄
     * @param returnId		退货ID
     */
    public static void returnDetail(AsyncHttpResponseHandler mHandler, String returnId) {
        RequestParams params = new RequestParams();
        params.put("returnId", returnId );	//退货ID
        Encryption.encryptSingple(params);
        post(URLs.ORDER_RETURNDETAIL, params, mHandler);
    }
    
    
    /**
     * 修改换货
     * 
     * @param mHandler		句柄
     * @param replaceId		换货ID
     * @param userId		用户ID
     * @param reasonId		理由ID
     * @param goodsJson		商品
     * @param describe		描述
     * @param img			图片-格式("img1,img2,img3")
     * @param origin		来源(1: web 2: pc 3:android, 4:ios 5:wp)
     */
    public static void amendReplace(AsyncHttpResponseHandler mHandler, String replaceId, String userId, String reasonId, String reasonName, List<GoodsListEntity> goodsList, String describe, String img, String origin) {
       
    	//将商品集合转化成json字符串
        String goodsJson = JSONParseUtils.get_amendReplace_goods_JSON(goodsList);	
    	
    	RequestParams params = new RequestParams();
        params.put("replaceId", replaceId);		// 换货ID
        params.put("userId", userId);			// 用户ID
        params.put("reasonId", reasonId);		// 理由ID
        params.put("reasonName", reasonName);	// 理由
        params.put("goodsJson", goodsJson);		// 商品
        params.put("describe", describe);		// 描述
        params.put("img", img);					// 图片-格式("img1,img2,img3")
        params.put("origin", origin);			// 来源(1: web 2: pc 3:android, 4:ios 5:wp)
        Encryption.encryptSingple(params);
        post(URLs.ORDER_AMENDREPLACE, params, mHandler);
    }
    
    
    /**
     * 修改退货
     * 
     * @param mHandler		句柄
     * @param orderId		订单ID
     * @param returnId		退货ID
     * @param userId		用户ID
     * @param reasonId		理由ID
     * @param goodsJson		商品
     * @param describe		描述
     * @param img			图片-格式("img1,img2,img3")
     * @param origin		来源(1: web 2: pc 3:android, 4:ios 5:wp)
     */
    public static void amendReturn(AsyncHttpResponseHandler mHandler, String orderId, String returnId, String userId, String reasonId, String reasonName, List<GoodsListEntity> goodsList, String describe, String img, String origin) {
       
    	//将商品集合转化成json字符串
        String goodsJson = JSONParseUtils.get_amendReturn_goods_JSON(goodsList);	
        
    	RequestParams params = new RequestParams();
    	params.put("orderId", orderId);			// 订单ID
    	params.put("returnId", returnId);		// 换货ID
        params.put("userId", userId);			// 用户ID
        params.put("reasonId", reasonId);		// 理由ID
        params.put("reasonName", reasonName);	// 理由
        params.put("goodsJson", goodsJson);		// 商品
        params.put("describe", describe);		// 描述
        params.put("img", img);					// 图片-格式("img1,img2,img3")
        params.put("origin", origin);			// 来源(1: web 2: pc 3:android, 4:ios 5:wp)
        Encryption.encryptSingple(params);
        post(URLs.ORDER_AMENDRETURN, params, mHandler);
    }
    
    
    /**
     * 换货新增
     * 
     * @param mHandler		句柄
     * @param orderId 		订单ID
     * @param userId		用户ID
     * @param store_id		店铺ID
     * @param reasonId		理由ID
     * @param reasonName	理由
     * @param goodsJson		商品
     * @param describe		描述
     * @param img			图片-格式("img1,img2,img3")
     * @param origin		来源(1: web 2: pc 3:android, 4:ios 5:wp)
     */
    public static void applyReplace(AsyncHttpResponseHandler mHandler, String orderId, String userId, String reasonId, String reasonName, List<GoodsListEntity> goodsList, String describe, String img, String origin) {


    	//将商品集合转化成json字符串
        String goodsJson = JSONParseUtils.get_applyReplace_goods_JSON(goodsList);	
    	
    	RequestParams params = new RequestParams();
        params.put("orderId", orderId);			// 订单ID
        params.put("userId", userId);			// 用户ID
        params.put("reasonId", reasonId);		// 理由ID
        params.put("reasonName", reasonName);	// 理由名称
        params.put("goodsJson", goodsJson);		// 商品
        params.put("describe", describe);		// 描述
        params.put("img", img);					// 图片-格式("img1,img2,img3")
        params.put("origin", origin);			// 来源(1: web 2: pc 3:android, 4:ios 5:wp)
        Encryption.encryptSingple(params);
        post(URLs.ORDER_APPLYREPLACE, params, mHandler);
    }
    
  
    /**
     * 申请退货
     * @param mHandler		句柄			
     * @param userId		用户ID
     * @param orderId		订单ID
     * @param goodsList		商品
     * @param reasonId		退货理由ID
     * @param reasonName	退货理由名称
     * @param describe		描述
     * @param origin		来源(1: web 2: pc 3:android, 4:ios 5:wp)
     * @param img			图片-格式("img1,img2,img3")
     */
    public static void addReturn(AsyncHttpResponseHandler mHandler, String userId, String orderId, List<GoodsListEntity> goodsList, String reasonId, String reasonName,  String describe, String origin, String img) {
    	
    	//将商品集合转化成json字符串
        String goodsJson = JSONParseUtils.get_applyReturn_goods_JSON(goodsList);	
    	
    	RequestParams params = new RequestParams();
    	params.put("userId", userId);			// 用户ID
        params.put("orderId", orderId);			// 订单ID
        params.put("goodsJson", goodsJson);		// 商品
        params.put("reasonId", reasonId);		// 理由ID
        params.put("reasonName", reasonName);	// 理由名称
        params.put("describe", describe);		// 描述
        params.put("origin", origin);			// 来源
        params.put("img", img);					// 图片-格式("img1,img2,img3")
        Encryption.encryptSingple(params);
        post(URLs.ORDER_ADDRETURN, params, mHandler);
    }
    
    
    /**
     * 修改换货初始化
     * 
     * @param mHandler		句柄
     * @param replaceId 	换货ID
     */
    public static void amendReplaceInit(AsyncHttpResponseHandler mHandler, String replaceId) {


    	RequestParams params = new RequestParams();
        params.put("replaceId", replaceId);			// 换货ID
        Encryption.encryptSingple(params);
        post(URLs.ORDER_AMENDREPLACEINIT, params, mHandler);
    }
    
    /**
     * 修改退货初始化
     * 
     * @param mHandler		句柄
     * @param returnId 		退货ID
     */
    public static void amendReturnInit(AsyncHttpResponseHandler mHandler, String returnId) {


    	RequestParams params = new RequestParams();
        params.put("returnId", returnId);			// 退货ID
        Encryption.encryptSingple(params);
        post(URLs.ORDER_AMENDRETURNINIT, params, mHandler);
    }
    
    
    /**
     * 修改换货初始化
     * 
     * @param mHandler		句柄
     * @param orderId 		订单ID
     */
    public static void commentsView(AsyncHttpResponseHandler mHandler, String orderId) {

    	RequestParams params = new RequestParams();
        params.put("orderId", orderId);	// 订单ID
        Encryption.encryptSingple(params);
        post(URLs.ORDER_COMMENTSVIEW, params, mHandler);
    }
    
    /**
     * 追加评价
     * 
     * @param mHandler		句柄
     * @param orderId 		订单ID
     */
    public static void goodsCommentsAdd(AsyncHttpResponseHandler mHandler, String evaGoodsId, String content, String img) {

    	RequestParams params = new RequestParams();
        params.put("evaGoodsId", evaGoodsId);	// 用户id
        params.put("content", content);			// 内容
        params.put("img", img);					// 图片
        Encryption.encryptSingple(params);
        post(URLs.ORDER_GOODSCOMMENTSADD, params, mHandler);
    }
    

    /**
     * 激活虎券
     * @param userid
     * @param ticketCode
     * @param mHandler
     */
    public static void activateCoupon(String userid,String ticketCode,String total,AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("userId", userid);
            map.put("ticketCode", ticketCode);
            map.put("total",  Des3.encode(total));
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userid);		//
            params.put("ticketCode", ticketCode);		//
            params.put("total", Des3.encode(total));			//
            params.put("mKey", paramStr);
            post(URLs.activateCoupon, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询当前购物车的可用虎券

     */
    public static void couponData(String userId,String total,String thirdIds,String brandIds,String goodIds,String storeIds,String pageIndex,AsyncHttpResponseHandler mHandler) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("userId", userId);		//
            map.put("total", Des3.encode(total));			//
            map.put("brandIds", brandIds);			//
            map.put("goodIds", goodIds);			//
            map.put("storeIds", storeIds);			//
            map.put("thirdIds", thirdIds);			//
            map.put("pageIndex", pageIndex);			//
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);		//
            params.put("total", Des3.encode(total));			//
            params.put("brandIds", brandIds);			//
            params.put("goodIds", goodIds);			//
            params.put("storeIds", storeIds);			//
            params.put("thirdIds", thirdIds);			//
            params.put("pageIndex", pageIndex);			//
            params.put("mKey", paramStr);
            post(URLs.couponData, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // modify by zhyao @2015/5/8  添加红包 折扣参数
    /**
     * 提交订单
     */
    public static void sumbitOrder(String userId,String shopIds,String addressId,String invoiceId,String points_deduction,String bonus_deduction,String remarksJson,String lh_id,String origin,String shipping_method,String deliveryTime,String nearbyId,String staffId,int type,AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);		//
        params.put("shopIds", shopIds);			//
        params.put("addressId", addressId);			//
        params.put("invoiceId", invoiceId);			//
        params.put("points_deduction", points_deduction);			//
        params.put("bonus_deduction", bonus_deduction);			// 红包抵扣金额
        params.put("remarksJson", remarksJson);			//
        params.put("lh_id", lh_id);			//
        params.put("origin", origin);			//
        params.put("shipping_method", shipping_method);			//
        params.put("deliveryTime", deliveryTime);			//
        params.put("nearbyId", nearbyId);			//
        params.put("staffId", staffId);			//
        params.put("type", type+"");			//
        Encryption.encryptSingple(params);
        post(URLs.sumbitOrder, params, mHandler);
    }
    
    // add by zhyao @2015/6/24 添加是不是全部消费卷参数isAllConsumption
    /**
     * 切换收货地址返回相应运费和总价
     */
    public static void freightByAddres(String userId,String province,String city,String area,String shopIds,String orderPrice,String freight, boolean isAllConsumption, AsyncHttpResponseHandler mHandler) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);		//
            map.put("province", province);			//
            map.put("city", city);			//
            map.put("area", area);			//
            map.put("shopIds", shopIds);			//
            map.put("orderPrice", Des3.encode(orderPrice));			//
            map.put("freight", freight);
            // add by zhyao @2015/6/24 添加是不是全部消费卷参数allConsumption
            if(isAllConsumption) {
            	map.put("allConsumption", "1");
            }
            else {
            	map.put("allConsumption", "0");
            }
            String paramStr = CryptionUtil.getSignData(map);

            RequestParams params = new RequestParams();
            params.put("userId", userId);		//
            params.put("province", province);			//
            params.put("city", city);			//
            params.put("area", area);			//
            params.put("shopIds", shopIds);			//
            params.put("orderPrice", Des3.encode(orderPrice));			//
            params.put("freight", freight);
            // add by zhyao @2015/6/24 添加是不是全部消费卷参数allConsumption
            if(isAllConsumption) {
            	params.put("allConsumption", "1");
            }
            else {
            	params.put("allConsumption", "0");
            }
            params.put("mKey", paramStr);
            post(URLs.freightByAddres, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询已有的增值税发票
     */
    public static void vat(String userId,AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);		//
        Encryption.encryptSingple(params);
        post(URLs.vat, params, mHandler);
    }
    
    /**
     * 提交举报
     * @param goodsId
     * @param userId
     * @param reportType
     * @param reportTheme
     * @param picUrls
     * @param platform
     * @param content
     * @param mHandler
     */
    public static void submitReport(String goodsId,String userId,String reportType,String reportTheme,String picUrls,String platform,String content, String reportTypeName, String reportTitleName, AsyncHttpResponseHandler mHandler){
    	RequestParams params = new RequestParams();
    	params.put("goodsId", goodsId);
    	params.put("userId", userId);
    	params.put("reportType", reportType);
    	params.put("reportTitle", reportTheme);
    	params.put("img", picUrls);
    	params.put("origin", platform);
    	params.put("content", content);
    	params.put("reportTypeName", reportTypeName);
    	params.put("reportTitleName", reportTitleName);	
        Encryption.encryptSingple(params);
    	post(URLs.submitReport, params, mHandler);
    }
    
    /**
     * 获取举报基本信息，如举报类型和举报主题
     * @param mHandler
     */
    public static void loadReportInfo(AsyncHttpResponseHandler mHandler){
    	post(URLs.loadReportInfo,null, mHandler);
    }
    
    /**
     * 获取举报列表信息
     * @param mHandler
     */
    public static void loadReportList(String userId,String pageIndex,AsyncHttpResponseHandler mHandler){
    	RequestParams params = new RequestParams();
    	params.put("userId", userId);
    	params.put("pageIndex", pageIndex);
        Encryption.encryptSingple(params);
    	post(URLs.loadReportList,params, mHandler);
    }
    
    /**
     * 获取举报详情
     * @param mHandler
     */
    public static void loadReportDetails(String userId,String reportId,AsyncHttpResponseHandler mHandler){
    	RequestParams params = new RequestParams();
    	params.put("userId", userId);
    	params.put("reportId", reportId);
        Encryption.encryptSingple(params);
    	post(URLs.loadReportDetails,params, mHandler);
    }

    /**
     * 投诉类型
     */
    public static void complainType(AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        Encryption.encryptSingple(params);
        post(URLs.complainType,params, mHandler);
    }

    /**
     * 发起投诉
         userId    用户id
         orderCode 订单编号
         typeId    投诉类型
         typeName：投诉类型名称
         img       图片，不可以超过五张
         title     举报标题
         content   举报内容
         origin    源（0 PC端 1android端，2点单端, 3.IOS 4  Wap端 5 微信端）
     */
    public static void addComplain(String userId,String orderCode ,String typeId ,String typeName,String img ,String title ,String content ,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("orderCode", orderCode);
        params.put("typeId", typeId);
        params.put("typeName", typeName);
        params.put("img", img);
        params.put("title", title);
        params.put("content", content);
        params.put("origin", "1");
        Encryption.encryptSingple(params);
        post(URLs.addComplain,params, mHandler);
    }
    /**
     * 继续留言
         userId  订单编号
         message 留言类型
         img     图片，不可以超过五张
         complaintId 留言id
     */
    public static void complainContinue(String userId,String message ,String img ,String complaintId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("message", message);
        params.put("img", img);
        params.put("complaintId", complaintId);
        Encryption.encryptSingple(params);
        post(URLs.complainContinue,params, mHandler);
    }

    /**
     * 获取投诉详情
     *  complaintId 投诉id
     */
    public static void complainDetail(String complaintId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("complaintId", complaintId);
        Encryption.encryptSingple(params);
        post(URLs.complainDetail,params, mHandler);
    }

    /**
     * 投诉列表
     *  userId 用户id
        pageIndex当前页码
     */
    public static void complainData(String userId,int pageIndex,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pageIndex", pageIndex+"");
        Encryption.encryptSingple(params);
        post(URLs.complainData,params, mHandler);
    }

    /**
     * 投诉解决
     *  complaintId  投诉id
     */
    public static void solveComplain(String complaintId, String orderCode, AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("complaintId", complaintId);
        params.put("orderCode", orderCode);
//        params.put("mKey", CryptionUtil.getSignData(params));
        Encryption.encryptSingple(params);
        post(URLs.solveComplain,params, mHandler);
    }

    /**
     * 投诉取消
     * complaintId  投诉id
     * orderNumber 	订单编号
     */
    public static void cancelComplain(String complaintId , String orderCode, AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("complaintId", complaintId);
        params.put("orderCode", orderCode);
//        params.put("mKey", CryptionUtil.getSignData(params));
        Encryption.encryptSingple(params);
        post(URLs.cancelComplain,params, mHandler);
    }

    /**
     * 查询所有浏览记录
     * @param userId
     * @param pageIndex
     * @param mHandler
     */
    public static void historyData(String userId,String pageIndex ,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pageIndex", pageIndex);
        Encryption.encryptSingple(params);
        post(URLs.historyData,params, mHandler);
    }

    /**
     * 投诉预约
     * recordId 新品预约记录id
     */
    public static void cancelNewGoods(String recordId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("recordId", recordId);
        Encryption.encryptSingple(params);
        post(URLs.cancelNewGoods,params, mHandler);
    }

    /**
     * 删除浏览记录
     * @param userId 用户id
     * @param flag 1全部，2具体某个商品
     * @param goodsId 商品id（如果flag=2是必传）
     */
    public static void cancelHistory(String userId,String flag,String goodsId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("flag", flag);
        params.put("goodsId", goodsId);
        Encryption.encryptSingple(params);
        post(URLs.cancelHistory,params, mHandler);
    }
    /**
     * 查询会员信息
     * @param userId 用户id
     */
    public static void NClub(String userId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        Encryption.encryptSingple(params);
        post(URLs.NClub,params, mHandler);
    }
    /**
     * 查询会员商品信息
     * @param userId 用户id
     */
    public static void clubGoods(String userId,String categoryId,String pageIndex,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("categoryId", categoryId);
        params.put("pageIndex", pageIndex);
        Encryption.encryptSingple(params);
        post(URLs.clubGoods,params, mHandler);
    }
    
    /**
     * 发送邮件到绑定邮箱</br>
     * 绑定邮箱 flag 0, mark 1</br>
     * 发送验证码 flag 1, mark 0</br>
     * @param userId
     * @param email		邮箱
     * @param userName 	用户名
     * @param flag 		0邮箱不能已经在后台数据库中存在过，1必须存在，2可以存在，可以不存在
     * @param mark 		0发送验证码，1发送完整路径
     * @param mHandler
     */
    public static void sendCheckEmail(String userId, String userName, String email, String flag, String mark, AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("email", email);
        params.put("flag", flag);
        params.put("mark", mark);
        params.put("mKey", CryptionUtil.getSignData(params));
        post(URLs.sendCheckEmail,params, mHandler);
    }
    
    public static void verifyPassword(String userId,String password,AsyncHttpResponseHandler mHandler){
       
    	String mPassword = CryptionUtil.md5(password.trim());
    	
    	RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("password", mPassword);
        Encryption.encryptSingple(params);
        post(URLs.checkPasswork,params, mHandler);
    }


    /**
     * 百度云推送绑定账户
     *
     * @param mHandler    句柄
     * @param channelId   推送频道ID
     * @param bdUserId    百度用户ID
     * @param userId      用户ID
     */
    public static void baiduPushInfo(AsyncHttpResponseHandler mHandler, String channelId, String bdUserId, String userId) {
        //origin 用户来源1: web 2: pc 3:android, 4:ios 5:wp
        String origin = "3";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelId", channelId);
        map.put("phoneUserId", bdUserId);
        map.put("userId", userId);
        map.put("origin", origin);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("channelId", channelId);
        params.put("phoneUserId", bdUserId);
        params.put("userId", userId);
        params.put("origin", origin);
//            params.put("mKey", paramStr);
        Encryption.encryptSingple(params);
        post(URLs.baiduPushInfo, params, mHandler);
    }


    /**
     * 登录日志
     *  userId 用户id
        pageIndex当前页码
     */
    public static void loginLog(String userId,int pageIndex,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pageIndex", pageIndex+"");
        Encryption.encryptSingple(params);
        post(URLs.loginLog,params, mHandler);
    }

    
    /**
     * 会员等级
     * @param userId
     * @param mHandler
     */
    public static void memberRankInfo(String userId,AsyncHttpResponseHandler mHandler) {
    	RequestParams params = new RequestParams();
    	params.put("userId", userId);
    	Encryption.encryptSingple(params);
        post(URLs.memberRank, params, mHandler);
    }
    
    /**
     * 修改性别
     * @param userId
     * @param sex
     * @param mHandler
     */
    public static void changeSex(String userId,String sex,AsyncHttpResponseHandler mHandler) {
    	RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("sex", sex);
        Encryption.encryptSingple(params);
        post(URLs.changeSex, params, mHandler);
    }

    /**
     * 修改生日
     * @param userId
     * @param birthday
     * @param mHandler
     */
    public static void changeBirthday(String userId,String birthday, AsyncHttpResponseHandler mHandler) {
    	RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("birthday", birthday);
        Encryption.encryptSingple(params);
        post(URLs.changeBirthday, params, mHandler);
    }

    /**
     * 修改密码
     * @param phone
     * @param password
     * @param pswSfae
     * @param mHandler
     */
    public static void postNewPsw(String phone, String password,String pswSfae, AsyncHttpResponseHandler mHandler) {
        try {
            String mPassword = CryptionUtil.md5(password.trim());
            String pwd3des;
            pwd3des = Des3.encode(password.trim());
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());

            RequestParams params = new RequestParams();
            params.put("phone", phone);
            params.put("password", mPassword);
            params.put("pswSfae", pswSfae);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("mKey", CryptionUtil.getSignData(params));
            post(URLs.FIND_PSW, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //验证完手机或者邮箱后修改密码
    public static void changePwd(String userId, String password,String pswSfae, AsyncHttpResponseHandler mHandler) {
        try {
            String mPassword = CryptionUtil.md5(password.trim());
            String pwd3des;
            pwd3des = Des3.encode(password.trim());
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str_time = sf.format(new Date());

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("password", mPassword);
            params.put("pswSfae", pswSfae);
            params.put("password3des", pwd3des);
            params.put("time", str_time);
            params.put("mKey", CryptionUtil.getSignData(params));
            post(URLs.changePwd, params, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询会员商品详情
     * @param giftId 商品id
     * @param mHandler 回调
     */
    public static void giftDetail(String userId,String giftId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("giftId", giftId);
        Encryption.encryptSingple(params);
        post(URLs.giftDetail, params, mHandler);
    }

    /**
     * 提交兑换的商品
     * @param userId 用户id
     * @param giftId 商品id
     * @param num 数量
     * @param mHandler 回调
     */
    public static void convertGift(String userId,String giftId,String num,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("giftId", giftId);
        params.put("num", num);
        Encryption.encryptSingple(params);
        post(URLs.convertGift, params, mHandler);
    }

    /**
     * 查询水煤电缴费地区
     * @param mHandler 回调
     */
    public static void cityList(AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        post(URLs.cityList, params, mHandler);
    }
    
    /**
     * 查询水电煤欠费，话费情况
     * @param userId 用户ID
     * @param payItem 0电费，1水费,2煤气费，3话费
     * @param areaCode 地区码（如果是话费，30000000）
     * @param areaName 地区名称
     * @param terminalNo 商户号
     * @param merchantNo 终端号
     * @param userNo 缴费用户号码
     * @param year 年份（如果是话费，传当前年份）
     * @param month （如果是话费，传当前月份）
     * @param phone 缴费用户手机号
     * @param mHandler
     */
    public static void shareBill(String userId,String payItem,String areaCode,String areaName,String terminalNo,String merchantNo,
                                 String userNo,String year,String month,String phone, AsyncHttpResponseHandler mHandler){
    	shareBill(userId, payItem, areaCode, areaName, terminalNo, merchantNo, userNo, year, month, phone, "0.0", mHandler);
    }
    
    /**
     * 查询水电煤欠费，话费情况
     * @param userId 用户ID
     * @param payItem 0电费，1水费,2煤气费，3话费
     * @param areaCode 地区码（如果是话费，30000000）
     * @param areaName 地区名称
     * @param terminalNo 商户号
     * @param merchantNo 终端号
     * @param userNo 缴费用户号码
     * @param year 年份（如果是话费，传当前年份）
     * @param month （如果是话费，传当前月份）
     * @param phone 缴费用户手机号
     * @param amount 缴费金额
     * @param mHandler
     */
    public static void shareBill(String userId,String payItem,String areaCode,String areaName,String terminalNo,String merchantNo,
                                 String userNo,String year,String month,String phone, String amount, AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("payItem", payItem);
        params.put("areaCode", areaCode);
        params.put("areaName", areaName);
        params.put("terminalNo", terminalNo);
        params.put("merchantNo", merchantNo);
        params.put("userNo", userNo);
        params.put("year", year);
        params.put("month", month);
        params.put("phone", phone);
        params.put("amount", amount);

        post(URLs.shareBill, params, mHandler);
    }
    
    
    /**
     * 查询所有水煤电缴费记录
     * @param userId
     * @param pageIndex 页码索引
     * @param payItem 缴费项目：0水电煤缴费记录,3话费缴费记录
     * @param mHandler
     */
    public static void shareBillData(String userId, int pageIndex, String payItem, AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("payItem", payItem);

        post(URLs.shareBillData, params, mHandler);
    }

    /**
     * 提交兑换的商品
       origin  用户来源1: web 2: pc 3:android, 4:ios 5:wp
     */
    public static void about(AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("origin", "3");
        post(URLs.about, params, mHandler);
    }
    
    
    /**
     * 账户安全数据
     * @param userId
     * @param mHandler
     */
    public static void accountSecurity(String userId, AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("userId", userId);
//        params.put("mKey", CryptionUtil.getSignData(params));
        Encryption.encryptSingple(params);
        post(URLs.accountSecurity, params, mHandler);
    }

    // add by zhyao @2015/6/13 新增查询消费卷
    /**
     *  查询订单消费卷
     *  orderCode  订单号
     */
    public static void queryCoupons(String orderId ,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        post(URLs.queryCoupons, params, mHandler);
    }
    
    // add by zhyao @2015/6/18 新增 转赠红包
    /**
     *   转赠红包
     *   @param userId      用户ID
	 *   @param userPhone   用户手机
	 *   @param giveId      转赠对方ID
	 *   @param givePhone   转赠手机
     *   @param bonus       转赠红包金额
     *   @param activity_type 活动类型：1.秀场
     *   @param activity_id   活动人的id
     */
    public static void userGiveBonus(String userId, String userPhone, String giveId, String givePhone, String bonus, String activity_type, String activity_id, AsyncHttpResponseHandler mHandler){
    	
    	RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("userPhone", userPhone);
        params.put("giveId", giveId);
        params.put("givePhone", givePhone);
        params.put("bonus", bonus);
        params.put("activity_type", activity_type);
        params.put("activity_id", activity_id);
        params.put("mKey", CryptionUtil.getSignData(params));
    	
    	post(URLs.userGiveBonus, params, mHandler);
    }
    
    // 秀场查询红包
    public static void appGiveBonus(MyCustomResponseHandler mHandler, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        map.put("userId", AppContext.userId);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("time", str_time);
        params.put("userId", AppContext.userId);
        params.put("mKey", paramStr);
        post(URLs.appGiveBonus, params, mHandler);
    }
    
    // add by zhyao @2015/6/18 新增 查询七牛云服务uptoken
    /**
     *  查询七牛云服务uptoken
     */
    public static void queryQiNiuUptoken(AsyncHttpResponseHandler mHandler){
    	post(URLs.queryQiNiuUptoken, null, mHandler);
    }

    // add by zhyao @2015/6/29 添加isMine参数：1-自己的，0-全部
    public static void delAppShow(MyCustomResponseHandler mHandler, String spotlightId) {
        RequestParams params = new RequestParams();
        params.put("spotlightId", spotlightId);
        post(URLs.delAppShow, params, mHandler);
    }
    
    // 查询人气组合
    public static void popularGroup( String goodsId,MyCustomResponseHandler mHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsId", goodsId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("goodsId", goodsId);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.popularGroup, params, mHandler);
    }

    /**
     *  查询推荐搭配
     * @param goodsNo 订单编号
     * @param storeId 店铺id
     * @param mHandler 回调
     */
    public static void goodsRecommend( String goodsNo,String storeId,MyCustomResponseHandler mHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsNo", goodsNo);
        map.put("storeId", storeId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("goodsNo", goodsNo);
        params.put("storeId", storeId);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.goodsRecommend, params, mHandler);
    }

    /**
     * 查询物流
     * @param orderId 订单id
     * @param handler 回调
     */
    public static void orderLogistics(String orderId,AsyncHttpResponseHandler handler){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignData(map);

        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        params.put("time", str_time);
        params.put("mKey", paramStr);
        post(URLs.orderLogistics, params, handler);
    }

    /**
     * 加入多个商品到购物车
     * @param userId 用户id
     * @param json
     * @param mHandler
     */
    public static void addCarts(String userId,String shoppingIds, String json,AsyncHttpResponseHandler mHandler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("shoppingIds", shoppingIds);
        params.put("json", json);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        params.put("time", str_time);
//        params.put("promotionId", String.valueOf(promotionId));
        Encryption.encryptSingple(params);
        post(URLs.addCarts, params, mHandler);
    }

    /**
     * 加载商品详情的猜你喜欢数据
     * @param goodsId 商品id
     * @param thirdCategoryId 商品三级分类id
     * @param mHandler 回调
     */
    public static void mayBeLike(String goodsId,String thirdCategoryId,AsyncHttpResponseHandler mHandler){
        RequestParams params = new RequestParams();
        params.put("goodsId", goodsId);
        params.put("thirdCategoryId", thirdCategoryId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        params.put("time", str_time);
        Encryption.encryptSingple(params);
        post(URLs.mayBeLike, params, mHandler);
    }
}
