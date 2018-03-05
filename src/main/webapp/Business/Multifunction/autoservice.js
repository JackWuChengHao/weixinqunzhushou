/**
 * 
 */

$(document).ready(function(){
});

var rsautoservice = new TableView('rsautoservice');
rsautoservice.url = '/wechatass/getPublicAskList';
rsautoservice.initHeader([
	{id:'index',name:'序号',width:'5%',align:'center',key:true},
	{id:'name',name:'申请人',width:'20%',align:'center'},
	{id:'content',name:'问题描述',width:'25%',align:'center'},
	{id:'time',name:'提问时间',width:'25%',align:'center'},
	{id:'type',name:'提问类型',width:'25%',align:'center'}
]);

rsautoservice.ajaxCallback = function(data){
	rsautoservice.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		rsautoservice.add({
			index: i+1, 
			id : data["rows"][i]["id"],
			name :  data["rows"][i]["name"],
			content:data["rows"][i]["content"],
			time:data["rows"][i]["createTime"],
			type:data["rows"][i]["askType"] == '0'?'矛盾调解':'法律援助',
		});
	}
};

rsautoservice.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
rsautoservice.clearQueryComp = function(){
};
rsautoservice.render();

