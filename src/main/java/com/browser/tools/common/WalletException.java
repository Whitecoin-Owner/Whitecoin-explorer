package com.browser.tools.common;

public class WalletException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private String retCode;

	private String retMsg;

	public WalletException() {
		super();
	}

	public WalletException(String retMsg) {
		super();
		this.retMsg = retMsg;
	}

	public WalletException(String retCode, String retMsg) {
		super();
		this.retCode = retCode;
		this.retMsg = retMsg;
	}

	public String getRetCode() {
		return retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}
}
