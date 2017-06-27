package com.xionger.qcb.model;

import java.math.BigDecimal;

/**
 * 每日跑批数据信息表
 * 
 * @author Administrator
 * 
 */
public class Stock extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code; // 股票代码
	private String codeName; // 股票名称
	private BigDecimal newPrice; // 最新价
	private BigDecimal amplitude; // 涨跌幅
	private BigDecimal amplitudePrice; // 涨跌额
	private BigDecimal buyPrice; // 买入
	private BigDecimal salePrice; // 卖出
	private Long dealVol; // 成交量
	private BigDecimal dealPrice; // 成交额
	private BigDecimal todayOpen; // 今开
	private BigDecimal yeatedayClose; // 昨收
	private BigDecimal heightPrice; // 最高
	private BigDecimal lowPrice; // 最低

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public BigDecimal getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(BigDecimal newPrice) {
		this.newPrice = newPrice;
	}

	public BigDecimal getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(BigDecimal amplitude) {
		this.amplitude = amplitude;
	}

	public BigDecimal getAmplitudePrice() {
		return amplitudePrice;
	}

	public void setAmplitudePrice(BigDecimal amplitudePrice) {
		this.amplitudePrice = amplitudePrice;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Long getDealVol() {
		return dealVol;
	}

	public void setDealVol(Long dealVol) {
		this.dealVol = dealVol;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public BigDecimal getTodayOpen() {
		return todayOpen;
	}

	public void setTodayOpen(BigDecimal todayOpen) {
		this.todayOpen = todayOpen;
	}

	public BigDecimal getYeatedayClose() {
		return yeatedayClose;
	}

	public void setYeatedayClose(BigDecimal yeatedayClose) {
		this.yeatedayClose = yeatedayClose;
	}

	public BigDecimal getHeightPrice() {
		return heightPrice;
	}

	public void setHeightPrice(BigDecimal heightPrice) {
		this.heightPrice = heightPrice;
	}

	public BigDecimal getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(BigDecimal lowPrice) {
		this.lowPrice = lowPrice;
	}


}
