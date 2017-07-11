 package com.xionger.qcb.common.constants;


public class Constants {

	public static final String UNDERLINE = "_";
 
	/**
	 * 分页参数
	 */
	public static final  int  DEFAULT_PAGE_NUM = 1;  //默认第一页
	public static final  int  DEFAULT_PAGE_SIZE = 10;//默认分页大小
	/**
	 * 编码方式
	 */
	public static final String UTF8 = "UTF-8";//UTF-8编码
	public static final String GBK = "gbk";//gbk编码
	/**
	 * HTTP client连接池配置
	 */
	public static final int HTTP_MAX_TOTAL = 30;//最大连接数
	public static final int HTTP_DEFAULT_MAX_PERROUTE = 10;//每个路由基础的连接
	public static final int HTTP_TIME_OUT = 5000;// 链接超期时间
	public static final int HTTP_EXECUTION_COUNT = 5;//已经重试次数，超过重拾次数就放弃
	
	/**
	 * 百度apikey
	 */
	public static final String BAIDU_API_KEY="3ac97e8778b557fc0dabab6459d4d225";
	/**
	 * 百度api工作日接口调用
	 */
	public static final String BAIDU_API_HOLIDAY="http://apis.baidu.com/xiaogg/holiday/holiday";
	
	/**
	 * 用爬虫爬去的历史的单个股票历史信息，需要后面追加参数
	 */
	public static final String STOCK_HISTORY_ONES_PATH="http://money.finance.sina.com.cn/quotes_service/view/vMS_tradehistory.php?";
	/**
	 * 下载的股票数据地址
	 */
	public static final String STOCK_XLS_DOWNLOAD_PATH="http://stock.gtimg.cn/data/get_hs_xls.php?id=ranka&type=1&metric=chr";
	/**
	 * 股票历史数据下载地址，下载的文件格式为csv格式，需要后面追加参数
	 */
	public static final String STOCK_CSV_DOWNLOAD_HISTORY_PATH="http://quotes.money.163.com/service/chddata.html?";
	/**
	 * 股票扩展基本信息接口
	 */
	public static final String STOCK_EXPAND_BASE_INFO="http://web.sqt.gtimg.cn/q=";
	/**
	 * 股票扩展信息流通股东信息接口
	 */
	public static final String STOCK_EXPAND_LTGD_INFO="http://web.ifzq.gtimg.cn/appstock/hs/ltgd/get?type=ltgd&_var=v_liutonggd&code=";
	
	/**
	 * 上市公司的基本信息，一般情况下不变动，从同花顺上采集下来的
	 */
	public static final String STOCK_BASE_INFO="https://basic.10jqka.com.cn/mobile/300033/companyn.html";
	
	
	/**
	 * 通用无效
	 */
	public static final String STATE_99="99";
	
	/**
	 * 通用有效
	 */
	public static final String STATE_00="00";
	
	/**
	 * 确定非交易日-星期六
	 */
	public static final String CONFIRM_NO_TRAD_SATURDAY_CN="星期六";
	
	/**
	 * 确定非交易日-星期日
	 */
	public static final String CONFIRM_NO_TRAD_SUNDAY_CN="星期日";
	
	/**
	 * 4位小数
	 */
	public static final String DECIMAL_DIGIT_4="0.0000";
	/**
	 * 2位小数
	 */
	public static final String DECIMAL_DIGIT_2="0.00";
	/**
	 * 整数
	 */
	public static final String DECIMAL_DIGIT_0="0";
	
	
	/**
	 * 已5为分组的均线分子，如5日线、5周线
	 */
	public static final String DECIMAL_5_DIGIT_2="5.00";
	/**
	 * 已5为分组的均线分子，如10日线、10周线
	 */
	public static final String DECIMAL_10_DIGIT_2="10.00";
	/**
	 * 已5为分组的均线分子，如20日线、20周线
	 */
	public static final String DECIMAL_20_DIGIT_2="20.00";
	
}



