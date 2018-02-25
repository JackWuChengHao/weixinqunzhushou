$(document).ready(function(){
	bind_add_welcome_msg();
	bind_close_add_welcome_msg();
}); 


function bind_add_welcome_msg(){
	$("#add_welcome_msg").on("click",function(){
		var content = $("#message-text").val();
		if(content === "" || content === undefined){
			swal("提示", "请填写有效内容", "info");
		}else{
			sendAjax("/wechatass/addWelcomeMsg",{"message":content});
		}
	});
}


function bind_close_add_welcome_msg(){
	$("#close_add_welcome_msg").on("click",function(){
	 $("#message-text").val("");
	});
}
