/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月13日 下午3:13:19
 */
package com.xionger.qcb.common.decrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.util.string.StringUtil;


/**
 * @Project: trust-common
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月13日 下午3:13:19
 */
public class MDUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MDUtil.class);

	public static String SHA1(String decript) {
		String ret = "";
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			ret = StringUtil.bytesToHexString(messageDigest);

		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("" + e);
		}
		return ret;
	}
	
}
