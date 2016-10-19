package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockChange;

public interface StockChangeDao {
    int deleteByPrimaryKey(String id);

    int insert(StockChange record);

    int insertSelective(StockChange record);

    StockChange selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockChange record);

    int updateByPrimaryKey(StockChange record);
    
    /**
     * 删除stockDate这天的异动数据
     * @param stockDate
     * @return
     */
    public int deleteByStockDate(String stockDate);
}