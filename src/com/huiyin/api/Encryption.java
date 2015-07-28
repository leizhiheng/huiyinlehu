package com.huiyin.api;

import com.huiyin.http.RequestParams;
import com.huiyin.utils.CryptionUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 加密类
 * 功能：对数据进行加密处理
 * Created by kuangyong on 2015/6/5.
 */
public class Encryption  {

    /**
     * 一般的加密
     * @param params
     */
    public static void encryptSingple(RequestParams params){
        Map<String ,String > paramsMap=params.strParams;
        Iterator it=paramsMap.keySet().iterator();
        Map<String ,String > map=new HashMap<String, String>();
        while (it.hasNext()){
            String key=(String)it.next();
            String value=paramsMap.get(key);
            map.put(key, value);
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str_time = sf.format(new Date());
        map.put("time", str_time);
        String paramStr = CryptionUtil.getSignDataString(map);
        params.put("time",str_time);
        params.put("mKey",paramStr);
    }
}
