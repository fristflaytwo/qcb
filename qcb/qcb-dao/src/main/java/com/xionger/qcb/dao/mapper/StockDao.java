package com.xionger.qcb.dao.mapper;

import java.util.List;

import com.xionger.qcb.model.Stock;


public interface StockDao {
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
    int deleteById(String id);

    /**
     * 保存对象
     * @param record
     * @return
     */
    int insert(Stock record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Stock selectById(String id);
    
    /**
     * 根据数据日期删除数据
     * @param dataDate
     */
    public void deleteStockListByDataDate(String dataDate);

    /**
     * 根据数据日期查询改天的所有数据记录
     * @param id
     * @return
     */
    public List<Stock> selectListByCreateDate(String createDate);
    
    
    /**
     * 根据code查询某只股票近20天的数据，按照数据日期进行倒序排列
     * @param code
     * @return
     */
    public List<Stock> select20ListByCodeOrderCreateDateDesc(String code);
}