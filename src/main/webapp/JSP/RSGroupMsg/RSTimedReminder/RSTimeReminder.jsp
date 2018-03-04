<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>定时提醒添加</title>
</head>
<body>
	<jsp:include page="/commPages/Navbar.jsp" flush="true"></jsp:include>
	<div class="main-container" id="main-container">
		<div class="main-container-inner">
			<jsp:include page="/commPages/Menu.jsp" flush="true"></jsp:include>
			<div class="main-content">
				<div class="container">
					<br />
					<div class="form-group">
						<img src="/wechatass/imgs/line.png">&nbsp;&nbsp;<span>定时提醒</span> 
					</div>
					<div class="form-group">
						<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addTimeReminderModel">
							<span class="glyphicon glyphicon-plus"></span> 新建定时提醒
						</button>
					</div>
					<hr />
					<div id="TimedReminderList"></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>
 
	
</html>