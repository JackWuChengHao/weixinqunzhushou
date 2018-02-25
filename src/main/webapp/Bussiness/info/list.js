$(document).ready(function(){
});

var testTable = new TableView('testTable');
testTable.url = '/wechatass/getmember?type=group';
testTable.initHeader([
	{id:'marker',width:2,align:'right',type:'radio'},
	{id:'index',name:'序号',width:30,align:'center',key:true},
	{id:'nickname',name:'名称',width:70,align:'center'},
]);
testTable.ajaxCallback = function(data){
	testTable.clear();
	var len = data["rows"].length;
	for(var i=0;i<len;i++){
		testTable.add({
			index: i+1, 
			nickname : data["rows"][i]["NickName"],
			data : data["rows"][i]
		});
	}
};
testTable.clearQueryComp = function(){
};
testTable.ajaxFailCallback = function(){
	swal("提示", "网络故障", "error");
};
testTable.render();