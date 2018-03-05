package com.rs.wxmgr.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.rs.wxmgr.entity.QuestionAndAnswer;
import com.rs.wxmgr.service.PublicAskService;
import com.rs.wxmgr.wechat.entity.PublicAsk;
import com.rs.wxmgr.wechat.utils.WXFileUtils;

@Controller
public class PublicAskAction {
	private static final Logger logger = LoggerFactory.getLogger(PublicAskAction.class);
	@Autowired
	private PublicAskService publicAskService;
	
	@ResponseBody
	@RequestMapping("/getPublicAskList")
	public JSONObject getPublicAskList(@RequestBody HashMap<String,Object> data,HttpServletRequest request) {
		TXResponse response = TXResponseFactory.CreateSuccess();
		try {
			TXTableRequest tableRequest = TXTableRequest.GetFromRequest(data);
			
			PageList<PublicAsk> publicAskList = (PageList<PublicAsk>) 
					publicAskService.selectPublicAskList(tableRequest.createPageBounds());
			response.put("rows", publicAskList);
			response.put("totalpage",publicAskList.getPaginator().getTotalPages());
		} catch (TXException e) {
			logger.error(e.getMessage(), e);
			response = TXResponseFactory.CreateFail(TXContentFlag.TX_EXCEPTION_CODE,e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = TXResponseFactory.CreateFail(TXErrorCode.SYSTEMRROR);
		}
		return response.getData();
	}
	
	@ResponseBody
	@RequestMapping(value="/addPublicAsk")
	public JSONObject addPublicAsk(@RequestBody HashMap<String,Object> data){
		TXResponse response = TXResponseFactory.CreateSuccess();
		try {
			if(!data.containsKey("name") || !data.containsKey("personalId") 
					|| !data.containsKey("phone") || !data.containsKey("content")
					|| !data.containsKey("askType")) {
				throw new TXException("参数异常");
			}
			
			String name = data.get("name").toString();
			String personalId = data.get("personalId").toString();
			String phone = data.get("phone").toString();
			String content = data.get("content").toString();
			String askType = data.get("askType").toString();
			PublicAsk publicAsk = new PublicAsk();
			publicAsk.setName(name);
			publicAsk.setPersonalId(personalId);
			publicAsk.setPhone(phone);
			publicAsk.setContent(content);
			publicAsk.setAskType(askType);
			String current = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
			publicAsk.setCreateTime(current);
			publicAskService.insertPublicAsk(publicAsk);
			String telephone = askType.equals("0")?"18068288108":"18006178899";
			//String telephone = askType.equals("0")?"18852992317":"13626122533";
			WXFileUtils.sendSms(name,telephone,personalId,current);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			response = TXResponseFactory.CreateFail(TXErrorCode.SYSTEMRROR);
		}
		return response.getData();
	}
}
