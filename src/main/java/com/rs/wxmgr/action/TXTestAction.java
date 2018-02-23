package com.rs.wxmgr.action;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.rs.wxmgr.action.entity.TXResponse;
import com.rs.wxmgr.action.entity.TXResponseFactory;
import com.rs.wxmgr.action.entity.TXTableRequest;
import com.rs.wxmgr.common.content.TXContentFlag;
import com.rs.wxmgr.common.content.TXErrorCode;
import com.rs.wxmgr.common.exception.TXException;
import com.rs.wxmgr.entity.User;
import com.rs.wxmgr.service.TestService;

@Controller
public class TXTestAction {
	
	private static final Logger logger = LoggerFactory.getLogger(TXTestAction.class);
	
	@Autowired
	private TestService testService;

	@RequestMapping("/index")
    public String index(ModelMap map){
        map.put("name", "wxtx");
        return "/index";
    }
	
	@ResponseBody
	@RequestMapping("getHistoryNcrInfoList")
	public JSONObject getHistoryNcrInfoList(@RequestBody HashMap<String,Object> data) {
		TXResponse response = TXResponseFactory.CreateSuccess();
		try {
			TXTableRequest tableRequest = TXTableRequest.GetFromRequest(data);
			PageList<User> userList = (PageList<User>) 
					testService.selectUserList(tableRequest.createPageBounds());
			response.put("rows", userList);
			response.put("totalpage",userList.getPaginator().getTotalPages());
		} catch (TXException e) {
			logger.error(e.getMessage(), e);
			response = TXResponseFactory.CreateFail(TXContentFlag.TX_EXCEPTION_CODE,e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = TXResponseFactory.CreateFail(TXErrorCode.SYSTEMRROR);
		}
		return response.getData();
	}
}
