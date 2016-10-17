package com.xionger.qcb.model;

import java.math.BigDecimal;

/**
 * 经过筛选得到的股票
 * @author admin
 *
 */
public class StockResult   extends BaseEntity {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = -7215638301108417561L;

	private String code;

    private String codename;

    private String channeltype;

    private BigDecimal maday5;

    private BigDecimal maday10;

    private BigDecimal maday20;

    private BigDecimal newprice;

    private BigDecimal heightprice;

   


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

    public String getChanneltype() {
        return channeltype;
    }

    public void setChanneltype(String channeltype) {
        this.channeltype = channeltype == null ? null : channeltype.trim();
    }

    public BigDecimal getMaday5() {
        return maday5;
    }

    public void setMaday5(BigDecimal maday5) {
        this.maday5 = maday5;
    }

    public BigDecimal getMaday10() {
        return maday10;
    }

    public void setMaday10(BigDecimal maday10) {
        this.maday10 = maday10;
    }

    public BigDecimal getMaday20() {
        return maday20;
    }

    public void setMaday20(BigDecimal maday20) {
        this.maday20 = maday20;
    }

    public BigDecimal getNewprice() {
        return newprice;
    }

    public void setNewprice(BigDecimal newprice) {
        this.newprice = newprice;
    }

    public BigDecimal getHeightprice() {
        return heightprice;
    }

    public void setHeightprice(BigDecimal heightprice) {
        this.heightprice = heightprice;
    }

   
}