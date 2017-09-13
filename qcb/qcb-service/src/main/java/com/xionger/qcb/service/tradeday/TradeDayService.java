package com.xionger.qcb.service.tradeday;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xionger.qcb.common.constants.Constants;
import com.xionger.qcb.common.util.date.DateUtil;
import com.xionger.qcb.common.util.http.HttpClientUtils;
import com.xionger.qcb.common.util.string.StringUtil;
import com.xionger.qcb.dao.mapper.TradeDayDao;
import com.xionger.qcb.model.TradeDay;
import com.xionger.qcb.model.vo.ResultVo;
import com.xionger.qcb.service.BaseStockAbstract;

@Service("tradeDayServiceImpl")
public class TradeDayService extends BaseStockAbstract{
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeDayService.class);
	
	@Value("${stock.xls.path}")
	private String stockXlsPath;//股票数据文件存放路径
	@Autowired
	private TradeDayDao tradeDayDao;
	
	
	/**
	 * 应用操作之前处理内容
	 * @param rv
	 */
	protected void processBefore(ResultVo rv) {
		BufferedOutputStream bw = null;//先下载数据到本地磁盘
		String path=stockXlsPath+DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short)+".xls";
		File f = new File(path);// 创建文件对象
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();// 创建文件路径
		try {
			byte[] result=HttpClientUtils.doGetReturnBytes(Constants.STOCK_XLS_DOWNLOAD_PATH, Constants.UTF8);
			bw = new BufferedOutputStream(new FileOutputStream(path));// 写入文件
			bw.write(result);
			rv.setSuccess(Boolean.TRUE);
			rv.setMsg(path);
		} catch (Exception e) {
			LOGGER.error("下载"+DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short)+"天股票信息数据异常",e);
			rv.setSuccess(Boolean.FALSE);
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception e) {
				LOGGER.error("关闭写入{}股票数据文件流异常",DateUtil.dateToString(new Date(),DateUtil.formatPattern_Short),e);
				rv.setSuccess(Boolean.FALSE);
			}
			bw=null;
		}
	}
	
	/**
	 * 日交易数据保存
	 * @param rv
	 */
	public void processExcute(ResultVo rv) {
		insertsTradeDayByXls(rv.getMsg());
	}
	
	/**
	 * 加载只等目录的Excel中的数据入库，并返回数据日期，返回null则表示异常
	 * @param path
	 * @return
	 */
	private String insertsTradeDayByXls(String path){
		LOGGER.info("开始读取{}目录的股票excel数据，并保存数据库",path);
		HSSFWorkbook wb=null;
    	POIFSFileSystem pfs=null;
    	FileInputStream fis=null;
    	String dataDate=null;
        try{
        	fis=new FileInputStream(new File(path));
        	pfs = new POIFSFileSystem(fis);  
            wb = new HSSFWorkbook(pfs);
            HSSFSheet  sheet = wb.getSheetAt(0);
            Iterator<Row> rows=sheet.rowIterator();
            Row row=null;
            int i=0;
            List<TradeDay> list=new ArrayList<TradeDay>();
            DecimalFormat df2 = new DecimalFormat(Constants.DECIMAL_DIGIT_2); 
        	DecimalFormat dfNum = new DecimalFormat(Constants.DECIMAL_DIGIT_0);
        	DecimalFormat df4 = new DecimalFormat(Constants.DECIMAL_DIGIT_4);
        	TradeDay tradeDay=null;
            while(rows.hasNext()){
            	row=rows.next();
            	if(i==0){
            		dataDate=delDataBackDataTime(row, 1);
            		if(StringUtil.isBlank(dataDate)) break;
            		rows.next();// 绕过数据标题
            		i++;
            		continue;
            	}
        		tradeDay=new TradeDay();
        		tradeDay.generateId();
        		tradeDay.setCreateDate(dataDate);
        		collectTradeDayInfo(row, tradeDay, df2, dfNum, df4);
        		list.add(tradeDay);
            	if(i%50==0 || !rows.hasNext()){//每50条或数据循环完毕则进行入库操作
            		this.tradeDayDao.inserts(list);//执行批量插入
            		list.clear();//清空数据
            	}
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
	 * 收集股票交易数据
	 * @param row
	 * @param td
	 */
	public void collectTradeDayInfo(Row row,TradeDay tradeDay,DecimalFormat df2,DecimalFormat dfNum,DecimalFormat df4){
		tradeDay.setCode(row.getCell(0).getStringCellValue());
		tradeDay.setCodeName(row.getCell(1).getStringCellValue());
		tradeDay.setNewPrice(new BigDecimal(df2.format(row.getCell(2).getNumericCellValue())));
		tradeDay.setAmplitude(new BigDecimal(df4.format(row.getCell(3).getNumericCellValue())));
		tradeDay.setAmplitudePrice(new BigDecimal(df2.format(row.getCell(4).getNumericCellValue())));
		tradeDay.setBuyPrice(new BigDecimal(df2.format(row.getCell(5).getNumericCellValue())));
		tradeDay.setSalePrice(new BigDecimal(df2.format(row.getCell(6).getNumericCellValue())));
		tradeDay.setDealVol(Long.parseLong(dfNum.format(row.getCell(7).getNumericCellValue())));
		tradeDay.setDealPrice(new BigDecimal(df2.format(row.getCell(8).getNumericCellValue())));
		tradeDay.setTodayOpen(new BigDecimal(df2.format(row.getCell(9).getNumericCellValue())));
		tradeDay.setYeatedayClose(new BigDecimal(df2.format(row.getCell(10).getNumericCellValue())));
		tradeDay.setHeightPrice(new BigDecimal(df2.format(row.getCell(11).getNumericCellValue())));
		tradeDay.setLowPrice(new BigDecimal(df2.format(row.getCell(12).getNumericCellValue())));
	}
	
	
	/**
	 * 返回row中的数据日期，并且如果数据库中存在该日期数据则删除
	 * @param row
	 * @param cellIndex
	 * @return
	 */
	private String delDataBackDataTime(Row row,int cellIndex){
		String dataDate=row.getCell(cellIndex).getStringCellValue();
		if(StringUtil.isBlank(dataDate)){
			return null;
		}else{
			dataDate=DateUtil.dateToString(new Date(), DateUtil.YEAR)+dataDate.split(" ")[0].replace("-", "");
			this.tradeDayDao.deleteByCreateDate(dataDate);//如果这天存在数据，则先删除数据
		}
		return dataDate;
	}
	
	
}
