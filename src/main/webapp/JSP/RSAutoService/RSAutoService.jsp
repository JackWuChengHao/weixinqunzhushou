<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>分流信息处理</title>
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
						<img src="/wechatass/imgs/line.png">&nbsp;&nbsp;<span>分流信息处理</span> 
					</div>
					<hr />
					<div id="rsautoservice"></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>

    <script type="text/javascript" src="/wechatass/Static/jQuery/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Plugin/sweetalert/js/sweetalert.min.js"></script>
	<script type="text/javascript" src="/wechatass/Static/Plugin/webui-popover/js/jquery.webui-popover.js"></script>
	<script type="text/javascript" src="/wechatass/Business/common/common.js"></script>
	<script type="text/javascript" src="/wechatass/Business/Multifunction/autoservice.js"></script>

</html>