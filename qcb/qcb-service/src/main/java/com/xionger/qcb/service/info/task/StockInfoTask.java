package com.xionger.qcb.service.info.task;

import java.math.BigDecimal;
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
import com.xionger.qcb.common.util.string.StringUtil;
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
			String gsyw="mainIntro";//公司主营业务
			StockInfo stockInfo=null;
			HtmlPage htmlpage=null;
			Document doc=null;
			Element element=null;
    		for(TradeDay tradeDay:this.list){
    			stockInfo=new StockInfo();
    			LOGGER.info("线程id{}:上市公司{}--->{}:基础信息获取开始",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    			try {
    				Thread.sleep(500);
    				stockInfo=new StockInfo();
    				stockInfo.generateId();
    				stockInfo.setCode(tradeDay.getCode());
    				stockInfo.setCodeName(tradeDay.getCodeName());
    				stockInfo.setCreateDate(date);
    				htmlpage = webclient.getPage(Constants.STOCK_INFO.replace(code, tradeDay.getCode().substring(2)));
    				doc=Jsoup.parse(htmlpage.asXml());
    				doc=Jsoup.parse(doc.toString().replaceAll("&nbsp;", ""));
    				LOGGER.info("线程id{}:上市公司{}--->{}:开始解析公司基本信息",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    				element=doc.getElementsByClass(gsxx)!=null && doc.getElementsByClass(gsxx).size()>0?doc.getElementsByClass(gsxx).get(0).child(1).child(0).child(0):null;
    				if(element==null) continue;
    				ListIterator<Element> elementList=element.children().listIterator();
    				while(elementList.hasNext()){
    					Element ele=elementList.next();
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
    					if(ele.hasText() && "主营业务".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setMainBusiness(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "控股股东".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setControlPartner(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "实际控制人".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setFactControl(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "最终控制人".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setFinalControl(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "上市日期".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setMarketDate(ele.child(1).text().replaceAll(" ", "").replace("-", ""));
    					}
    					if(ele.hasText() && ele.child(0).text().replaceAll(" ", "").indexOf("发行价")==0){
    						String fxj=ele.child(1).text().replaceAll(" ", "").split("/")[0].replace("元", "").replaceAll("-", "");
    						stockInfo.setLssuePrice(new BigDecimal(StringUtil.isNotBlank(fxj)?fxj:"0"));
    					}
    					if(ele.hasText() && ele.child(0).text().replaceAll(" ", "").indexOf("市盈率")>0){
    						String syl=ele.child(1).text().replaceAll(" ", "").split("/")[1].replace("倍", "").replaceAll("-", "");
    						stockInfo.setLssuePe(new BigDecimal(StringUtil.isNotBlank(syl)?syl:"0"));
    					}
    				}
    				LOGGER.info("线程id{}:上市公司{}--->{}:开始解析公司主营业务",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    				element=doc.getElementsByClass(gsyw)!=null && doc.getElementsByClass(gsyw).size()>0? doc.getElementsByClass(gsyw).get(0).child(1).child(0).child(0):null;
    				if(element==null) continue;
    				elementList=element.children().listIterator();
    				while(elementList.hasNext()){
    					Element ele=elementList.next();
    					if(ele.hasText() && "产品类型".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setProductType(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "产品名称".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setProductName(ele.child(1).text().replaceAll(" ", ""));
    					}
    					if(ele.hasText() && "经营范围".equals(ele.child(0).text().replaceAll(" ", ""))){
    						stockInfo.setBusinessScope(ele.child(1).text().replaceAll(" ", ""));
    					}
    				}
    				siList.add(stockInfo);
    				LOGGER.info("线程id{}:上市公司{}--->{}:解析基础信息完毕。",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName());
    				LOGGER.info("线程id{}:上市公司{}--->{},数据对象{}",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),stockInfo.toString());
    			} catch (Exception e) {
					LOGGER.error("线程id{}:上市公司{}--->{}:基础信息获取失败.",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),e);
					LOGGER.error("线程id{}:上市公司{}--->{}:失败的数据对象：",Thread.currentThread().getName(),tradeDay.getCode(),tradeDay.getCodeName(),stockInfo!=null?stockInfo.toString():"数据组装时发生异常");
				}
    		}
    		webclient.close();
			if(CollectionUtil.isNotEmpty(siList)){
				stockInfoDao.inserts(siList);
			}
		}
	}
	
}
