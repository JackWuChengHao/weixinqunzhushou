package com.rs.wxmgr.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rs.wxmgr.entity.User;

@Repository
public class TestDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<User> selectUserList() {
		return sqlSessionTemplate.selectList("UserMapper.selectUserList");
	}
	
}
