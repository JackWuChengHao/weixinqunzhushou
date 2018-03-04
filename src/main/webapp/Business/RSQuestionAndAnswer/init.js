$(document).ready(function(){
	bindAddQuestionAndAnswerModalCommitBtn();
	bindDelQuestionAndAnswerBtn();
});

function bindAddQuestionAndAnswerModalCommitBtn(){
	$("#addQuestionAndAnswerModalCommitBtn").on("click",function(){
		var keywords = $("#keywordsText").val();
		var question = $("#questionText").val();
		var answer = $("#answerText").val();
		
		alertResult(sendAjax("/wechatass/addQuestionAndAnswer",
				{'keywords':keywords,'question':question,'answer':answer}
		));
	});
}

function bindDelQuestionAndAnswerBtn(){
	$("#delQuestionAndAnswerBtn").on("click",function(){
		var selectedList = questionAndAnswerTable.getSelected();
		if(selectedList.length !== 1){
			return;
		}
		var questionAndAnswer = selectedList[0];
		var id = questionAndAnswer["data"]["id"];
		
		alertResult(sendAjax("/wechatass/deleteQuestionAndAnswer",{'id':id}));
	});
}