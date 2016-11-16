package com.xionger.qcb.common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static final String formatPattern = "yyyy-MM-dd";

	public static final String formatPattern_Short = "yyyyMMdd";

	public static final String formatPattern_rand = "yyyyMMddHHmmss";
	public static final String formatPattern_14 = "yyyy-MM-dd HH:mm:ss";
	public static final String YEAR = "yyyy";// 年
	public static final String MONTH = "MM";// 月
	public static final String DAY = "dd";// 日

	
	
	public static long betweenDays(Date startDate,Date endDate) {
		long day = 0;
		try {
			day = (endDate.getTime()-startDate.getTime())/(24*60*60*1000);
		} catch (Exception e) {
			LOGGER.error("计算日期天数异常",e);
		}
		return day;
	}
	

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(new Date());
	}

	/**
	 * 获取制定毫秒数之前的日期
	 * 
	 * @param timeDiff
	 * @return
	 */
	public static String getDesignatedDate(long timeDiff) {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		long nowTime = System.currentTimeMillis();
		long designTime = nowTime - timeDiff;
		return format.format(designTime);
	}

	/**
	 * 
	 * 获取前几天的日期
	 */
	public static String getPrefixDate(String count) {
		Calendar cal = Calendar.getInstance();
		int day = 0 - Integer.parseInt(count);
		cal.add(Calendar.DATE, day); // int amount 代表天数
		Date datNew = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(datNew);
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(date);
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 字符串转换日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date stringToDate(String str) {
		// str = " 2008-07-10 19:20:00 " 格式
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		if (!str.equals("") && str != null) {
			try {
				return format.parse(str);
			} catch (ParseException e) {
				LOGGER.error("" + e);
			}
		}
		return null;
	}

	public static Date stringToDate(String str, String pattern) {
		// str = " 2008-07-10 19:20:00 " 格式
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		if (!str.equals("") && str != null) {
			try {
				return format.parse(str);
			} catch (ParseException e) {
				LOGGER.error("" + e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param sBegin
	 * @param sEnd
	 */
	// java中怎样计算两个时间如：“21:57”和“08:20”相差的分钟数、小时数 java计算两个时间差小时 分钟 秒 .
	public static String timeSubtract(String sBegin, String sEnd) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = null;
		Date end = null;
		try {
			begin = dfs.parse(sBegin);
			end = dfs.parse(sEnd);
		} catch (ParseException e) {
			LOGGER.error("" + e);
		}

		long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60;
		return day1+"_"+hour1+"_"+minute1+"_"+second1;
	}

	/**
	 * 生成时间随机数
	 * 
	 * @return
	 */
	public static String getRandTimeByDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(formatPattern_rand);
		return sdf.format(new Date());
	}

	/**
	 * 
	 * @return
	 */
	public static Date getDate(String date, String pattern) {
		Date ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			ret = sdf.parse(date);
		} catch (ParseException e) {
			LOGGER.error("" + e);
		}
		return ret;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getLangCurrentDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/*
	 * @Description:获取当前添加时间
	 * 
	 * @param @param minute
	 * 
	 * @param @return
	 * 
	 * @throws
	 */
	public static Date getNowAddTime(int minute) {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, minute);
		return nowTime.getTime();
	}

	/**
	 * 得到增加单位时间后的date
	 * 
	 * @return
	 */
	public static Date getAddTimeDate(String type, Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (type.equals(DateUtil.YEAR)) {
			cal.add(Calendar.YEAR, num);
		} else if (type.equals(DateUtil.MONTH)) {
			cal.add(Calendar.MONTH, num);
		} else if (type.equals(DateUtil.DAY)) {
			cal.add(Calendar.DAY_OF_MONTH, num);
		}
		return cal.getTime();
	}
	
	/**
	 * 获取date日期对应的星期
	 * @param date
	 * @return
	 */
	public static String getWeek(Date date){
		String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(week_index<0){
			week_index = 0;
		} 
		return weeks[week_index];
	}
	
	public static void main(String[] args) {
		long a=betweenDays(DateUtil.stringToDate("20160101",formatPattern_Short), DateUtil.stringToDate("20161116",formatPattern_Short));
		System.out.println(a);
	}

}
