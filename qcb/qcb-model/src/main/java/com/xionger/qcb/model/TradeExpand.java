package com.xionger.qcb.model;

import java.math.BigDecimal;

public class TradeExpand extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String codeName;

    private BigDecimal turnover;

    private BigDecimal amplitude;

    private BigDecimal circulationValue;

    private BigDecimal totalMarketValue;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName == null ? null : codeName.trim();
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public BigDecimal getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(BigDecimal amplitude) {
        this.amplitude = amplitude;
    }

    public BigDecimal getCirculationValue() {
        return circulationValue;
    }

    public void setCirculationValue(BigDecimal circulationValue) {
        this.circulationValue = circulationValue;
    }

    public BigDecimal getTotalMarketValue() {
        return totalMarketValue;
    }

    public void setTotalMarketValue(BigDecimal totalMarketValue) {
        this.totalMarketValue = totalMarketValue;
    }

}