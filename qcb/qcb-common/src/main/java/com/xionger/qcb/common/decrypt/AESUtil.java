package com.xionger.qcb.common.decrypt;  
  

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.util.string.StringUtil;


  
/**
 *  AES 对称加密
 * @author    leo
 * @date      2016-7-28 下午12:34:18
 * @version   v1.0
 */
public class AESUtil {  
	
    static Cipher cipher;  
    static final String KEY_ALGORITHM = "AES"; 
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);
      
    static{
 	   try {
 		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	 	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	 		LOGGER.error("aes初始化异常", e);
	 	} 
    }
          
	/**
	 * 生成密钥(长度32位)
	 * @throws Exception 
	 */
	public static String generateKey() {
		
		return generatorKey(32);
//		try {
//			//密钥生成器
//			KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
//			//初始化密钥生成器
//			keyGen.init(128);  //默认128，获得无政策权限后可用192或256
//			//生成密钥
//			SecretKey secretKey = keyGen.generateKey();
//			return StringUtil.bytesToHexString(secretKey.getEncoded());
//		} catch (NoSuchAlgorithmException e) {
//			LOGGER.error("生成Key异常", e);
//			return null;
//		}
	}
	
	 /**
     * 生成指定位数的key
     *
     * @param pwdLen
     * @return
     */
    public static String generatorKey(int pwdLen){
        String pwdchars = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz";
        String tmpstr = "";
        int iRandNum;
        Random rnd = new Random();
        for (int i = 0; i < pwdLen; i++)
        {
            iRandNum = rnd.nextInt(pwdchars.length());
            tmpstr += pwdchars.charAt(iRandNum);
        }
        return tmpstr;
    }

    /**
     * des 加密，模式 AES/CBC/PKCS5Padding 
     * @param  
     * @author leo 
     * @throws
     */
    public static String encryptAES(String data, String key) throws Exception {  
    	try {
			byte[] dataByte = data.getBytes(Charsets.UTF_8);
			SecretKey secretKey = new SecretKeySpec(StringUtil.hexStringToBytes(key), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用加密模式初始化 密钥  
			byte[] encrypt = cipher.doFinal(dataByte); 
			return StringUtil.bytesToHexString(encrypt);
		} catch (Exception e) {
			LOGGER.error("encryptAES异常", e);
			return null;
		}
    }  
    
    /**
     * des 解密 模式 AES/CBC/PKCS5Padding 
     * @param  
     * @author leo 
     * @throws
     */
    public static String decryptAES(String data, String key) throws Exception {  
        try {
			SecretKey secretKey = new SecretKeySpec(StringUtil.hexStringToBytes(key), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用加密模式初始化 密钥  
			byte[] decrypt = cipher.doFinal(StringUtil.hexStringToBytes(data)); //按单部分操作加密或解密数据，或者结束一个多部分操作。  
			return new String(decrypt,Charsets.UTF_8);
		} catch (Exception e) {
			LOGGER.error("decryptAES异常", e);
			return null;
		}
    }  

    /**
     * IV length: must be 16 bytes long  
     * @param  
     * @author leo 
     * @throws
     */
    public static byte[] getIV() {  
        String iv = "1234567812345678"; 
        return iv.getBytes();  
    }  
      
    
    public static void main(String[] args) throws Exception {
    	String key = generateKey();
    	String data = "a*jal)k32J8czx囙国为国宽";
    	String enstr = encryptAES(data, key);
    	System.out.println(decryptAES(enstr, key));
    	System.out.println(data);
	}
    
      
}  