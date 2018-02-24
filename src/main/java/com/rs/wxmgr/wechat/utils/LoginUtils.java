package com.rs.wxmgr.wechat.utils;

import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rs.wxmgr.wechat.common.Account;
import com.rs.wxmgr.wechat.common.SyncKey;
import com.rs.wxmgr.wechat.common.WXHttpClient;

public class LoginUtils {

    /**
     * 开始登录会话，生成 UUID，并将传入的参数作为其它信息保存到 context 中
     * @param otherInfo
     * @return
     * @throws Exception
     */
	public static String genUUID() throws Exception {
        // appid=wx782c26e4c19acffb 为 Web微信
        // appid=wxeb7ec651dd0aefa9 为 微信网页版
        URI uri = new URIBuilder("https://login.weixin.qq.com/jslogin")
                .addParameter("appid", "wx782c26e4c19acffb")
                .addParameter("fun", "new")
                .addParameter("lang", "zh_CN")
                .addParameter("_", String.valueOf(System.currentTimeMillis()))
                .build();
        HttpGet get = new HttpGet(uri);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(get);
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            Pattern pattern = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\"");
            Matcher matcher = pattern.matcher(data);
            if(matcher.find()) {
                String code = matcher.group(1);
                String uuid = matcher.group(2);
                if("200".equals(code)) {
                    return uuid;
                }
            }
        } catch (Exception e) {
        	throw e;
		} finally {
			if(resp!=null) resp.close();
            if(client!=null) client.close();
        }
        return null;
    }
    
    /**
     * 生成二维码
     * @param uuid
     * @param stream
     * @throws Exception
     */
    public static void genQrCode(String uuid, OutputStream stream) throws Exception {
        String content = "https://login.weixin.qq.com/l/" + uuid;
        BitMatrix bitMatrix = new QRCodeWriter().encode(content,  
                BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", stream);
    }
    
    /**
     * 测试是否已经扫码登录
     * @param client
     * @param uuid
     * @return
     * @throws Exception
     */
    public static boolean testLogin(WXHttpClient client) throws Exception {

        String uuid = client.getUuid();
        URI uri = new URIBuilder("https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login")
                .addParameter("tip", String.format("%d",new Random().nextInt()))
                .addParameter("uuid", uuid)
                .addParameter("_", String.valueOf(System.currentTimeMillis()))
                .build();
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse resp = client.execute(get);
        try {
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            Pattern pattern = Pattern.compile("window.code=(\\d+);");
            Matcher matcher = pattern.matcher(data);
            if(matcher.find()) {
                String code = matcher.group(1);
                if("201".equals(code)) {
                	System.out.println(uuid + "已扫码，待确认");
                } else if("200".equals(code)) {
                	System.out.println(uuid + "登录成功");
                    pattern = Pattern.compile("window.redirect_uri=\"(\\S+?)\";");
                    matcher = pattern.matcher(data);
                    if(matcher.find()) {
                        String redirectURI = matcher.group(1) + "&fun=new";
                        client.setRedirectUrl(redirectURI);
                        client.setBaseUri(redirectURI.substring(0, redirectURI.lastIndexOf("/")));
                        String tempHost = client.getBaseUri().substring(8);
                        client.setBaseHost(tempHost.substring(0, tempHost.indexOf("/")));
                        System.out.println(uuid + " 重定向到登录地址：" + redirectURI);
                        return true;
                    }
                } else {
                	System.out.println(uuid + " 登录失败");
                }
            }
        } catch (Exception e) {
        	throw e;
		} finally {
            resp.close();
        }
        return false;
    }
    
    public static void redirect(WXHttpClient client) throws Exception {
        String uuid = client.getUuid();
        HttpGet get = new HttpGet(client.getRedirectUrl());
        CloseableHttpResponse resp = client.execute(get);
        try {
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(new InputSource(new StringReader(data)));
            
            Node root = xmlDoc.getDocumentElement();
            NodeList childNodes = root.getChildNodes();
            for(int i = 0; i < childNodes.getLength(); i ++) {
                Node node = childNodes.item(i);
                if(node.getNodeName().equals("skey")) {
                    String skey = node.getChildNodes().item(0).getNodeValue();
                    client.getBaseRequest().setSkey(skey);;
                } else if(node.getNodeName().equals("wxsid")) {
                    String sid = node.getChildNodes().item(0).getNodeValue();
                    client.getBaseRequest().setSid(sid);;
                } else if(node.getNodeName().equals("wxuin")) {
                    String uin = node.getChildNodes().item(0).getNodeValue();
                    client.getBaseRequest().setUin(uin);
                } else if(node.getNodeName().equals("pass_ticket")) {
                    String passTicket = node.getChildNodes().item(0).getNodeValue();
                    client.setPassTicket(passTicket);
                }
            }
            client.setLoginTime(new Date());
            System.out.println(uuid + " 登录成功，待初始化");
        }catch (Exception e) {
        	e.printStackTrace();
		}
    }
    
    public static void init(WXHttpClient client)
            throws Exception {
        
        String uuid = client.getUuid();
        URI uri = new URIBuilder(client.getBaseUri() + "/webwxinit")
                .addParameter("r", String.valueOf(System.currentTimeMillis() / 1000.0))
                .addParameter("lang", "zh_CN")
                .addParameter("pass_ticket", client.getPassTicket()).build();
        HttpPost post = new HttpPost(uri);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("BaseRequest", client.getBaseRequest());
        post.setEntity(new StringEntity(JSON.toJSONString(params)));
        CloseableHttpResponse resp = client.execute(post);
        try {
            String data = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
            JSONObject dict = JSON.parseObject(data);
            client.setMyAccount(dict.getObject("User", Account.class));
            client.setSyncKey(dict.getObject("SyncKey", SyncKey.class));
            
            if(dict.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
                
            	client.setStatus("inited");
                System.out.println(uuid + " 初始化成功");
            } else {
            	client.setStatus("loginout");
                System.out.println(uuid + " 初始化失败：\r\n" + data);
            }
        } finally {
            resp.close();
        }
    }
    
}
