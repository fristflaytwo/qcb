package com.xionger.qcb.common.decrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.string.StringUtil;




/***
 * 3DES加密
 * @author    leo
 * @date      2016-7-19 上午10:43:19
 * @version   v1.0
 */
public class DESede {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(DESede.class);
	
	
	private DESede(){
		
	}
	
	/**
	 * 生成密钥
	 * @throws Exception 
	 */
	public static byte[] initKey() throws Exception{
		//密钥生成器
		KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
		//初始化密钥生成器
		keyGen.init(168);   //可指定密钥长度为112或168，默认168
		//生成密钥
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}
	
	
	/**
	 * 加密: 
	 * data 密文的utf-8
	 * @param  
	 * @author leo 
	 * @throws
	 */
	public static String encrypt3DES(String data, String key) {
		try {
			byte[] dataByte = data.getBytes(Charsets.UTF_8);
			//恢复密钥
			SecretKey secretKey = new SecretKeySpec(StringUtil.hexStringToBytes(key), "DESede");
			//Cipher完成加密
			Cipher cipher = Cipher.getInstance("DESede");
			//cipher初始化
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return StringUtil.bytesToHexString(cipher.doFinal(dataByte));
		}catch(Exception e) {
			LOGGER.error("3DE加密异常", e);
			return "";
		}
	}
	
	/**
	 * 解密
	 * 
	 * @param  
	 * @author leo 
	 * @throws
	 */
	public static String decrypt3DES(String data, String key) {
		try {
			byte[] dataByte = StringUtil.hexStringToBytes(data);
			//恢复密钥
			SecretKey secretKey = new SecretKeySpec(StringUtil.hexStringToBytes(key), "DESede");
			//Cipher完成解密
			Cipher cipher = Cipher.getInstance("DESede");
			//初始化cipher
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(dataByte),Charsets.UTF_8);
		}catch(Exception e) {
			LOGGER.error("3DE解密异常", e);
			return "";
		} 
	}
	
	
	public static void main(String[] args) throws Exception {
	  //  System.out.println(initKey());
		//jdbc:mysql://kcdzapp01.mysql.rds.aliyuncs.com:3306/demo_trust?useUnicode=true&characterEncoding=utf8
		String data = "jdbc:mysql://kcdzapp01.mysql.rds.aliyuncs.com:3306/demo_trust?useUnicode=true&characterEncoding=utf8";
		//加密
//		String mw = DESede.encrypt3DES(data, Constants.appKey);
//		System.out.println(mw);
		String destr = DESede.decrypt3DES("707c4c82b29303c80169214d33fd4bd1", Constants.appKey);
		System.out.println(destr );
		
	}
	
	
	
	
	
}
