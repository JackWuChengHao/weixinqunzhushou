package com.rs.wxmgr.wechat.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class GroupUtils {

	/**
	 * 获取人数增加的组群username
	 * @param oldGoupList
	 * @param currGroupList
	 * @return
	 */
	public static List<String> getGroupListForUserAdd(List<JSONObject> oldGoupList,List<JSONObject> currGroupList){
		List<String> usernameList = new ArrayList<String>();
		if(oldGoupList == null) {
			return usernameList;
		}
		for(JSONObject currGroup:currGroupList) {
			String currUsername = currGroup.getString("UserName");
			for(JSONObject oldGroup:oldGoupList) {
				String oldUsername = oldGroup.getString("UserName");
				if(oldUsername.equals(currUsername)) {
					Integer currMemberCount = currGroup.getInteger("MemberCount");
					Integer oldMemberCount = oldGroup.getInteger("MemberCount");
					
					// 组群人数增加
					if(currMemberCount!=null && oldMemberCount != null 
						&& currMemberCount.compareTo(oldMemberCount)>0) {
						usernameList.add(currUsername);
					}
				}
			}
		}
		return usernameList;
	}
}
