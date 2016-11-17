package com.xionger.qcb.dao.mapper;

import java.util.List;

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
    
    /**
     * 查询改天的均线数据
     * @param date
     * @return
     */
    List<StockMa> selectByCreateDate(String date);
}