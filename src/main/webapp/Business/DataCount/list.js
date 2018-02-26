$(document).ready(function(){
});


var datacounttable = new TableView('datacounttable');
datacounttable.url = '/wechatass/getgroup';
datacounttable.initHeader([
	{id:'index',name:'序号',width:'10%',align:'center',key:true},
	{id:'name',name:'群名称',width:'10%',align:'center'},
	{id:'memberCount',name:'群人数',width:'10%',align:'center'},
	{id:'inpersonnumber',name:'入群人数',width:'10%',align:'center'},
	{id:'outpersonnumber',name:'退群人数',width:'10%',align:'center'},
	{id:'talks',name:'发言数',width:'10%',align:'center'},
]);
datacounttable.ajaxCallback = function(data){
	datacounttable.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		datacounttable.add({
			index: i+1, 
			name : data["rows"][i]["NickName"],
		    memberCount : data["rows"][i]["MemberCount"],
		    inpersonnumber:0,
		    outpersonnumber:0,
		    talks:0,
		});
	}
};
datacounttable.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
datacounttable.clearQueryComp = function(){
};
datacounttable.render();
