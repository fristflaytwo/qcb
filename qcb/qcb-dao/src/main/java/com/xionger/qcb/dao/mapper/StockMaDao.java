package com.xionger.qcb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xionger.qcb.model.StockMa;

public interface StockMaDao {
	/**
	 * 根据code查询均线
	 * @param code
	 * @return
	 */
	StockMa selectByCode(String code);

	/**
	 * 均线批量插入
	 * @param stockMaList
	 * @return
	 */
	int inserts(@Param("stockMaList")List<StockMa> stockMaList);

	int insertSelective(StockMa record);

	/**
	 * 删除对应日期的数据结果集
	 * 
	 * @param createDate
	 * @return
	 */
	int deleteByCreateDate(String createDate);

	/**
	 * 查询该天的均线数据
	 * 
	 * @param date
	 * @return
	 */
	List<StockMa> selectByCreateDate(String createDate);
}