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
					<div class="form-inline">
						<h4>定时提醒</h4>
						<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addTimeReminderModel">
							<span class="glyphicon glyphicon-plus"></span> 添加定时提醒
						</button>
					</div>
					<br />
					<br />
					<br />
					<div id="TimedReminderList"></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>
 
<script type="text/javascript"
	src="/webapp/Business/RSTimedReminder/list.js"></script>
<script type="text/javascript"
	src="/webapp/Business/RSTimedReminder/init.js"></script>
	
</html>