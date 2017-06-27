package com.xionger.qcb.dao.mapper;



import com.xionger.qcb.model.StockExpand;

public interface StockExpandDao {
    int deleteByPrimaryKey(String id);

    int insert(StockExpand record);

    int insertSelective(StockExpand record);

    StockExpand selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockExpand record);

    int updateByPrimaryKey(StockExpand record);
}