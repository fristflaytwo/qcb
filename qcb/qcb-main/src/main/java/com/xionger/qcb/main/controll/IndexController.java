package com.xionger.qcb.main.controll;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xionger.qcb.common.util.date.DateUtil;
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
	 * app下载页
	 */
	@RequestMapping("/test")
	public ResultVo test(){
		String path="d:/stock_xls/20161013.xls";
		stockService.insertStockListByXlsdate(new Date(), path);
		return new ResultVo();
	}
	
}
