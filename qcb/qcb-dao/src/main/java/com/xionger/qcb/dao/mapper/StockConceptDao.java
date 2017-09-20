package com.xionger.qcb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xionger.qcb.model.StockConcept;

public interface StockConceptDao {
    int deleteByPrimaryKey(String id);

    /**
     * 批量插入
     * @param stockConceptList
     * @return
     */
    int inserts(@Param("stockConceptList")List<StockConcept> stockConceptList);

    int insertSelective(StockConcept record);

    StockConcept selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockConcept record);

    int updateByPrimaryKey(StockConcept record);
}