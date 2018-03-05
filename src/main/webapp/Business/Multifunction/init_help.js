/**
 * 
 */

$(document).ready(function(){
	bindcommitHelpQuestion();
});


function bindcommitHelpQuestion(){
	$("#commitHelpQuestion").on('click',function(){
		var name = $("#name").val();
		var cardnum = $("#cardnum").val();
		var describe = $("#describe").val();
		var type = "1";
		var phone = $("#phone").val();
		var result = sendAjax("/wechatass/addPublicAsk",{'name':name,'personalId':cardnum,'content':describe,'askType':type,'phone':phone});
		console.log(typeof(result['code']));
		if(result['code']  !== 0){
			alert("提交失败");
		}else{
			 $("#name").val('');
			$("#cardnum").val('');
			$("#describe").val('');
			$("#phone").val('');
//			window.href = '/wechatass/JSP/RSMultifunctionWizard/RSbiaozhunNotice.jsp'
			 //window.history.back(-1);
			 window.location.href="/wechatass/JSP/RSMultifunctionWizard/RSbiaozhunNotice.jsp";
		}
	});
}