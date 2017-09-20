package com.xionger.qcb.service.concept;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.dao.mapper.StockConceptDao;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.BaseStockAbstract;
import com.xionger.qcb.service.concept.task.StockConceptTask;

@Service("stockConceptServiceImpl")
public class StockConceptService extends BaseStockAbstract{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockConceptService.class);
	
	@Autowired
	private StockConceptDao stockConceptDao;
	@Autowired
	private TradeDayDao tradeDayDao;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 先查询最后一天股票交易代码信息
	 * @param rv
	 */
	protected void processBefore(ResultVo rv) {
		List<TradeDay> list=this.tradeDayDao.selectListForUpdateStockConcept(this.tradeDayDao.getLastCreateDate(),DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
		rv.setList(list);
		LOGGER.info("总共需要获取{}多上市公司信息，进行概念爬取",CollectionUtil.isNotEmpty(list)?list.size():0);
	}
	
	/**
	 * 日交股票概念爬去
	 * @param rv
	 */
	public void processExcute(ResultVo rv) {
		LOGGER.info("开始获取同花顺中股票概念信息数据");
		insertStockConcept(rv);
	}
	
	/**
     * 获取同花顺中股票概念数据
     */
    public void insertStockConcept(ResultVo rv){
    	@SuppressWarnings("unchecked")
		List<TradeDay> list=(List<TradeDay>) rv.getList();
    	if(CollectionUtil.isNotEmpty(list)){
    		int size=list.size()%50==0?list.size()/50:list.size()/50+1;
    		for(int i=0;i<size;i++){
    			taskExecutor.execute(new StockConceptTask(list.subList(i*50, (i==size-1)?list.size():(i+1)*50), stockConceptDao));
    		}
    	}
    }
    
  
}
