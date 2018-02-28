$(document).ready(function(){
	bindSendGroupMessageBtn();
});

function bindSendGroupMessageBtn(){
	$("#add_normal_msg").on("click",function(){
		var message = $("#normal_message_text").val();
		if(message==null || message ===""){
			return;
		}
	
		var selectedList = normalGroupList.getSelected();
		if(selectedList.length < 1){
			return;
		}
		var groupnamelist = [];
		for(var i = 0 ; i < selectedList.length;i++){
			groupnamelist.push(selectedList[i]["data"]["username"]);
		}
		sendAjax("/wechatass/sendmessagetoGroup",{"message":message,"groupnamelist":groupnamelist});
	});
}
