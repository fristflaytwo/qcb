package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockRecover;

public interface StockRecoverDao {
    int deleteByPrimaryKey(String id);

    int insert(StockRecover record);

    int insertSelective(StockRecover record);

    StockRecover selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockRecover record);

    int updateByPrimaryKey(StockRecover record);
    
    int deleteByCreateDate(String createDate);
}