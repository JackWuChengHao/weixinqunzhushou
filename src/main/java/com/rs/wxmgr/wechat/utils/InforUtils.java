package com.rs.wxmgr.wechat.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXHttpClient;

public class InforUtils {

    public static List<JSONObject> getMemberist(WXHttpClient client) throws Exception {
        
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
}
