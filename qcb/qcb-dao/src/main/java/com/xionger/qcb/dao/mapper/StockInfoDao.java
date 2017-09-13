package com.xionger.qcb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xionger.qcb.model.StockInfo;

public interface StockInfoDao {
    int deleteByPrimaryKey(String id);

    int inserts(@Param("stockInfoList")List<StockInfo> stockInfoList);

    int insertSelective(StockInfo record);

    StockInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockInfo record);

    int updateByPrimaryKey(StockInfo record);
}