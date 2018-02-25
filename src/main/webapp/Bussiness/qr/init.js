$(document).ready(function(){
	self.setInterval("checkLoginStatus()",1*1000);
});

/**
 * 检查机器人是否已经登录
 * @returns
 */
function checkLoginStatus(){
	var result = sendAjax("/wechatass/checklogin");
	// 登录成功
	if(result["data"]==true){
		window.location.href="/wechatass/";
	}
}
