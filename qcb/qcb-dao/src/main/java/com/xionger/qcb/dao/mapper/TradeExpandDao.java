package com.xionger.qcb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xionger.qcb.model.TradeExpand;

public interface TradeExpandDao {
    int deleteByPrimaryKey(String id);

    /**
     * 批量插入
     * @param tradeExpandList
     * @return
     */
    int inserts(@Param("tradeExpandList")List<TradeExpand> tradeExpandList);

    int insertSelective(TradeExpand record);

    TradeExpand selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TradeExpand record);

    int updateByPrimaryKey(TradeExpand record);
}