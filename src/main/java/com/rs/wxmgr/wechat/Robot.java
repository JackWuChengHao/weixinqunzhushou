package com.rs.wxmgr.wechat;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.wechat.common.WXContact;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.utils.InforUtils;
import com.rs.wxmgr.wechat.utils.LoginUtils;
import com.rs.wxmgr.wechat.utils.MessageUtils;
import com.rs.wxmgr.wechat.utils.SyncCheckUtils;

public class Robot implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(Robot.class);
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
	/**
	 * 最后一次心跳包连接时间
	 */
	private Long lastConectTime = null;
	
	/**
	 * 心跳包间隔最短时长
	 */
	private static final long SYNC_CHECK_INTERVAL = 1000;
	/**
	 * 线程池
	 */
	private ThreadPoolExecutor threadPoolExecutor = 
			new ThreadPoolExecutor(1, 10, 1, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>());
	
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
		threadPoolExecutor.execute(new Runnable() {
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
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
	}
	
	/**
	 * 心跳包保持账号在线
	 * @throws Exception
	 */
	private void keepOnline() throws Exception {
		threadPoolExecutor.execute(new Runnable() {
			public void run() {
				while(true) {
					try {
						Robot.this.isOnline = SyncCheckUtils.check(client);
						if(!Robot.this.isOnline) {
							Robot.this.close();
							break;
						}
						if(Robot.this.lastConectTime != null && 
							System.currentTimeMillis()-Robot.this.lastConectTime<SYNC_CHECK_INTERVAL ) {
							Thread.sleep(1000);
						}
						Robot.this.lastConectTime = System.currentTimeMillis();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
	}
	
	/**
	 * 扫码后重定向到指定链接,完成登录。
	 * 并打开心跳包保持在线
	 * @throws Exception
	 */
	private void init() throws Exception {
		LoginUtils.redirect(client);
		isOnline = LoginUtils.init(client);
		keepOnline();
	}
	
	/**
	 * 获取联系人列表
	 * @return
	 * @throws Exception
	 */
	public WXContact getContact() throws Exception {
		if(!isOnline) {
			return null;
		}
		List<JSONObject> memberList = InforUtils.getMemberist(client);
		contact.setMemberList(memberList);
		return contact;
	}
	
	/**
	 * 向群或好友发送消息
	 * @param message
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public String testSendMeasure(String message,String username) throws Exception {
		if(!isOnline) {
			return null;
		}
		return MessageUtils.sendMessageByUsername(client, username, message);
	}
	/**
	 * 是否在线
	 * @return
	 */
	public boolean isOnline() {
		return isOnline;
	}
	
	public void close() throws IOException {
		try {
			client.close();
			threadPoolExecutor.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client = null;
		}
	}
	
	public boolean isClose() {
		return client == null;
	}
	
}
