package com.rs.wxmgr.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.rs.wxmgr.wechat.entity.PublicAsk;

@Repository
public class PublicAskDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<PublicAsk> selectPublicAskList(PageBounds pageBounds) {
		return sqlSessionTemplate.selectList("PublicAskMapper.selectPublicAskList", null, pageBounds);
	}
	
	public PublicAsk selectPublicAskById(Integer id) {
		return sqlSessionTemplate.selectOne("PublicAskMapper.selectPublicAskById", id);
	}
	
	public void insertPublicAsk(PublicAsk publicAsk) {
		sqlSessionTemplate.insert("PublicAskMapper.insertPublicAsk", publicAsk);
	}
}
