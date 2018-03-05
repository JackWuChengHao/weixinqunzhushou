<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/wechatass/Static/Bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/wechatass/Static/Plugin/sweetalert/css/sweetalert.css" />
<link rel="stylesheet" type="text/css" href="/wechatass/Static/Plugin/webui-popover/css/jquery.webui-popover.css" />
<link rel="stylesheet" type="text/css" href="/wechatass/CSS/RSWechatMgrStyle.css" />
<title>矛盾调解</title>
</head>
<body>
	<div class="container">
		<div class="page-header" style="text-align:center;">
  			<h1>请填写以下信息</h1>
  			<h3>（带“*”为必填项）</h3>
		</div> 
		<div class="form-group">
			<label class="col-md-3 control-label" style="text-align:center;font-size:30px;">姓名<span style="color:red;">*</span></label>
		  	<br/>
		  	<div class="col-md-9">
		  		<input class="form-control" id="name" style="height:70px;font-size:35px;"/>
		  	</div>
		</div><br/>
		<div class="form-group">
			<label class="col-md-3 control-label" style="text-align:center;font-size:30px;">证件号码<span style="color:red;">*</span></label>
			<br/>
			<div class="col-md-9">
		  		<input class="form-control" id="cardnum" style="height:70px;font-size:35px;"/>
		  	</div>
		</div><br/>
		<div class="form-group">
			<label class="col-md-3 control-label" style="text-align:center;font-size:30px;">联系电话<span style="color:red;">*</span></label>
			<br/>
			<div class="col-md-9">
		  		<input class="form-control" id="phone" style="height:70px;font-size:35px;"/>
		  	</div>
		</div><br/>
		<div class="form-group">
			<label class="col-md-3 control-label" style="text-align:center;font-size:30px;">问题描述<span style="color:red;">*</span></label>
			<br/>
			<div class="col-md-9">
		  		<textarea class="form-control" id="describe" style="font-size:35px;" rows="10"></textarea>
		  	</div>
		</div><br/>	
		
		<div class="row" style="text-align:center;">
			<button type="button" class="col-md-4 col-md-offset-4 btn btn-success" style="font-size:35px;width:450px;height:100px;" id="commitQuestion">提 交</button>
		</div>
		<br/><br/>
	</div>

    <script type="text/javascript" src="/wechatass/Static/jQuery/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Plugin/sweetalert/js/sweetalert.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Plugin/webui-popover/js/jquery.webui-popover.js"></script>
	<script type="text/javascript" src="/wechatass/Business/common/common.js"></script>


</body>
</html>