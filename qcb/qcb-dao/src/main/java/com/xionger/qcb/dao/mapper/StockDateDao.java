package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockDate;

public interface StockDateDao {
    int deleteByPrimaryKey(String id);

    int insert(StockDate record);

    int insertSelective(StockDate record);

    StockDate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockDate record);

    int updateByPrimaryKey(StockDate record);
    
    /**
     * 查询小于等于该日期的最后一次周的结束日期
     * @param date
     * @return
     */
    StockDate selectByIstrueEndWeekDay(String date);
    
    /**
     * 根据交易日期查询交易日期对象
     * @param date
     * @return
     */
    StockDate selectByStockDate(String date);
    
    /**
     * 删除指定日期的交易日期信息
     * @param date
     */
    void deleteByStockDate(String date);
}