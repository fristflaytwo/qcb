 package com.xionger.qcb.common.constants;

import java.util.HashMap;
import java.util.Map;

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
	 * application 密文加密 秘钥
	 */
	public static final String appKey = "c75751f820bc430d807c49ea76516e4f6767464a515e5d5d";//配置文件秘钥key 
	
	/**
	 * API接口调用类型标识
	 */
	public static final String API_TYPE_MOBILE="mobile";//手机信息
	public static final String API_TYPE_CREDIT="credit";//央行信用
	public static final String API_TYPE_JD="jd";//京东数据
	public static final String API_TYPE_HOUSEFUND="housefund";//公积金
	public static final String API_TYPE_SOCIALSECURITY="socialsecurity";//社保
	public static final String API_TYPE_EDUCATION="education";//学信网
	public static final String API_TYPE_MAIMAI="maimai";//脉脉
	public static final String API_TYPE_LINKEDIN="linkedin";//领英
	public static final String API_TYPE_TAOBAO="taobao";//淘宝
	public static final String API_TYPE_EMAILBILL="emailbill";//失信执行人
	
	/**
	 * API接口方法类型
	 */
	public static final String API_METHOD_MOBILE = "api.mobile.get";
	public static final String API_METHOD_MOBILE_SMS = "api.mobile.sendSms";
	public static final String API_METHOD_MOBILE_FPWD = "api.mobile.findPwd";
	public static final String API_METHOD_MOBILE_NPWD = "api.mobile.inputNewPwd";
	public static final String API_METHOD_CREDIT = "api.credit.get";
	public static final String API_METHOD_JD = "api.jd.get";
	public static final String API_METHOD_HOUSEFUND = "api.housefund.get";
	public static final String API_METHOD_SOCIAL = "api.socialsecurity.get";
	public static final String API_METHOD_EDUCATION = "api.education.get";
	public static final String API_METHOD_STATUS = "api.common.getStatus";
	public static final String API_METHOD_RESULT = "api.common.getResult";
	public static final String API_METHOD_MOBILE_AREA = "api.mobile.area";
	public static final String API_METHOD_MAIMAI = "api.maimai.get";
	public static final String API_METHOD_LINKEDIN = "api.linkedin.get";
	public static final String API_METHOD_TAOBAO = "api.taobao.get";
	public static final String API_METHOD_EMAILBILL = "api.emailbill.get";
	
	/** 根据Method 取BizType **/
	public static final Map<String, String> GET_METHOD_TYPE_MAP = new HashMap<String, String>();
	static {
		GET_METHOD_TYPE_MAP.put(API_METHOD_MOBILE, API_TYPE_MOBILE);
		GET_METHOD_TYPE_MAP.put(API_METHOD_MOBILE_SMS, API_TYPE_MOBILE);
		GET_METHOD_TYPE_MAP.put(API_METHOD_MOBILE_FPWD, API_TYPE_MOBILE);
		GET_METHOD_TYPE_MAP.put(API_METHOD_MOBILE_NPWD, API_TYPE_MOBILE);
		GET_METHOD_TYPE_MAP.put(API_METHOD_MOBILE_AREA, API_TYPE_MOBILE);
		
		GET_METHOD_TYPE_MAP.put(API_METHOD_CREDIT, API_TYPE_CREDIT);
		GET_METHOD_TYPE_MAP.put(API_METHOD_JD, API_TYPE_JD);
		GET_METHOD_TYPE_MAP.put(API_METHOD_HOUSEFUND, API_TYPE_HOUSEFUND);
		GET_METHOD_TYPE_MAP.put(API_METHOD_SOCIAL, API_TYPE_SOCIALSECURITY);
		GET_METHOD_TYPE_MAP.put(API_METHOD_EDUCATION, API_TYPE_EDUCATION);
		GET_METHOD_TYPE_MAP.put(API_METHOD_MAIMAI, API_TYPE_MAIMAI);
		GET_METHOD_TYPE_MAP.put(API_METHOD_LINKEDIN, API_TYPE_LINKEDIN);
		GET_METHOD_TYPE_MAP.put(API_METHOD_TAOBAO, API_TYPE_TAOBAO);
	}
	
	/** 服务生成序列号规则 **/
	public static Map<String, String> SERIAL_NO_MAP = new HashMap<String,String>();
	static {
		SERIAL_NO_MAP.put(Constants.API_TYPE_HOUSEFUND, "20");
		SERIAL_NO_MAP.put(Constants.API_TYPE_SOCIALSECURITY, "21");
		SERIAL_NO_MAP.put(Constants.API_TYPE_JD, "22");
		SERIAL_NO_MAP.put(Constants.API_TYPE_TAOBAO, "23");
		SERIAL_NO_MAP.put(Constants.API_TYPE_CREDIT, "24");
		SERIAL_NO_MAP.put(Constants.API_TYPE_EDUCATION, "25");
		SERIAL_NO_MAP.put(Constants.API_TYPE_MAIMAI ,"26");
		SERIAL_NO_MAP.put(Constants.API_TYPE_LINKEDIN, "27");
		SERIAL_NO_MAP.put(Constants.API_TYPE_MOBILE, "28");
	}
	
	/**
	 * 短信业务类型
	 */
	public static final String SMS_SEND_TEMPLET_TYPE_1 = "1";//验证码短信发送
	public static final String SMS_SEND_TEMPLET_TYPE_2 = "2";//其他短信发送    

	public static final String SMS_CHANNEL_NO = "0003";//短信渠道
	public static final String MAIL_CHANNEL_NO = "0003";//邮件渠道
	
	/**
	 * api接口返回成功状态码
	 */
	public static final String API_STATUS_SUCCESS="00";
	public static final String MARKER_SEARCH = "77";
	public static final String MARKER_POINT = "88";
	
	/**
	 * @Fields RECEPTION_SUCCEE :接收状态
	 */
	public static final String RECEPTION_SUCCEE = "succee";
	public static final String RECEPTION_FAIL = "fail";
	
	/**
	 * 短信发送次数限制
	 */
	public static final String INTERCEPTION_SMS_NUM = "INTERCEPTION_SMS_NUM";//短信发送限制次数
	public static final String INTERCEPTION_IP_NUM = "INTERCEPTION_IP_NUM";//短信IP发送限制次数

	/**
	 * 系统属性配置表  邮箱有效链接时间
	 */
	public static final String MAIL_VALTIME="MAILVALIDTIME";
	
	/**
	 * 短信接口的key
	 */
	public static final String SMS_URL="SMS_URL";
	
	/**
	 * 短信平台发送邮件方法
	 */
	public static final String SEND_MAIL="sendMail";
	
	/**
	 * 接口-短信模板发送路径
	 */
	public static final String API_SMS_SENDMODEL = "sendByModel";
	
	/**
	 * 邮件发送内容格式
	 */
	public static final String MAIL_FORMAT_HTML="html";
	/**
     * 邮件发送地址
     */
	public static final String MALL_FORMAT_URL="MALL_FORMAT_URL";
	/**
	 * 短信到期时间
	 */
	public static final String CODE_SMS_EXPIRE_DATE = "EXPIRE_NUMBER"; 
	
	
	/**
	 * 用户类型
	 */
	public static final String USER_TYPE_HT="01";//用户类型：后台
	public static final String USER_TYPE_QT="00";//用户类型：后台
	/**
	 * area状态启用类型
	 */
	public static final String AREA_STATUS_ENABLE="00";//用户类型：后台
	
	/**
	 * 用户状态
	 */
	public static final String USER_STATUS_00="00";//有效用户

	/**
	 * 验证码key
	 */
	public static final String CAPTEXT="capText";
	
	/**
	 * 服务器端ip
	 */
	public static final String LOCAL_IP="127.0.0.1";
	public static final String LOCALHOST_IP="0:0:0:0:0:0:0:1";
	
	/**
	 * api 公共属性
	 * @author    leo
	 * @date      2016-7-28 下午5:37:54
	 * @version   v1.0
	 */
	public static class ApiCommon{
		/** 商户标识 **/
		public static final String BUSIFLAG = "busiflag"; //业务标识
		public static final String VERSION_1_0_0 = "1.0.0";//api接口版本
		public static final String FILE_APIKEY = "apiKey"; //apikey编码
		public static final String API_PROP_TYPEFLAG = "typeflag";//业务标识
		public static final String API_RESP_CODE_0000="0000"; //
		public static final String API_RESP_CODE_5000="5000"; //验签成功
		
		
		/***** 以下以错误编码文档为准   **/
		/** 验签失败**/
		public static final String API_RESP_CODE_1002 =" 1002";
		/** apikey无效或不存在 */
		public static final String API_RESP_CODE_1001 = "1001";//
		/** 账户冻结 */
		public static final String API_RESP_CODE_1014 = "1014";//
		/** 账户金额不够 */
		public static final String API_RESP_CODE_1015 = "1015";//
		/** 请求参数校验异常  */
		public static final String API_RESP_CODE_1013 = "1013";
		/** 服务器异常 */
		public static final String API_RESP_CODE_1012 = "1012";
		/** 被禁止的IP */
		public static final String API_RESP_CODE_1004 = "1004";
		/** 接口对应地区没有开通 */
		public static final String API_RESP_CODE_1104 = "1104";
		/***** 上以错误编码文档为准   **/

		
		public static final String API_CITY_CODE_4103 = "4103";//洛阳

	}
	
	
	
	
	/**
	 * 网页title
	 */
	public static final String CODE_WEBTITLE = "webTitle";
    /**
     * 公司名称
     */
	public static final String CODE_COMPANYNAME = "companyName";
    /**
     * 平台网址
     */
	public static final String CODE_PLATWEB = "platWeb";
    /**
     * 平台名称
     */
	public static final String CODE_PLATNAME = "platName";
    /**
     * 公司地址
     */
	public static final String CODE_COMPANYADDR = "companyAddr";
    /**
     * 微博地址
     */
	public static final String CODE_WEIBOADDR = "weiBoAddr";
    /**
     * 微博名称
     */
	public static final String CODE_WEIBONAME = "weiBoName";
    /**
     * icp 版权所有
     */
	public static final String CODE_COPYRIGHT = "copyRight";
    /**
     * 企业邮箱
     */
	public static final String CODE_BUSIMAIL = "busiMail";
    /**
     * 工作时间
     */
	public static final String CODE_WORKTIME = "workTime";
    /**
     * 客服电话
     */
	public static final String CODE_SERVICEPHONE = "servicePhone";

	public static final String NO_111111 = "{$$$$}";//针对 洛阳社保  设置默认值
	
	/**
	 * 爬虫URL 对应表中的key
	 */
	public static final String SPIDER_URL = "SPIDER_URL";
	/**
	 * 定义签名是否打开 key
	 */
	public static final String SIGN_STATE = "SIGN_STATE";
	public static final String SIGN_STATE_OPEN = "open";
	
	/**
	 * 配置默认余额
	 */
	public static final String CREDIT_AMT = "credit_amt";
	
	/**
	 * 定义白名单是否打开 key
	 */
	public static final String WHITE_LIST_STATE = "WHITE_LIST_STATE";
	public static final String WHITE_LIST_STATE_OPEN = "open";
	
	
	
	/** 下拉框 配置的dataType **/
