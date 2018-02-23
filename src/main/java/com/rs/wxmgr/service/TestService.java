package com.rs.wxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.rs.wxmgr.dao.TestDao;
import com.rs.wxmgr.entity.User;

@Service
@Transactional(rollbackFor=Throwable.class)
public class TestService {

	@Autowired
	private TestDao testDao;
	
	public List<User> selectUserList(PageBounds pageBounds) {
		return testDao.selectUserList(pageBounds);
	}
	 
}