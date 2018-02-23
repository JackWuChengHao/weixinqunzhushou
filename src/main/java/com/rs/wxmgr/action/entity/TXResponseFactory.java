package com.rs.wxmgr.action.entity;

import com.rs.wxmgr.common.content.TXErrorCode;

/**
 * 回复json工厂类
 * @author sun_zeming
 *
 */
public class TXResponseFactory {

	/**
	 * 创建成功回复
	 * @return
	 */
	public static TXResponse CreateSuccess(){
		TXResponse response = new TXResponse();
		response.setCode(TXResponse.SUCCESS_CODE);
		response.setMsg(TXResponse.SUCCESS_MSG);
		return response;
	}
	/**
	 * 创建失败回复
	 * @param errorCode 错误枚举类
	 * @return
	 */
	public static TXResponse CreateFail(TXErrorCode errorCode){
		TXResponse response = new TXResponse();
		response.setCode(errorCode.getCode());
		response.setMsg(errorCode.getMsg());
		return response;
	}
	/**
	 * 创建失败回复
	 * @param code 错误码
	 * @param msg 错误信息
	 * @return
	 */
	public static TXResponse CreateFail(int code,String msg){
		TXResponse response = new TXResponse();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}
}
