package com.huiyin.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.huiyin.constants.Config;
import com.huiyin.http.RequestParams;

/**
 * 自定义加解密算法
 * 
 * @author
 */
public class CryptionUtil {
	/**
	 * MD5加密密码
	 * 
	 * @param pwd
	 * @return
	 */
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	 /**
     * 获得签名
     * @param params
     * @return
     */
    public static String getSignData(RequestParams params){
    	Map<String, Object> map = new HashMap<String, Object>();
    	for(String key : params.strParams.keySet()){
    		map.put(key, params.strParams.get(key));
    	}
    	return getSignData(map);
    }

	/** 
	* 参数排序签名 
	* 
	* @param params 
	* @return 
	* @throws Exception 
	*/ 
	public static String getSignData(Map<String, Object> params){ 
		
		StringBuffer content = new StringBuffer(); 
		// 按照key做排序 
		try{
			List<String> keys = new ArrayList<String>(params.keySet()); 
			Collections.sort(keys); 

			for (int i = 0; i < keys.size(); i++) { 
				
				String key = (String) keys.get(i); 
				System.out.println(keys); 
				if(params.get(key)!=null)
				{
					String valueStr = (String) params.get(key);
					Log.i("FD","vlaue++++++++++++"+valueStr); 
					String valueStr1=Des3.encode(valueStr); 
					Log.i("DF","未加密vlaue++++++++++++"+valueStr); 
					Log.i("DF","已加密vlaue++++++++++++"+valueStr1); 
					// 具体逻辑修改下面的路径进行拼接 
					content.append(valueStr1); 
				}
		
			} 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return md5(content.toString()+Config.KEY); 
	
	
	}

	/**
	* 参数排序签名
	*
	* @param params Map<String, String>
	* @return
	* @throws Exception
	*/
	public static String getSignDataString(Map<String, String> params){

		StringBuffer content = new StringBuffer();
		// 按照key做排序
		try{
			List<String> keys = new ArrayList<String>(params.keySet());
			Collections.sort(keys);

			for (int i = 0; i < keys.size(); i++) {

				String key = (String) keys.get(i);
				System.out.println(keys);
				if(params.get(key)!=null)
				{
					String valueStr = (String) params.get(key);
					Log.i("FD","vlaue++++++++++++"+valueStr);
					String valueStr1=Des3.encode(valueStr);
					Log.i("DF","未加密vlaue++++++++++++"+valueStr);
					Log.i("DF","已加密vlaue++++++++++++"+valueStr1);
					// 具体逻辑修改下面的路径进行拼接
					content.append(valueStr1);
				}

			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return md5(content.toString()+Config.KEY);


	}

	
	/**
	 * 获取密码强度  
	 * @param password
	 * @return 1弱  2中  3强
	 */
	public static String getPasswordSfae(String password){
		if(TextUtils.isEmpty(password)){
			return "1";
		}
		
		//数字，字母，特殊符号
		int hasNumber = 0;
		int hasZimu = 0;
		int hasOther = 0;
		
		//判断每一个字符
		char[] chars = password.toCharArray();
		for(char cha : chars){
			if(cha >= '0' && cha <= '9'){
				//0-9数字
				hasNumber = 1;
				continue;
			}
			
			//65-90, 97-122
			if((cha >= 'A' && cha <= 'Z') || (cha >= 'a' && cha <= 'z')){
				hasZimu = 1;
				continue;
			}else{
				
				//其他字符都算为特殊字符
				hasOther = 1;
			}
		}
		
		return String.valueOf(hasNumber + hasZimu + hasOther);
	}
}




