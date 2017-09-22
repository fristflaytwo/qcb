package com.xionger.qcb.service.tradeday.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.model.StockMa;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.service.tradeday.TradeMaService;

public class StockMaTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeMaService.class);
	private List<TradeDay> list=new ArrayList<TradeDay>();
	private TradeDayDao tradeDayDao;
	private StockMaDao stockMaDao;
	private String date;
	
	public StockMaTask(List<TradeDay> list,TradeDayDao tradeDayDao,StockMaDao stockMaDao,String date){
		this.list=list;
		this.tradeDayDao=tradeDayDao;
		this.stockMaDao=stockMaDao;
		this.date=date;
	}
	
	@Override
	public void run() {
		if(CollectionUtil.isNotEmpty(this.list)){
			List<StockMa> malist=new ArrayList<StockMa>();
    		List<TradeDay> stock30List=null;
    		BigDecimal DIGIT_5_2=new BigDecimal(Constants.DECIMAL_5_DIGIT_2);
			BigDecimal DIGIT_10_2=new BigDecimal(Constants.DECIMAL_10_DIGIT_2);
			BigDecimal DIGIT_20_2=new BigDecimal(Constants.DECIMAL_20_DIGIT_2);
			for(TradeDay stock:list){
				LOGGER.info("#--->系统进入[StockMaTask->run],开始计算{}天，代码{}的股票均线",date,stock.getCode());
    			stock30List=this.tradeDayDao.select30ListByCodeCreateDateOrderDesc(stock.getCode(),date);//必须结果集倒序
    			//均线计算
    			BigDecimal day5=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal day10=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal day20=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal day30=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal maSum=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			if(CollectionUtil.isNotEmpty(stock30List)){
    				for(int i=0;i<stock30List.size();i++){
    					maSum=maSum.add(stock30List.get(i).getNewPrice());
    					//均线区间统计
    					if(stock30List.size()>20 && stock30List.size()<=30){
    						if(i==4){
    							day5=maSum.divide(DIGIT_5_2,2);
            				}
    						if(i==9){
            					day10=maSum.divide(DIGIT_10_2,2);
            				}
    						if(i==19){
    							day20=maSum.divide(DIGIT_20_2,2);
            				}
    						if(i==(stock30List.size()-1)){
    							day30=maSum.divide(new BigDecimal(Integer.valueOf(stock30List.size()).toString()),2);
            				}
    					}else if(stock30List.size()>10 && stock30List.size()<=20){
    						if(i==4){
    							day5=maSum.divide(DIGIT_5_2,2);
            				}
    						if(i==9){
            					day10=maSum.divide(DIGIT_10_2,2);
            				}
    						if(i==(stock30List.size()-1)){
    							day20=maSum.divide(new BigDecimal(Integer.valueOf(stock30List.size()).toString()),2);
            				}
    					}else if(stock30List.size()>5 && stock30List.size()<=10){
    						if(i==4){
    							day5=maSum.divide(DIGIT_5_2,2);
            				}
    						if(i==(stock30List.size()-1)){
    							day10=maSum.divide(new BigDecimal(Integer.valueOf(stock30List.size()).toString()),2);
            				}
    					}else{
    						if(i==(stock30List.size()-1)){
    							day5=maSum.divide(new BigDecimal(Integer.valueOf(stock30List.size()).toString()),2);
            				}
    					}
        			}
    			}
    			
    			//保存均线数据
    			StockMa stockMa=new StockMa();
    			stockMa.generateId();
    			stockMa.setCode(stock.getCode());
    			stockMa.setCodeName(stock.getCodeName());
    			stockMa.setCreateDate(date);//数据日期
    			stockMa.setDay5(day5);
    			stockMa.setDay10(day10);
    			stockMa.setDay20(day20);
    			stockMa.setDay30(day30);
    			malist.add(stockMa);
			}
			if(CollectionUtil.isNotEmpty(malist)){
				stockMaDao.inserts(malist);
			}
		}
	}
}
