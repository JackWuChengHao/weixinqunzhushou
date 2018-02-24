package com.rs.wxmgr.wechat.common;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class Account implements Serializable {

    private static final long serialVersionUID = -8484614277609913861L;
    @JSONField(name="Uin") private String uin;
    @JSONField(name="UserName") private String userName;
    @JSONField(name="NickName") private String nickName;
    @JSONField(name="HeadImgUrl") private String headImgUrl;
    @JSONField(name="RemarkName") private String remarkName;
    @JSONField(name="PYInitial") private String pyInitial;
    @JSONField(name="PYQuanPin") private String pyQuanPin;
    @JSONField(name="RemarkPYInitial") private String remarkPyInitial;
    @JSONField(name="RemarkPYQuanPin") private String remarkPyQuanPin;
    @JSONField(name="StarFriend") private String starFriend;
    @JSONField(name="Signature") private String signature;
    @JSONField(name="Sex") private String sex;
    @JSONField(name="AppAccountFlag") private String appAccountFlag;
    @JSONField(name="HeadImgFlag") private String headImgFlag;
    
    public String getUserName() {
        return userName;
    }
    public String getUin() {
        return uin;
    }
    public void setUin(String uin) {
        this.uin = uin;
    }
    public String getRemarkName() {
        return remarkName;
    }
    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }
    public String getPyInitial() {
        return pyInitial;
    }
    public void setPyInitial(String pyInitial) {
        this.pyInitial = pyInitial;
    }
    public String getPyQuanPin() {
        return pyQuanPin;
    }
    public void setPyQuanPin(String pyQuanPin) {
        this.pyQuanPin = pyQuanPin;
    }
    public String getRemarkPyInitial() {
        return remarkPyInitial;
    }
    public void setRemarkPyInitial(String remarkPyInitial) {
        this.remarkPyInitial = remarkPyInitial;
    }
    public String getRemarkPyQuanPin() {
        return remarkPyQuanPin;
    }
    public void setRemarkPyQuanPin(String remarkPyQuanPin) {
        this.remarkPyQuanPin = remarkPyQuanPin;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getStarFriend() {
        return starFriend;
    }
    public void setStarFriend(String starFriend) {
        this.starFriend = starFriend;
    }
    public String getHeadImgUrl() {
        return headImgUrl;
    }
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAppAccountFlag() {
        return appAccountFlag;
    }
    public void setAppAccountFlag(String appAccountFlag) {
        this.appAccountFlag = appAccountFlag;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getHeadImgFlag() {
        return headImgFlag;
    }
    public void setHeadImgFlag(String headImgFlag) {
        this.headImgFlag = headImgFlag;
    }
    
}
