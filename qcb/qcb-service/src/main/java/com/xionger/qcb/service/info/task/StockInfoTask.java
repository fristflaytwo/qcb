package com.xionger.qcb.service.info.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.dao.mapper.StockInfoDao;
import com.xionger.qcb.model.StockInfo;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.service.info.StockInfoService;

public class StockInfoTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockInfoService.class);
	
	private List<TradeDay> list=new ArrayList<TradeDay>();
	private StockInfoDao stockInfoDao;
	
	public StockInfoTask(List<TradeDay> list,StockInfoDao stockInfoDao){
		this.list=list;
		this.stockInfoDao=stockInfoDao;
	}
	
	@Override
	public void run() {
		if(CollectionUtil.isNotEmpty(this.list)){
			WebClient webclient = new WebClient();
			webclient.getOptions().setCssEnabled(false);  
            webclient.getOptions().setJavaScriptEnabled(false);
			List<StockInfo> siList=new ArrayList<StockInfo>();
			String code="#code#";
			StockInfo stockInfo=null;
    		for(TradeDay tradeDay:this.list){
    			stockInfo=new StockInfo();
    			LOGGER.info("线程id{}:上市公司{}--->{}:基础信息获取开始",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    			try {
					HtmlPage htmlpage = webclient.getPage(Constants.STOCK_INFO.replace(code, tradeDay.getCode().substring(2)));
					DomElement domElement= htmlpage.getElementById("cOuter");
		            DomElement stockHtmlData=domElement.getNextElementSibling();
		            DomNodeList<HtmlElement> nodeList=stockHtmlData.getElementsByTagName("tr");
		            //如果停牌或非工作日，则不需要保存该数据
		            if("0.00".equals(nodeList.get(0).getElementsByTagName("td").get(1).asText().replaceAll(" ", ""))){
		        		//return null;
		        	}
				} catch (FailingHttpStatusCodeException | IOException e) {
					LOGGER.info("线程id{}:上市公司{}--->{}:基础信息获取失败.",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),e);
				}  
    		}
			if(CollectionUtil.isNotEmpty(siList)){
				stockInfoDao.inserts(siList);
			}
		}
	}
	
	
	public static void main(String[] args) {
		WebClient webclient = new WebClient();
		webclient.getOptions().setCssEnabled(false);  
        webclient.getOptions().setJavaScriptEnabled(false);
        webclient.getOptions().setUseInsecureSSL(true); 
        HtmlPage htmlpage=null;
		try {
			htmlpage = webclient.getPage("https://basic.10jqka.com.cn/mobile/002008/profilen.html");
			DomNodeList domNodeList=htmlpage.getElementsByTagName("div");
			HtmlDivision obj=(HtmlDivision) domNodeList.get(3);
			System.out.println(obj.asText());
			System.out.println("************************");
			System.out.println(obj.asXml());

		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}finally{
			webclient.close();
		}
		
	}
}
