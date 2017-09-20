package com.xionger.qcb.console.timer;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.StockTimerService;
import com.xionger.qcb.service.concept.StockConceptService;
import com.xionger.qcb.service.info.StockInfoService;
import com.xionger.qcb.service.tradeday.TradeDayService;
import com.xionger.qcb.service.tradeday.TradeMaService;
import com.xionger.qcb.service.tradeexpand.TradeExpandService;

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
	@Autowired
	private StockInfoService stockInfoServiceImpl;
	@Autowired
	private StockConceptService stockConceptServiceImpl;
	@Autowired
	private TradeExpandService tradeExpandServiceImpl;
	
	
	
	/**
	 * 股票日批数据
	 */
	public void insertTradeDay() {
		LOGGER.info("开始下载每天的交易数据");
		tradeDayServiceImpl.process(new ResultVo());
	}
	
	/**
	 * 初始化每天的日均线
	 */
	public void insertStockDayMa(){
		LOGGER.info("开始初始化每天交易均线数据");
		ResultVo rv=new ResultVo();
		rv.setMsg(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
		tradeMaServiceImpl.process(rv);
	}
	
	/**
	 * 初始化每月的股票基本数据
	 */
	public void insertStockInfo(){
		LOGGER.info("开始爬取股票信息每月的基本信息更新");
		this.stockInfoServiceImpl.process(new ResultVo());
	}
	
	/**
	 * 初始化股票概念
	 */
	public void insertStockConcept(){
		LOGGER.info("开始爬取股票信息的概念数据");
		this.stockConceptServiceImpl.process(new ResultVo());
	}
	
	
	/**
	 * 扩展股票基本信息
	 */
	public void insertStockExpand(){
		LOGGER.info("开始爬取股票信息的每天交易扩展数据");
		this.tradeExpandServiceImpl.process(new ResultVo());
	}
	
	/**
	 * 扩展股票结果信息
	 */
	public void insertStockResult(){
		this.stockTimerService.insertStockResult(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
	}
	
	
	
}
