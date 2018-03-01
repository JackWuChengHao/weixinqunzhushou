package com.rs.wxmgr.wechat;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rs.wxmgr.service.TestService;
import com.rs.wxmgr.wechat.common.WXContact;
import com.rs.wxmgr.wechat.common.WXHttpClient;
import com.rs.wxmgr.wechat.entity.Group;
import com.rs.wxmgr.wechat.entity.Member;
import com.rs.wxmgr.wechat.listener.SyncCheckListener;
import com.rs.wxmgr.wechat.listener.impl.GroupMemberChangeListener;
import com.rs.wxmgr.wechat.listener.impl.NewMessageListener;
import com.rs.wxmgr.wechat.utils.InforUtils;
import com.rs.wxmgr.wechat.utils.LoginUtils;
import com.rs.wxmgr.wechat.utils.MessageUtils;
import com.rs.wxmgr.wechat.utils.SyncCheckUtils;

/**
 *  tomcat，则catalina.sh增加：-Djsse.enableSNIExtension=false
 * @author zm
 *
 */
public class Robot implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(Robot.class);
	
	private List<SyncCheckListener> listenerList = new ArrayList<SyncCheckListener>();
	
	private WXHttpClient client = new WXHttpClient();
	/**
	 * 通讯录
	 */
	private WXContact contact = new WXContact();
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
	 * 线程池
	 */
	private ThreadPoolExecutor threadPoolExecutor = 
			new ThreadPoolExecutor(1, 10, 1, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>());
	
//	/**
//	 * 检测组群成员增加的间隔
//	 */
//	private static final long GROUP_MEMBER_ADD_INTERVAL = 3000L;
	
//	private Timer reflushContactTimer;
//    private TimerTask reflushContactTask;
	
	private TestService testService;
	
	public Robot(TestService testService) {
		this.testService = testService;
		initListener();
	}
	
	private void initListener() {
		listenerList.add(new NewMessageListener(client,contact));
		listenerList.add(new GroupMemberChangeListener(client, contact,testService));
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
						int result = SyncCheckUtils.check(client);
						if(result == -1) {
							// 掉线
							isOnline = false;
							Robot.this.offLine();
							break;
						} else if(result != 0) {
							JSONObject response = SyncCheckUtils.syncMessage(client);
							
							for(SyncCheckListener listener:listenerList) {
								listener.handle(response);
							}
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
		isOnline = LoginUtils.init(client,contact);
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
		return contact;
	}
	
	/**
	 * 刷新组群信息
	 * @throws Exception 
	 */
	public void reflushContact() throws Exception {
		// 获取联系人列表
		List<JSONObject> memberList = InforUtils.getMemberList(client);
		for(JSONObject memberJson:memberList) {
			String username = memberJson.getString("UserName");
			if(username == null) {
				continue;
			}
			if(username.startsWith("@@")) {
				// 组群
				contact.addGroupSet(username);
			} else if(username.startsWith("@")) {
				// 好友
				contact.putMember(Member.parse(memberJson));
			}
		}
		
		// 通过组群的id获取组群详细信息
		JSONObject batchContact = InforUtils.getBatchContact(client, contact.getGroupSet());
		String contactList = batchContact.getString("ContactList");
		if(contactList != null) {
			List<JSONObject> groupJsonList = JSONArray.parseArray(batchContact.getString("ContactList"), JSONObject.class);
			for(JSONObject groupJson:groupJsonList) {
				Group group = Group.parse(groupJson);
				contact.putGroup(group);
			}
		}
		
	}
	
	/**
	 * 向群或好友发送消息
	 * @param message
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String message,String username) throws Exception {
		if(!isOnline) {
			return null;
		}
		return MessageUtils.sendMessageByUsername(client, username, message);
	}
	
//	/**
//	 * 向有新人的组群发送消息
//	 * @throws Exception
//	 */
//	private void sendGroupMessageForUserAdd(Group group) throws Exception{
//		if(group == null || StringUtils.isBlank(group.getUsername())) {
//			return;
//		}
//		Group oldGroup = contact.getGroup(group.getUsername());
//		if(oldGroup != null) {
//			// 组群人数减少
//			if(oldGroup.getMemnerList().size() < group.getMemnerList().size()) {
//				List<WelcomeMsg> messageList = testService.selectMessageList();
//				int index = new Random().nextInt(messageList.size());
//				if(index!=0&&index==messageList.size()) index--;
//				String message = messageList.get(index).getMessage();
//				
//				sendMessage(message, group.getUsername());
//			}
//		}
//	}
	
	/**
	 * 是否在线
	 * @return
	 */
	public boolean isOnline() {
		return isOnline;
	}
	
//	/**
//	 * 定时任务刷新组群信息。
//	 * 因为心跳包不会发送组群人员变动,只能定时刷群成员情况
//	 */
//	private void startReflushGroupContact() {
//		reflushContactTimer = new Timer();
//	    reflushContactTask = new TimerTask() {
//			@Override
//			public void run() {
//				try {
//					reflushContact();
//				} catch (Exception e) {
//					logger.error(e.getMessage(), e);
//				}
//			}
//		};
//		reflushContactTimer.schedule(reflushContactTask, 0, GROUP_MEMBER_ADD_INTERVAL);
//	}
//	/**
//	 * 关闭刷新联系人定时任务
//	 */
//	private void stopReflushGroupContact() {
//		reflushContactTask.cancel();
//		reflushContactTimer.purge();
//		reflushContactTimer.cancel();
//		reflushContactTask=null;
//	}
	
	/**
	 * 上线后的操作
	 */
	private void upLine() {
		try {
			reflushContact();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	/**
	 * 下线后的操作
	 */
	private void offLine() {
		try {
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
