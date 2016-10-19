package com.xionger.qcb.service;

import java.util.Date;




/**
 *	股票Service层接口
 * @author lll
 * @date  2016年5月29日 上午11:30:19
 */
public interface StockService{
	
    /**
     * 从下载的excel冲将数据导入到数据库,并返回数据日期
     * @param xlsDate
     * @param path excel路径
     * 
     */
    public String insertStockListByXlsdate(Date xlsDate,String path);
    
    /**
     * 保存股票结果集，并进行均线统计，Channel01：量比昨天大，收盘大于昨天最高，（收盘-开盘）> (最高-收盘)，收盘>5日均线,5周大于等于10和20周
     * @param date 股票信息表的股票数据日期
     */
    public void insertStockResultListByChannel01(String date);
    
    /**
     * 保存该日期的股票数据存在移动的数据 02:涨停；03：跳高；04：回缺
     * @param date
     */
    public void insertStockChange(String date);
	
}
