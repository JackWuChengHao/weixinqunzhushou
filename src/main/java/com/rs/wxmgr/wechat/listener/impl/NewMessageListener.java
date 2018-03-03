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
				String toUserName = msgJson.getString("ToUserName");
				
				// 消息类型，1:文本消息;47:图片消息
				Integer msgType = msgJson.getInteger("MsgType");
				if(msgType == null || (!msgType.equals(1) && msgType.equals(47))) {
					continue;
				}
				
				if(StringUtils.isBlank(fromUsername) || StringUtils.isBlank(content) || StringUtils.isBlank(toUserName)) {
					continue;
				}
				if(fromUsername.startsWith("@@") && content.contains(":<br/>")) {
					// 群消息
					handleMsgFromGroup(fromUsername, content);
				} else if (toUserName.startsWith("@@") && fromUsername.equals(client.getMyAccount().getUserName())) {
					// 机器人向群发送的消息
					handleMsgToGroup(toUserName, content);
				} else if (fromUsername.startsWith("@") && fromUsername.lastIndexOf("@@") == -1  &&
						!fromUsername.equals(client.getMyAccount().getUserName())) {
					// 好友消息
					handleMsgFromFriend(fromUsername, content);
				} else if (toUserName.startsWith("@") && fromUsername.lastIndexOf("@@") == -1 &&
						fromUsername.equals(client.getMyAccount().getUserName())) {
					// 向好友发送消息
					handleMasToFriend(toUserName, content);
				}
			}
		}
		
	}
	
	/**
	 * 处理群消息
	 * @param groupUsername
	 * @param content
	 */
	private void handleMsgFromGroup(String groupUsername,String content) {
		Group group = contact.getGroup(groupUsername);
		if(group == null) {
			return;
		}
		// 发言者
		String sayUsername = content.split(":<br/>")[0];
		// 发言内容
		String sayContent = content.split(":<br/>")[1];
		
		Member sayMember = group.getMember(sayUsername);
		if(sayMember == null) {
			return;
		}
		System.out.println(String.format("%s的群消息, %s : %s", group.getNickname(),sayMember.getNickname(),sayContent));
		
		// @后自动回复
		if(sayContent.contains("@"+client.getMyAccount().getNickName())) {
			try {
				MessageUtils.sendMessageByUsername(client, group.getUsername(), "@" + sayMember.getNickname() + " " + "@我干嘛");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 机器人向群发送的消息
	 * @param groupUsername
	 * @param content
	 */
	private void handleMsgToGroup(String groupUsername,String content) {
		Group group = contact.getGroup(groupUsername);
		if(group == null) {
			return;
		}
		System.out.println(String.format("%s的群消息, %s : %s", group.getNickname(),"我",content));
	}
	
	/**
	 * 好友发送的消息
	 * @param username
	 * @param content
	 */
	private void handleMsgFromFriend(String username,String content) {
		Member member = contact.getMember(username);
		if(member == null) {
			return;
		}
		System.out.println(String.format("好友消息 %s : %s", member.getNickname(),content));
	}
	
	/**
	 * 机器人向好友发送的消息
	 * @param username
	 * @param content
	 */
	private void handleMasToFriend(String username,String content) {
		Member member = contact.getMember(username);
		if(member == null) {
			return;
		}
		System.out.println(String.format("我发送的好友消息 %s : %s", member.getNickname(),content));
	}

}
