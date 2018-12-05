package com.zealot.sentinel.web.result;

import java.io.Serializable;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResultResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6315323674589215331L;

	private Integer code = 0;
	
	private String msg = "成功";
	
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
