$(document).ready(function(){
});

function stripencodescript(s){
	if(typeof s != "string"){
		return s;
	}else{
		s = s.replace(/&/g,'&amp;');
		s = s.replace(/</g,'&lt;');
		s = s.replace(/>/g,'&gt;');
		s = s.replace(/ /g,'&nbsp;');
		s = s.replace(/"/g,'&quot;');
		s = s.replace(/'/g,'&#39;');
		s = s.replace(/\//g,'&divide;');
		s = s.replace(/\*/g,'&times;');
		return s;
	}
}

function stripdecodescript(s){
	if(typeof s != "string"){
		return s;
	}else{
		s = s.replace(/&amp;/gi,'&');
		s = s.replace(/&nbsp;/gi,' ');
		s = s.replace(/&quot;/gi,'"');
		s = s.replace(/&#39;/g,"'");
		s = s.replace(/&lt;/gi,'<');
		s = s.replace(/&gt;/gi,'>');
		s = s.replace(/&divide;/g,'\/');
		s = s.replace(/&times;/g,'*');
		return s;
	}
}

String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g,"");
}

/**
 * 判断搜索条件是否存在合法
 * 大小写字母、数字、英文“-”
 */
function isNumValid(id){
	var jueryId = "#" + id;
	$(jueryId).keyup(function(e){
		if(e.which==37 || e.which==39){
			return;
		}else{
			value = $(jueryId).val();
			value = value.replace(/[^a-zA-Z0-9--]/g,'');
			$(jueryId).val(value);
		}
	});
	$(jueryId).on("input",function(){
		value = $(jueryId).val();
		value = value.replace(/[^a-zA-Z0-9--]/g,'');
		$(jueryId).val(value);
	});
	$(jueryId).on("paste",function(){
		value = $(jueryId).val();
		value = value.replace(/[^a-zA-Z0-9--]/g,'');
		$(jueryId).val(value);
	});
	
}


/**
 * 姓名输入是否合法
 * @param id
 */
function isNameValid(id){
	var jueryId = "#" + id;
	$(jueryId).on("change",function(e){
		if(e.which==37 || e.which==39){
			return;
		}else{
			value = $(jueryId).val();
			value = value.replace(/[^\u4e00-\u9fa5|0-9]/g,'');
			value = value.replace(/[\|]/g,'');
			$(jueryId).val(value);
		}
	});
	$(".TXPageSearch-BTN btn btn-default userInfoTable-query-submit").on("click",function(){
			value = $(jueryId).val();
			value = value.replace(/[^\u4e00-\u9fa5|0-9]/g,'');
			$(jueryId).val(value);
	});
}

function formatDate(){
	Date.prototype.format = function(fmt){
		var o = {
				"M+":this.getMonth()+1,
				"d+":this.getDate(),
				"H+":this.getHours(),
				"m+":this.getMinutes(),
				"s+":this.getSeconds()
		};
		if(/(y+)/.test(fmt)){
			fmt=fmt.replace(RegExp.$1,
					(this.getFullYear()+"").substr(4-RegExp.$1.length)		
			);
			for(var k in o){
				if(new RegExp("("+k+")").test(fmt)){
					fmt=fmt.replace(RegExp.$1,(RegExp.$1.length==1)?(o[k]):(("00"+o[k]).substr((""+o[k]).length)));
				}
			}
			return fmt;
		}
	}
}

function getFormatDate(date){
	if(date == null){
		return "";
	}
	return new Date(date).format("yyyy-MM-dd");
}
function getFormatDateH(date){
	if(date == null){
		return "";
	}
	return new Date(date).format("yyyy-MM-dd HH:mm");
}

//显示返回内容
function alertResult(data){
	var isSuccess = false;
	if(data == null){
	} else if(data["code"] !== 0){
		swal("", data["msg"], "error");
	} else {
		swal("", "操作成功", "success");
		isSuccess = true;
	}
	return isSuccess;
}

//发送ajax请求
function sendAjax(url,data){
	var result = undefined;
	$.ajax({
		type:"POST",
		contentType : "application/json;charset=utf-8",
		url : url,
		async : false,
		data : JSON.stringify(data),
		success : function(data,status){
			result = data;
		},
		error : function(){
			swal("", "网络故障", "error");
		}
	});
	return result;
}

/**
 * 下载文件
 * @param id
 */
function downloadFile(id){
	if(id == null){
		swal("", "文件不存在", "error");
		return;
	}
	window.location.href = "/wechatass/downloadFile?id=" + id;
}

/**
 * 通过措施id下载证件zip
 * @param id
 */
function downloadEvidences(id){
	var data = sendAjax("/wechatass/isNcrMeasureHasEvidence",{"measureId":id});
	if(data["code"] !== 0){
		swal("提示", data["msg"], "error");
		return;
	} 
	if(data["data"] === false){
		swal("提示", "该措施未上传证据文件", "warning");
		return;
	}
	window.location.href = "/wechatass/downloadEvidences?id=" + id;
}
/**
 * 通过措施id下载表单和措施变更文件
 * @param id
 * @returns
 */
function downloadFileAndChangeFiles(id){
	window.location.href = "/wechatass/downloadFileAndChangeFiles?id=" + id;
}

/**
 * 开具项来源
 * @returns
 */
function bindSourceSelect(sourceSelectId){
	var result = sendAjax("/wechatass/getTypeSourceList");
	if(result["code"] !== 0){
		swal("", data["msg"], "error");
		return;
	}
	$("#"+sourceSelectId).empty();
	$("#"+sourceSelectId).append('<option value="-1">--请选择--</option>');
	for(var i in result['rows']){
		$("#"+sourceSelectId).append('<option value="'+result['rows'][i]['id']+'">'+result['rows'][i]['sourceName']+'</option>');
	}
}
/**
 * 开具项类型
 * @returns
 */
function bindTypeSelect(sourceSelectId,typeSelectId){
	$("#"+sourceSelectId).on('change',function(){
		var sourceId = $("#"+sourceSelectId).find("option:selected").val();
		if(sourceId == '-1'){
			sourceId = null;
		}
		initTypeSelect(typeSelectId,sourceId);
	});
	initTypeSelect(typeSelectId,null);
}


var typeList = null;
/**
 * 开具项类型是否需要上传表单
 * @param typeId
 * @returns
 */
function isTypeNeedForm(typeId){
	for(var i in typeList){
		if(typeList[i]["id"] === Number(typeId)){
			return typeList[i]["hasSheet"] === "y";
		}
	}
}
function initTypeSelect(typeSelectId,sourceId){
	var result = sendAjax("/wechatass/getTypeListBySource",{'sourceId':sourceId});
	if(result["code"] !== 0){
		swal("", data["msg"], "error");
		return;
	}
	$("#"+typeSelectId).empty();
	$("#"+typeSelectId).append('<option value="-1">--请选择--</option>');
	for(var i in result['rows']){
		$("#"+typeSelectId).append('<option value="'+result['rows'][i]['id']+'">'+result['rows'][i]['type']+'</option>');
	}
}
/**
 * 部门
 * @returns
 */
function initDepartmentSelect(departmentSelectId){
	var data = sendAjax("/wechatass/getDepartmentList");
	if(data["code"] !== 0){
		swal("", data["msg"], "error");
		return;
	}
	var departmentList = data.rows;

	$("#"+departmentSelectId).empty();
	$("#"+departmentSelectId).append('<option value="-1">--请选择--</option>');
	for(var i in departmentList){
		$("#"+departmentSelectId).append('<option value="'+departmentList[i].id+'">'+departmentList[i]['departmentName']+'</option>');
	}
}
/**
 * 开具人
 * @returns
 */
function initCreatePersionSelect(createPersionSelectId){
	var element = $("#"+createPersionSelectId);
	element.on("mousedown",function(){
		var data = sendAjax("/wechatass/getCreatePersionList");
		if(data["code"] !== 0){
			swal("", data["msg"], "error");
			return;
		}
		var userList = data.rows;

		var oldVal = element.val();
		element.empty();
		element.append('<option value="-1">--请选择--</option>');
		for(var i in userList){
			element.append('<option value="'+userList[i].id+'">'+userList[i]['name']+'</option>');
		}
		element.val(oldVal);
	});
}

/**
 * 初始化时间控件
 * @returns
 */
function initTimeSerach(timeInputId){
	laydate({
        elem: '#'+timeInputId,
		event:"focus",
		istime:true,
		istoday:false,
		festival:true,
        format:'YYYY-MM-DD',
		zindex:99999999,
        choose: function(datas){
        	if($("#"+timeInputId+"Val")!=null){
            	$("#"+timeInputId+"Val").val(datas);
        	}
        },
        clear: function(){
        	if($("#"+timeInputId+"Val")!=null){
            	$("#"+timeInputId+"Val").val("");
        	}
        }
    });
	laydate.skin("dahong");
}
function initTimeSerachH(timeInputId){
	laydatex({
        elem: '#'+timeInputId,
		event:"focus",
		istime:true,
		istoday:false,
		festival:true,
        format:'YYYY-MM-DD hh:mm',
		zindex:99999999,
        choose: function(datas){
        	if($("#"+timeInputId+"Val")!=null){
            	$("#"+timeInputId+"Val").val(datas);
        	}
        },
        clear: function(){
        	if($("#"+timeInputId+"Val")!=null){
            	$("#"+timeInputId+"Val").val("");
        	}
        }
    });
	laydate.skin("dahong");
}

/**
 * 文本域判空
 * @returns
 */
function judgeNullAndEmpty(item){
	var tmp = "#" + item;
	var content = $(tmp).val();
	if(content === null || content.trim() === ""){
		return 0;
	}
}

/**
 * 必填项气泡
 * @returns
 */
function initNecessaryItems(){
	$(".TXNecessaryItem").webuiPopover({
		placement:'right',
		html:true,
		content:"必填项",
		style:"",
		animation:"fade",
		delay:{
			show:null,
			hide:300
		},
		trigger:"hover"
	});
}


function clearUploadController(){
	$(".fileinput-remove-button").click(function(){
		$(".TXFileId").val("");
	});
}
/**
 * 刷新时间
 * @returns
 */
function flushSystemTime(){
	$.ajax({
		type:"POST",
		contentType : "application/json;charset=utf-8",
		url : "/wechatass/getSystemTime",
		async : true,
		success : function(data,status){
			if(data == null || data["code"] !== 0){
				return;
			}
			var systemTime = data["data"];
			$("#TXWelcomeDate").text(new Date(systemTime).format("yyyy-MM-dd HH:mm"));
		},
		error : function(){
			return;
		}
	});
}
