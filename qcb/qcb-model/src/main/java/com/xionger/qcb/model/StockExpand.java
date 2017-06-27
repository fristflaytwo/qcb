package com.xionger.qcb.model;



import java.math.BigDecimal;

public class StockExpand extends BaseEntity {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String codeName;

    private BigDecimal turnover;

    private BigDecimal totalMarketValue;

    private BigDecimal circulationValue;

    private BigDecimal realValue;

    private Integer bodiesNum;

    private String firstPartner;

    private BigDecimal stockRatio;

    private String businessAddress;


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

    public BigDecimal getTotalMarketValue() {
        return totalMarketValue;
    }

    public void setTotalMarketValue(BigDecimal totalMarketValue) {
        this.totalMarketValue = totalMarketValue;
    }

    public BigDecimal getCirculationValue() {
        return circulationValue;
    }

    public void setCirculationValue(BigDecimal circulationValue) {
        this.circulationValue = circulationValue;
    }

    public BigDecimal getRealValue() {
        return realValue;
    }

    public void setRealValue(BigDecimal realValue) {
        this.realValue = realValue;
    }

    public Integer getBodiesNum() {
        return bodiesNum;
    }

    public void setBodiesNum(Integer bodiesNum) {
        this.bodiesNum = bodiesNum;
    }

    public String getFirstPartner() {
        return firstPartner;
    }

    public void setFirstPartner(String firstPartner) {
        this.firstPartner = firstPartner == null ? null : firstPartner.trim();
    }

    public BigDecimal getStockRatio() {
        return stockRatio;
    }

    public void setStockRatio(BigDecimal stockRatio) {
        this.stockRatio = stockRatio;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress == null ? null : businessAddress.trim();
    }

}