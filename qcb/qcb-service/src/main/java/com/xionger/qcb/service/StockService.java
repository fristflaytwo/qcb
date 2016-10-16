package com.xionger.qcb.service;

import java.util.Date;




/**
 *	股票Service层接口
 * @author lll
 * @date  2016年5月29日 上午11:30:19
 */
public interface StockService{
	
    /**
     * 从下载的excel冲将数据导入到数据库
     * @param xlsDate
     * @param path excel路径
     * 
     */
    public void insertStockListByXlsdate(Date xlsDate,String path);
	
}
