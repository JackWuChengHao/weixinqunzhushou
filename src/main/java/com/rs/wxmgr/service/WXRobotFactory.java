package com.rs.wxmgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rs.wxmgr.template.SolrQuestionTempldate;
import com.rs.wxmgr.wechat.Robot;

@Component
public class WXRobotFactory {

	@Autowired
	private TestService testService;
	@Autowired
	private SolrQuestionTempldate solrQuestionTemplate;
	
	public Robot createRobot() {
		return new Robot(testService,solrQuestionTemplate);
	}
}
