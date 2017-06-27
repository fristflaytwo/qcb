package com.xionger.qcb.model;

import java.math.BigDecimal;

/**
 * 股票均线计算统计表
 * @author admin
 *
 */
public class StockMa  extends BaseEntity {
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String codeName;

    private BigDecimal day5;

    private BigDecimal day10;

    private BigDecimal day20;

    private BigDecimal week5;

    private BigDecimal week10;

    private BigDecimal week20;

   

   

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
		this.codeName = codeName;
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

    public BigDecimal getWeek5() {
        return week5;
    }

    public void setWeek5(BigDecimal week5) {
        this.week5 = week5;
    }

    public BigDecimal getWeek10() {
        return week10;
    }

    public void setWeek10(BigDecimal week10) {
        this.week10 = week10;
    }

    public BigDecimal getWeek20() {
        return week20;
    }

    public void setWeek20(BigDecimal week20) {
        this.week20 = week20;
    }

   
}