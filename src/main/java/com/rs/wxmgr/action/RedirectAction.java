package com.rs.wxmgr.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectAction {
	
	@RequestMapping("/multifunctionWizard")
	public String getWelcomeMsgList(HttpServletRequest request) {
		return "RSMultifunctionWizard/RSMultifunctionWizard";
	}
	
}
