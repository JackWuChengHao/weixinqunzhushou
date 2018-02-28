package com.rs.wxmgr.wechat.entity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Member {

	private String username;
	private String nickname;
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
	@Override
	public String toString() {
		return "Member [username=" + username + ", nickname=" + nickname + "]";
	}
	public static Member parse(JSONObject json) {
		Member member = new Member();
		member.setUsername(json.getString("UserName"));
		member.setNickname(json.getString("NickName"));
		return member;
	}
	public static List<Member> parseList(JSONArray jsonArray) {
		List<Member> list = new ArrayList<Member>();
		List<JSONObject> jsonList = JSONArray.parseArray(jsonArray.toJSONString(), JSONObject.class);
		for(JSONObject json:jsonList) {
			list.add(Member.parse(json));
		}
		return list;
	}
}
