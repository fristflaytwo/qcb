package com.xionger.qcb.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.StockDao;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.model.Stock;
import com.xionger.qcb.model.StockMa;

@Service("stockTimerService")
public class StockTimerServiceImpl implements StockTimerService{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockTimerServiceImpl.class);
	
	@Autowired
	private StockDao stockDao;
	@Autowired
	private StockMaDao stockMaDao;
	
	/**
     * 从下载的excel冲将数据导入到数据库,调用此方法必须要求该日数据excel必须存在
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
            	
            	DecimalFormat df2 = new DecimalFormat(Constants.DECIMAL_DIGIT_2); 
            	DecimalFormat dfNum = new DecimalFormat(Constants.DECIMAL_DIGIT_0);
            	DecimalFormat df4 = new DecimalFormat(Constants.DECIMAL_DIGIT_4);
            	
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
        		stockDao.insertSelective(stock);
                i++;
            }
        }catch(Exception e){
        	LOGGER.error(">>>>>\t"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\t 加载股票数据文件数据入库发生异常："+ ExceptionUtils.getMessage(e) + "\n" + ExceptionUtils.getStackTrace(e));
        }finally{
        	try {
        		if(fis!=null)fis.close();
			} catch (Exception e) {}
        	try {
        		if(pfs!=null)pfs.close();
			} catch (Exception e) {}
        	try {
        		if(wb!=null)wb.close();
			} catch (Exception e) {}
        	fis=null;
    		pfs=null;
    		wb=null;
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
    			BigDecimal day5=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal day10=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal day20=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			BigDecimal maSum=new BigDecimal(Constants.DECIMAL_DIGIT_2);
    			
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
    			stockMa.setCodeName(stock.getCodeName());
    			stockMa.setCreateDate(date);//数据日期
    			stockMa.setDay5(day5);
    			stockMa.setDay10(day10);
    			stockMa.setDay20(day20);
    			stockMaDao.insertSelective(stockMa);
    		}
    	}
    }
    
    
    
    
}
