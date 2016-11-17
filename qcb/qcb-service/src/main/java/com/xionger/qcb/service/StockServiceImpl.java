package com.xionger.qcb.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
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
import com.xionger.qcb.common.util.HolidayUtil;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.StockChangeDao;
import com.xionger.qcb.dao.mapper.StockDao;
import com.xionger.qcb.dao.mapper.StockDateDao;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.dao.mapper.StockResultDao;
import com.xionger.qcb.model.Stock;
import com.xionger.qcb.model.StockChange;
import com.xionger.qcb.model.StockDate;
import com.xionger.qcb.model.StockMa;
import com.xionger.qcb.model.StockResult;

@Service("userService")
public class StockServiceImpl implements StockService{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);
	
	@Value("${stock.csv.path}")
	private String stockCsvPath;//股票历史csv数据下载后保存本地磁盘路径
	
	@Autowired
	private StockDao stockDao;
	@Autowired
	private StockMaDao stockMaDao;
	@Autowired
	private StockResultDao stockResultDao;
	@Autowired
	private StockChangeDao stockChangeDao;
	@Autowired
	private StockDateDao stockDateDao;
	
	/**
     * 从下载的excel冲将数据导入到数据库,调用次方法必须要求该日数据excel必须存在
     * @param xlsDate
     */
    public String insertStockListByXlsdate(Date xlsDate,String path){
    	LOGGER.info("开始读取{0}日下载的股票excel数据，并保存数据库",xlsDate);
    	HSSFWorkbook wb=null;
    	POIFSFileSystem pfs=null;
    	FileInputStream fis=null;
    	String dataDate=null;
        try{
        	File file = new File(path);
        	fis=new FileInputStream(file);
        	pfs = new POIFSFileSystem(fis);  
            wb = new HSSFWorkbook(pfs);
            HSSFSheet  sheet = wb.getSheetAt(0);
            Iterator<Row> rows=sheet.rowIterator();
            Row row=null;
            int i=0;
            while(rows.hasNext()){
            	row=rows.next();
            	if(i==0){
            		dataDate=row.getCell(1).getStringCellValue();
            		if(StringUtil.isBlank(dataDate)){
            			break;
            		}else{
            			dataDate=DateUtil.dateToString(new Date(), DateUtil.YEAR)+dataDate.split(" ")[0].replace("-", "");
            			this.stockDao.deleteStockListByDataDate(dataDate);//如果这天存在数据，则先删除数据
            		}
            		rows.next();
            		i++;
            		continue;
            	}
            	
            	DecimalFormat df2 = new DecimalFormat("0.00"); 
            	DecimalFormat dfNum = new DecimalFormat("0");
            	DecimalFormat df4 = new DecimalFormat("0.0000");
            	
        		String stockCode=row.getCell(0).getStringCellValue();
        		String stockName=row.getCell(1).getStringCellValue();
        		String newPrice=df2.format(row.getCell(2).getNumericCellValue());
        		String amplitude=df4.format(row.getCell(3).getNumericCellValue());
        		String amplitudePrice=df2.format(row.getCell(4).getNumericCellValue());
        		String buyPrice=df2.format(row.getCell(5).getNumericCellValue());
        		String salePrice=df2.format(row.getCell(6).getNumericCellValue());
        		String dealVol=dfNum.format(row.getCell(7).getNumericCellValue());
        		String dealPrice=df2.format(row.getCell(8).getNumericCellValue());
        		String todayOpen=df2.format(row.getCell(9).getNumericCellValue());
        		String yeatedayClose=df2.format(row.getCell(10).getNumericCellValue());
        		String heightPrice=df2.format(row.getCell(11).getNumericCellValue());
        		String lowPrice=df2.format(row.getCell(12).getNumericCellValue());
        		
        		Stock stock=new Stock();
        		stock.generateId();
        		stock.setCode(stockCode);
        		stock.setCodeName(stockName);
        		stock.setNewPrice(new BigDecimal(newPrice));
        		stock.setAmplitude(new BigDecimal(amplitude));
        		stock.setAmplitudePrice(new BigDecimal(amplitudePrice));
        		stock.setBuyPrice(new BigDecimal(buyPrice));
        		stock.setSalePrice(new BigDecimal(salePrice));
        		stock.setDealVol(Long.parseLong(dealVol));
        		stock.setDealPrice(new BigDecimal(dealPrice));
        		stock.setTodayOpen(new BigDecimal(todayOpen));
        		stock.setYeatedayClose(new BigDecimal(yeatedayClose));
        		stock.setHeightPrice(new BigDecimal(heightPrice));
        		stock.setLowPrice(new BigDecimal(lowPrice));
        		stock.setCreateDate(dataDate);
        		stockDao.insert(stock);
                i++;
            }
        }catch(Exception e){
        	LOGGER.error(">>>>>\t"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\t 加载股票数据文件数据入库发生异常："+ ExceptionUtils.getMessage(e) + "\n" + ExceptionUtils.getStackTrace(e));
        }finally{
        	try {
        		if(fis!=null){
        			fis.close();
                }
        		if(pfs!=null){
        			pfs.close();
                }
        		if(wb!=null){
        			wb.close();
        		}
        		fis=null;
        		pfs=null;
        		wb=null;
			} catch (Exception e) {
				
			}
        }
        return dataDate;
    }
    
    /**
     * 进行均线计算
     * @param date 股票信息表的股票数据日期
     */
    public void insertStockDayMa(String date){
    	List<Stock> list=this.stockDao.selectListByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(list)){
    		this.stockMaDao.deleteByCreateDate(date);//删除均线
    		//均线和过滤适合条件的股票算法开始
    		List<Stock> stock20List=null;
    		for(Stock stock:list){
    			Map<String, String> map=new HashMap<String, String>();
    			map.put("code", stock.getCode());
    			map.put("createDate", date);
    			stock20List=this.stockDao.select20ListByCodeAndCreateDateOrderCreateDateDesc(map);//必须结果集倒序
    			//均线计算
    			BigDecimal day5=new BigDecimal("0.00");
    			BigDecimal day10=new BigDecimal("0.00");
    			BigDecimal day20=new BigDecimal("0.00");
    			BigDecimal maSum=new BigDecimal("0.00");
    			
    			if(CollectionUtil.isNotEmpty(stock20List)){
    				for(int i=0;i<stock20List.size();i++){
    					maSum=maSum.add(stock20List.get(i).getNewPrice());
    					//均线区间统计
    					if(stock20List.size()>10 && stock20List.size()<=20){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
    						if(i==9){
            					day10=maSum.divide(new BigDecimal("10.00"),2);
            				}
    						if(i==(stock20List.size()-1)){
    							day20=maSum.divide(new BigDecimal((stock20List.size()+"")),2);
            				}
    					}else if(stock20List.size()>5 && stock20List.size()<=10){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
    						if(i==(stock20List.size()-1)){
    							day10=maSum.divide(new BigDecimal((stock20List.size()+"")),2);
            				}
    					}else{
    						if(i==(stock20List.size()-1)){
    							day5=maSum.divide(new BigDecimal((stock20List.size()+"")),2);
            				}
    					}
        			}
    			}
    			
    			//保存均线数据
    			StockMa stockMa=new StockMa();
    			stockMa.generateId();
    			stockMa.setCode(stock.getCode());
    			stockMa.setCodename(stock.getCodeName());
    			stockMa.setCreateDate(date);//数据日期
    			stockMa.setDay5(day5);
    			stockMa.setDay10(day10);
    			stockMa.setDay20(day20);
    			stockMaDao.insertSelective(stockMa);
    		}
    	}
    }
    
    /**
     * 周均线统计,需要放在日均线之后执行
     * @param date 自然日
     */
    public void insertStockWeekMa(String date){
    	//查询改天的均线数据
    	List<StockMa> stockMaList= this.stockMaDao.selectByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(stockMaList)){
    		for(StockMa maObj:stockMaList){
    			Stock stock=null;//当date不是本周交易最后一天时，该值指向当日数据
    			Map<String, String> map=new HashMap<String, String>();
    			map.put("code", maObj.getCode());
    			map.put("createDate", date);
    			List<Stock> stockList= this.stockDao.select20WeekListByCodeAndCreateDateOrderCreateDateDesc(map);
    			if(CollectionUtil.isNotEmpty(stockList)){
    				if(!stockList.get(0).getCreateDate().equals(date)){
    					stock=new Stock();
    					stock.setCode(maObj.getCode());
    					stock.setCreateDate(DateUtil.dateToString(DateUtil.getAddTimeDate(DateUtil.DAY, DateUtil.stringToDate(date,DateUtil.formatPattern_Short), -1), DateUtil.formatPattern_Short));
    					stock =this.stockDao.selectListByCodeAndCreateDateAsc(stock).get(0);
    				}
    			}else{
    				continue;
    			}
    			BigDecimal week5=new BigDecimal("0.00");
    			BigDecimal week10=new BigDecimal("0.00");
    			BigDecimal week20=new BigDecimal("0.00");
    			BigDecimal maSum=new BigDecimal("0.00");
    			if(stock!=null){
    				stockList.remove(stockList.size()-1);
    				stockList.add(stock);
    				Collections.sort(stockList, new Comparator<Stock>() {
    					public int compare(Stock o1, Stock o2) {
    						int ret = 0;
    						try {
    							Date d1 = DateUtil.stringToDate(o1.getCreateDate(), DateUtil.formatPattern_Short);
    							Date d2 = DateUtil.stringToDate(o2.getCreateDate(), DateUtil.formatPattern_Short);
    							if (d1.before(d2)) {
    								ret = 1;
    							} else {
    								ret = -1;
    							}
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
    						return ret;
    					}
    				});
    			}
    			for(int i=0;i<stockList.size();i++){
					maSum=maSum.add(stockList.get(i).getNewPrice());
					//均线区间统计
					if(stockList.size()>10 && stockList.size()<=20){
						if(i==4){
							week5=maSum.divide(new BigDecimal("5.00"),2);
        				}
						if(i==9){
							week10=maSum.divide(new BigDecimal("10.00"),2);
        				}
						if(i==(stockList.size()-1)){
							week20=maSum.divide(new BigDecimal((stockList.size()+"")),2);
        				}
					}else if(stockList.size()>5 && stockList.size()<=10){
						if(i==4){
							week5=maSum.divide(new BigDecimal("5.00"),2);
        				}
						if(i==(stockList.size()-1)){
							week10=maSum.divide(new BigDecimal((stockList.size()+"")),2);
        				}
					}else{
						if(i==(stockList.size()-1)){
							week5=maSum.divide(new BigDecimal((stockList.size()+"")),2);
        				}
					}
    			}
    			maObj.setWeek5(week5);
    			maObj.setWeek10(week10);
    			maObj.setWeek20(week20);
    			this.stockMaDao.updateByPrimaryKeySelective(maObj);
    		}
    	}
    }
    
    /**
     * 保存该日期的股票数据存在异动的数据 02:涨停；
     * 							 03：跳高；
     * 							 04：回缺;
     * 							 05单阳不破(未开发);
     * 							 06常规战法庄家吸筹上传散户，需要注意仓位（抓去短中线，未开发）
     * 							 07中线上升趋势，日线均线支撑15日内有涨停或跳空5点之上最好，创新高一定要放量，周线要过滤创新高但上影大于红柱，或大阴线（未开发）
     * @param date 股票数据日期
     */
    public void insertStockChange(String date){
    	//先判断当天是否存在数据
    	List<Stock> stockList=this.stockDao.selectListByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(stockList)){//如果存在数据
    		
    		//如果统计异常的日期存在数据则先删除
    		stockChangeDao.deleteByStockDate(date);
    		
    		StockChange stockChange=null;
    		for(Stock stock:stockList){
    			stockChange=new StockChange();
    			stockChange.generateId();
    			stockChange.setCode(stock.getCode());
    			stockChange.setCodename(stock.getCodeName());
    			stockChange.setStockdate(stock.getCreateDate());
    			stockChange.setPrice(stock.getNewPrice());
    			
    			//03跳高
    			if(stock.getTodayOpen().compareTo(stock.getYeatedayClose())==1 
    					&& stock.getNewPrice().compareTo(stock.getYeatedayClose())==1
    					&& stock.getAmplitude().compareTo(new BigDecimal("0.0500"))>=0){
    				Stock filterStock=new Stock();
    				filterStock.setCode(stock.getCode());
    				filterStock.setCreateDate(date);
    				filterStock=this.stockDao.selectByCodeAndBeforCreateDateDescOne(filterStock);
    				if(filterStock!=null){//此处逻辑最好保留，因为表示特别强势
    					if(stock.getTodayOpen().compareTo(filterStock.getTodayOpen())==1){
        					stockChange.setChangetype("03");
            				stockChange.setPrice(stock.getYeatedayClose());
        				}
    				}
    				
    			}
    			
    			//02判断涨停
    			if(stock.getAmplitude().compareTo(new BigDecimal("0.0995"))>=0){
    				stockChange.setChangetype("02");
    				stockChange.setPrice(stock.getNewPrice());
    			}
    			
    			//04跳空低开，后面监控缺口回补
    			if(stock.getTodayOpen().compareTo(stock.getYeatedayClose())==-1 
    					&& stock.getNewPrice().compareTo(stock.getYeatedayClose())==-1
    					&& stock.getAmplitude().compareTo(new BigDecimal("-0.0300"))<=0){//比较重要低开缺口，此处的监控对象比较合理，但具体的监控过程逻辑需要优化，目的抓去大牛股，一定注意需要注意量能，压力、支撑
    				stockChange.setChangetype("04");
    				stockChange.setPrice(stock.getYeatedayClose());
    			}
    			
    			//保存需要监控的对象
    			if(StringUtil.isNotBlank(stockChange.getChangetype())){
    				this.stockChangeDao.deleteByCodeAndChangeType(stockChange);//先根据code和type删除之前已经产生的监控对象
    				this.stockChangeDao.insertSelective(stockChange);//保存新的监控对象
    			}
    			
    		}
    	}
    }
    
    
    /**
     * 对需要监控的股票进行监听
     * 
     */
    public void updateStockListenerChange(){
    	List<StockChange> needList=this.stockChangeDao.selectChangeStockList();//需要异常监控的数据
    	if(CollectionUtil.isNotEmpty(needList)){
    		String date=DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short);
    		this.stockResultDao.deleteByCreateDate(date,"02");//删除已经过滤出来的结果集
    		this.stockResultDao.deleteByCreateDate(date,"03");//删除已经过滤出来的结果集
    		this.stockResultDao.deleteByCreateDate(date,"04");//删除已经过滤出来的结果集
    		List<Stock> list=null;
    		for(StockChange obj:needList){
    			Stock stock=new Stock();
    			stock.setCode(obj.getCode());
    			stock.setCreateDate(obj.getStockdate());
    			list=this.stockDao.selectListByCodeAndCreateDateAsc(stock);
    			if(CollectionUtil.isNotEmpty(list)){
    				stock=list.get(list.size()-1);//将最后一条记录赋值最新的股票信息对象
    				if(list.size()>5){//超过监控期限，释放监控对象
    					this.stockChangeDao.deleteByPrimaryKey(obj.getId());
    					continue;
    				}
    				if("02".equals(obj.getChangetype())||"03".equals(obj.getChangetype())){//02:涨停；03：跳高；
    					int i=1;//当前循环记录数
    					for(Stock afterStock:list){
    						if(afterStock.getNewPrice().compareTo(obj.getPrice())<=0){
    							i=0;//表示删除该异动数据
    							if(new Date().getTime()>DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short)+"153000", DateUtil.formatPattern_rand).getTime()){
    								this.stockChangeDao.deleteByPrimaryKey(obj.getId());
    							}
    							break;
    						}else{
    							setStockChangeForUpdate(obj, afterStock, i);
    						}
    						i++;
    					}
    					if(i>0){
    						this.stockChangeDao.updateByPrimaryKeySelective(obj);
    						if(i==6){
    							StockResult stockResult=new StockResult();
    	    					stockResult.generateId();
    	    					stockResult.setChanneltype(obj.getChangetype());
    	    					stockResult.setCode(stock.getCode());
    	    					stockResult.setCodename(stock.getCodeName());
    	    					stockResult.setCreateDate(date);//因为基本每天跑，所以这里是数据日期
    	    					stockResult.setHeightprice(stock.getHeightPrice());
    	    					stockResult.setNewprice(stock.getNewPrice());
    	    					this.stockResultDao.insertSelective(stockResult);
    						}
    					}
    					continue;
    				}else if("04".equals(obj.getChangetype())){//04：回缺;
    					int i=1;//当前循环记录数
    					for(Stock afterStock:list){
    						if(afterStock.getNewPrice().compareTo(obj.getPrice())<=0){
    							setStockChangeForUpdate(obj, afterStock, i);
    						}else{
    							Stock filterStock=new Stock();
    		    				filterStock.setCode(stock.getCode());
    		    				filterStock.setCreateDate(afterStock.getCreateDate());
    		    				filterStock=this.stockDao.selectByCodeAndBeforCreateDateDescOne(filterStock);
    		    				if(filterStock!=null && afterStock.getDealVol()>filterStock.getDealVol()){
    		    					i=0;
    		    					break;
    		    				}else{
    		    					setStockChangeForUpdate(obj, afterStock, i);
    		    				}
    						}
    						i++;
    					}
    					if(i==0){
    						if(new Date().getTime()>DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.formatPattern_Short)+"153000", DateUtil.formatPattern_rand).getTime()){
        						this.stockChangeDao.deleteByPrimaryKey(obj.getId());
    						}
    						StockResult stockResult=new StockResult();
	    					stockResult.generateId();
	    					stockResult.setChanneltype(obj.getChangetype());
	    					stockResult.setCode(stock.getCode());
	    					stockResult.setCodename(stock.getCodeName());
	    					stockResult.setCreateDate(date);//因为基本每天跑，所以这里是数据日期
	    					stockResult.setHeightprice(stock.getHeightPrice());
	    					stockResult.setNewprice(stock.getNewPrice());
	    					this.stockResultDao.insertSelective(stockResult);
    					}else{
    						this.stockChangeDao.updateByPrimaryKeySelective(obj);
    					}
    					continue;
    				}
    			}
    		}
    		
    	}
    }
    
    /**
     * 给异常股票设置值
     * @param obj
     * @param afterStock
     * @param index
     */
    private void setStockChangeForUpdate(StockChange obj,Stock afterStock,int index){
    	if(index==1){
			obj.setPrice1(afterStock.getNewPrice());
			obj.setStockdate1(afterStock.getCreateDate());
		}
		if(index==2){
			obj.setPrice2(afterStock.getNewPrice());
			obj.setStockdate2(afterStock.getCreateDate());
		}
		if(index==3){
			obj.setPrice3(afterStock.getNewPrice());
			obj.setStockdate3(afterStock.getCreateDate());								
		}
		if(index==4){
			obj.setPrice4(afterStock.getNewPrice());
			obj.setStockdate4(afterStock.getCreateDate());
		}
		if(index==5){
			obj.setPrice5(afterStock.getNewPrice());
			obj.setStockdate5(afterStock.getCreateDate());
		}
    }
    
    
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
    		Thread.sleep(1000);
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
    
    /**
     * 扫描下载csv目录下的txt文件并进行股票历史数据保存
     */
    public void insertScanStockTxt(){
    	String path="d:/stock_txt/";
    	File dir = new File(path);
    	File[] files=dir.listFiles();
    	for(int i=0; i<files.length; i++){
    		InputStream in=null;
    		InputStreamReader read = null;
    		BufferedReader bufferedReader =null;
    		try {
				in = new FileInputStream(files[i]);
				read = new InputStreamReader(in,"GBK");//考虑到编码格式
                bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    if(StringUtil.isNotBlank(lineTxt)&&!lineTxt.startsWith("日期")){
                    	String[] values=lineTxt.split(",");
                    	System.out.println("#################:\t"+values[0]+"\t"+values[1]+"\t"+values[2]);
                    	Stock stock=new Stock();
                    	stock.generateId();
                    	stock.setAmplitude(new BigDecimal(values[9].replaceAll(" ", "").replace("None", "0")).divide(new BigDecimal("100"), 4));
                    	stock.setAmplitudePrice(new BigDecimal(values[8].replaceAll(" ", "").replace("None", "0")));
                    	stock.setBuyPrice(null);
                    	values[1]=values[1].replace("'", "");
                    	stock.setCode(values[1].startsWith("6")?("sh"+values[1]):("sz"+values[1]));
                    	stock.setCodeName(values[2]);
                    	stock.setCreateDate(values[0].replaceAll("-", ""));
                    	stock.setDealPrice(new BigDecimal(values[12].replace("None", "0").trim()));
                    	stock.setDealVol(Long.parseLong(Math.round(Long.parseLong(values[11].replace("None", "0").trim())/100)+""));//手
                    	stock.setHeightPrice(new BigDecimal(values[4].replace("None", "0").trim()));
                    	stock.setLowPrice(new BigDecimal(values[5].replace("None", "0").trim()));
                    	stock.setNewPrice(new BigDecimal(values[3].replace("None", "0").trim()));
                    	stock.setSalePrice(null);
                    	stock.setTodayOpen(new BigDecimal(values[6].replace("None", "0").trim()));
                    	stock.setYeatedayClose(new BigDecimal(values[7].replace("None", "0").trim()));
                    	this.stockDao.insertSelective(stock);
                    }
                }
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(in!=null){
						in.close();
					}
					if(read!=null){
						read.close();
					}
					if(bufferedReader!=null){
						bufferedReader.close();
					}
					in=null;
	                read=null;
	                bufferedReader=null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
    	}
    }

    /**
     * 股票日期保存
     * @param date yyyyMMdd格式
     */
    public void insertStockDate(String date){
    	String startDate="20160101";
    	StockDate stockDate= stockDateDao.selectByIstrueEndWeekDay(date);
    	if(stockDate!=null){
    		startDate=stockDate.getStockDate();
    	}
    	long days=DateUtil.betweenDays(DateUtil.stringToDate(startDate,DateUtil.formatPattern_Short), DateUtil.stringToDate(date,DateUtil.formatPattern_Short));
    	int flag=-1;//锁标识，星期天解锁，星期六加锁，星期天记录上周的起始时间
    	String startTime="";//周的起始日期
    	for(int i=0;i<days;i++){
    		StockDate sd=new StockDate();
    		sd.generateId();
    		sd.setIsTradeDate("99");
			sd.setIsStartWeekDay("99");
			sd.setIsEndWeekDay("99");
    		Date day=DateUtil.getAddTimeDate(DateUtil.DAY, DateUtil.stringToDate(date,DateUtil.formatPattern_Short), (i*-1));
    		sd.setStockDate(DateUtil.dateToString(day, DateUtil.formatPattern_Short));
    		if("星期六".equals(DateUtil.getWeek(day))){
    			flag=0;
    		}
    		if("星期日".equals(DateUtil.getWeek(day))){
    			if(flag==1){
    				StockDate obj=this.stockDateDao.selectByStockDate(startTime);
    				obj.setIsStartWeekDay("00");
    				this.stockDateDao.updateByPrimaryKeySelective(obj);
    			}
    			flag=-1;
    		}
    		if("0".equals(HolidayUtil.request(DateUtil.dateToString(day,DateUtil.formatPattern_Short)).replace("\r\n",""))
    				&&!"星期日".equals(DateUtil.getWeek(day))&&!"星期六".equals(DateUtil.getWeek(day))){
    			sd.setIsTradeDate("00");
    			if(flag==0){
    				flag=1;
    				sd.setIsEndWeekDay("00");
    			}
    			startTime=DateUtil.dateToString(day, DateUtil.formatPattern_Short);
    		}
    		this.stockDateDao.deleteByStockDate(sd.getStockDate());
    		this.stockDateDao.insertSelective(sd);
    	}
    }
    
    
}
