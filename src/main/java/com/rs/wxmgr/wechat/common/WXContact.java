package com.rs.wxmgr.wechat.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.entity.Group;
import com.rs.wxmgr.wechat.entity.Member;

public class WXContact implements Serializable {

    private static final long serialVersionUID = 2488496462459062123L;

    /**
     * 用户
     */
    private Map<String,Member> memberMap = new HashMap<String, Member>();
    /**
     * 组群
     */
    private Map<String,Group> groupMap = new HashMap<String, Group>();
    
    private Set<String> groupSet = new HashSet<String>();
    
    public List<Member> getMemberList() {
        return new ArrayList<Member>(memberMap.values());
    }
    public Member getMember(String username) {
    	return memberMap.get(username);
    }
    public void putMember(Member member) {
    	memberMap.put(member.getUsername(), member);
    }
    public List<Group> getGroupList() {
		return new ArrayList<Group>(groupMap.values());
	}
	public Group getGroup(String username) {
		return groupMap.get(username);
	}
	public void addGroupSet(String username) {
		groupSet.add(username);
	}
	public Set<String> getGroupSet() {
		return groupSet;
	}
	public void putGroup(Group group) {
		if(group!=null && group.getMemnerList()!= null &&
				group.getMemnerList().size() == 0) {
			groupSet.remove(group.getUsername());
			groupMap.remove(group.getUsername());
		} else {
			groupSet.add(group.getUsername());
			groupMap.put(group.getUsername(), group);
		}
	}
	
	public void initPut(JSONArray jsonArray) {
		List<JSONObject> jsonList = JSONArray.parseArray(jsonArray.toJSONString(), JSONObject.class);
		for(JSONObject json:jsonList) {
			String username = json.getString("UserName");
			if(username.startsWith("@@")) {
				// 组群
				Group group = Group.parse(json);
				putGroup( group);
			} else {
				// 用户
				Member member = Member.parse(json);
				putMember(member);
			}
		}
	}
}
