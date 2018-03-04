<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/wechatass/Static/Bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/wechatass/Static/Plugin/sweetalert/css/sweetalert.css" />
<link rel="stylesheet" type="text/css" href="/wechatass/Static/Plugin/webui-popover/css/jquery.webui-popover.css" />
<link rel="stylesheet" type="text/css" href="/wechatass/CSS/RSWechatMgrStyle.css" />
<title>法律咨询</title>
</head>
<body style="background-color:#223a5e">
	<div class="container">
		<div class="page-header" style="text-align:center;">
  			<h1 style="color:#fafafa">请选择您所在的社区</h1>
  			<h3 style="color:#fafafa">我们将为您转接相应的咨询室</h3>
		</div>
		<div class="row">
			<select class="col-md-12 form-control" id="hscommunities">
				<option value ="-1">--请选择--</option>
				<option value ="1">苏庙社区</option>
				<option value ="2">堰桥社区</option>
				<option value="3">礼社村</option>
				<option value="4">曙光村</option>
				<option value="5">石塘湾社区</option>
				<option value="6">寺头社区</option>
				<option value="7">金惠社区</option>
				<option value="8">陡门村</option>
			</select>
		</div>
		<br/><br/>
		<div class="row">
			<button type="button" class="col-md-12 btn btn-success" id="enterRoom">进 入</button>
		</div>
		<br/><br/>
		<div class="row">
			<a href="/wechatass/JSP/RSMultifunctionWizard/RSMultifunctionWizard.jsp"><button type="button" class="col-md-12 btn btn-danger">返 回</button></a>
		</div>
	</div>
	
	<jsp:include page="./RSLawFAQModal.jsp" flush="true"></jsp:include>
    <script type="text/javascript" src="/wechatass/Static/jQuery/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Plugin/sweetalert/js/sweetalert.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Plugin/webui-popover/js/jquery.webui-popover.js"></script>
	<script type="text/javascript" src="/wechatass/Business/common/common.js"></script>
	<script type="text/javascript" src="/wechatass/Business/RSDeal/common.js"></script>
</body>
</html>