package com.rs.wxmgr.wechat;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXContact;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.utils.InforUtils;
import com.rs.wxmgr.wechat.utils.LoginUtils;
import com.rs.wxmgr.wechat.utils.MessageUtils;
import com.rs.wxmgr.wechat.utils.SyncCheckUtils;

public class Robot implements Closeable {
	
	/*
	 * tomcat，则catalina.sh增加：-Djsse.enableSNIExtension=false
	 */
	
	private WXHttpClient client = null;
	/**
	 * 通讯录
	 */
	private WXContact contact = null;
	/**
	 * 是否在检查是否已经扫码
	 */
	private boolean isChecking = false;
	/**
	 * 是否在线
	 */
	private boolean isOnline = false;
	
	private Long lastConectTime = null;
	
	/**
	 * 心跳包间隔最短时长
	 */
	private static final long SYNC_CHECK_INTERVAL = 1000;
	
	public Robot() {
		client = new WXHttpClient();
		contact = new WXContact();
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
	public void checkLogin() throws Exception {
		if(isChecking || isOnline) {
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				Robot.this.isChecking = true;
				long lastTime = System.currentTimeMillis();
				while(true) {
					try {
						long currentTime = System.currentTimeMillis();
						if(currentTime-lastTime<SYNC_CHECK_INTERVAL) {
							Thread.sleep(SYNC_CHECK_INTERVAL);
							lastTime = System.currentTimeMillis();
						}
						boolean isLogin = LoginUtils.testLogin(client);
						if(isLogin) {
							Robot.this.isChecking = false;
							init();
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * 心跳包保持账号在线
	 * @throws Exception
	 */
	private void keepOnline() throws Exception {
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Robot.this.isOnline = SyncCheckUtils.check(client);
						if(!Robot.this.isOnline) {
							break;
						}
						if(Robot.this.lastConectTime != null && 
							System.currentTimeMillis()-Robot.this.lastConectTime<SYNC_CHECK_INTERVAL ) {
							Thread.sleep(1000);
						}
						Robot.this.lastConectTime = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void init() throws Exception {
		LoginUtils.redirect(client);
		LoginUtils.init(client);
		keepOnline();
	}
	
	/**
	 * 获取联系人列表
	 * @return
	 * @throws Exception
	 */
	public WXContact getContact() throws Exception {
		List<JSONObject> memberList = InforUtils.getMemberist(client);
		contact.setMemberList(memberList);
		return contact;
	}
	
	public String testSendMeasure(String message,String username) throws Exception {
		return MessageUtils.sendMessageByUsername(client, username, message);
	}
	
	public String testSendroupMeasure(String message,String groupname) throws Exception {
		return MessageUtils.sendMessageByUsername(client, groupname, message);
	}
	
	public void close() throws IOException {
		try {
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
