package com.rs.wxmgr.wechat;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.utils.InforUtils;
import com.rs.wxmgr.wechat.utils.LoginUtils;
import com.rs.wxmgr.wechat.utils.SyncCheckUtils;

public class Robot implements Closeable {
	
	/*
	 * tomcat，则catalina.sh增加：-Djsse.enableSNIExtension=false
	 */
	
	private WXHttpClient client = null;
	
	public Robot() {
		client = new WXHttpClient();
	}
	
	/**
	 * 获取登录二维码字节数组
	 * @return
	 * @throws Exception 
	 */
	public byte[] getRQCode() throws Exception {
        String uuid = LoginUtils.genUUID();
        client.setUuid(uuid);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        LoginUtils.genQrCode(uuid, byteOut);
        byte[] RQbyte = byteOut.toByteArray();
        byteOut.close();
        return RQbyte;
	}

	/**
	 * 检查是否登录
	 * @return
	 * @throws Exception
	 */
	public boolean checkLogin() throws Exception {
		for(int i=0;i<10;i++) {
			boolean isLogin = LoginUtils.testLogin(client);
			if(isLogin) {
				return true;
			}
		}
		return false;
	}
	
	public void syncCheck() throws Exception {
		SyncCheckUtils.check(client);
	}

	public void init() throws Exception {
		LoginUtils.redirect(client);
		LoginUtils.init(client);
	}
	
	public String getInfo() throws Exception {
		return InforUtils.getMemberist(client).toString();
	}
	
	public void close() throws IOException {
		try {
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
