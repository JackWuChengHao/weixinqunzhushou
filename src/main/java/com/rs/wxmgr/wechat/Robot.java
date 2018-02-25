package com.rs.wxmgr.wechat;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.entity.WelcomeMsg;
import com.rs.wxmgr.service.TestService;
import com.rs.wxmgr.wechat.common.WXContact;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.utils.GroupUtils;
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
	private static final long SYNC_CHECK_INTERVAL = 1000L;
	/**
	 * 检测组群成员增加的间隔
	 */
	private static final long GROUP_MEMBER_ADD_INTERVAL = 2000L;
	/**
	 * 线程池
	 */
	private ThreadPoolExecutor threadPoolExecutor = 
			new ThreadPoolExecutor(1, 10, 1, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>());
	
	private Timer groupMemberAddTimer;
    private TimerTask groupMemberAddtask;
    
    private TestService testService;
	
	public Robot(TestService testService) {
		this.testService = testService;
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
						if(Robot.this.isOnline) {
							break;
						}
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
							Robot.this.offLine();
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
		// 上线后进行一些操作
		if(isOnline) {
			upLine();
		}
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
	public String testSendMessage(String message,String username) throws Exception {
		if(!isOnline) {
			return null;
		}
		return MessageUtils.sendMessageByUsername(client, username, message);
	}
	
	private void sendGroupMessageForUserAdd() throws Exception{
		List<JSONObject> goupList = getGroupList();
		List<String> usernameList = GroupUtils.getGroupListForUserAdd(this.contact.getGroupInfoList(), goupList);
		this.contact.setGroupInfoList(goupList);
		List<WelcomeMsg> messageList = null;
		if(usernameList.size()>0) {
			messageList = testService.selectMessageList();
			for(String username:usernameList) {
				int index = new Random().nextInt(messageList.size());
				if(index!=0&&index==usernameList.size()) index--;
				String message = messageList.get(index).getMessage();
				System.out.println(message);
				testSendMessage(message, username);
			}
		}
	}
	
	public List<JSONObject> getGroupList() throws Exception{
		List<JSONObject> groupList = this.contact.getGroupList();
		List<String> usernameList = new ArrayList<String>(groupList.size());
		for(JSONObject group:groupList) {
			usernameList.add(group.getString("UserName"));
		}
		return JSONArray.parseArray(InforUtils.getBatchContact(client, usernameList)
				.getJSONArray("ContactList").toJSONString(),JSONObject.class);
	}
	
	private void startGroupMemberAddCheck() {
		groupMemberAddTimer = new Timer();
	    groupMemberAddtask = new TimerTask() {
			@Override
			public void run() {
				try {
					getContact();
					sendGroupMessageForUserAdd();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		};
		groupMemberAddTimer.schedule(groupMemberAddtask, 0, GROUP_MEMBER_ADD_INTERVAL);
	}
	private void stopGroupMemberAddCheck() {
		groupMemberAddtask.cancel();
		groupMemberAddTimer.purge();
		groupMemberAddTimer.cancel();
		groupMemberAddtask=null;
	}
	
	/**
	 * 是否在线
	 * @return
	 */
	public boolean isOnline() {
		return isOnline;
	}
	
	/**
	 * 上线后的操作
	 */
	private void upLine() {
		try {
			startGroupMemberAddCheck();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	/**
	 * 下线后的操作
	 */
	private void offLine() {
		try {
			stopGroupMemberAddCheck();
			close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
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
