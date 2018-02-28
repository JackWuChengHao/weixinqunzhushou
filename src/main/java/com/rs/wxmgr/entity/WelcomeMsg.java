package com.rs.wxmgr.entity;

import java.io.Serializable;

public class WelcomeMsg implements Serializable{

	private static final long serialVersionUID = -7933572995048408702L;
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
