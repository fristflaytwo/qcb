package com.xionger.qcb.service.tradeexpand.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.TradeExpandDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.TradeExpand;

public class TradeExpandTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeExpandTask.class);

	private List<TradeDay> list=new ArrayList<TradeDay>();
	private TradeExpandDao tradeExpandDao;
	
	public TradeExpandTask(List<TradeDay> list,TradeExpandDao tradeExpandDao){
		this.list=list;
		this.tradeExpandDao=tradeExpandDao;
	}
	
	@Override
	public void run() {
		LOGGER.info("#--->线程id:{}--->开始进入股票交易扩展信息爬取线程",Thread.currentThread().getId());
		if(CollectionUtil.isNotEmpty(this.list)){
			List<TradeExpand> tradeExpandList=new ArrayList<TradeExpand>();
			String date=DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short);
			String result="";
			String [] expandInfo=null;
			String $spilt="~";
			String bigVal="0.0000";
			TradeExpand tradeExpand=null;
			for(TradeDay tradeDay:this.list){
				LOGGER.info("#--->线程id:{}--->开始爬取股票代码{}的交易扩展信息",Thread.currentThread().getId(),tradeDay.getCode());
				try {
					Thread.sleep(500);
					result=HttpClientUtils.doGet(Constants.STOCK_EXPAND_BASE_INFO+tradeDay.getCode(), Constants.UTF8);
					expandInfo=result.split($spilt);
					if(expandInfo!=null && expandInfo.length>45){
						tradeExpand=new TradeExpand();
						tradeExpand.generateId();
						tradeExpand.setCode(tradeDay.getCode());
						tradeExpand.setCodeName(tradeDay.getCodeName());
						tradeExpand.setTurnover(new BigDecimal(StringUtil.isNotBlank(expandInfo[38])?expandInfo[38]:bigVal));
						tradeExpand.setAmplitude(new BigDecimal(StringUtil.isNotBlank(expandInfo[43])?expandInfo[43]:bigVal));
						tradeExpand.setCirculationValue(new BigDecimal(StringUtil.isNotBlank(expandInfo[44])?expandInfo[44]:bigVal));
						tradeExpand.setTotalMarketValue(new BigDecimal(StringUtil.isNotBlank(expandInfo[45])?expandInfo[45]:bigVal));
						tradeExpand.setCreateDate(date);
						
						tradeExpandList.add(tradeExpand);
					}
				} catch (Exception e) {
					LOGGER.error("#--->线程id:{}--->爬取股票代码:{}的交易扩展信息失败，爬取到的数据为:{}",Thread.currentThread().getId(),tradeDay.getCode(),result,e);
				}
			}
			if(CollectionUtil.isNotEmpty(tradeExpandList)){
				tradeExpandDao.inserts(tradeExpandList);
				LOGGER.info("#--->线程id:{}--->爬取股票的交易扩展信息成功，本线程共插入{}条记录。",Thread.currentThread().getId(),tradeExpandList.size());
			}
		}
	}
}
