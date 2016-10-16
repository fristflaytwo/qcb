package com.xionger.qcb.dao.mapper;

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

   
}