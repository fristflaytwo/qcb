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
     * 根据code和数据日期查询某只股票近20天的数据，按照数据日期进行倒序排列
     * @param map
     * @return
     */
    public List<Stock> select20ListByCodeAndCreateDateOrderCreateDateDesc(Map<String, String> map);
    
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
    
    /**
     * 得到最近20周的每周最后一个交易日的数据
     * @param map
     * @return
     */
    public List<Stock> select20WeekListByCodeAndCreateDateOrderCreateDateDesc(Map<String, String> map);
    
    /**
     * 查询该code股票再改日期包括该日期的只一次非停牌数据
     * 
     * @param map
     * @return
     */
    public Stock select1ByCreateDateDesc(Map<String, String> map);
    
    /**
     * 异动形态：跳空长阳
     * @param map
     * @return
     */
    public List<Stock> selectStockChangeBy01(Map<String, Object> map);
    
    /**
     * 异动形态：跳空长阴
     * @param map
     * @return
     */
    public List<Stock> selectStockChangeBy02(Map<String, Object> map);
    
    /**
     * 异动形态：上吊流星
     * @param map
     * @return
     */
    public List<Stock> selectStockChangeBy04(Map<String, Object> map);
    
    /**
     * 异动形态：揉搓线
     * @param map
     * @return
     */
    public List<Stock> selectStockChangeBy05(Map<String, Object> map);
    
    /**
     * 异动形态：7连阳
     * @param map
     * @return
     */
    public List<Stock> selectStockChangeBy03(Map<String, Object> map);
}