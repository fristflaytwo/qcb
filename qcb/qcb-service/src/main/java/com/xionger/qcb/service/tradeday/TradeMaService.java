package com.xionger.qcb.service.tradeday;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.BaseStockAbstract;
import com.xionger.qcb.service.tradeday.task.StockMaTask;

@Service("tradeMaServiceImpl")
public class TradeMaService extends BaseStockAbstract{
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeMaService.class);
	
	@Autowired
	private TradeDayDao tradeDayDao;
	@Autowired
	private StockMaDao stockMaDao;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 计算均线必须传入日期
	 */
	protected boolean validate(ResultVo rv) {
		if(rv==null || StringUtil.isBlank(rv.getMsg())){//计算均线必须传入日期
			LOGGER.info("#--->系统传入TradeMaService->validate 参数【rv】或rv.getMsg()为空");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 股票交易均线计算前准备工作
	 * @param rv
	 */
	public void processExcute(ResultVo rv) {
		if(rv.getMsg().equals(this.tradeDayDao.getLastCreateDate())){
			insertStockDayMa(rv.getMsg());
			LOGGER.info("#--->系统传入TradeMaService->processExcute {}天均线计算完毕",rv.getMsg());
		}else{
			LOGGER.info("#--->系统传入TradeMaService->processExcute {}天均线已存在不需重复计算",rv.getMsg());
		}
	}
	
	/**
     * 进行均线计算
     * @param date 股票信息表的股票数据日期
     */
    public void insertStockDayMa(String date){
    	List<TradeDay> list=this.tradeDayDao.selectListByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(list)){
    		this.stockMaDao.deleteByCreateDate(date);//删除均线
    		int size=list.size()%50==0?list.size()/50:list.size()/50+1;
    		for(int i=0;i<size;i++){
    			taskExecutor.execute(new StockMaTask(list.subList(i*50, (i==size-1)?list.size():(i+1)*50), tradeDayDao, stockMaDao, date));
    		}
    	}
    }
    
  
}
