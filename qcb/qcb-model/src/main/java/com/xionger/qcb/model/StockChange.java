package com.xionger.qcb.model;

public class StockChange extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8363341990263683071L;

	private String code;

	private String codeName;

	private String changeType;

	private String changeTypeName;

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

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType == null ? null : changeType.trim();
	}

	public String getChangeTypeName() {
		return changeTypeName;
	}

	public void setChangeTypeName(String changeTypeName) {
		this.changeTypeName = changeTypeName == null ? null : changeTypeName
				.trim();
	}

}