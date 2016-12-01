package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockChange;

public interface StockChangeDao {
	int deleteByPrimaryKey(String id);

	int insert(StockChange record);

	int insertSelective(StockChange record);

	StockChange selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(StockChange record);

	int updateByPrimaryKey(StockChange record);
}