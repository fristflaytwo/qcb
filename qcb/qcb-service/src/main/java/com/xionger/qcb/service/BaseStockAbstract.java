package com.xionger.qcb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.model.vo.ResultVo;


/**
 * 股票基本数据信息入库
 * @author admin
 *
 */
public abstract class BaseStockAbstract{
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseStockAbstract.class);

	/**
	 * 应用处理入口
	 * @param rv
	 */
	public final void process(ResultVo rv){
		LOGGER.info("股票基本信息记录入库--->begin--->{}",rv.toString());
		if(validate(rv)){
			processBefore(rv);
			if(rv.isSuccess()){
				processExcute(rv);
				if(rv.isSuccess()){
					processAfter(rv);
				}
			}
		}
		LOGGER.info("股票基本信息记录入库--->end--->{}",rv.toString());
	}
	
	/**
	 * 业务校验入口
	 * @param rv
	 */
	protected boolean validate(ResultVo rv) {
		return Boolean.TRUE;
	}
	
	/**
	 * 应用操作之前处理内容
	 * @param rv
	 */
	protected void processBefore(ResultVo rv) {}
	
	/**
	 * 应用操作处理内容
	 * @param rv
	 */
	protected void processExcute(ResultVo rv) {}
	
	/**
	 * 应用操作之后处理内容
	 * @param rv
	 */
	protected void processAfter(ResultVo rv) {}
}
