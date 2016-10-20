package com.xionger.qcb.dao.mapper;

import java.util.List;

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
    
    /**
     * 查询所有的异动的股票
     * @return
     */
    public List<StockChange> selectChangeStockList();
    
    /**
     * 根据code和Type产出异动的股票监控对象
     * @param record
     * @return
     */
    public int deleteByCodeAndChangeType(StockChange record);
}