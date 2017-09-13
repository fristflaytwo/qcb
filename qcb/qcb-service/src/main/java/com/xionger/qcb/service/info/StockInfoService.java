package com.xionger.qcb.service.info;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.StockInfoDao;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.BaseStockAbstract;
import com.xionger.qcb.service.tradeday.task.StockMaTask;

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
	 * 先查询最后一天股票交易代码信息
	 * @param rv
	 */
	protected void processBefore(ResultVo rv) {
		List<TradeDay> list=this.tradeDayDao.selectListByCreateDate(this.tradeDayDao.getLastCreateDate());
		rv.setList(list);
		LOGGER.info("总共需要获取{}多上市公司信息",CollectionUtil.isNotEmpty(list)?list.size():0);
	}
	
	/**
	 * 日交易数据保存
	 * @param rv
	 */
	public void processExcute(ResultVo rv) {
		LOGGER.info("开始获取同花顺中股票基本信息数据");
		insertStockInfo(rv);
	}
	
	/**
     * 获取同花顺中股票基本信息数据
     */
    public void insertStockInfo(ResultVo rv){
    	List<TradeDay> list=(List<TradeDay>) rv.getList();
    	if(CollectionUtil.isNotEmpty(list)){
    		int size=list.size()%50==0?list.size()/50:list.size()/50+1;
    		for(int i=0;i<size;i++){
    			//taskExecutor.execute(new StockMaTask(list.subList(i*50, (i==size-1)?list.size():(i+1)*50), tradeDayDao, stockMaDao, date));
    		}
    	}
    }
    
  
}
