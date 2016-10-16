/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年9月10日 上午10:20:52
 */
package com.xionger.qcb.common.util.ip;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Project: trust-common
 * @Author zy
 * @Company:
 * @Create Time: 2016年9月10日 上午10:20:52
 */
public class IpUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);
			
	public static String getLocalIp() {
		String ipAddrStr = "";
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
			byte[] ipAddr = addr.getAddress();
			for (int i = 0; i < ipAddr.length; i++) {
				if (i > 0) {
					ipAddrStr += ".";
				}
				ipAddrStr += ipAddr[i] & 0xFF;
			}
		} catch (Exception e) {
			LOGGER.error("获取本机IP异常", e);
			ipAddrStr = "127.0.0.1";
		}

		return ipAddrStr;
	}
}
