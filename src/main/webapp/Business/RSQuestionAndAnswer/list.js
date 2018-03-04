$(document).ready(function(){
});


var questionAndAnswerTable = new TableView('questionAndAnswerTable');
questionAndAnswerTable.url = '/wechatass/getQuestionAndAnswerList';
questionAndAnswerTable.initHeader([
	{id:'marker',name:'',width:2,align:'center',type:'radio'},
	{id:'index',name:'序号',width:10,align:'center',key:true},
	{id:'keywords',name:'关键词',width:20,align:'center'},
	{id:'question',name:'问题',width:20,align:'center'},
	{id:'answer',name:'答案',width:40,align:'center'}
]);
questionAndAnswerTable.ajaxCallback = function(data){
	questionAndAnswerTable.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		questionAndAnswerTable.add({
			index: i+1, 
			keywords : data["rows"][i]["keywords"],
			question : data["rows"][i]["question"],
			answer : data["rows"][i]["answer"],
			data: data["rows"][i]
		});
	}
};
questionAndAnswerTable.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
questionAndAnswerTable.clearQueryComp = function(){
};
questionAndAnswerTable.render();



