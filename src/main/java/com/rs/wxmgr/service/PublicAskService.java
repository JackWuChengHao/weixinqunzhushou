package com.rs.wxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.rs.wxmgr.dao.PublicAskDao;
import com.rs.wxmgr.wechat.entity.PublicAsk;

@Service
@Transactional(rollbackFor=Throwable.class)
public class PublicAskService {
	@Autowired
	private PublicAskDao publicAskDao;
	
	public List<PublicAsk> selectPublicAskList(PageBounds pageBounds) {
		return publicAskDao.selectPublicAskList(pageBounds);
	}
	
	public void insertPublicAsk(PublicAsk publicAsk) {
		publicAskDao.insertPublicAsk(publicAsk);
	}
}
