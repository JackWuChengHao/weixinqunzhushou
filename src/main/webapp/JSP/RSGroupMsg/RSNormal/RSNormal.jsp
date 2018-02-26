<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>普通群发</title>
</head>
<body>
	<jsp:include page="./addNormalModel.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/Navbar.jsp" flush="true"></jsp:include>
	<div class="main-container" id="main-container">
		<div class="main-container-inner">
			<jsp:include page="/commPages/Menu.jsp" flush="true"></jsp:include>
			<div class="main-content">
				<div class="container">
					<br />
					<div class="form-inline">
						<h4>普通群发</h4>
						<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addNormalModal">
							<span class="glyphicon glyphicon-plus"></span>
						</button>
					</div>
					<br />
					<br />
					<br />
					<div id="NormalGroupList"></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>
 
<script type="text/javascript"
	src="/wechatass/Business/RSNormal/list.js"></script>
<script type="text/javascript"
	src="/wechatass/Business/RSNormal/init.js"></script>
	
</html>