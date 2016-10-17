package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockMa;

public interface StockMaDao {
    int deleteByPrimaryKey(String id);

    int insert(StockMa record);

    int insertSelective(StockMa record);

    StockMa selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockMa record);

    int updateByPrimaryKey(StockMa record);
    
    /**
     * 删除对应日期的数据结果集
     * @param createDate
     * @return
     */
    int deleteByCreateDate(String createDate);
}