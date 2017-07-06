package com.xionger.qcb.console.controll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.common.util.json.JsonUtil;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.StockService;
import com.xionger.qcb.service.StockTimerService;



/**
 * 首页  index
 * @author    leo
 * @date      2016-7-22 上午10:41:33
 * @version   v1.0
 */

@Controller
public class IndexController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Value("${stock.xls.path}")
	private String stockXlsPath;//股票数据文件存放路径
	
	@Autowired
	private StockTimerService stockTimerService;
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
		String path=stockXlsPath+fileName+".xls";
		stockTimerService.insertStockListByXlsdate(new Date(), path);
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	@RequestMapping("/downLoadHisData")
	@ResponseBody
	public ResultVo downLoadHisData(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		this.stockService.downLoadHisData(map.get("date"),map.get("startTime"),map.get("endTime"));
		return new ResultVo();
	}
	
	
	
	/**
	 * 日均线统计
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/insertStockDayMa")
	@ResponseBody
	public ResultVo insertStockDayMa(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		this.stockTimerService.insertStockDayMa(map.get("date"));
		return rv;
	}
	
	/**
	 * 查询个股的数据
	 * @param data
	 * @param req
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryStockByCode")
	@ResponseBody
	public ResultVo queryStockByCode(@RequestBody String data, HttpServletRequest req, HttpServletResponse res){
		Map<String,String> map=(Map<String, String>) JsonUtil.jsonToMap(data);
		ResultVo rv=new ResultVo();
		List<String> list=new ArrayList<String>();
		if(StringUtil.isBlank(map.get("code"))){
			rv.setMsg("codes值不能为空");
			return rv;
		}
		String[] codes=map.get("code").split(",");
		
		for(String code:codes){
			String info=HttpClientUtils.doGet("http://hq.sinajs.cn/?list="+code, Constants.UTF8);
			String [] contents=info.split(",");
			list.add(contents[0].replace("var hq_str_", "").replaceAll("\"", "")+" 今开:"+contents[1]+" 昨收:"+contents[2] +" 现在:"+contents[3] +" 今日最高价:"+contents[4] +" 今日最低价:"+contents[5]);
		}
		rv.setList(list);
		return rv;
	}
	
	/**
	 * 加载本次保存的excel数据到股票信息表
	 */
	@RequestMapping("/test")
	@ResponseBody
	public ResultVo test(){
		stockTimerService.insertStockExpand("20170706");
		return new ResultVo();
	}
}
