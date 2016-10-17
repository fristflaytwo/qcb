package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockMa;

public interface StockMaDao {
    int deleteByPrimaryKey(String id);

    int insert(StockMa record);

    int insertSelective(StockMa record);

    StockMa selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockMa record);

    int updateByPrimaryKey(StockMa record);
}