package com.rs.wxmgr.wechat.listener.impl;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.entity.WelcomeMsg;
import com.rs.wxmgr.service.TestService;
import com.rs.wxmgr.wechat.common.WXContact;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.entity.Group;
import com.rs.wxmgr.wechat.listener.SyncCheckListener;
import com.rs.wxmgr.wechat.utils.InforUtils;
import com.rs.wxmgr.wechat.utils.MessageUtils;

/**
 * 群人数变动监听器
 * @author zm
 *
 */
public class GroupMemberChangeListener implements SyncCheckListener{

	private WXContact contact;
	private WXHttpClient client;
	private TestService testService;
	public GroupMemberChangeListener(WXHttpClient client,WXContact contact,TestService testServie) {
		this.client = client;
		this.contact = contact;
		this.testService = testServie;
	}

	public void handle(JSONObject json) {
		// 消息
		Integer addMsgCount = json.getInteger("AddMsgCount");
		if(addMsgCount != null && addMsgCount>0) {
			List<JSONObject> msgJsonList = JSONArray.parseArray(json.getString("AddMsgList"), JSONObject.class);
			for(JSONObject msgJson:msgJsonList) {
				String content = msgJson.getString("Content");
				String fromUsername = msgJson.getString("FromUserName");
				
				// 消息类型，10000:群成员变动
				Integer msgType = msgJson.getInteger("MsgType");
				if(msgType == null || !msgType.equals(10000)) {
					continue;
				}
				
				if(StringUtils.isBlank(fromUsername) || StringUtils.isBlank(content)) {
					continue;
				}
				
				if(fromUsername.startsWith("@@")) {
					// 群成员变动
					Group oldGroup = contact.getGroup(fromUsername);
					if(oldGroup == null) {
						continue;
					}
					
					try {
						Group group = InforUtils.getGroupContact(client, fromUsername);
						// 群人数增加
						if(group != null && oldGroup.getMemnerList().size() < group.getMemnerList().size()) {
							MessageUtils.sendMessageByUsername(client, fromUsername, getWelcomeMessage());
						}
						contact.putGroup(group);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
	
	private String getWelcomeMessage() {
		List<WelcomeMsg> messageList = testService.selectMessageList();
		int index = new Random().nextInt(messageList.size());
		if(index!=0&&index==messageList.size()) index--;
		return messageList.get(index).getMessage();
	}

}
