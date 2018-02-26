$(document).ready(function(){
});


var normalGroupList = new TableView('NormalGroupList');
normalGroupList.url = '/wechatass/getgroup';
normalGroupList.initHeader([
	{id:'marker',name:'',width:2,align:'center',type:'radio'},
	{id:'index',name:'序号',width:10,align:'center',key:true},
	{id:'name',name:'组群名',width:70,align:'center'},
	{id:'memberCount',name:'人数',width:18,align:'center'}
]);
normalGroupList.ajaxCallback = function(data){
	normalGroupList.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		normalGroupList.add({
			index: i+1, 
			name : data["rows"][i]["NickName"],
			memberCount : data["rows"][i]["MemberCount"],
			data: data["rows"][i]
		});
	}
};
normalGroupList.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
normalGroupList.clearQueryComp = function(){
};
normalGroupList.render();

