package com.rs.wxmgr.wechat.entity;

import java.io.Serializable;

public class PublicAsk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2986064139515591669L;

	private Integer id;
	private String name;
	private String personalId;
	private String phone;
	private String content;
	private String voiceGroup;
	private String voiceIndex;
	private String picGroup;
	private String picIndex;
	private String askType;
	private String createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getVoiceGroup() {
		return voiceGroup;
	}
	public void setVoiceGroup(String voiceGroup) {
		this.voiceGroup = voiceGroup;
	}
	public String getVoiceIndex() {
		return voiceIndex;
	}
	public void setVoiceIndex(String voiceIndex) {
		this.voiceIndex = voiceIndex;
	}
	public String getPicGroup() {
		return picGroup;
	}
	public void setPicGroup(String picGroup) {
		this.picGroup = picGroup;
	}
	public String getPicIndex() {
		return picIndex;
	}
	public void setPicIndex(String picIndex) {
		this.picIndex = picIndex;
	}
	public String getAskType() {
		return askType;
	}
	public void setAskType(String askType) {
		this.askType = askType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
