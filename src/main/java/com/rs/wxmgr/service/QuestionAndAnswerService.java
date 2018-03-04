package com.rs.wxmgr.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rs.wxmgr.dao.QuestionAndAnswerDao;
import com.rs.wxmgr.entity.QuestionAndAnswer;
import com.rs.wxmgr.template.SolrQuestionTempldate;

@Service
@Transactional(rollbackFor=Throwable.class)
public class QuestionAndAnswerService {

	@Autowired
	private QuestionAndAnswerDao questionAndAnswerDao;
	@Autowired
	private SolrQuestionTempldate solrTemplate;
	
	public List<QuestionAndAnswer> selectMessageList(RowBounds rowBounds) {
		return questionAndAnswerDao.selectMessageList(rowBounds);
	}
	
	public void insertQuestionAndAnswer(QuestionAndAnswer questionAndAnswer) {
		questionAndAnswerDao.insertQuestionAndAnswer(questionAndAnswer);
		solrTemplate.addOrUpdate(questionAndAnswer);
	}
	
	public void deleteQuestionAndAnswerById(Integer id) {
		questionAndAnswerDao.deleteQuestionAndAnswerById(id);
		solrTemplate.delete(id);
	}
	/**
	 * 通过搜索引擎获取与关键字最匹配的答案
	 * @param keywords
	 * @return
	 */
	public QuestionAndAnswer searchQuestionAndAnswerByKeywords(String keywords) {
		return searchQuestionAndAnswerByKeywords(keywords);
	}
}
