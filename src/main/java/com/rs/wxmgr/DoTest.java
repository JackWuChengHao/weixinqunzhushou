package com.rs.wxmgr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rs.wxmgr.service.SolrService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
public class DoTest {

	@Autowired
	private SolrService service;
	
	@Test
	public void testAdd() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
