package com.xionger.qcb.service;

import java.util.Date;




/**
 *	股票Service层接口
 * @author lll
 * @date  2016年5月29日 上午11:30:19
 */
public interface StockService{
	
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
    
    /**
     * 周均线统计
     * @param date 自然日
     */
    public void insertStockWeekMa(String date);
    
    /**
     * 保存该日期的股票数据存在移动的数据 02:涨停；03：跳高；04：回缺
     * @param date
     */
    public void insertStockChange(String date);
    
    /**
     * 对需要坚挺的股票进行监听
     */
    public void updateStockListenerChange();
    
    /**
     * 插入从start到end日期的股票数据信息
     * @param date 最新股票数据日期
     * @param start
     * @param end
     */
    public void insertHistoryStock(String date,String start,String end);
    
    /**
     * 批量下载历史股票数据信息
     * @param date 指定参考日期的股票数据
     * @param startTime 起始日期
     * @param endTime 结束日期
     */
    public void downLoadHisData(String date,String startTime,String endTime);
	
    /**
     * 扫描下载csv目录下的txt文件并进行股票历史数据保存
     */
    public void insertScanStockTxt();
    
    /**
     * 股票日期保存
     * @param date
     */
    public void insertStockDate(String date);
    
    
}
