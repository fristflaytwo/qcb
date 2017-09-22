package com.xionger.qcb.service.info;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.dao.mapper.StockInfoDao;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.BaseStockAbstract;
import com.xionger.qcb.service.info.task.StockInfoTask;

@Service("stockInfoServiceImpl")
public class StockInfoService extends BaseStockAbstract{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockInfoService.class);
	
	@Autowired
	private StockInfoDao stockInfoDao;
	@Autowired
	private TradeDayDao tradeDayDao;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 获取股票基础信息之前准备工作
	 * @param rv
	 */
	protected void processBefore(ResultVo rv) {
		List<TradeDay> list=this.tradeDayDao.selectListForUpdateStockInfo(this.tradeDayDao.getLastCreateDate(),DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
		rv.setList(list);
		LOGGER.info("#--->总共需要获取{}多上市公司信息",CollectionUtil.isNotEmpty(list)?list.size():0);
	}
	
	/**
	 * 开始获取股票基本信息
	 * @param rv
	 */
	public void processExcute(ResultVo rv) {
		LOGGER.info("#--->开始获取同花顺中股票基本信息数据");
		insertStockInfo(rv);
	}
	
	/**
     * 获取同花顺中股票基本信息数据
     */
    public void insertStockInfo(ResultVo rv){
    	@SuppressWarnings("unchecked")
		List<TradeDay> list=(List<TradeDay>) rv.getList();
    	if(CollectionUtil.isNotEmpty(list)){
    		int size=list.size()%50==0?list.size()/50:list.size()/50+1;
    		for(int i=0;i<size;i++){
    			taskExecutor.execute(new StockInfoTask(list.subList(i*50, (i==size-1)?list.size():(i+1)*50), stockInfoDao));
    		}
    	}
    }
    
  
}
