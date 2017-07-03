package com.xionger.qcb.console.timer;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.service.StockService;

/**
 * 定时计划任务处理服务类
 * @author Administrator
 *
 */
@Component
public class ExecutorTimer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorTimer.class);
	
	@Value("${stock.xls.path}")
	private String stockXlsPath;//股票数据文件存放路径
	
	@Autowired
	private StockService stockService;
	
	/**
	 * 初始化股票交易日期数据
	 */
	public void initStockDate(){
		this.stockService.insertStockDate(DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short));
	}
	
	/**
	 * 利用httpclient获取股票信息数据
	 */
	public void initStockData() {
		Date date=new Date();
		//先下载数据到本地磁盘
		BufferedOutputStream bw = null;
		String path=stockXlsPath+DateUtil.dateToString(date,DateUtil.formatPattern_Short)+".xls";
		// 创建文件对象
		File f = new File(path);
		// 创建文件路径
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
		try {
			byte[] result=HttpClientUtils.doGetReturnBytes(Constants.STOCK_XLS_DOWNLOAD_PATH, Constants.UTF8);
			// 写入文件
			bw = new BufferedOutputStream(new FileOutputStream(path));
			bw.write(result);
		} catch (Exception e) {
			LOGGER.error("下载"+DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short)+"天股票信息数据异常",e);
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception e) {
			}
			bw=null;
		}
		
		//调用server层进行数据保存
		stockService.insertStockListByXlsdate(date, path);//保存基本数据并返回数据日期
	}
	
	/**
	 * 初始化每天的日均线
	 */
	public void initStockDayMa(){
		this.stockService.insertStockDayMa(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
	}
	
	/**
	 * 初始化每周的周均线,需要放在日均线之后执行
	 */
	public void initStockWeekMa(){
		this.stockService.insertStockWeekMa(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
	}
	
	
	
}
