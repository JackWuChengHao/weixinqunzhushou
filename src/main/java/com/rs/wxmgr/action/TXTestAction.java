package com.rs.wxmgr.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 手机扫描二维码后,实现登录
	 * @param request
	 */
	@RequestMapping(value="/checkLogin")
	public void checkLogin(HttpServletRequest request){
		try {
			if(robot.checkLogin()) {
				robot.init();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 轮询,保证账号不掉线,并获取相关消息信息
	 * @param request
	 */
	@RequestMapping(value="/synccheck")
	public void syncCheck(HttpServletRequest request){
		try {
			robot.syncCheck();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 测试获取好友列表
	 * @param request
	 */
	@RequestMapping(value="/testInfo")
	public void testInfo(HttpServletRequest request){
		try {
			System.out.println(robot.getInfo());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
}
