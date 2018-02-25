package com.rs.wxmgr.entity;

public class WelcomeMsg {

	private Integer id;
	private String message;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "WelcomeMsg [id=" + id + ", message=" + message + "]";
	}
	
}
