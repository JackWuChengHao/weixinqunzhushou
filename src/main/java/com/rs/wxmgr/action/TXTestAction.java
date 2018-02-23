package com.rs.wxmgr.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rs.wxmgr.entity.User;
import com.rs.wxmgr.service.TestService;

@Controller
public class TXTestAction {
	
	private static final Logger logger = LoggerFactory.getLogger(TXTestAction.class);
	
	@Autowired
	private TestService testService;

	@RequestMapping("/index")
    public String index(ModelMap map){
		try {
			List<User> userList = testService.selectUserList();
			for(int i=0;i<userList.size();i++) {
				System.out.println(userList.get(i).toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        map.put("name", "wxtx");
        return "/index";
    }
}
