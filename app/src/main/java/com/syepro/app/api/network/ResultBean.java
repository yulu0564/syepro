package com.syepro.app.api.network;


import java.io.Serializable;

/*
 * 文件名: Result.java
 * 描    述: 请求返回结果对象
 * 参   考: http://www.cnblogs.com/qq78292959/p/3781808.html
 * 
 * */
public class ResultBean<T> implements Serializable {

	private int code;

	private String msg;

	/**
	 * 数据
	 */
	private T data;

	public ResultBean() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
