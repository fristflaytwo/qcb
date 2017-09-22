package com.xionger.qcb.service.tradeexpand;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.dao.mapper.TradeExpandDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.BaseStockAbstract;
import com.xionger.qcb.service.tradeexpand.task.TradeExpandTask;

@Service("tradeExpandServiceImpl")
public class TradeExpandService extends BaseStockAbstract{
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeExpandService.class);
	
	@Autowired
	private TradeExpandDao tradeExpandDao;
	@Autowired
	private TradeDayDao tradeDayDao;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 先查询最后一天股票交易代码信息
	 * @param rv
	 */
	protected void processBefore(ResultVo rv) {
		if(rv==null){
			rv=new ResultVo(false,"9999","传入参数错误");
			LOGGER.info("#--->系统进入[TradeExpandService->processBefore]参数【rv】为空");
		}else{
			if(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short).equals(this.tradeDayDao.getLastCreateDate())){
				List<TradeDay> list=this.tradeDayDao.selectListForUpdateTradeExpand(this.tradeDayDao.getLastCreateDate(),DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short));
				rv.setList(list);
			}
			LOGGER.info("#--->爬取交易扩展信息，总共需要获取{}上市公司信息",CollectionUtil.isNotEmpty(rv.getList())?rv.getList().size():0);
		}
	}
	
	/**
	 * 日交易数据保存
	 * @param rv
	 */
	public void processExcute(ResultVo rv) {
		LOGGER.info("#--->开始获取交易扩展信息");
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
    			taskExecutor.execute(new TradeExpandTask(list.subList(i*50, (i==size-1)?list.size():(i+1)*50), tradeExpandDao));
    		}
    	}
    }
    
  
}
