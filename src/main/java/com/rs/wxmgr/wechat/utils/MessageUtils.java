package com.rs.wxmgr.wechat.utils;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXHttpClient;

public class MessageUtils {

	public static String sendMessageByUsername(WXHttpClient client,String username,String word) throws Exception {
        String uuid = client.getUuid();
        URI uri = new URIBuilder(client.getBaseUri() + "/webwxsendmsg")
                .addParameter("pass_ticket", client.getPassTicket())
                .build();
        String msgId = String.valueOf(System.currentTimeMillis()) + RandomStringUtils.randomNumeric(4);
        JSONObject params = new JSONObject();
        JSONObject msg = new JSONObject();
        params.put("BaseRequest", client.getBaseRequest());
        params.put("Msg", msg);
        msg.put("Type", 1);
        msg.put("Content", word);
        msg.put("FromUserName", client.getMyAccount().getUserName());
        msg.put("ToUserName", username);
        msg.put("LocalID", msgId);
        msg.put("ClientMsgId", msgId);
        
        System.out.println(params.toJSONString());
        
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(params.toJSONString(), "UTF-8"));
        post.setHeader("content-type", "application/json; charset=UTF-8");
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(post);
            IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            System.out.println(uuid + " 发送消息 " + word + " 返回");
        } finally {
            if(resp != null) resp.close();
        }
		return null;
	}
}
