package com.rs.wxmgr.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rs.wxmgr.entity.WelcomeMsg;

@Repository
public class TestDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<WelcomeMsg> selectMessageList() {
		return sqlSessionTemplate.selectList("WelcomeMsgMapper.selectMessageList");
	}
	
	public void insertMessage(String message) {
		WelcomeMsg welcomeMsg = new WelcomeMsg();
		welcomeMsg.setMessage(message);
		sqlSessionTemplate.insert("WelcomeMsgMapper.insertMessage", welcomeMsg);
	}
	
}
