package com.xionger.qcb.main.timer;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private StockService stockService;
	
	/**
	 * 利用httpclient获取股票信息数据
	 */
	public void initStockData() {
		//先下载数据到本地磁盘
		BufferedOutputStream bw = null;
		String path="d:/luolonglong/work_cs/qcb/qcb/qcb-main/stock_xls/"+DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short)+".xls";
		// 创建文件对象
		File f = new File(path);
		// 创建文件路径
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
		try {
			byte[] result=HttpClientUtils.doGetReturnBytes("http://stock.gtimg.cn/data/get_hs_xls.php?id=ranka&type=1&metric=chr", Constants.UTF8);
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
		String dataDate=stockService.insertStockListByXlsdate(new Date(), path);//保存基本数据并返回数据日期
		
		//重新计算均线
		this.stockService.insertStockMa(dataDate);
	}
	
	
	/**
	 * 每天异动股票信息记录
	 */
	public void initStockChange(){
		this.stockService.insertStockChange(DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short));
	}
	
	/**
	 * 对移动股票数据进行监控
	 */
	public void initStockListenerChange(){
		this.stockService.updateStockListenerChange();
	}
	
	
}
