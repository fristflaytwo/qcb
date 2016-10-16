package com.xionger.qcb.common.util.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 字符串操作类
 */
public class StringUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);
	
	

	public static List<String[]> splitToList(String orgStr, String splitStr1,
			String splitStr2) {
		List<String[]> list = new ArrayList<String[]>();

		String[] newStr = removeLastStr(orgStr, splitStr1).split(splitStr1);
		for (String tmpStr : newStr) {
			list.add(removeLastStr(tmpStr, splitStr2).split(splitStr2));
		}
		return list;
	}
	

	public static String[] splitToArray(String orgStr, String splitStr) {
		return removeLastStr(orgStr, splitStr).split(splitStr);
	}

	public static String removeLastStr(String orgStr, String lastStr) {
		if (orgStr.endsWith(lastStr)) {
			orgStr = orgStr.substring(0, orgStr.lastIndexOf(lastStr));
		}
		return orgStr;
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 替换字符串
	 * 
	 * @param str
	 * @param map
	 * @param tag
	 * @return
	 */
	public static String replaceString(String str,
			final Map<String, Object> map, final String tag) {
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();

			String replaceStr = tag + key + tag;
			if (str.contains(replaceStr)) {
				str = str.replaceAll(replaceStr, toString(value));
			}
		}
		return str;
	}

	/**
	 * 替换字符串
	 * 
	 * @param str
	 * @param map
	 * @return
	 */
	public static String replaceString(String str, final Map<String, Object> map) {
		return replaceString(str, map, "%");
	}
	
	public static String randString(){
		String str = "";
		String[] array = {"3","5","p","q","6","r","s","t","u","v","7","8","f","g","h","i","j","k","l","m","n",
				"o","w","x","y","z","A","B","C","D","E","F","b","c","d",
				"G","4","H","I","J","2","K","L","M","N","0","O","P","Q","R","S","T","1","U","9","a","e","V","W","X","Y","Z"};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			String tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		for(int i = 0; i < 6; i++){
			str += array[i];
		}
		
		return str;
	}
	public static void main(String[] args) {
		//String str = randString();
		LOGGER.info(StringUtil.ToDBC("ＡＢＣＤＥＦ我们"));
	}
	
	/**
	 * 替换字符串
	 * 
	 * @param str
	 * @param map
	 * @return
	 */
	public static String replaceString(String str,  HashMap<String, String> map) {
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();

			String replaceStr = "%" + key + "%";
			if (str.contains(replaceStr)) {
				str = str.replaceAll(replaceStr, toString(value));
			}
		}
		return str;
	}
	
	/**
	 * 判断某字符串是否为空，为空的标准是 str==null 或 str.length()==0
	 * @param str
	 * @return (boolean)
	 */
	public static boolean isEmpty(String str){
		return StringUtils.isEmpty(str);
	}
	/**
	 * 判断某字符串是否为空长度为0或由空白符(whitespace) 构成
	 * @param str
	 * @return (boolean)
	 */
	public static boolean isBlank(String str){
		return StringUtils.isBlank(str);
	}
	
	/**
	 * 判断某字符串是否不为空，不为空的标准是 str!=null && str.length()!=0
	 * @param str
	 * @return (boolean)
	 */
	public static boolean isNotEmpty(String str){
		return StringUtils.isNotEmpty(str);
	}
	/**
	 * 判断某字符串是否不为为空:长度不为0并且不为空白符(whitespace)
	 * @param str
	 * @return (boolean)
	 */
	public static boolean isNotBlank(String str){
		return StringUtils.isNotBlank(str);
	}
	/**
	 * 查找嵌套字符串
	 * @desc 在testString中取得header和tail之间的字符串。不存在则返回空
 	 * @param testString header tail
	 * @return (String)
	 */
	public static String substringBetween(String testString,String header,String tail){
		return StringUtils.substringBetween(testString,header,tail);
	}
	/**
	 * 比较两个字符串是否相等，如果两个均为空则也认为相等。
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2){
		return StringUtils.equals(str1, str2);
	}
	/**
	 * @desc 比较两个字符串是否相等，不区分大小写，如果两个均为空则也认为相等
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCase(String str1, String str2){
		return StringUtils.equalsIgnoreCase(str1, str2);
	}

	/**
	 * 返回字符 searchChar 在字符串 str 中第一次出现的位置
	 * 如果 searchChar 没有在 str 中出现则返回-1，
     * 如果 str 为 null 或 "" ，则也返回-1
	 * @param str
	 * @param searchChar
	 * @return
	 */
	public static int indexOf(String str, char searchChar) {
		return StringUtils.indexOf(str, searchChar);
	}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);
               }
             }
             return new String(c);
    }
    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {        
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);
               }
             }
        String returnString = new String(c);
             return returnString;
    }
    
    /**
	 * @Description:随机字符串的代码 
	 * @param @param length
	 * @param @return 
	 * @throws
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "0123456789";  
	    Random random = new Random();  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int number = random.nextInt(base.length());  
	        sb.append(base.charAt(number));  
	    }  
	    return sb.toString();  
	}
	
	/**
	 * 随机字符串
	 * 
	 * @param len
	 * @param set
	 * @param sets
	 * @return
	 */
	public static String random(int len, String set, String... sets) {
		// 初始化
		Random random = new Random();
		StringBuilder strset = new StringBuilder(set).append(join("", sets));
		StringBuilder builder = new StringBuilder(len);
		
		// 生成随机字符串
		for (int i = 0; i < len; i++) {
			builder.append(strset.charAt(random.nextInt(strset.length())));
		}
		
		// 返回结果
		return builder.toString();
	}
	
	/**
	 * 合并成字符串
	 * 
	 * @param separator
	 * @param strs
	 * @return
	 */
	public static String join(String separator, String... strs) {
		StringBuilder str = new StringBuilder();
		for (String s: strs) {
			str.append(separator).append(String.valueOf(s));
		}
		return str.substring(separator.length());
	}

    /**
	 * 字符串集合
	 */
	public static final class StringSet {
		
		/** 数字 */
		public static final String NUMERIC			= "1234567890";

		/** 小写字母 */
		public static final String LOWER_ALPHABET 	= "abcdefghijklmnopqrstuvwxyz";

		/** 大写字母 */
		public static final String UPPER_ALPHABET 	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		/** 符号 */
		public static final String SYMBOL			= "!@#$%^&*_+-=|:;?";
	}
	
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	
	/**
	 * 参数转换为map集合
	 * @param str 格式{"client_id":"YOUR_APPID","openid":"YOUR_OPENID"}
	 * 					
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseJsonToMap(String str){
		Map<String, String> map = null;
		if(str!=null){
			map=JSON.parseObject(str, HashMap.class);
		}
		return map;
	}
	
	/**
	 * 将map 转换为json
	 * @param map
	 * @return
	 */
	public static String parseMapToJson(Map<String, Object> map){
		return JSON.toJSONString(map, true);
	}

	/**
	 * 截取子字符串
	 * 
	 * @param 　msg 截取字符串
	 * @param　statrStr 从那位开始截取
	 * @param　endStr 从那位字符串截止
	 * @return　如果没找到要截取子字符串，则返回空字串
	 */
	public static String interSub(String msg, Integer len) {

		if (isEmpty(msg)) {
			return "";
		}

		if(len > msg.length()) {
			len = msg.length();
		}
		
		return msg.substring(0,len);
	}

}
