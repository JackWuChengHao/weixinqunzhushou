$(document).ready(function(){
});

var WelcomeList = new TableView('WelcomeList');
WelcomeList.url = '/wechatass/getWelcomeMsgList';
WelcomeList.initHeader([
	{id:'index',name:'序号',width:20,align:'center',key:true},
	{id:'name',name:'入群欢迎语',width:60,align:'center'},
	{id:'op',name:'操作',width:20,align:'center'},
]);

WelcomeList.ajaxCallback = function(data){
	WelcomeList.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		WelcomeList.add({
			index: i+1, 
			id : data["rows"][i]["id"],
			name : data["rows"][i]["message"],
			op:'<button type="button" class="btn btn-success"><span class="glyphicon glyphicon-pencil"></span></button>&nbsp;&nbsp;<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span></button>',
		});
	}
};

WelcomeList.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
WelcomeList.clearQueryComp = function(){
};
WelcomeList.render();

