/**
 * 
 */

$(document).ready(function(){
	bindCheckBtn();
});


function bindCheckBtn(){
	$("#btnLogin").on("click",function(){
		var username = $("#txtUserName").val();
		var pass = $("#txtPassword").val();
		
		if(username === 'XJ' && pass === 'hssfj'){
			window.location.replace("/wechatass/index.jsp");
		}else if(username !== 'XJ' && pass === 'hssfj'){
			swal('','用户名错误','error')
			return;
		}else if(username === 'XJ' && pass !== 'hssfj'){
			swal('','密码错误','error')
			return;
		}
	});
}