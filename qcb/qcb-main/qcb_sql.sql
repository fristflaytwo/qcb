//股票原始数据采集表
CREATE TABLE `t_stock` (
  `id` varchar(32) NOT NULL,
  `code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `codeName` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `newPrice` decimal(20,2) DEFAULT NULL COMMENT '最新价',
  `amplitude` decimal(20,4) DEFAULT NULL COMMENT '涨跌幅',
  `amplitudePrice` decimal(20,2) DEFAULT NULL COMMENT '涨跌额',
  `buyPrice` decimal(20,2) DEFAULT NULL COMMENT '买入',
  `salePrice` decimal(20,2) DEFAULT NULL COMMENT '卖出',
  `dealVol` bigint(20) DEFAULT NULL COMMENT '成交量',
  `dealPrice` decimal(20,2) DEFAULT NULL COMMENT '成交额',
  `todayOpen` decimal(20,2) DEFAULT NULL COMMENT '今开',
  `yeatedayClose` decimal(20,2) DEFAULT NULL COMMENT '昨收',
  `heightPrice` decimal(20,2) DEFAULT NULL COMMENT '最高',
  `lowPrice` decimal(20,2) DEFAULT NULL COMMENT '最低',
  `createDate` varchar(8) DEFAULT NULL COMMENT '数据日期',
  PRIMARY KEY (`id`),
  KEY `i_createDate` (`createDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




//股票均线统计表
CREATE TABLE `t_stock_ma` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `codeName` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `day5` decimal(20,2) DEFAULT NULL COMMENT '5日均线',
  `day10` decimal(20,2) DEFAULT NULL COMMENT '10日均线',
  `day20` decimal(20,2) DEFAULT NULL COMMENT '20日均线',
  `week5` decimal(20,2) DEFAULT NULL COMMENT '5周均线',
  `week10` decimal(20,2) DEFAULT NULL COMMENT '10周均线',
  `week20` decimal(20,2) DEFAULT NULL COMMENT '20周均线',
  `createDate` varchar(8) DEFAULT NULL COMMENT '统计日期服务器时间',
  PRIMARY KEY (`id`),
  KEY `i_ma_createDate` (`createDate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



//过滤出来的股票数据
CREATE TABLE `t_stock_result` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `codeName` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `channelType` varchar(2) DEFAULT NULL COMMENT '统计渠道类别：01：量和昨日最高价',
  `maDay5` decimal(20,2) DEFAULT NULL COMMENT '5日均线',
  `maDay10` decimal(20,2) DEFAULT NULL COMMENT '10日均线',
  `maDay20` decimal(20,2) DEFAULT NULL COMMENT '20日均线',
  `newPrice` decimal(20,2) DEFAULT NULL COMMENT '最新价格',
  `heightPrice` decimal(20,2) DEFAULT NULL COMMENT '今日最高价',
  `createDate` varchar(8) DEFAULT NULL COMMENT '统计日期，服务器时间',
  PRIMARY KEY (`id`),
  KEY `i_result_createDate` (`createDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 快速寻找合适的股票
 */
select * from t_stock_result t order by (t.heightPrice-t.newPrice) ASC;


