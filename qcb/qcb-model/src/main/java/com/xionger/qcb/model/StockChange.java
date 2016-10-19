package com.xionger.qcb.model;

import java.math.BigDecimal;

/**
 * 股票移动监控表
 * @author admin
 *
 */
public class StockChange extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2018462007988211747L;


	private String code;

    private String codename;

    private String changetype;

    private String stockdate;

    private BigDecimal price;

    private String stockdate1;

    private BigDecimal price1;

    private String stockdate2;

    private BigDecimal price2;

    private String stockdate3;

    private BigDecimal price3;

    private String stockdate4;

    private BigDecimal price4;

    private String stockdate5;

    private BigDecimal price5;

    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename == null ? null : codename.trim();
    }

    public String getChangetype() {
        return changetype;
    }

    public void setChangetype(String changetype) {
        this.changetype = changetype == null ? null : changetype.trim();
    }

    public String getStockdate() {
        return stockdate;
    }

    public void setStockdate(String stockdate) {
        this.stockdate = stockdate == null ? null : stockdate.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStockdate1() {
        return stockdate1;
    }

    public void setStockdate1(String stockdate1) {
        this.stockdate1 = stockdate1 == null ? null : stockdate1.trim();
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public String getStockdate2() {
        return stockdate2;
    }

    public void setStockdate2(String stockdate2) {
        this.stockdate2 = stockdate2 == null ? null : stockdate2.trim();
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public String getStockdate3() {
        return stockdate3;
    }

    public void setStockdate3(String stockdate3) {
        this.stockdate3 = stockdate3 == null ? null : stockdate3.trim();
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public String getStockdate4() {
        return stockdate4;
    }

    public void setStockdate4(String stockdate4) {
        this.stockdate4 = stockdate4 == null ? null : stockdate4.trim();
    }

    public BigDecimal getPrice4() {
        return price4;
    }

    public void setPrice4(BigDecimal price4) {
        this.price4 = price4;
    }

    public String getStockdate5() {
        return stockdate5;
    }

    public void setStockdate5(String stockdate5) {
        this.stockdate5 = stockdate5 == null ? null : stockdate5.trim();
    }

    public BigDecimal getPrice5() {
        return price5;
    }

    public void setPrice5(BigDecimal price5) {
        this.price5 = price5;
    }
}