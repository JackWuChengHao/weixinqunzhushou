<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>信息分流</title>
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
						<img src="/wechatass/imgs/line.png">&nbsp;&nbsp;<span>信息分流</span> 
					</div>
					<hr/>
					<div class="form-group">
						<div class="row">
							<div class="col-md-4">
							<button class="btn btn-success" style="height:100px;width:100px;">
								法律咨询
							</button>
							</div>
							<div class="col-md-4">
							<button class="btn btn-warning" style="height:100px;width:100px;">
								法律援助
							</button>
							</div>					
		 					<div class="col-md-3">
		 					<button class="btn btn-danger" style="height:100px;width:100px;">
								法律法规
							</button>
							</div>	
							<div class="col-md-3">
		 					<button class="btn btn-primary" style="height:100px;width:100px;">
								其他咨询
							</button>
							</div>	
	 					</div>
					</div>
					
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>
</html>