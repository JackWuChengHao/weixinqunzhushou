package com.rs.wxmgr.wechat.listener;

import com.alibaba.fastjson.JSONObject;

public interface SyncCheckListener {

	public void handle(JSONObject json);
}
