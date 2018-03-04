<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>群数据统计</title>
</head>
<body>
	<jsp:include page="/commPages/Navbar.jsp" flush="true"></jsp:include>
	<div class="main-container" id="main-container">
		<div class="main-container-inner">
			<jsp:include page="/commPages/Menu.jsp" flush="true"></jsp:include>
			<div class="main-content">
				<div class="container">
					<br>
					<div class="form-group">
						<img src="/wechatass/imgs/line.png">&nbsp;&nbsp;<span>群数据统计</span> 
					</div>
					<hr />
					<div class="row" >
						<div class="col-md-4" id="chatTime" style="height:500px;width:400px"></div>
						<div class="col-md-4" id="radarChart" style="height:500px;width:400px"></div>
						<div class="col-md-4" id="popgroupChart" style="height:500px;width:400px"></div>
					</div>
					<br/>
					<div id="datacounttable" style="height:500px;width:1200px"></div>
					<br/>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/chartscommon.jsp" flush="true"></jsp:include>
</body>

<!-- <script type="text/javascript" -->
<!-- 	src="/wechatass/Business/DataCount/list.js"></script> -->
	<script type="text/javascript" src="/wechatass/Business/DataCount/init.js"></script>
	
</html>