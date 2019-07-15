package com.jadeStoneStronger.SSO.base.config.exception;

public class PageExeption extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageExeption() {
		super();
	}

	public PageExeption(String message, Throwable cause) {
		super(message, cause);
	}

	public PageExeption(String message) {
		super(message);
	}

	public PageExeption(Throwable cause) {
		super(cause);
	}
}
