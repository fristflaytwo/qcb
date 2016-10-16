package com.xionger.qcb.common.util.serial;

import java.security.SecureRandom;
import java.util.UUID;

import com.xionger.qcb.common.util.date.DateUtil;




/**
 * 序列工具类
 * @description 
 * @author yyj
 * @date 2016年6月1日 下午5:34:28
 * @version V1.0
 */
public class SerialUtil {
	
	
	/** 
     * 每位允许的字符 
     */  
    private static final String POSSIBLE_CHARS1 = "0123456789ABCDEFGHIJKL0123456789MNOPQRSTUVWXYZ0918273645ABCDEFGHIJKL0123456789MNOPQRSTUVWXYZ9876543210";
    private static final String POSSIBLE_CHARS3 = "0123456789";
    
    /**
     * 生成20位协议流水号
     * @description 时间戳16位+6位随机数
     * @author yyj
     * @create 2016年6月20日 下午3:28:20
     * @return
     */
    public static synchronized String getSerialo(){
    	return DateUtil.getRandTimeByDate().substring(2)+generateRandomString(8, POSSIBLE_CHARS1);
    }
    
    /**
     * 生成32位唯一token号
     * @param  
     * @author leo 
     * @throws
     */
    public static synchronized String getTokenSerial(){
    	return  UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    
    /**
     * 生成33位唯一系统流水号
     * @param  
     * @author leo 
     * @throws
     */
    public static synchronized String getsSysSerial(){
    	return  "s_"+UUID.randomUUID().toString().replaceAll("-", "");
    }
   
    
    
    
    
    
    /**
     * 生产一个指定长度的随机字符串 
     * @description 
     * @author yyj
     * @create 2016年6月1日 下午5:39:39
     * @param length 生成长度
     * @param possibleChars 允许的字符 
     * @return
     */
    private static String generateRandomString(int length, String possibleChars) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(possibleChars.charAt(random.nextInt(possibleChars.length())));
        }
        return sb.toString();
    }
    
    /**
     * 生成16位API的key
     * @return
     */
    public static synchronized  String getApiKey(){
    	return generateRandomString(16, POSSIBLE_CHARS3);
    }
    
    /**
     * 测试
     * @description 
     * @author yyj
     * @create 2016年6月1日 下午6:38:26
     * @param args
     */
    public static void main(String[] args) {
    	System.out.println(getsSysSerial());
    }
    
    
}
