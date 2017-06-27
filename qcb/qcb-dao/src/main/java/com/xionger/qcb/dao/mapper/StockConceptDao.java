package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockConcept;

public interface StockConceptDao {
    int deleteByPrimaryKey(String id);

    int insert(StockConcept record);

    int insertSelective(StockConcept record);

    StockConcept selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockConcept record);

    int updateByPrimaryKey(StockConcept record);
}