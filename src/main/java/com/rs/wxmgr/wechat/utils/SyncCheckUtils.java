package com.rs.wxmgr.wechat.utils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.entity.SyncKey;

//import cn.lzxz1234.wxbot.vo.SyncKey;

public class SyncCheckUtils {

	private static final Logger logger = LoggerFactory.getLogger(SyncCheckUtils.class);
	/**
	 * 心跳包
	 * -1:掉线,0:正常,且没有消息,2:有新消息,3:未知,
	 * 4:通讯录更新,6:可能是红包,7:手机上操作了微信
	 * @param client
	 * @return 是否在线
	 * @throws Exception
	 */
    public static int check(WXHttpClient client)
            throws Exception {

        String uuid = client.getUuid();
        CloseableHttpResponse resp = null;
        try {
            if(StringUtils.isEmpty(client.getSyncHost()))
                testSyncCheck(client);
            // 获取不到同步服务器时出退出
            if(StringUtils.isEmpty(client.getSyncHost())) return -1;
            
            client.setStatus("success");
            URI uri = new URIBuilder("https://" + client.getSyncHost() + "/cgi-bin/mmwebwx-bin/synccheck")
                    .addParameter("r", String.valueOf(System.currentTimeMillis() / 1000))
                    .addParameter("sid", client.getBaseRequest().getSid())
                    .addParameter("uin", client.getBaseRequest().getUin())
                    .addParameter("skey", client.getBaseRequest().getSkey())
                    .addParameter("deviceid", client.getBaseRequest().getDeviceId())
                    .addParameter("synckey", client.getSyncKeyString())
                    .addParameter("_", String.valueOf(System.currentTimeMillis() / 1000))
                    .build();
            HttpGet get = new HttpGet(uri);
            get.setConfig(RequestConfig.custom().setSocketTimeout(30000).build());
            resp = client.execute(get);
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            Matcher matcher = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}").matcher(data);
            if(matcher.find()) {
                String retcode = matcher.group(1);
                String selector = matcher.group(2);
                logger.info(uuid + " sync_check: " + retcode + "，" + selector);
                
                if(!"0".equals(retcode)) {
                	/*
                	 * 1100 从微信客户端上登出
                	 * 1101 从其它设备上登录了网页微信
                	 * 1102 不知道啥问题，反正就是退出登录了
                	 */
                	return -1;
                } else {
                	/*
                	 * 0 正常,且没有消息
            		 * 2 有新消息
            		 * 3 未知
            		 * 4 通讯录更新
            		 * 6 可能是红包
            		 * 7 手机上操作了微信
                	 */
                	int selectorVal = Integer.parseInt(selector);
                	return selectorVal;
                }
                
            }
        } catch(Exception e) {
        	throw e;
        } finally {
            if(resp != null) resp.close();
        }
        return 0;
    }

    /**
     * 获取消息服务器地址
     * @param client
     * @throws Exception
     */
    private static void testSyncCheck(WXHttpClient client) throws Exception {
        
        String uuid = client.getUuid();
        Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}");
        for(String host : new String[] {"webpush.", "webpush2."}) {
            URI uri = new URIBuilder("https://" + host + client.getBaseHost() + "/cgi-bin/mmwebwx-bin/synccheck")
                    .addParameter("r", String.valueOf(System.currentTimeMillis() / 1000))
                    .addParameter("sid", client.getBaseRequest().getSid())
                    .addParameter("uin", client.getBaseRequest().getUin())
                    .addParameter("skey", client.getBaseRequest().getSkey())
                    .addParameter("deviceid", client.getBaseRequest().getDeviceId())
                    .addParameter("synckey", client.getSyncKeyString())
                    .addParameter("_", String.valueOf(System.currentTimeMillis() / 1000))
                    .build();
            HttpGet get = new HttpGet(uri);
            CloseableHttpResponse resp = null;
            try {
                resp = client.execute(get);
                String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
                Matcher matcher = pattern.matcher(data);
                if(matcher.find() && "0".equals(matcher.group(1))) {
                	client.setSyncHost(host + client.getBaseHost());
                	System.out.println(uuid + " 加载同步服务器：" + client.getSyncHost());
                    return;
                } else {
                	System.out.println("测试服务器返回:" + data);
                }
            } catch (Exception e) {
            	System.out.println("加载服务器失败:" + e.getMessage());
            } finally {
                if(resp != null) resp.close();
            }
        }
    }

    /**
     * 获取消息
     * @param client
     * @return
     * @throws Exception
     */
    public static JSONObject syncMessage(WXHttpClient client) throws Exception {
        
        URI uri = new URIBuilder(client.getBaseUri() + "/webwxsync")
                .addParameter("sid", client.getBaseRequest().getSid())
                .addParameter("skey", client.getBaseRequest().getSkey())
                .addParameter("lang", "en_US")
                .addParameter("pass_ticket", client.getPassTicket())
                .build();
        HttpPost post = new HttpPost(uri);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BaseRequest", client.getBaseRequest());
        params.put("SyncKey", client.getSyncKey());
        params.put("rr", String.valueOf(~(System.currentTimeMillis() / 1000)));
        post.setEntity(new StringEntity(JSON.toJSONString(params)));

        CloseableHttpResponse resp = client.execute(post);
        try {
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            JSONObject dict = JSON.parseObject(data);
            if(dict.getJSONObject("BaseResponse").getInteger("Ret") == 0) {
            	client.setSyncKey(dict.getObject("SyncCheckKey", SyncKey.class));
                return dict;
            }
        } finally {
            resp.close();
        }
        return null;
    }
}
