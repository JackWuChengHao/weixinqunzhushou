$(document).ready(function(){
	bindChoose();
	bindSubmit();
	bindclear();
});


function bindChoose(){
	$("#enterRoom").click(function(){
		bindCheckChoice();
	});
};

function bindCheckChoice(){
	var community = $("#hscommunities").val();
	if(community === "-1" || community === "0"){
		swal("","请选择您所在的社区","info");
		return;
	}else{
		var roomName = $("#hscommunities option:selected").text();
		var tmp = roomName + "法律咨询";
		$("#roomName").html(tmp);
		$("#RSLawFAQModal").modal("show");
	}
}

function bindSubmit(){
	$("#commitQuestion").click(function(){
		var lawquestions = $("#lawquestions").val();
		var name = $("#name").val();
		var cardnum = $("#cardnum").val();
		var sex = $("#sex").val();
		var phone = $("#phone").val();

		if(lawquestions == "" || name == "" || cardnum == "" || sex == "" || phone == ""){
			swal("","请填写完整","warning");
			return;
		}else{
//			for zm and xiaowu
		}
	});
}

function bindclear(){
	$("#cancelQuestion").click(function(){
		$("#lawquestions").val("");
		$("#name").val("");
		$("#cardnum").val("");
		$("#sex").val("1");
		$("#phone").val("");
		$("#RSLawFAQModal").modal("hide");
	});
}