package com.rs.wxmgr.action;

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
import com.rs.wxmgr.service.QuestionAndAnswerService;

@Controller
public class QuestionAndAnswerAction {
	
	private static final Logger logger = LoggerFactory.getLogger(QuestionAndAnswerAction.class);
	
	@Autowired
	private QuestionAndAnswerService questionAndAnswerService;

	@ResponseBody
	@RequestMapping("/getQuestionAndAnswerList")
	public JSONObject getQuestionAndAnswerList(@RequestBody HashMap<String,Object> data,HttpServletRequest request) {
		TXResponse response = TXResponseFactory.CreateSuccess();
		try {
			TXTableRequest tableRequest = TXTableRequest.GetFromRequest(data);
			
			PageList<QuestionAndAnswer> questionAndAnswerList = (PageList<QuestionAndAnswer>) 
					questionAndAnswerService.selectMessageList(tableRequest.createPageBounds());
			response.put("rows", questionAndAnswerList);
			response.put("totalpage",questionAndAnswerList.getPaginator().getTotalPages());
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
	@RequestMapping(value="/addQuestionAndAnswer")
	public JSONObject addQuestionAndAnswer(@RequestBody HashMap<String,Object> data){
		TXResponse response = TXResponseFactory.CreateSuccess();
		try {
			if(!data.containsKey("keywords") || !data.containsKey("question") || !data.containsKey("answer")) {
				throw new TXException("参数异常");
			}
			
			String keywords = data.get("keywords").toString();
			String question = data.get("question").toString();
			String answer = data.get("answer").toString();
			QuestionAndAnswer questionAndAnswe = new QuestionAndAnswer();
			questionAndAnswe.setKeywords(keywords);
			questionAndAnswe.setQuestion(question);
			questionAndAnswe.setAnswer(answer);
			
			questionAndAnswerService.insertQuestionAndAnswer(questionAndAnswe);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			response = TXResponseFactory.CreateFail(TXErrorCode.SYSTEMRROR);
		}
		return response.getData();
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteQuestionAndAnswer")
	public JSONObject deleteQuestionAndAnswer(@RequestBody HashMap<String,Object> data){
		TXResponse response = TXResponseFactory.CreateSuccess();
		try {
			if(!data.containsKey("id")) {
				throw new TXException("参数异常");
			}
			
			Integer id = Integer.valueOf(data.get("id").toString());
			questionAndAnswerService.deleteQuestionAndAnswerById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			response = TXResponseFactory.CreateFail(TXErrorCode.SYSTEMRROR);
		}
		return response.getData();
	}
	
}
