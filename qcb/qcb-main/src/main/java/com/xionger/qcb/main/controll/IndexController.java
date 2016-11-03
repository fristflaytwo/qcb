package com.xionger.qcb.main.controll;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.StockService;



/**
 * 首页  index
 * @author    leo
 * @date      2016-7-22 上午10:41:33
 * @version   v1.0
 */

@Controller
public class IndexController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private StockService stockService;
	
	@RequestMapping("")
	public String index() {
		return "index";
	}
 
	/**
	 * 加载本次保存的excel数据到股票信息表
	 */
	@RequestMapping("/test")
	public ResultVo test(String fileName){
		String path="d:/stock_xls/"+fileName+".xls";
		stockService.insertStockListByXlsdate(new Date(), path);
		return new ResultVo();
	}
	
	/**
	 * 找出需要监控的股票信息数据
	 * @param date
	 * @return
	 */
	@RequestMapping("/insertStockChange")
	@ResponseBody
	public ResultVo insertStockChange(String date){
		this.stockService.insertStockChange(date);
		return new ResultVo();
	}
	
	/**
	 * 对重点股票信息进行监控
	 * @return
	 */
	@RequestMapping("/updateStockListenerChange")
	@ResponseBody
	public ResultVo updateStockListenerChange(){
		this.stockService.updateStockListenerChange();
		return new ResultVo();
	}
	
	/**
	 * 保存股票的历史数据
	 * @return
	 */
	@RequestMapping("/insertHistoryStock")
	@ResponseBody
	public ResultVo insertHistoryStock(String codes,String start,String end){
		this.stockService.insertHistoryStock(codes, start, end);
		return new ResultVo();
	}
	
}
