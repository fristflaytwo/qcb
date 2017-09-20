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
import org.springframework.transaction.annotation.Transactional;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.conllection.CollectionUtil;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.common.util.json.FastJsonUtil;
import com.xionger.qcb.common.util.string.Native2AsciiUtils;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.StockDao;
import com.xionger.qcb.dao.mapper.StockExpandDao;
import com.xionger.qcb.dao.mapper.StockMaDao;
import com.xionger.qcb.dao.mapper.StockResultDao;
import com.xionger.qcb.model.Stock;
import com.xionger.qcb.model.StockExpand;

@Service("stockTimerService")
public class StockTimerServiceImpl implements StockTimerService{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockTimerServiceImpl.class);
	
	@Autowired
	private StockDao stockDao;
	@Autowired
	private StockMaDao stockMaDao;
	@Autowired
	private StockExpandDao stockExpandDao;
	@Autowired
	private StockResultDao stockResultDao;
	
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
     * 插入指定日期的股票扩展信息
     * @param date
     */
    @SuppressWarnings("unchecked")
    @Transactional(noRollbackFor={Exception.class})
	public void insertStockExpand(String date){
    	if(StringUtil.isNotBlank(date)){
    		//删除这天数据
    		stockExpandDao.deleteByCreateDate(date);
    		List<Stock> list=this.stockDao.selectListByCreateDate(date);
    		String result=null;
    		if(CollectionUtil.isNotEmpty(list)){
    			String startResult="v_";
    			String $spilt="~";
    			String [] expandInfo=null;
    			StockExpand se=null;
    			HashMap<String, Object> ltgdMap=null;
    			String ltgdMap_code="code";
    			String ltgdMap_data="data";
    			String ltgdMap_rows="rows";
    			String ltgdMap_gfxz="gfxz";
    			Map<String, Object> rows=null;
    			Map<String, Object> ltgf=null;
    			String ltgdMap_ltbl="ltbl";
    			String jnfr_Type="境内法人股";
    			String ltgdMap_noData="NO_DATA";
    			String ltgdMap_gdmc="gdmc";
    			List<Map<String, Object>> nearLtgds=null;
    			for(Stock stock:list){
    				LOGGER.info("开始遍历"+stock.getCode()+"的扩展股票信息");
    				result=HttpClientUtils.doGet(Constants.STOCK_EXPAND_BASE_INFO+stock.getCode(), Constants.UTF8);
    				if(StringUtil.isNotBlank(result)&& result.startsWith(startResult)){
    					expandInfo=result.split($spilt);
    					if(expandInfo!=null&& expandInfo.length>45){
    						se=new StockExpand();
    						se.generateId();
    						se.setCode(stock.getCode());
    						se.setCodeName(stock.getCodeName());
    						se.setTurnover(new BigDecimal(StringUtil.isNotBlank(expandInfo[38])?expandInfo[38]:Constants.DECIMAL_DIGIT_2));
    						se.setTotalMarketValue(new BigDecimal(StringUtil.isNotBlank(expandInfo[45])?expandInfo[45]:Constants.DECIMAL_DIGIT_2));
    						se.setCirculationValue(new BigDecimal(StringUtil.isNotBlank(expandInfo[44])?expandInfo[44]:Constants.DECIMAL_DIGIT_2));
    						se.setCreateDate(StringUtil.isNotBlank(expandInfo[30])?expandInfo[30].substring(0, 8):"");
    						result=HttpClientUtils.doGet(Constants.STOCK_EXPAND_LTGD_INFO+stock.getCode(), Constants.UTF8);
    						result=result.substring(12);
    						result=Native2AsciiUtils.ascii2Native(result);//汉字编码成native
    						ltgdMap=FastJsonUtil.jsonToBean(result, Map.class);
    						if(ltgdMap!=null && !ltgdMap.isEmpty() && !ltgdMap_noData.equals(ltgdMap.get(ltgdMap_code).toString())&&(int)ltgdMap.get(ltgdMap_code)==0){
    							rows=(Map<String, Object>) ltgdMap.get(ltgdMap_data);
    							nearLtgds=(List<Map<String, Object>>) rows.get(ltgdMap_data);
    							rows=(Map<String, Object>)(nearLtgds.get(0));
    							nearLtgds=(List<Map<String, Object>>) rows.get(ltgdMap_rows);
    							ltgf=(Map<String, Object>)(nearLtgds.get(0));
    							se.setStockRatio(new BigDecimal(ltgf.get(ltgdMap_ltbl).toString()));//第一大股东占比
    							se.setFirstPartner(ltgf.get(ltgdMap_gdmc).toString());
    							int i=0;
    							for(Map<String, Object> map:nearLtgds){
    								if(map.get(ltgdMap_gfxz)!=null && jnfr_Type.equals(map.get(ltgdMap_gfxz).toString())){
    									i++;
    								}
    							}
    							se.setBodiesNum(i);
    						}
    						stockExpandDao.insertSelective(se);
    					}else{
    						LOGGER.info("执行{0}日扩展的股票编号{1}:{2}扩展信息异常，扩展结果为:{3}",date,stock.getCode(),stock.getCodeName(),result);
    					}
    				}
    			}
    		}
    	}
    }
    
    
    /**
     * 插入需要关注的股票信息，需要进入之前均线已初始化完毕
     * @param date
     */
    public void insertStockResult(String date){
    	//删除当天的结果集所有数据
    	stockResultDao.deleteByCreateDate(date);
    	//插入当天类型为反包需要关注的股票信息
    	stockResultDao.insertByResultType01(date);
    	//插入当天类型为阶段底-防御需要关注的股票信息
    	stockResultDao.insertByResultType02(date);
    	//插入当天类型为阶段底-进攻需要关注的股票信息
    	stockResultDao.insertByResultType03(date);
    	//插入这天的空中加油数据
    	stockResultDao.insertByResultType04(date);
        //插入这天的风口神龙数据
    	stockResultDao.insertByResultType05(date);
    }
   
}
