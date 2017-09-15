package com.xionger.qcb.service.info.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.xionger.qcb.common.util.date.DateUtil;
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
	        webclient.getOptions().setJavaScriptEnabled(false);// 2 启动JS
	        webclient.getOptions().setCssEnabled(false);// 3 禁用Css，可避免自动二次請求CSS进行渲染
	        webclient.getOptions().setRedirectEnabled(true);// 4 启动客戶端重定向
	        webclient.getOptions().setThrowExceptionOnScriptError(false);// 5 js运行错誤時，是否拋出异常
	        webclient.getOptions().setTimeout(30000);// 6 设置超时
	        webclient.getOptions().setUseInsecureSSL(true);
			List<StockInfo> siList=new ArrayList<StockInfo>();
			String date=DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short);
			String code="#code#";
			String gsxx="detailData";//公司基本信息
			StockInfo stockInfo=null;
			HtmlPage htmlpage=null;
			Document doc=null;
			Element element=null;
    		for(TradeDay tradeDay:this.list){
    			stockInfo=new StockInfo();
    			LOGGER.info("线程id{}:上市公司{}--->{}:基础信息获取开始",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    			try {
    				stockInfo=new StockInfo();
    				stockInfo.generateId();
    				stockInfo.setCode(tradeDay.getCode());
    				stockInfo.setCodeName(tradeDay.getCodeName());
    				stockInfo.setCreateDate(date);
    				htmlpage = webclient.getPage(Constants.STOCK_INFO.replace(code, tradeDay.getCode().substring(2)));
    				doc=Jsoup.parse(htmlpage.asXml());
    				LOGGER.info("线程id{}:上市公司{}--->{}:开始解析公司基本信息",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    				element=doc.getElementsByClass(gsxx).get(0).child(1).child(0).child(0);
    				ListIterator<Element> trdetailList=element.children().listIterator();
    				while(trdetailList.hasNext()){
    					Element ele=trdetailList.next();
    					if(ele.hasText() && "公司名称".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setCompanyName(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "注册地址".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setRegisterAddress(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "董事长".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setChairman(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "董秘".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setSecretary(ele.child(1).text().replaceAll(" ", ""));
    					}
    					
    					System.out.println("********************************************************************************\n\n\n");
    					System.out.println(ele.outerHtml());
    				}
    				
    				Elements mainIntro=doc.getElementsByClass("mainIntro");
    				
    				
					
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
        webclient.getOptions().setJavaScriptEnabled(false);// 2 启动JS
        webclient.getOptions().setCssEnabled(false);// 3 禁用Css，可避免自动二次請求CSS进行渲染
        webclient.getOptions().setRedirectEnabled(true);// 4 启动客戶端重定向
        webclient.getOptions().setThrowExceptionOnScriptError(false);// 5 js运行错誤時，是否拋出异常
        webclient.getOptions().setTimeout(30000);// 6 设置超时
        webclient.getOptions().setUseInsecureSSL(true);
        HtmlPage htmlpage=null;
		try {
			htmlpage = webclient.getPage("https://basic.10jqka.com.cn/mobile/002008/profilen.html");
			Document doc=Jsoup.parse(htmlpage.asXml());
			Elements detailData=doc.getElementsByClass("detailData");
			Elements mainIntro=doc.getElementsByClass("mainIntro");
			
			Element element=doc.getElementsByClass("detailData").get(0).child(1).child(0).child(0);
			ListIterator<Element> trdetailList=element.children().listIterator();
			while(trdetailList.hasNext()){
				Element ele=trdetailList.next();
				System.out.println("********************************************************************************");
				System.out.println(ele.child(0).text());
				System.out.println(ele.child(1).text());
			}
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}finally{
			webclient.close();
		}
		
	}
}
