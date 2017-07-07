package com.xionger.qcb.model;

import java.awt.Menu;
import java.math.BigDecimal;

public class StockResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String codeName;

    private String resultType;

    private String resultTypeName;

    private BigDecimal newPrice;

    private BigDecimal amplitude;

    private BigDecimal amplitudePrice;

    private BigDecimal turnover;

    private BigDecimal totalMarketValue;

    private BigDecimal circulationValue;

    private BigDecimal realValue;

    private Integer bodiesNum;

    private String firstPartner;

    private BigDecimal stockRatio;

    private BigDecimal day5;

    private BigDecimal day10;

    private BigDecimal day20;



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

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType == null ? null : resultType.trim();
    }

    public String getResultTypeName() {
        return resultTypeName;
    }

    public void setResultTypeName(String resultTypeName) {
        this.resultTypeName = resultTypeName == null ? null : resultTypeName.trim();
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

    public BigDecimal getDay5() {
        return day5;
    }

    public void setDay5(BigDecimal day5) {
        this.day5 = day5;
    }

    public BigDecimal getDay10() {
        return day10;
    }

    public void setDay10(BigDecimal day10) {
        this.day10 = day10;
    }

    public BigDecimal getDay20() {
        return day20;
    }

    public void setDay20(BigDecimal day20) {
        this.day20 = day20;
    }
    
    
    public enum  ResultType{
    	TYPE_01("01","反包");
    	private String key;
    	private String value;
    	
    	private ResultType(String key,String value){
    		this.key=key;
    		this.value=value;
    	}
    	// 普通方法  
        public static String getValue(String key) {  
            for (ResultType rt : ResultType.values()) {  
                if (rt.getKey().equals(key)) {  
                    return rt.getValue();  
                }  
            }  
            return null;  
        } 
    	public String getKey() {  
            return key;  
        }  
        public void setKey(String key) {  
            this.key = key;  
        }  
        public String getValue() {  
            return value;  
        }  
        public void setValue(String value) {  
            this.value = value;  
        }  
    	
    }

}