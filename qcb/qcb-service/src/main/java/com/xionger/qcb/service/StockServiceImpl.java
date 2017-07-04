package com.xionger.qcb.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.dao.mapper.StockDao;
import com.xionger.qcb.model.Stock;

@Service("stockService")
public class StockServiceImpl implements StockService{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);
	
	@Value("${stock.csv.path}")
	private String stockCsvPath;//股票历史csv数据下载后保存本地磁盘路径
	
	@Autowired
	private StockDao stockDao;
	
    
    /**
     * 插入指定股票代码从start到end日期的数据信息,此方式比较慢
     * @param date 最新股票数据日期
     * @param start 需要时分秒yyyy-MM-dd HH:mm:ss
     * @param end 需要时分秒yyyy-MM-dd HH:mm:ss
     */
    public void insertHistoryStock(String date,String start,String end){
    	List<Stock> stockList=this.stockDao.selectListByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(stockList)){
    		// 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了  
            WebClient webclient = new WebClient();  
            String subTime=DateUtil.timeSubtract(start, end);
            int subDay=Integer.parseInt(subTime.split("_")[0]);
            Map<String, String> map =new HashMap<String, String>();
            map.put("start", (start.split(" ")[0]).replaceAll("-", ""));
            map.put("end", (end.split(" ")[0]).replaceAll("-", ""));
            this.stockDao.deleteStockListByStartAndEnd(map);
            Stock obj=null;
    		for(Stock stock:stockList){
    			for(int i=0;i<=subDay;i++){
    				obj=getHistoryStockInfo(webclient,stock.getCode(), DateUtil.dateToString(DateUtil.getAddTimeDate(DateUtil.DAY, DateUtil.getDate(start, DateUtil.formatPattern_14), i)));
    				if(obj!=null&&obj.getNewPrice().compareTo(new BigDecimal("0.00"))==1){
    					this.stockDao.insertSelective(obj);
    				}
    			}
        	}
    		webclient.closeAllWindows();
    	}
    	
    }
    
    
    /**
     * 利用爬虫爬去新浪财经的股票历史数据
     * @param code
     * @param date
     * @return
     */
    private Stock getHistoryStockInfo(WebClient webclient,String code,String date){
    	Stock stock=null;
    	try {
    		Thread.sleep(1000);//防止被拉黑名单
    		stock=new Stock();
    		String[] times=date.split("-");
        	if(times[1].startsWith("0")){
        		times[1]=times[1].replace("0", "");
        	}
        	if(times[2].startsWith("0")){
        		times[2]=times[1].replace("0", "");
        	}
        	String url=Constants.STOCK_HISTORY_ONES_PATH+"symbol="+code+"&date="+times[0]+"-"+times[1]+"-"+times[2];
          
            // 这里是配置一下不加载css和javaScript,配置起来很简单，是不是  
            webclient.getOptions().setCssEnabled(false);  
            webclient.getOptions().setJavaScriptEnabled(false);  
            
            
            // 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可  
            HtmlPage htmlpage = webclient.getPage(url);  
            DomElement domElement= htmlpage.getElementById("cOuter");
            DomElement stockHtmlData=domElement.getNextElementSibling();
            DomNodeList<HtmlElement> nodeList=stockHtmlData.getElementsByTagName("tr");
            //如果停牌或非工作日，则不需要保存该数据
            if("0.00".equals(nodeList.get(0).getElementsByTagName("td").get(1).asText().replaceAll(" ", ""))){
        		return null;
        	}
            stock.generateId();
            stock.setCreateDate(date.replaceAll("-", ""));
            stock.setCode(code);
           
            for(HtmlElement node:nodeList){
            	List<HtmlElement> tdList=node.getHtmlElementsByTagName("td");
            	if("收盘价:".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setNewPrice(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "")));
            	}
            	if("涨跌幅:".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setAmplitude(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "").replace("%", "")).divide(new BigDecimal("100"), 4));
            	}
            	if("前收价:".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setYeatedayClose(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "")));
            	}
            	if("开盘价:".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setTodayOpen(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "")));
            	}
            	if("最高价:".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setHeightPrice(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "")));
            	}
            	if("最低价:".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setLowPrice(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "")));
            	}
            	if("成交量(手):".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setDealVol(Long.parseLong((tdList.get(1).asText().replaceAll(" ", "").split("\\."))[0]));
            	}
            	if("成交额(千元):".equals(tdList.get(0).asText().replaceAll(" ", ""))){
            		stock.setDealPrice(new BigDecimal(tdList.get(1).asText().replaceAll(" ", "")));
            	}
            }
            return stock;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 批量下载历史股票数据信息
     * @param date 指定参考日期的股票数据
     * @param startTime 起始日期
     * @param endTime 结束日期
     */
    public void downLoadHisData(String date,String startTime,String endTime){
    	List<Stock> stockList=this.stockDao.selectListByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(stockList)){
    		for(Stock stock:stockList){
    			downLoadHisDataByCodeAndStartTimeAndEndTime(stock.getCode(), startTime, endTime);
    		}
    	}
    }
    
    /**
     * 下载该股票在该时间段内的历史数据
     * @param code
     * @param startTime
     * @param endTime
     */
    private void downLoadHisDataByCodeAndStartTimeAndEndTime(String code,String startTime,String endTime){
    	//先下载数据到本地磁盘
		BufferedOutputStream bw = null;
		String path=stockCsvPath+code+".csv";
		// 创建文件对象
		File f = new File(path);
		// 创建文件路径
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
		try {
			String stockCode="";
			if(code.startsWith("sz")){
				stockCode=code.replaceAll("sz", "1");
			}else{
				stockCode=code.replaceAll("sh", "0");
			}
			String url=Constants.STOCK_CSV_DOWNLOAD_HISTORY_PATH+"code="+stockCode+"&start="+startTime.replaceAll("-", "")+"&end="+endTime.replaceAll("-", "");
			byte[] result=HttpClientUtils.doGetReturnBytes(url, Constants.UTF8);
			// 写入文件
			bw = new BufferedOutputStream(new FileOutputStream(path));
			bw.write(result);
		} catch (Exception e) {
			LOGGER.error("下载"+code+"历史股票信息数据异常",e);
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception e) {
			}
			bw=null;
		}
    }
    
    
}
