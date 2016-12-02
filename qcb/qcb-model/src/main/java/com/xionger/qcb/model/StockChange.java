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
	
	public enum ChangeType {
		// 利用构造函数传参
		CHANGETYPE_01("01", "跳空长阳"),
		CHANGETYPE_02("02", "跳空长阴"),
		CHANGETYPE_03("03", "七连阳"),
		CHANGETYPE_04("04", "上吊流星"),
		CHANGETYPE_05("05", "揉搓线");
		private String key;
		private String val;

		// 构造函数，枚举类型只能为私有
		private ChangeType(String key, String val) {
			this.key = key;
			this.val = val;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}

		public static String getChannelValueByKey(String key) {
			ChangeType[] enums = ChangeType.values();
			for (int i = 0; i < enums.length; i++) {
				if (enums[i].getKey().equals(key)) {
					return enums[i].getVal();
				}
			}
			return "";
		}
	}

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