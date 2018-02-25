$(document).ready(function(){
	checkLoginStatus();
});

/**
 * 检查机器人是否已经登录
 * @returns
 */
function checkLoginStatus(){
	var result = sendAjax("/wechatass/checklogin");
	// 登录成功
	if(result["data"]===true){
		window.location.href="/wechatass/info";
	} else {
		window.location.href="/wechatass/weqrpage";
	}
}
