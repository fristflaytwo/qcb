package com.xionger.qcb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xionger.qcb.model.TradeDay;

public interface TradeDayDao {
	/**
	 * 批量插入股票日交易数据
	 * @param tradeDayList
	 * @return
	 */
    int inserts(@Param("tradeDayList")List<TradeDay> tradeDayList);

    /**
     * 单个股票日交易数据插入
     * @param record
     * @return
     */
    int insertSelective(TradeDay record);
    
    /**
     * 根据数据日期删除数据
     * @param dataDate
     */
    void deleteByCreateDate(@Param("createDate")String createDate);
    
    /**
     * 根据日期查询当日股票交易数据
     * @param createDate
     * @return
     */
    List<TradeDay> selectListByCreateDate(@Param("createDate")String createDate);
    
    /**
     * 查询某个股票近30天非停牌的数据
     * @param code
     * @param createDate
     * @return
     */
    List<TradeDay> select30ListByCodeCreateDateOrderDesc(@Param("code")String code,@Param("createDate")String createDate);
    
    /**
     * 得到最后一天交易日期
     * @return
     */
    String getLastCreateDate();
    
    /**
     * 查询需要爬去股票信息的股票记录
     * @param tradeDayDate
     * @param stockInfoDate
     * @return
     */
    List<TradeDay> selectListForUpdateStockInfo(@Param("tradeDayDate")String tradeDayDate,@Param("stockInfoDate")String stockInfoDate);
    
    /**
     * 查询需要爬去股票信息的概念信息
     * @param tradeDayDate
     * @param stockConceptDate
     * @return
     */
    List<TradeDay> selectListForUpdateStockConcept(@Param("tradeDayDate")String tradeDayDate,@Param("stockConceptDate")String stockConceptDate);
    
    /**
     * 查询需要爬去股票交易扩展信息
     * @param tradeDayDate
     * @param tradeExpandDate
     * @return
     */
    List<TradeDay> selectListForUpdateTradeExpand(@Param("tradeDayDate")String tradeDayDate,@Param("tradeExpandDate")String tradeExpandDate);
    
    
    
}