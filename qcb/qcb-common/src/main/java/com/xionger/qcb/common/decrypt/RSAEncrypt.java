package com.xionger.qcb.common.decrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.util.date.DateUtil;

import sun.misc.BASE64Decoder;



public class RSAEncrypt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RSAEncrypt.class);
	
	public static String DEFAULT_PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfzggZEJXQ4ESFzcpMAQWL5IxpGVK1tGMQMfyNpcsp/D2mDPM9dxblKDDV/f+H0pmt8PoG8oNaoZdZwD+fCnUJnycCLKF3XZPZrgLcMEY2VrCjABr2/HyjazcDWu8IX1CkRtxe4RhgORWUDgMdMG7UgqXnqB71vg2ar4bVmux8eQIDAQAB";
	public static String DEFAULT_PRIVATE_KEY="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ/OCBkQldDgRIXNykwBBYvkjGkZUrW0YxAx/I2lyyn8PaYM8z13FuUoMNX9/4fSma3w+gbyg1qhl1nAP58KdQmfJwIsoXddk9muAtwwRjZWsKMAGvb8fKNrNwNa7whfUKRG3F7hGGA5FZQOAx0wbtSCpeeoHvW+DZqvhtWa7Hx5AgMBAAECgYAsJ4VdR5PdjtngDqhir7WUnaWeNH0B78Gfa/BYQwMLqsJt6w4aYZlV3/D+b5v3L11/DiJHYSKiHBvxlIj/rKgBfEOURY9CTqYKw/ZOf16hkTXX4Nlz9qMRgRdo3qWPL7+lwPcSGqcBSONAUr0+EHBj0uyVM5L45Jq26tuEnLi9wQJBAN8uSGKGaeWQEnbl5NnY4zxo1IMbny61UkBaJswr+DbQWGaTYrCmXqw5XmgwvE5Ld9w2CxrA7jCJM1BVGtQC5mUCQQC3TfA7CrYvkd4Icc/0lI6J3HXHyzFZTNQiT5KswWWzAfGATwyQc2uyAXVoVItUDQVnYN/F+JKruGEvi4mwRgKFAkEAr7rOrh1uNp65mzsrdkjrq/5TDqTqMyn8iMUzdWJNwENOx/3XIIbvVqaZamqtauzLhO5c0gUFx9vQNrm8NSHPbQJAPcENIOb/79DFq7SkKbLuvnU+XNwQuRQQF6sVMAxLtd3+vYpkIjowWxzwsABjnCJy4pwZZ3nR9/Y7D6iD75X2fQJBAImHDvzZgYf1dnYyG+T0Js1qgB0K8oU4zlOROI987reDmtoicmwk8EgyTyP2FDiTvSQWVkDtwfBFUCkgmC7WDjg=";
	
	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	
	/**
	 * 随机生成密钥对
	 */
	public static void genKeyPair() {
		LOGGER.info(DateUtil.getCurrentDate()+"-->开始生成rsa密钥对");
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		Base64 base64 = new Base64();  
		DEFAULT_PUBLIC_KEY = base64.encodeToString(((RSAPublicKey) keyPair.getPublic()).getEncoded());  
		DEFAULT_PRIVATE_KEY = base64.encodeToString(((RSAPrivateKey) keyPair.getPrivate()).getEncoded());  
		System.out.println("DEFAULT_PUBLIC_KEY:\t"+DEFAULT_PUBLIC_KEY);
		System.out.println("DEFAULT_PRIVATE_KEY:\t"+DEFAULT_PRIVATE_KEY);
		LOGGER.info(DateUtil.getCurrentDate()+"-->生成rsa密钥对结束");
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static RSAPrivateKey loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		//产生一对公私密钥
		//RSAEncrypt.genKeyPair();
		
		// 加载公钥
//		try {
//			RSAEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
//			System.out.println("加载公钥成功");
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			System.err.println("加载公钥失败");
//		}

		// 加载私钥
//		try {
//			RSAEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
//			System.out.println("加载私钥成功");
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			System.err.println("加载私钥失败");
//		}

		// 测试字符串
		String encryptStr = "abc";
		try {
//			System.out.println("公钥长度：" + RSAEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY).toString().length());
//			System.out.println("私钥长度：" + RSAEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY).toString().length());
			
			// 加密
//			byte[] cipher = RSAEncrypt.encrypt(RSAEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY),encryptStr.getBytes());
//			System.out.println(Base64.encodeBase64String(cipher));
			String jiamichuan="iuUbdqmbldQXPSV/9nUWkLGw9OTPf8kYuC+GQqPf8/Nri2W9mIjdcWs4wbdRc7IU6t9YmM9RnxqriGV/STu1848jLD57ucJNemPhXkUjJx5z0OYSMUeD32TMVqMJQNJi7CW4qqkwKCAaPUTzheb21u+hAvmSkxA0qB+SQjRserY=";
			// 解密
			byte[] plainText = RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY), Base64.decodeBase64(jiamichuan));

			
			
//			System.out.println("密文长度:" + cipher.length);
//			System.out.println(RSAEncrypt.byteArrayToString(cipher));
//			System.out.println("明文长度:" + plainText.length);
//			System.out.println(RSAEncrypt.byteArrayToString(plainText));
			System.out.println(new String(plainText));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}