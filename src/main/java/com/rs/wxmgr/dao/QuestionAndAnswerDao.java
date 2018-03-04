package com.rs.wxmgr.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rs.wxmgr.entity.QuestionAndAnswer;

@Repository
public class QuestionAndAnswerDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<QuestionAndAnswer> selectMessageList(RowBounds rowBounds) {
		return sqlSessionTemplate.selectList("QuestionAndAnswerMapper.selectQuestionAndAnswerList",null,rowBounds);
	}
	
	public void insertQuestionAndAnswer(QuestionAndAnswer questionAndAnswer) {
		sqlSessionTemplate.insert("QuestionAndAnswerMapper.insertQuestionAndAnswer", questionAndAnswer);
	}
	
	public void deleteQuestionAndAnswerById(Integer id) {
		sqlSessionTemplate.delete("QuestionAndAnswerMapper.deleteQuestionAndAnswerById",id);
	}
	
}
