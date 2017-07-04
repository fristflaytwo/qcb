package com.xionger.qcb.service;





/**
 *	股票Service层接口
 * @author lll
 * @date  2016年5月29日 上午11:30:19
 */
public interface StockService{
	
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
	
}
