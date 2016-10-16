package com.xionger.qcb.common.util.file;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.constants.Constants;



public class Base64Utils {

	private static Logger logger = LoggerFactory
			.getLogger(Base64Utils.class);
	/**
	 * s
	 * 创建日期2014-4-24上午10:12:38 修改日期 作者： TODO 使用Base64加密算法加密字符串 return
	 */
	public static String encodeStr(String plainText) {
		try {
			return new String(Base64.encodeBase64(
					plainText.getBytes(Constants.UTF8), true));
		} catch (UnsupportedEncodingException e) {
			logger.error("加密异常",e.toString());
		}
		return "";
	}

	/**
	 * 
	 * 创建日期2014-4-24上午10:15:11 修改日期 作者： TODO 使用Base64加密 return
	 */
	public static String decodeStr(String encodeStr) {
		try {
			return new String(Base64.decodeBase64(encodeStr
					.getBytes(Constants.UTF8)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("解密异常",e.toString());
		}
		return "";
	}
	
	/**
	 * 加密二进制文件，返回加密后的文件
	
	 * @Description:
	
	 * @param @param bytes
	 * @param @param targetPath
	 * @param @return 
	
	 * @throws
	 */
	public static String toFile(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}
	/**
	 * 取得二进制文件
	
	 * @Description:
	
	 * @param @param base64Code
	 * @param @param targetPath
	 * @param @return 
	
	 * @throws
	 */
	public static byte[] decoderBase64File(String base64Code) {
		  byte[] bytes = Base64.decodeBase64(base64Code);
		  for (int i = 0; i < bytes.length; ++i) {  
              if (bytes[i] < 0) {// 调整异常数据  
                  bytes[i] += 256;  
              } 

		 }
		return bytes;
	}
}
