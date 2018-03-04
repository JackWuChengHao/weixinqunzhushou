$(document).ready(function(){
});

var rsautoservice = new TableView('rsautoserviceList');
rsautoservice.url = '/wechatass/getrsautoserviceList';
rsautoservice.initHeader([
	{id:'index',name:'序号',width:10,align:'center',key:true},
	{id:'area',name:'社区',width:10,align:'center'},
	{id:'question',name:'问题描述',width:40,align:'center'},
	{id:'time',name:'提交时间',width:10,align:'center'},
	{id:'name',name:'提问者姓名',width:10,align:'center'},
	{id:'phone',name:'联系方式',width:10,align:'center'},
	{id:'fix',name:'是否解决',width:10,align:'center'},
]);

rsautoservice.ajaxCallback = function(data){
	rsautoservice.clear();
	var len = data["rows"].length;
	for(var i=0;i<1;i++){
		MyAssistantsList.add({
			index: i+1, 
			area : data["rows"][i]["id"],
			question : 'asdadasdasdasdasdasdasdasdasdsad',
			time:'2018-2-25',
			name:'xiaowu',
			phone:'123456789',
			fix:'是',
		});
	}
};

rsautoservice.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
rsautoservice.clearQueryComp = function(){
};
rsautoservice.render();

