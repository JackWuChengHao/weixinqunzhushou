package com.rs.wxmgr.service;

import java.util.Iterator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rs.wxmgr.entity.QuestionAndAnswer;

@Service
public class SolrService {

	@Autowired
	private HttpSolrClient solrClient;

	public void addOrUpdate(QuestionAndAnswer questonAndAsnwer) throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		//默认id为主键，当id存在时更新数据，否则添加数据
		doc.addField("id", questonAndAsnwer.getId());
		doc.addField("keywords", questonAndAsnwer.getKeywords());
		doc.addField("question", questonAndAsnwer.getQuestion());
		doc.addField("answer", questonAndAsnwer.getAnswer());
		solrClient.add(doc);
		solrClient.commit();
	}
	
	public void delete(Integer id) throws Exception {
		//通过id删除索引
		solrClient.deleteById(id.toString());
		solrClient.commit();
	}

	public QuestionAndAnswer queryByQuestion(String keyword) throws Exception {
		QuestionAndAnswer questionAndAnswer = null;
		
		SolrQuery query = new SolrQuery();
		// *标示多个任意字符,?标示单个任意字符,~模糊搜索
		query.setQuery("*:*");    //全搜索
		//query.setQuery("name:你好世?");   
		//query.setQuery("name:你好我号~");   //搜索相似的结果
		//query.setQuery("name:你好我号~0.5");    //搜索相似度为0.5的结果
		//query.setQuery("age:[1 TO 3]"); //范围搜索,包括边界
		//query.setQuery("age:{0 TO *}"); //范围搜索,不包括边界
		//query.setQuery("*:* AND name:*好*");     //组合搜索
		//query.setQuery("NOT name:*好*");     //不匹配搜索
		//query.setQuery("-name:*好*");        //同NOT
		//query.setQuery("+name:hello* OR age:[1 TO 3]"); //+表示该条件必须符合
		//query.set("fl", "id","name");   // 只获取字段id,name
		//query.set("fl", "id,name");   // 同上,只获取字段id,name
		//query.addSort("id", ORDER.desc);    //排序

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
		return questionAndAnswer;
	}
}
