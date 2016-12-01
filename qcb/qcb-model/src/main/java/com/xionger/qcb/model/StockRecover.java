package com.xionger.qcb.model;

public class StockRecover extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 224365562208328344L;

	private String code;

	private String codeName;

	private String recoverType;

	private String recoverTypeName;

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

	public String getRecoverType() {
		return recoverType;
	}

	public void setRecoverType(String recoverType) {
		this.recoverType = recoverType == null ? null : recoverType.trim();
	}

	public String getRecoverTypeName() {
		return recoverTypeName;
	}

	public void setRecoverTypeName(String recoverTypeName) {
		this.recoverTypeName = recoverTypeName == null ? null : recoverTypeName
				.trim();
	}

}