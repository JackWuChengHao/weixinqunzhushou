package com.rs.wxmgr.wechat.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.entity.Group;

public class InforUtils {

    public static List<JSONObject> getMemberList(WXHttpClient client) throws Exception {
        
        List<JSONObject> result = new ArrayList<JSONObject>();
        String uuid = client.getUuid();
        List<JSONObject> dicList = new ArrayList<JSONObject>();
        int seq = 0;
        do {
            URI uri = new URIBuilder(client.getBaseUri() + "/webwxgetcontact")
                    .addParameter("seq", String.valueOf(seq))
                    .addParameter("pass_ticket", client.getPassTicket())
                    .addParameter("skey", client.getBaseRequest().getSkey())
                    .addParameter("r", String.valueOf(System.currentTimeMillis() / 1000))
                    .build();
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity("{}"));
            CloseableHttpResponse resp = client.execute(post);
            try {
                String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
                JSONObject dic = JSON.parseObject(data);
                dicList.add(dic);
                seq = dic.getIntValue("Seq");
                System.out.println(uuid + " 获取好友列表，已获取 " + dic.getString("MemberCount"));
            } finally {
                resp.close();
            }
        } while(seq != 0);
        
        for(JSONObject dic : dicList) {
            JSONArray memberList = dic.getJSONArray("MemberList");
            for(int i = 0; i < memberList.size(); i ++)
                result.add(memberList.getJSONObject(i));
        }
        return result;
    }
    
    /**
     * 批量获取用户信息
     * @param client
     * @param usernameList
     * @return
     * @throws Exception
     */
    public static JSONObject getBatchContact(WXHttpClient client,Set<String> usernameList) throws Exception {
        
        JSONObject result = new JSONObject();
        URI uri = new URIBuilder(client.getBaseUri() + "/webwxbatchgetcontact")
                .addParameter("type", "ex")
                .addParameter("r", String.valueOf(System.currentTimeMillis() / 1000))
                .addParameter("pass_ticket", client.getPassTicket())
                .build();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BaseRequest", client.getBaseRequest());
        params.put("Count", usernameList.size());
        params.put("List", extractGroupInfoList(usernameList));
        
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(JSON.toJSONString(params)));
        CloseableHttpResponse resp = client.execute(post);
        try {
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            result = JSON.parseObject(data);
        } finally {
            resp.close();
        }
        return result;
    }
    private static JSONArray extractGroupInfoList(Set<String> usernameList) {
        
        JSONArray list = new JSONArray();
        for(String username : usernameList) {
            JSONObject ele = new JSONObject();
            ele.put("UserName", username);
            ele.put("EncryChatRoomId", "");
            list.add(ele);
        }
        return list;
    }
    
    public static Group getGroupContact(WXHttpClient client,String username) throws Exception {
    	Set<String> set = new HashSet<String>();
    	set.add(username);
    	JSONObject batchContact = getBatchContact(client, set);
    	List<JSONObject> list = JSONArray.parseArray(batchContact.getString("ContactList"), JSONObject.class);
    	if(list.size() ==0) {
    		return null;
    	}
    	return Group.parse(list.get(0));
    }
}
