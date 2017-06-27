package com.xionger.qcb.model;


public class StockRecover extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private String codeName;

	private String recoverType;

	private String recoverTypeName;
	
	//必须底部
	public enum RecoverType {
		// 利用构造函数传参
		RECOVER_01("01", "底部锤子线"),
		RECOVER_02("02", "底部到锤子线"),
		RECOVER_03("03", "组合锤子线"),
		RECOVER_04("04", "长腿十字"),
		RECOVER_05("05", "螺旋桨"),
		RECOVER_06("06", "孕线"),
		RECOVER_07("07", "阳包阴"),
		RECOVER_08("08", "岛型反转");//未开发，没必要
		
		private String key;
		private String val;

		// 构造函数，枚举类型只能为私有
		private RecoverType(String key, String val) {
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
			RecoverType[] enums = RecoverType.values();
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