package com.xionger.qcb.common.enums;

public enum ResultEnum {
	
	RESULT_CODE_0000 ("0000","处理成功"),
	RESULT_CODE_9999 ("9999","处理失败");
	
	
	private String key;
	private String val;
	
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

	private ResultEnum(String key,String val){
		this.key=key;
		this.val=val;
	}
}
