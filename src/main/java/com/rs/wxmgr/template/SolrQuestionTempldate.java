package com.rs.wxmgr.template;

import java.util.Iterator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rs.wxmgr.common.exception.TXException;
import com.rs.wxmgr.entity.QuestionAndAnswer;

@Component
public class SolrQuestionTempldate {

	@Autowired
	private HttpSolrClient solrClient;

	public void addOrUpdate(QuestionAndAnswer questonAndAsnwer) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			//默认id为主键，当id存在时更新数据，否则添加数据
			doc.addField("id", questonAndAsnwer.getId());
			doc.addField("keywords", questonAndAsnwer.getKeywords());
			doc.addField("question", questonAndAsnwer.getQuestion());
			doc.addField("answer", questonAndAsnwer.getAnswer());
			solrClient.add(doc);
			solrClient.commit();
		} catch (Exception e) {
			throw new TXException("solr异常",e);
		}
	}
	
	public void delete(Integer id) {
		try {
			//通过id删除索引
			solrClient.deleteById(id.toString());
			solrClient.commit();
		} catch (Exception e) {
			throw new TXException("solr异常",e);
		}
	}

	public QuestionAndAnswer queryByQuestion(String keyword) {
		QuestionAndAnswer questionAndAnswer = null;
		
		try {
			SolrQuery query = new SolrQuery();
			query.setQuery("keywords:"+keyword);
			QueryResponse queryResponse = solrClient.query(query);

			SolrDocumentList docs = queryResponse.getResults();
			Iterator<SolrDocument> iter = docs.iterator();
			while(iter.hasNext()){
			    SolrDocument doc = iter.next();
			    System.out.println(doc.toString());
			    
			    if(questionAndAnswer == null) {
			    	questionAndAnswer = new QuestionAndAnswer();
			    	questionAndAnswer.setId(Integer.valueOf(doc.get("id").toString()));
			    	questionAndAnswer.setKeywords(doc.get("keywords").toString());
			    	questionAndAnswer.setQuestion(doc.get("question").toString());
			    	questionAndAnswer.setAnswer(doc.get("answer").toString());
			    }
			}
			solrClient.commit();
		} catch (Exception e) {
			throw new TXException("solr异常",e);
		}
		return questionAndAnswer;
	}
}
