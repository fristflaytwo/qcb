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
  `createDate` varchar(8) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




SELECT
	t.*, t.todayOpen - t.yeatedayClose AS 'aa',
	t.lowPrice - t.yeatedayClose AS 'bb'
FROM
	t_stock t
WHERE
	t.todayOpen > (t.yeatedayClose+0.1)
AND t.lowPrice > (t.yeatedayClose+0.1)
and t.newPrice>t.todayOpen
AND t.newPrice < 30
and t.codeName not like '%ST%'
and t.createDate='20160912'
order by CAST(REPLACE(t.amplitude,'%','') AS signed) desc,t.todayOpen - t.yeatedayClose desc,t.lowPrice - t.yeatedayClose desc;