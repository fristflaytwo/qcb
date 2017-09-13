package com.xionger.qcb.model;

import java.math.BigDecimal;

public class StockInfo extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String codeName;

    private String companyName;

    private String registerAddress;

    private String chairman;

    private String secretary;

    private String mainBusiness;

    private String controlPartner;

    private String factControl;

    private String finalControl;

    private String marketDate;

    private BigDecimal lssuePrice;

    private BigDecimal lssuePe;

    private String businessScope;

    private String productType;

    private String productName;



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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress == null ? null : registerAddress.trim();
    }

    public String getChairman() {
        return chairman;
    }

    public void setChairman(String chairman) {
        this.chairman = chairman == null ? null : chairman.trim();
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary == null ? null : secretary.trim();
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness == null ? null : mainBusiness.trim();
    }

    public String getControlPartner() {
        return controlPartner;
    }

    public void setControlPartner(String controlPartner) {
        this.controlPartner = controlPartner == null ? null : controlPartner.trim();
    }

    public String getFactControl() {
        return factControl;
    }

    public void setFactControl(String factControl) {
        this.factControl = factControl == null ? null : factControl.trim();
    }

    public String getFinalControl() {
        return finalControl;
    }

    public void setFinalControl(String finalControl) {
        this.finalControl = finalControl == null ? null : finalControl.trim();
    }

    public String getMarketDate() {
        return marketDate;
    }

    public void setMarketDate(String marketDate) {
        this.marketDate = marketDate == null ? null : marketDate.trim();
    }

    public BigDecimal getLssuePrice() {
        return lssuePrice;
    }

    public void setLssuePrice(BigDecimal lssuePrice) {
        this.lssuePrice = lssuePrice;
    }

    public BigDecimal getLssuePe() {
        return lssuePe;
    }

    public void setLssuePe(BigDecimal lssuePe) {
        this.lssuePe = lssuePe;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope == null ? null : businessScope.trim();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

}