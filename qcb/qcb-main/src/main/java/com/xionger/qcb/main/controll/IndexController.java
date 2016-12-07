package com.xionger.qcb.main.controll;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.json.JsonUtil;
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
	@RequestMapping("/insertStockListByXlsdate")
	public ResultVo insertStockListByXlsdate(String fileName){
		String path="d:/stock_xls/"+fileName+".xls";
		stockService.insertStockListByXlsdate(new Date(), path);
		return new ResultVo();
	}
	
	
	/**
	 * 保存股票的历史数据
	 * http://127.0.0.1:8080/insertHistoryStock
	 * {
			"date":"20161107",
			"start":"2016-10-11 23:59:59",
			"end":"2016-10-12 23:59:59"
		}
		缺少12号的数据，最好在跑之前先删除当天的数据，然后最好中间有时间间隔
	 * @return
	 */
	@RequestMapping("/insertHistoryStock")
	@ResponseBody
	public ResultVo insertHistoryStock(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		this.stockService.insertHistoryStock(map.get("date"), map.get("start"), map.get("end"));
		return new ResultVo();
	}
	
	/**
	 * 批量下载股票历史数据
	 * @param data date:参考的股票信息日期 startTime:起始时间 endTime：结束时间
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/downLoadHisData")
	@ResponseBody
	public ResultVo downLoadHisData(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		this.stockService.downLoadHisData(map.get("date"),map.get("startTime"),map.get("endTime"));
		return new ResultVo();
	}
	
	/**
	 * 将股票历史数据通过扫描保存到数据库
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/insertScanStockTxt")
	@ResponseBody
	public ResultVo insertScanStockTxt(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		this.stockService.insertScanStockTxt();
		return new ResultVo();
	}
	
	
	/**
	 * 计算股票的交易日期
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/insertStockDate")
	@ResponseBody
	public ResultVo insertStockDate(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		ResultVo rv=new ResultVo();
		this.stockService.insertStockDate(DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short));
		return rv;
	}
	
	/**
	 * 周均线统计
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/insertStockWeekMa")
	@ResponseBody
	public ResultVo insertStockWeekMa(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		this.stockService.insertStockWeekMa(map.get("date"));
		return rv;
	}
	
	/**
	 * 日均线统计
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/insertStockDayMa")
	@ResponseBody
	public ResultVo insertStockDayMa(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		this.stockService.insertStockDayMa(map.get("date"));
		return rv;
	}
	
	/**
	 * 批量执行均线统计
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/stockMa")
	@ResponseBody
	public ResultVo stockMa(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		int size=Integer.parseInt(map.get("size"));
		for(int i=0;i<size;i++){
			Date day=DateUtil.getAddTimeDate(DateUtil.DAY, DateUtil.stringToDate(map.get("date"),DateUtil.formatPattern_Short), i);
			System.out.println(DateUtil.dateToString(day, DateUtil.formatPattern_Short)+"日开始");
			this.stockService.insertStockDayMa(DateUtil.dateToString(day, DateUtil.formatPattern_Short));
			System.out.println(DateUtil.dateToString(day, DateUtil.formatPattern_Short)+"日结束");
			System.out.println(DateUtil.dateToString(day, DateUtil.formatPattern_Short)+"周开始");
			this.stockService.insertStockWeekMa(DateUtil.dateToString(day, DateUtil.formatPattern_Short));
			System.out.println(DateUtil.dateToString(day, DateUtil.formatPattern_Short)+"周结束");
		}
		return rv;
	}
	
	/**
	 * 找出异动信息票子
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/insertStockChange")
	@ResponseBody
	public ResultVo insertStockChange(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		this.stockService.insertStockChange(map.get("date"));
		return rv;
	}
	
	/**
	 * 找出底部开始反转的票子
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/insertStockRecover")
	@ResponseBody
	public ResultVo insertStockRecover(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		this.stockService.insertStockRecover(map.get("date"));
		return rv;
	}
}
