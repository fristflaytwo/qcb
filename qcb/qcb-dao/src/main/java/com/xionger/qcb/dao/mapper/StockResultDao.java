package com.xionger.qcb.dao.mapper;

import com.xionger.qcb.model.StockResult;

public interface StockResultDao {
    int deleteByPrimaryKey(String id);

    int insert(StockResult record);

    int insertSelective(StockResult record);

    StockResult selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockResult record);

    int updateByPrimaryKey(StockResult record);
    
    /**
     * 删除指定日期的结果集数据
     * @param createDate
     * @return
     */
    int deleteByCreateDate(String createDate);
    
    /**
     * 插入这天的反包数据
     * @param createDate
     * @return
     */
    int insertByResultType01(String createDate);
    
    /**
     * 插入这天的阶段底-防御数据
     * @param createDate
     * @return
     */
    int insertByResultType02(String createDate);
    
    /**
     * 插入这天的阶段底-进攻数据
     * @param createDate
     * @return
     */
    int insertByResultType03(String createDate);
    
    /**
     * 插入这天的空中加油数据
     * @param createDate
     * @return
     */
    int insertByResultType04(String createDate);
    
    /**
     * 插入这天的风口神龙数据
     * @param createDate
     * @return
     */
    int insertByResultType05(String createDate);
}