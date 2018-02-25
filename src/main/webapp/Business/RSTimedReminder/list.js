$(document).ready(function(){
});


var TimedReminderList = new TableView('TimedReminderList');
TimedReminderList.url = '/wechatass/TimedReminderList';
TimedReminderList.initHeader([
	{id:'marker',name:'',width:2,align:'center',type:'radio'},
	{id:'index',name:'序号',width:5,align:'center',key:true},
	{id:'name',name:'姓名',width:10,align:'center'},
	{id:'username',name:'用户名',width:10,align:'center'},
	{id:'post',name:'职位',width:10,align:'center'},
	{id:'department',name:'部门',width:10,align:'center'},
	{id:'allowOperate',name:'人员资质',width:10,align:'center'}
]);
TimedReminderList.ajaxCallback = function(data){
	userInfoTable.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		userInfoTable.add({
			index: i+1, 
			id : data["rows"][i]["id"],
			roleList : data["rows"][i]["roleList"],
			name: data["rows"][i]["name"],
			username: data["rows"][i]["username"],
			post: data["rows"][i]["post"], 
			department: data["rows"][i]["department"]["departmentName"],
			allowOperate:data["rows"][i]["allowOperate"]==="y"?"有":"无"
		});
	}
};
TimedReminderList.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
TimedReminderList.clearQueryComp = function(){
};
TimedReminderList.render();

