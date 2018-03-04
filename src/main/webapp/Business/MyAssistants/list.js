$(document).ready(function(){
});

var MyAssistantsList = new TableView('MyAssistantsList');
MyAssistantsList.url = '/wechatass/getWelcomeMsgList';
MyAssistantsList.initHeader([
	{id:'index',name:'序号',width:10,align:'center',key:true},
	{id:'name',name:'助手名称',width:50,align:'center'},
	{id:'num',name:'已开通群数',width:20,align:'center'},
	{id:'time',name:'开通时间',width:20,align:'center'},
]);

MyAssistantsList.ajaxCallback = function(data){
	MyAssistantsList.clear();
	var len = data["rows"].length;
	for(var i=0;i<1;i++){
		MyAssistantsList.add({
			index: i+1, 
			id : data["rows"][i]["id"],
			name : '桃娃助手1号',
			num:'12/100',
			time:'2018-3-4'
		});
	}
};

MyAssistantsList.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
MyAssistantsList.clearQueryComp = function(){
};
MyAssistantsList.render();

