package com.rs.wxmgr.action.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * json回复封装类
 * @author sun_zeming
 *
 */
public class TXResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int SUCCESS_CODE = 0;
	public static final String SUCCESS_MSG = "success";
	
	private JSONObject data = new JSONObject();;
	public void setCode(int code) {
		data.put("code", code);
	}
	public void setMsg(String msg) {
		data.put("msg", msg);
	}
	/**
	 * 获取json对象
	 * @return
	 */
	public JSONObject getData() {
		return data;
	}
	/**
	 * 向json对象中添加键值对
	 * @param key
	 * @param value
	 */
	public void put(String key,Object value) {
		data.put(key, value);
	}
	@Override
	public String toString() {
		return data.toJSONString();
	}
	
}
