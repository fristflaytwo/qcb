package com.xionger.qcb.main.start;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 平台启动
 * @author    leo
 * @date      2016-7-18 上午11:25:52
 * @version   v1.0
 */
public class MainStart {
	private static Logger logger = LoggerFactory.getLogger(MainStart.class);

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getConnector().setURIEncoding("UTF-8");
		String path = MainStart.class.getResource("/").getPath();
		tomcat.addWebapp("", path.substring(0, path.indexOf("target")) + "src/main/webapp");
		tomcat.start();
		logger.info("MainStart Started tomcat");
		tomcat.getServer().await();
	}
}