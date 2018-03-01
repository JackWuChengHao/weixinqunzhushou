package com.rs.wxmgr.wechat.listener.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXContact;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.entity.Group;
import com.rs.wxmgr.wechat.entity.Member;
import com.rs.wxmgr.wechat.listener.SyncCheckListener;
import com.rs.wxmgr.wechat.utils.MessageUtils;

/**
 * 新消息监听器
 * @author zm
 *
 */
public class NewMessageListener implements SyncCheckListener{

	private WXContact contact;
	private WXHttpClient client;
	public NewMessageListener(WXHttpClient client,WXContact contact) {
		this.contact = contact;
		this.client = client;
	}

	public void handle(JSONObject json) {
		// 消息
		Integer addMsgCount = json.getInteger("AddMsgCount");
		if(addMsgCount != null && addMsgCount>0) {
			System.out.println(json.toJSONString());
			List<JSONObject> msgJsonList = JSONArray.parseArray(json.getString("AddMsgList"), JSONObject.class);
			for(JSONObject msgJson:msgJsonList) {
				String content = msgJson.getString("Content");
				String fromUsername = msgJson.getString("FromUserName");
				
				// 消息类型，1:文本消息;47:图片消息
				Integer msgType = msgJson.getInteger("MsgType");
				if(msgType == null || (!msgType.equals(1) && msgType.equals(47))) {
					continue;
				}
				
				if(StringUtils.isBlank(fromUsername) || StringUtils.isBlank(content)) {
					continue;
				}
				if(fromUsername.startsWith("@@") && content.contains(":<br/>")) {
					// 群消息
					Group group = contact.getGroup(fromUsername);
					if(group == null) {
						continue;
					}
					// 发言者
					String sayUsername = content.split(":<br/>")[0];
					// 发言内容
					String sayContent = content.split(":<br/>")[1];
					for(Member sayMember : group.getMemnerList()) {
						if(sayMember.getUsername().equals(sayUsername)) {
							System.out.println(String.format("%s的群消息, %s : %s", group.getNickname(),sayMember.getNickname(),sayContent));
							if(sayContent.startsWith("@"+client.getMyAccount().getNickName())) {
								try {
									MessageUtils.sendMessageByUsername(client, group.getUsername(), "@我干嘛");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} else if (fromUsername.startsWith("@")) {
					// 好友消息
					Member member = contact.getMember(fromUsername);
					if(member != null) {
						System.out.println(String.format("好友消息 %s : %s", member.getNickname(),content));
					}
				}
			}
		}
		
	}

}
