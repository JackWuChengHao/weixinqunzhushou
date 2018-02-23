package com.rs.wxmgr.common.content;

/**
 * 错误码枚举
 * @author sun_zeming
 *
 */
public enum TXErrorCode {

	/**
	 * 系统错误
	 */
	SYSTEMRROR(1,"系统错误");
	private int code;
	private String msg;
	private TXErrorCode(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
