package com.rs.wxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rs.wxmgr.dao.TestDao;
import com.rs.wxmgr.entity.WelcomeMsg;

@Service
@Transactional(rollbackFor=Throwable.class)
public class TestService {

	@Autowired
	private TestDao testDao;
	
	public List<WelcomeMsg> selectMessageList() {
		return testDao.selectMessageList();
	}
	
	public void insertMessage(String message) {
		testDao.insertMessage(message);
	}
}
