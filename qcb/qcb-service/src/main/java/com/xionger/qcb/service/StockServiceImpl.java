package com.xionger.qcb.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.StockChangeDao;
import com.xionger.qcb.dao.mapper.StockDao;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.dao.mapper.StockResultDao;
import com.xionger.qcb.model.Stock;
import com.xionger.qcb.model.StockChange;
import com.xionger.qcb.model.StockMa;
import com.xionger.qcb.model.StockResult;

@Service("userService")
public class StockServiceImpl implements StockService{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);
	@Autowired
	private StockDao stockDao;
	@Autowired
	private StockMaDao stockMaDao;
	@Autowired
	private StockResultDao stockResultDao;
	@Autowired
	private StockChangeDao stockChangeDao;
	
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
        	LOGGER.error(">>>>>\t"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\t 下载渠道类别模板文件是发生异常："+ ExceptionUtils.getMessage(e) + "\n" + ExceptionUtils.getStackTrace(e));
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
     * 保存股票结果集，并进行均线统计，Channel01：量比昨天大，收盘大于昨天最高，（收盘-开盘）> (最高-收盘)，当前>5日均线
     * @param date 股票信息表的股票数据日期
     */
    public void insertStockResultListByChannel01(String date){
    	List<Stock> list=this.stockDao.selectListByCreateDate(date);
    	if(CollectionUtil.isNotEmpty(list)){
    		//先删除该天的均线数据和已经过滤好的结果集数据
    		String createDate=DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short);
    		this.stockMaDao.deleteByCreateDate(createDate);//删除均线
    		this.stockResultDao.deleteByCreateDate(createDate,"01");//删除已经过滤出来的结果集
    		//均线和过滤适合条件的股票算法开始
    		List<Stock> stock100List=null;
    		for(Stock stock:list){
    			stock100List=this.stockDao.select100ListByCodeOrderCreateDateDesc(stock.getCode());//必须结果集倒序
    			//均线计算
    			BigDecimal day5=new BigDecimal("0.00");
    			BigDecimal day10=new BigDecimal("0.00");
    			BigDecimal day20=new BigDecimal("0.00");
    			BigDecimal week5=new BigDecimal("0.00");
    			BigDecimal week10=new BigDecimal("0.00");
    			BigDecimal week20=new BigDecimal("0.00");
    			BigDecimal maSum=new BigDecimal("0.00");
    			
    			boolean isResult=false;//是否需要保存到result中，默认不符合规则，无需保存
    			if(CollectionUtil.isNotEmpty(stock100List)){
    				for(int i=0;i<stock100List.size();i++){
    					maSum=maSum.add(stock100List.get(i).getNewPrice());
    					//判断是否符合结果集的过滤条件
    					if(i==1){
    						Stock tStock=stock100List.get(0);//今天数据对象
    						Stock yStock=stock100List.get(1);//昨天数据对象
    						if(tStock.getDealVol().compareTo(yStock.getDealVol())==1 
    								&& tStock.getNewPrice().compareTo(yStock.getHeightPrice())==1
    								&& ((tStock.getNewPrice().subtract(tStock.getTodayOpen())).compareTo(tStock.getHeightPrice().subtract(tStock.getNewPrice()))==1)){
    							isResult=true;
    						}
    					}
    					//均线区间统计
    					if(stock100List.size()>50){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
            				if(i==9){
            					day10=maSum.divide(new BigDecimal("10.00"),2);
            				}
            				if(i==19){
            					day20=maSum.divide(new BigDecimal("20.00"),2);
            				}
            				if(i==24){
            					week5=maSum.divide(new BigDecimal("25.00"),2);
            				}
            				if(i==49){
            					week10=maSum.divide(new BigDecimal("50.00"),2);
            				}
            				if(i==(stock100List.size()-1)){
            					week20=maSum.divide(new BigDecimal((stock100List.size()+"")),2);
            				}
            				continue;
    					}else if(stock100List.size()>25 && stock100List.size()<=50){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
    						if(i==9){
            					day10=maSum.divide(new BigDecimal("10.00"),2);
            				}
    						if(i==19){
            					day20=maSum.divide(new BigDecimal("20.00"),2);
            				}
    						if(i==24){
            					week5=maSum.divide(new BigDecimal("25.00"),2);
            				}
    						if(i==(stock100List.size()-1)){
    							week10=maSum.divide(new BigDecimal((stock100List.size()+"")),2);
            				}
            				continue;
    					}else if(stock100List.size()>20 && stock100List.size()<=25){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
    						if(i==9){
            					day10=maSum.divide(new BigDecimal("10.00"),2);
            				}
    						if(i==19){
            					day20=maSum.divide(new BigDecimal("20.00"),2);
            				}
    						if(i==(stock100List.size()-1)){
    							week5=maSum.divide(new BigDecimal((stock100List.size()+"")),2);
            				}
    					}else if(stock100List.size()>10 && stock100List.size()<=20){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
    						if(i==9){
            					day10=maSum.divide(new BigDecimal("10.00"),2);
            				}
    						if(i==(stock100List.size()-1)){
    							day20=maSum.divide(new BigDecimal((stock100List.size()+"")),2);
            				}
    					}else if(stock100List.size()>5 && stock100List.size()<=10){
    						if(i==4){
    							day5=maSum.divide(new BigDecimal("5.00"),2);
            				}
    						if(i==(stock100List.size()-1)){
    							day10=maSum.divide(new BigDecimal((stock100List.size()+"")),2);
            				}
    					}else{
    						if(i==(stock100List.size()-1)){
    							day5=maSum.divide(new BigDecimal((stock100List.size()+"")),2);
            				}
    					}
        			}
    			}
    			
    			if(day10.doubleValue()<1){
    				day10=day5;
    			}
    			if(day20.doubleValue()<1){
    				day20=day10;
    			}
    			if(week5.doubleValue()<1){
    				week5=day20;
    			}
    			if(week10.doubleValue()<1){
    				week10=week5;
    			}
    			if(week20.doubleValue()<1){
    				week20=week10;
    			}
    			
    			//保存均线数据
    			StockMa stockMa=new StockMa();
    			stockMa.generateId();
    			stockMa.setCode(stock.getCode());
    			stockMa.setCodename(stock.getCodeName());
    			stockMa.setCreateDate(createDate);
    			stockMa.setDay5(day5);
    			stockMa.setDay10(day10);
    			stockMa.setDay20(day20);
    			stockMa.setWeek5(week5);
    			stockMa.setWeek10(week10);
    			stockMa.setWeek20(week20);
    			stockMaDao.insertSelective(stockMa);
    			
    			//如果isResult=true,则初步确定为我想要的数据
    			if(isResult){
    				if(stock.getNewPrice().compareTo(day5)==1 && week5.compareTo(week10)>=0 && week5.compareTo(week20)>=0){//当前价大于5日均线,5周均线分别在10周和20周准线之上
    					StockResult stockResult=new StockResult();
    					stockResult.generateId();
    					stockResult.setChanneltype("01");
    					stockResult.setCode(stock.getCode());
    					stockResult.setCodename(stock.getCodeName());
    					stockResult.setCreateDate(createDate);
    					stockResult.setHeightprice(stock.getHeightPrice());
    					stockResult.setNewprice(stock.getNewPrice());
    					stockResult.setMaday5(day5);
    					stockResult.setMaday10(day10);
    					stockResult.setMaday20(day20);
    					this.stockResultDao.insertSelective(stockResult);
    				}
    			}
    			
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
     * 插入指定股票代码从start到end日期的数据信息
     * @param codes
     * @param start
     * @param end
     */
    public void insertHistoryStock(String codes,String start,String end){
    	String[] stockCodes=codes.split(",");
    	if(stockCodes!=null){
    		// 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了  
            WebClient webclient = new WebClient();  
    		for(String code:stockCodes){
    			getHistoryStockInfo(webclient,code, start);
        	}
    	}
    	
    }
    
    /**
     * 利用爬虫爬去新浪财经的股票历史数据
     * @param code
     * @param date
     * @return
     */
    private Stock getHistoryStockInfo(WebClient webclient,String code,String date){
    	try {
    		String[] times=date.split("-");
        	if(times[1].startsWith("0")){
        		times[1]=times[1].replace("0", "");
        	}
        	if(times[2].startsWith("0")){
        		times[2]=times[1].replace("0", "");
        	}
        	//String url="http://money.finance.sina.com.cn/quotes_service/view/vMS_tradehistory.php?symbol="+code+"&date="+times[0]+"-"+times[1]+"-"+times[2];
        	final String url="http://money.finance.sina.com.cn/quotes_service/view/vMS_tradehistory.php?symbol=sz300303&date=2016-10-29";
          
            // 这里是配置一下不加载css和javaScript,配置起来很简单，是不是  
            webclient.getOptions().setCssEnabled(false);  
            webclient.getOptions().setJavaScriptEnabled(false);  
          
            // 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可  
            HtmlPage htmlpage = webclient.getPage(url);  
            DomElement domElement= htmlpage.getElementById("cOuter");
            DomElement stockHtmlData=domElement.getNextElementSibling();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
}
