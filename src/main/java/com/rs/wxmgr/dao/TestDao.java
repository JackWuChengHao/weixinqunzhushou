package com.rs.wxmgr.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.rs.wxmgr.entity.User;

@Repository
public class TestDao {

//	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<User> selectUserList(PageBounds pageBounds) {
		return sqlSessionTemplate.selectList("UserMapper.selectUserList",null,pageBounds);
	}
	
}
