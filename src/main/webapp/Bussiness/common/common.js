$(document).ready(function(){
});

String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g,"");
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

