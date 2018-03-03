package com.rs.wxmgr.wechat.entity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Group {

	private String username;
	private String nickname;
	private String owner;
	private List<Member> memnerList;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public List<Member> getMemnerList() {
		return memnerList;
	}
	public void setMemnerList(List<Member> memnerList) {
		this.memnerList = memnerList;
	}
	public Member getMember(String username) {
		for(Member sayMember : memnerList) {
			if(sayMember.getUsername().equals(username)) {
				return sayMember;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return "Group [username=" + username + ", nickname=" + nickname + ", owner=" + owner + ", memnerList="
				+ memnerList + "]";
	}
	public static Group parse(JSONObject json) {
		Group group = new Group();
		group.setUsername(json.getString("UserName"));
		group.setNickname(json.getString("NickName"));
		group.setMemnerList(Member.parseList(json.getJSONArray("MemberList")));
		return group;
	}
	public static List<Group> parseList(JSONArray jsonArray) {
		List<Group> list = new ArrayList<Group>();
		List<JSONObject> jsonList = JSONArray.parseArray(jsonArray.toJSONString(), JSONObject.class);
		for(JSONObject json:jsonList) {
			list.add(Group.parse(json));
		}
		return list;
	}
	
}
