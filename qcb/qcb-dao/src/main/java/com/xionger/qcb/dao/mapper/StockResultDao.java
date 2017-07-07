package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockResult;

public interface StockResultDao {
    int deleteByPrimaryKey(String id);

    int insert(StockResult record);

    int insertSelective(StockResult record);

    StockResult selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockResult record);

    int updateByPrimaryKey(StockResult record);
    
    /**
     * 删除指定日期的结果集数据
     * @param createDate
     * @return
     */
    int deleteByCreateDate(String createDate);
}