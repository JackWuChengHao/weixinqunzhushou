package com.rs.wxmgr.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TXTestAction {

	@RequestMapping("/index")
    public String index(ModelMap map){
        map.put("name", "wxtx");
        return "/index";
    }
}
