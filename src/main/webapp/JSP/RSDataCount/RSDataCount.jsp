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
						<img src="/webapp/imgs/line.png">&nbsp;&nbsp;<span>群数据统计</span> 
					</div>
					<hr />
					<div class="row" style="height:20px;">
						<div class="col-md-2" style="color:#223a5e;">23个 &nbsp;&nbsp;总群数</div>
						<div class="col-md-2" style="color:#223a5e;">50个 &nbsp;&nbsp;去重前人数</div>
						<div class="col-md-2" style="color:#223a5e;">0个 &nbsp;&nbsp;入群人数</div>
						<div class="col-md-2" style="color:#223a5e;">0个 &nbsp;&nbsp;退群人数</div>
					</div>
					<br/>
					<div class="row" >
						<div id="datacharts" style="width: 80%px;height:400px;"></div>
					</div>
					<div class="row" style="height:30px;">
						<span class="col-md-1">详细数据 </span>
						<select class="col-md-1"><option>今天</option></select>
						&nbsp;&nbsp;
						<button type="button" id="add_welcome" class="btn btn-primary" > 搜索</button>
						<button type="button" id="add_welcome" class="btn btn-primary" >数据导出</button>
					</div>
					<br/>
					<div id="datacounttable"></div>
					<br/>
				</div>
			
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>

<script type="text/javascript"
	src="/wechatass/Business/DataCount/list.js"></script>
<script type="text/javascript"
	src="/wechatass/Business/DataCount/init.js"></script>
	
</html>