//	public static final String DATA_TYPE_HOUSEFUND = "housefundDataType";
//	public static final String DATA_TYPE_SOCIALSECURITY = "socialsecurityDataType";
	
	
	/**
	 * 京东订单状态
	 */
	public static final String JD_ORDER_STATIC_WANCHENG="完成";
	
	/**
	 * 央行征信记录概要类型
	 */
	public static final String CREDIT_RECORD_SUMMARY_XYK="信用卡";
	public static final String CREDIT_RECORD_SUMMARY_GFDK="购房贷款";
	public static final String CREDIT_RECORD_SUMMARY_QTDK="其他贷款";
	
	
	/**
	 * app版本类型
	 */
	public static final String APP_CONFIG_TYPE_ANDROID="ANDROID";
	public static final String APP_CONFIG_TYPE_IOS="IOS";
	/**
	 * app各版本
	 */
	public static final String APP_VERSION_1_2_0="1.2.0";
	public static final String APP_VERSION_1_3_0="1.3.0";
	public static final String APP_VERSION_1_3_3="1.3.3";
	/**
	 * 非爬虫端的app接口类的key值
	 */
	public static final String APP_METHOD_MEMBER_AUTH="APP_METHOD_MEMBER_AUTH";//资质认证
	public static final String APP_METHOD_QUERY_MEMBER="APP_METHOD_QUERY_MEMBER";//查询会员
	public static final String APP_METHOD_QUERY_CITIES="APP_METHOD_QUERY_CITIES";//查询接口城市
	public static final String APP_METHOD_RECORD_DETAIL="APP_METHOD_RECORD_DETAIL";//查询记录明细接口
	public static final String APP_METHOD_RECORD_LIST="APP_METHOD_RECORD_LIST";//查询记录列表接口
	public static final String APP_METHOD_CHANGE_PICTURE="APP_METHOD_CHANGE_PICTURE";//更换会员图像接口
	public static final String APP_METHOD_CHECK_MBOILE_CODE="APP_METHOD_CHECK_MBOILE_CODE";//校验手机短信验证码
	public static final String APP_METHOD_CHECK_USERNAME_REGISTER="APP_METHOD_CHECK_USERNAME_REGISTER";//校验用户名是否注册
	public static final String APP_METHOD_LOGIN="APP_METHOD_LOGIN";//用户登录
	public static final String APP_METHOD_MODIFY_MOBILE="APP_METHOD_MODIFY_MOBILE";//修改登录名
	public static final String APP_METHOD_MODIFY_PWD="APP_METHOD_MODIFY_PWD";//修改登录密码
	public static final String APP_METHOD_QUERY_APP_VERSION_INFO="APP_METHOD_QUERY_APP_VERSION_INFO";//查询最新APP版本信息
	public static final String APP_METHOD_REGISTER="APP_METHOD_REGISTER";//用户注册
	public static final String APP_METHOD_RESET_PWD_FOR_FORGET_PWD="APP_METHOD_RESET_PWD_FOR_FORGET_PWD";//用户忘记密码重置密码
	public static final String APP_METHOD_SEND_MBOILE_CODE="APP_METHOD_SEND_MBOILE_CODE";//发送手机验证码
	public static final String APP_METHOD_UNFIND_PROCESS="APP_METHOD_UNFIND_PROCESS";//为找到对应的服务类
	public static final String APP_METHOD_CONNECTUS_PROCESS="APP_METHOD_CONNECTUS_PROCESS";//关于我们服务类
	public static final String APP_METHOD_FREE_RULE_PROCESS="APP_METHOD_FREE_RULE_PROCESS";//免费使用服务类
	public static final String APP_METHOD_COMPANY_BANK_INFO_PROCESS="APP_METHOD_COMPANY_BANK_INFO_PROCESS";//立木征信银行账户信息服务类
	public static final String APP_METHOD_BANK_TRANSFER_PROCESS="APP_METHOD_BANK_TRANSFER_PROCESS";//提交转账信息处理类
	public static final String APP_METHOD_RECHARGE_INFO_PROCESS="APP_METHOD_RECHARGE_INFO_PROCESS";//充值账户信息服务类
	public static final String APP_METHOD_SPEND_RECORD_SERVICE_PROCESS="APP_METHOD_SPEND_RECORD_SERVICE_PROCESS";//我-消费记录-服务类型
	public static final String APP_METHOD_CONSUDETL_SERVICE_PROCESS="APP_METHOD_CONSUDETL_SERVICE_PROCESS";//我-消费记录
	public static final String APP_METHOD_RECHARGE_RECORD_STATUS_PROCESS="APP_METHOD_RECHARGE_RECORD_STATUS_PROCESS";//充值记录-状态
	public static final String APP_METHOD_RECHANGE_DETL_PROCESS="APP_METHOD_RECHANGE_DETL_PROCESS";//充值记录
	public static final String APP_METHOD_RECHARGE_CANCEL_PROCESS="APP_METHOD_RECHARGE_CANCEL_PROCESS";//充值记录-取消
	public static final String APP_METHOD_ALIPAY_WITH_APP_PROCESS="APP_METHOD_ALIPAY_WITH_APP_PROCESS";//APP支付请求签名接口
	public static final String APP_METHOD_CHECK_ORDER_STATUS_PROCESS="APP_METHOD_CHECK_ORDER_STATUS_PROCESS";//APP查看支付状态接口
	public static final String APP_METHOD_WEIXIN_WITH_APP_PROCESS="APP_METHOD_WEIXIN_WITH_APP_PROCESS";//APP微信支付
	public static final String APP_METHOD_MAIMAI_PROCESS="APP_METHOD_MAIMAI_PROCESS";//脉脉查询
	public static final String APP_METHOD_LINKEDIN_PROCESS="APP_METHOD_LINKEDIN_PROCESS";//领英查询
	public static final String APP_METHOD_TAOBAO_PROCESS="APP_METHOD_TAOBAO_PROCESS";//淘宝查询
	public static final String APP_METHOD_APPSUGGESTIONS_PROCESS="APP_METHOD_APPSUGGESTIONS_PROCESS";//意见反馈接口
	
	
	
	/**
	 * 活动免费天数
	 */
	public static final Integer API_FREE_RULE_DAYS=60;//为找到对应的服务类
	
	
	public static final class AmtType{
		public static final String AMTTYPE_REDPACKETS = "2";
		public static final String AMTTYPE_RECHARGE = "1";
		public static final String AMTTYPE_CREDIT = "3";
		public static final String AMTTYPE_CREDIT_AVAL = "4";
	}
	
	public static final class Sequence{
		/** 序列名为seq_serial_no **/
		public static final String SEQ_SERIAL_NO = "seq_serial_no";
	}
	
	/**
	 * 立木征信收款人户名
	 */
	public static final String SYS_PROPERTY_LIMU_BANK_SKR="limu_bank_skr";
	/**
	 * 立木征信收款帐号
	 */
	public static final String SYS_PROPERTY_LIMU_BANK_SKZH="limu_bank_skzh";
	/**
	 * 立木征信开户行
	 */
	public static final String SYS_PROPERTY_LIMU_BANK_KHH="limu_bank_khh";
	
	/**
	 * 银行转账提醒财务人员邮箱
	 */
	public static final String SYS_PROPERIY_LIMU_BANK_FINANCE_EMAIL_STRING="limu_finance_email";
	
	public static final String ACTIVE_FLAG ="ACTIVE_FLAG" ;
	public static final String ACTIVE_FLAG_ON="1";
	public static final String ACTIVE_FLAG_OFF="0";
	public static final String RED_END_TIME="RED_END_TIME";
	public static final String RED_AMT_REG="RED_AMT_REG";//注册送红包
	public static final String RED_AMT_MEMPASS="RED_AMT_MEMPASS";//企业认证送红包
	/**
	 * 阿里支付接口参数
	 */
	public static final String ALIPAY_ZHIFU_INTERFACE="alipay.trade.app.pay";//阿里支付接口方法
	public static final String ALIPAY_ZHIFU_FORMAT="JSON";//阿里支付接口-数据格式方式
	public static final String ALIPAY_ZHIFU_CHARSET="utf-8";//阿里支付接口-请求使用的编码格式
	
	public static final String SYS_RECHANGE_SUBJECT = "立木征信账户余额充值";
	
	public static final String SYS_TAOBAO_CONSUSTATISTICS_STATUS="交易成功";//淘宝按月统计-订单状态
	
	public static final class Channel{
		public static final String API = "API";
		public static final String APP = "APP";
		public static final String WEB = "WEB";
		/** 非API渠道 **/
		public static final String UN_API = "WEB/APP/API";
	}
	
}



