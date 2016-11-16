package com.xionger.qcb.dao.mapper;

import java.util.List;
import java.util.Map;

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
    
    /**
     * 根据股票代码和数据日期，查询大于这天的该股票的数据结果集，并按照正序排列
     * @param record
     * @return
     */
    public List<Stock> selectListByCodeAndCreateDateAsc(Stock record);
    
    /**
     * 根据股票代码查询createDate前一天的数据
     * @param record
     * @return
     */
    public Stock selectByCodeAndBeforCreateDateDescOne(Stock record);
    
    /**
     * 保存股票信息
     * @param record
     * @return
     */
    public int insertSelective(Stock record);
    
    /**
     * 删除时间段内的数据
     * @param map
     */
    public void deleteStockListByStartAndEnd(Map<String, String> map);
}