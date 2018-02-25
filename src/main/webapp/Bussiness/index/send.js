$(document).ready(function(){
	bindSendBtn();
});

function bindSendBtn(){
	$("#sendBtn").on("click",function(){
		var selectedList = testTable.getSelected();
		if(selectedList.length !== 1){
			return;
		}
		
		var groupid = selectedList[0]["data"]["UserName"];
		var content = $("#sendContent").val();
		sendAjax("/wechatass/sendmessage",{"message":content,"username":groupid});
	});
}