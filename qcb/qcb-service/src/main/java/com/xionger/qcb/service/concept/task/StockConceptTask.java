package com.xionger.qcb.service.concept.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.dao.mapper.StockConceptDao;
import com.xionger.qcb.model.StockConcept;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.service.info.StockInfoService;

public class StockConceptTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockInfoService.class);
	
	private List<TradeDay> list=new ArrayList<TradeDay>();
	private StockConceptDao stockConceptDao;
	
	public StockConceptTask(List<TradeDay> list,StockConceptDao stockConceptDao){
		this.list=list;
		this.stockConceptDao=stockConceptDao;
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
			List<StockConcept> siList=new ArrayList<StockConcept>();
			String date=DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short);
			String code="#code#";
			String gnxx="gn-tab-cont";//公司概念信息
			String sNull1=" ";
			String sNull0="";
			String gnxxSpilt="#";//概念分割符
			StockConcept stockConcept=null;
			HtmlPage htmlpage=null;
			Document doc=null;
			Element element=null;
			StringBuffer stockGnxx=null;
    		for(TradeDay tradeDay:this.list){
    			LOGGER.info("#--->线程id{}:上市公司{}--->{}:概念信息获取开始",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    			try {
    				stockGnxx=new StringBuffer("");
    				Thread.sleep(500);
    				stockConcept=new StockConcept();
    				stockConcept.generateId();
    				stockConcept.setCode(tradeDay.getCode());
    				stockConcept.setCreateDate(date);
    				htmlpage = webclient.getPage(Constants.STOCK_CONCEPT.replace(code, tradeDay.getCode().substring(2)));
    				doc=Jsoup.parse(htmlpage.asXml());
    				doc=Jsoup.parse(doc.toString().replaceAll("&nbsp;", sNull0));
    				LOGGER.info("#--->线程id{}:上市公司{}--->{}:开始解析公司概念信息",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    				element=doc.getElementsByClass(gnxx)!=null && doc.getElementsByClass(gnxx).size()>0?doc.getElementsByClass(gnxx).get(0):null;
    				if(element==null) continue;
    				ListIterator<Element> elementList=element.children().listIterator();
    				while(elementList.hasNext()){
    					Element ele=elementList.next();
    					stockGnxx.append(gnxxSpilt).append(ele.child(0).text().replaceAll(sNull1, sNull0));
    				}
    				stockConcept.setConceptName(stockGnxx.toString());
    				siList.add(stockConcept);
    				LOGGER.info("#--->线程id{}:上市公司{}--->{}:解析概念信息完毕。",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    				LOGGER.info("#--->线程id{}:上市公司{}--->{},概念数据对象{}",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),stockConcept.toString());
    			} catch (Exception e) {
					LOGGER.error("#--->线程id{}:上市公司{}--->{}:概念信息获取失败.",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),e);
					LOGGER.error("#--->线程id{}:上市公司{}--->{}:概念失败的数据对象：",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),stockConcept!=null?stockConcept.toString():"数据组装时发生异常");
				}
    		}
    		webclient.close();
			if(CollectionUtil.isNotEmpty(siList)){
				stockConceptDao.inserts(siList);
			}
		}
	}
	
}
