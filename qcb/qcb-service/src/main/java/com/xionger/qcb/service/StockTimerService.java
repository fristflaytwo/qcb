package com.xionger.qcb.service;

import java.util.Date;




/**
 *	股票Service层接口
 * @author lll
 * @date  2016年5月29日 上午11:30:19
 */
public interface StockTimerService{
	
    /**
     * 从下载的excel中将数据导入到数据库,并返回数据日期
     * @param xlsDate
     * @param path excel路径
     * 
     */
    public String insertStockListByXlsdate(Date xlsDate,String path);
    
    /**
     * 日均线统计
     * @param date 股票信息表的股票数据日期
     */
    public void insertStockDayMa(String date);
    
    
	
}
