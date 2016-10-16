package com.xionger.qcb.model.vo;


import java.util.List;

import com.xionger.qcb.common.enums.ResultEnum;



public class ResultVo implements java.io.Serializable {
	
	private static final long serialVersionUID = -2239462826180955897L;
	private String msg;
	private String code;
	private boolean success = true;
	private List<?> list;
	private Object data;
	
	public ResultVo(boolean success, String msg) {
		super();
		this.msg = msg;
		this.success = success;
	}
	
	public ResultVo(boolean success,String code, String msg) {
		super();
		this.msg = msg;
		this.code = code;
		this.success = success;
	}
	
	public ResultVo(boolean success,ResultEnum resultEnum) {
		super();
		this.msg = resultEnum.getVal();
		this.code = resultEnum.getKey();
		this.success = success;
	}

	public ResultVo() {
	}
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
