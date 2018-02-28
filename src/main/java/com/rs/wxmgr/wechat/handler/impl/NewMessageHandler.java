package com.rs.wxmgr.wechat.handler.impl;

import com.alibaba.fastjson.JSONObject;

public class NewMessageHandler implements BaseHandler {

	public void handle(JSONObject json) {
		System.out.println("新消息"+json);
	}

}
