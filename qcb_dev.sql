/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.6.21-log : Database - qcb_dev
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`qcb_dev` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `qcb_dev`;

/*Table structure for table `t_stock` */

DROP TABLE IF EXISTS `t_stock`;

CREATE TABLE `t_stock` (
  `id` char(32) NOT NULL,
  `code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `code_name` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `new_price` decimal(20,2) DEFAULT NULL COMMENT '最新价',
  `amplitude` decimal(20,4) DEFAULT NULL COMMENT '涨跌幅',
  `amplitude_price` decimal(20,2) DEFAULT NULL COMMENT '涨跌额',
  `buy_price` decimal(20,2) DEFAULT NULL COMMENT '买入',
  `sale_price` decimal(20,2) DEFAULT NULL COMMENT '卖出',
  `deal_vol` bigint(20) DEFAULT NULL COMMENT '成交量',
  `deal_price` decimal(20,2) DEFAULT NULL COMMENT '成交额',
  `today_open` decimal(20,2) DEFAULT NULL COMMENT '今开',
  `yeateday_close` decimal(20,2) DEFAULT NULL COMMENT '昨收',
  `height_price` decimal(20,2) DEFAULT NULL COMMENT '最高',
  `low_price` decimal(20,2) DEFAULT NULL COMMENT '最低',
  `create_date` varchar(8) DEFAULT NULL COMMENT '数据日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_ts_id` (`id`),
  KEY `i_ts_createDate` (`create_date`) USING BTREE,
  KEY `i_ts_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_stock` */

/*Table structure for table `t_stock_concept` */

DROP TABLE IF EXISTS `t_stock_concept`;

CREATE TABLE `t_stock_concept` (
  `id` char(32) NOT NULL,
  `code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `concept_name` varchar(128) DEFAULT NULL COMMENT '股票概念名称',
  `create_date` varchar(8) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_stock_concept` */

/*Table structure for table `t_stock_date` */

DROP TABLE IF EXISTS `t_stock_date`;

CREATE TABLE `t_stock_date` (
  `id` char(32) NOT NULL COMMENT 'id',
  `stock_date` varchar(8) DEFAULT NULL COMMENT '股票交易日期',
  `is_trade_date` varchar(2) DEFAULT NULL COMMENT '是否交易日00：是 99：否',
  `is_start_week_day` varchar(2) DEFAULT NULL COMMENT '是否本周开始日期00：是 99：否',
  `is_end_week_day` varchar(2) DEFAULT NULL COMMENT '是否本周结束日期00：是 99：否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_tsd_id` (`id`),
  KEY `i_tsd_stockDate` (`stock_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_stock_date` */

/*Table structure for table `t_stock_expand` */

DROP TABLE IF EXISTS `t_stock_expand`;

CREATE TABLE `t_stock_expand` (
  `id` char(32) NOT NULL COMMENT '主键',
  `code` varchar(64) NOT NULL COMMENT '股票代码',
  `code_name` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `turnover` decimal(5,2) DEFAULT NULL COMMENT '换手率',
  `total_market_value` decimal(20,2) DEFAULT NULL COMMENT '市值',
  `circulation_value` decimal(20,2) DEFAULT NULL COMMENT '流通值',
  `real_value` decimal(20,2) DEFAULT NULL COMMENT '真实流通值',
  `bodies_num` int(11) DEFAULT NULL COMMENT '机构数量（基金、组合等等）',
  `first_partner` varchar(128) DEFAULT NULL COMMENT '第一大股东',
  `stock_ratio` decimal(5,2) DEFAULT NULL COMMENT '第一大股东占比',
  `business_address` varchar(128) DEFAULT NULL COMMENT '企业所在地址',
  `create_date` varchar(8) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_stock_expand` */

/*Table structure for table `t_stock_ma` */

DROP TABLE IF EXISTS `t_stock_ma`;

CREATE TABLE `t_stock_ma` (
  `id` char(32) NOT NULL DEFAULT '',
  `code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `code_name` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `day5` decimal(20,2) DEFAULT NULL COMMENT '5日均线',
  `day10` decimal(20,2) DEFAULT NULL COMMENT '10日均线',
  `day20` decimal(20,2) DEFAULT NULL COMMENT '20日均线',
  `week5` decimal(20,2) DEFAULT NULL COMMENT '5周均线',
  `week10` decimal(20,2) DEFAULT NULL COMMENT '10周均线',
  `week20` decimal(20,2) DEFAULT NULL COMMENT '20周均线',
  `create_date` varchar(8) DEFAULT NULL COMMENT '数据日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_tsm_id` (`id`),
  KEY `i_tsm_createDate` (`create_date`) USING BTREE,
  KEY `i_tsm_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_stock_ma` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
