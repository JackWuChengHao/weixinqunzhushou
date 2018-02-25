package com.rs.wxmgr.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.rs.wxmgr.wechat.Robot;

@Controller
public class TXTestAction {
	
	private static final Logger logger = LoggerFactory.getLogger(TXTestAction.class);
	
	@Autowired
	private TestService testService;
	
	private Robot robot = new Robot();

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
	
	/**
	 * 登录二维码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/wechatlogin")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response){
		try {
			response.getOutputStream().write(robot.getRQCode());
			robot.checkLogin();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 测试获取好友列表
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/getmember")
	public JSONObject testInfo(HttpServletRequest request){
		TXResponse response = TXResponseFactory.CreateSuccess();
		List<JSONObject> memberList = new ArrayList<JSONObject>();
		try {
			String type = request.getParameter("type");
			if("group".equals(type)){
				memberList = robot.getContact().getGroupList();
			} else if("normal".equals(type)) {
				memberList = robot.getContact().getMemberList();
			}
			response.put("rows", memberList);
			response.put("totalpage",1);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return response.getData();
	}
	
	@ResponseBody
	@RequestMapping(value="/sendmessage")
	public String testMessage(@RequestBody HashMap<String,Object> data){
		String result = null;
		try {
			String message = data.get("message").toString();
			String username = data.get("username").toString();
			if(StringUtils.isNotBlank(message) && StringUtils.isNotBlank(username)) {
				System.out.println(robot.testSendMeasure(message,username));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
}
