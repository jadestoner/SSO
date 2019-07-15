package com.jadeStoneStronger.SSO.base.wrapper;

import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 *  通用返回值
 * 
 * @author 
 * @param <T>
 *            返回值类型
 */
public class RespMessage<T> {
	public static final int CODE_SUCCESS = 20000;
	/**
	 * 返回码
	 */
	private int code = 20000;
	/**
	 * 错误消息
	 */
	private String msg;
	/**
	 * 返回值内容
	 */
	private T content;
	/**
	 * 返回值为list时的总数
	 */
	private String totalCount;

	public RespMessage() {
		super();
	}

	public RespMessage(int code, T t) {
		this(code, null, t);
	}

	public RespMessage(int code, String msg, T t) {
		super();
		this.code = code;
		this.msg = msg;
		this.content = t;
	}
	
	public RespMessage(int code, String msg, T t,String totalCount) {
		super();
		this.code = code;
		this.msg = msg;
		this.content = t;
		this.totalCount = totalCount;
	}
	
	public boolean success() {
		return CODE_SUCCESS == getCode();
	}

	public int getCode() {
		return code;
	}

	public T getContent() {
		return content;
	}

	public String getMsg() {
		return msg;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
