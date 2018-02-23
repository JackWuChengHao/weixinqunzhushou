package com.rs.wxmgr.common.exception;

public class TXException extends RuntimeException {

	private static final long serialVersionUID = -3317154699264181877L;

	public TXException() {
	}
    public TXException(String message) {
        super(message);
    }
    public TXException(String message, Throwable cause) {
        super(message, cause);
    }
    public TXException(Throwable cause) {
        super(cause);
    }
}
