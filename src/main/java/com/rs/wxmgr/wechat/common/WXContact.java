package com.rs.wxmgr.wechat.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class WXContact implements Serializable {

    private static final long serialVersionUID = 2488496462459062123L;

    private static final Set<String> SPECIAL_USERS = new HashSet<String>(Arrays.asList(
            "newsapp", "fmessage", "filehelper", "weibo", "qqmail",
            "fmessage", "tmessage", "qmessage", "qqsync", "floatbottle",
            "lbsapp", "shakeapp", "medianote", "qqfriend", "readerapp",
            "blogapp", "facebookapp", "masssendapp", "meishiapp",
            "feedsapp", "voip", "blogappweixin", "weixin", "brandsessionholder",
            "weixinreminder", "wxid_novlwrv3lqwv11", "gh_22b87fa7cb3c",
            "officialaccounts", "notification_messages", "wxid_novlwrv3lqwv11",
            "gh_22b87fa7cb3c", "wxitil", "userexperience_alarm", "notification_messages"));
    
    private List<JSONObject> memberList;
    
    public List<JSONObject> getMemberList() {
        return memberList;
    }
    public void setMemberList(List<JSONObject> memberList) {
        this.memberList = memberList;
    }
    
    /**
     * 公众号
     * @return
     */
    @JSONField(serialize=false)
    public List<JSONObject> getPublicList() {
        
        List<JSONObject> result = new ArrayList<JSONObject>();
        if(memberList != null)
            for(JSONObject contact : this.memberList)
                if((contact.getIntValue("VerifyFlag") & 8) != 0)
                    result.add(contact);
        return result;
    }
    
    /**
     * 特殊账户
     * @return
     */
    @JSONField(serialize=false)
    public List<JSONObject> getSpecialList() {
        
        List<JSONObject> result = new ArrayList<JSONObject>();
        if(memberList != null)
            for(JSONObject contact : this.memberList)
                if(SPECIAL_USERS.contains(contact.getString("UserName")))
                    result.add(contact);
        return result;
    }
    
    /**
     * 群聊
     * @return
     */
    @JSONField(serialize=false)
    public List<JSONObject> getGroupList() {
        
        List<JSONObject> result = new ArrayList<JSONObject>();
        if(memberList != null)
            for(JSONObject contact : this.memberList)
                if(contact.getString("UserName").indexOf("@@") != -1)
                    result.add(contact);
        return result;
    }
    
    /**
     * 好友
     * @return
     */
    @JSONField(serialize=false)
    public List<JSONObject> getContactList() {
        
        List<JSONObject> result = new ArrayList<JSONObject>();
        if(memberList != null)
            for(JSONObject contact : this.memberList)
                if((contact.getIntValue("VerifyFlag") & 8) == 0
                && !SPECIAL_USERS.contains(contact.getString("UserName"))
                && contact.getString("UserName").indexOf("@@") == -1)
                    result.add(contact);
        return result;
    }
}
