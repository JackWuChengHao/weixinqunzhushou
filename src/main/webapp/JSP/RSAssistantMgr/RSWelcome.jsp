<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>入群欢迎语</title>
</head>
<body>

	<jsp:include page="./addWelcomeContent.jsp" flush="true"></jsp:include>
	
	<jsp:include page="/commPages/Navbar.jsp" flush="true"></jsp:include>
	<div class="main-container" id="main-container">
		<div class="main-container-inner">
			<jsp:include page="/commPages/Menu.jsp" flush="true"></jsp:include>
			<div class="main-content">
				<div class="container">
					<br>
					<div class="form-group">
						<img src="/wechatass/imgs/line.png">&nbsp;&nbsp;<span>入群欢迎语</span> 
					</div>
					<div class="alert alert-info" role="alert"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;&nbsp;可设置多条欢迎语内容，当有新成员入群时，系统会随机推送一条欢迎语</div>
					
					<div class="form-group">
						<button type="button" id="add_welcome" class="btn btn-primary" data-toggle="modal" data-target="#welcomeModal">
							<span class="glyphicon glyphicon-plus"></span> 添加欢迎语
						</button>
					</div>
					<hr/>
					<div id="WelcomeList"></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>

<script type="text/javascript"
	src="/wechatass/Business/common/common.js"></script>
<script type="text/javascript"
	src="/wechatass/Business/GroupWelcomeList/list.js"></script>
<script type="text/javascript"
	src="/wechatass/Business/GroupWelcomeList/init.js"></script>

</html>