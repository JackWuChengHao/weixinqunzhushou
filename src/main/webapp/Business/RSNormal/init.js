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
		if(selectedList.length !== 1){
			return;
		}
		var username = selectedList[0]["data"]["UserName"];
		
		sendAjax("/wechatass/sendmessage",{"message":message,"username":username});
	});
}
