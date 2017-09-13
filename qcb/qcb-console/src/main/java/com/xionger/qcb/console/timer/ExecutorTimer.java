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
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.StockTimerService;
import com.xionger.qcb.service.tradeday.TradeDayService;
import com.xionger.qcb.service.tradeday.TradeMaService;

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
	private StockTimerService stockTimerService;
	@Autowired
	private TradeDayService tradeDayServiceImpl;
	@Autowired
	private TradeMaService tradeMaServiceImpl;
	
	
	
	/**
	 * 股票日批数据
	 */
	public void insertTradeDay() {
		tradeDayServiceImpl.process(new ResultVo());
	}
	
	/**
	 * 初始化每天的日均线
	 */
	public void insertStockDayMa(){
		ResultVo rv=new ResultVo();
		rv.setMsg(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
		tradeMaServiceImpl.process(rv);
	}
	
	/**
	 * 扩展股票基本信息
	 */
	public void insertStockExpand(){
		this.stockTimerService.insertStockExpand(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
	}
	
	/**
	 * 扩展股票结果信息
	 */
	public void insertStockResult(){
		this.stockTimerService.insertStockResult(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
	}
	
	
	
}
