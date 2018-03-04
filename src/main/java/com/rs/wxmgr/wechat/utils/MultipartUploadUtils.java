package com.rs.wxmgr.wechat.utils;

import java.io.ByteArrayInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXHttpClient;

public class MultipartUploadUtils {

	public static void sendImageByUsername(WXHttpClient client,byte[] buffer,String fileName,String username) throws Exception {
		System.err.println(client.getBaseUri());
		HttpPost post = new HttpPost("https://file.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json");
		ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer);
		InputStreamBody bin = new InputStreamBody(byteIn, fileName);
		
		JSONObject json = new JSONObject();
		json.put("BaseRequest", client.getBaseRequest());
		json.put("ClientMediaId", System.currentTimeMillis());
		json.put("TotalLen", buffer.length);
		json.put("StartPos", "0");
		json.put("DataLen", buffer.length);
		json.put("MediaType", "4");
		
		
		HttpEntity reqEntity = MultipartEntityBuilder.create()
		    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
		    .addPart("file", bin)
		    .addTextBody("id", "WU_FILE_1")
		    .addTextBody("name", fileName)
		    .addTextBody("type", "image/jpeg")
		    .addTextBody("lastModifiedDate", System.currentTimeMillis()+"")
		    .addTextBody("size", buffer.length+"")
		    .addTextBody("mediatype", "pic")
		    .addTextBody("uploadmediarequest", json.toJSONString())
		    .addTextBody("webwx_data_ticket", client.getCookieValue("webwx_data_ticket"))
		    .addTextBody("pass_ticket", client.getPassTicket())
		    .addTextBody("filename", fileName)
		    .setCharset(CharsetUtils.get("UTF-8")).build();
		post.setEntity(reqEntity);
		CloseableHttpResponse response = client.execute(post);
		String html = EntityUtils.toString(response.getEntity());
		response.close();
		System.out.println(html);
		
		byteIn.close();
	}
}
