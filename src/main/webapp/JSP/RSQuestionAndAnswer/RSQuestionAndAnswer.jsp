<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/commPages/commCSS.jsp" flush="true"></jsp:include>
<title>添加问答</title>
</head>
<body>

	<jsp:include page="./addQuestoinAndAnswer.jsp" flush="true"></jsp:include>
	
	<jsp:include page="/commPages/Navbar.jsp" flush="true"></jsp:include>
	<div class="main-container" id="main-container">
		<div class="main-container-inner">
			<jsp:include page="/commPages/Menu.jsp" flush="true"></jsp:include>
			<div class="main-content">
				<div class="container">
					<br>
					<div class="form-group">
						<img src="/wechatass/imgs/line.png">&nbsp;&nbsp;<span>机器人问答</span> 
					</div>
					<div class="alert alert-info" role="alert"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;&nbsp;可添加机器人问答</div>
					
					<div class="form-group">
						<button type="button" id="addQuestionAndAnswerBtn" class="btn btn-primary" 
							data-toggle="modal" data-target="#addQuestionAndAnswerModal">
							<span class="glyphicon glyphicon-plus"></span> 添加
						</button>
						<button type="button" id="delQuestionAndAnswerBtn" class="btn btn-primary">
							<span class="glyphicon glyphicon-remove"></span> 删除
						</button>
					</div>
					<hr/>
					<div id="questionAndAnswerTable"></div>
					<br/>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
	<jsp:include page="/commPages/commJS.jsp" flush="true"></jsp:include>
</body>

<script type="text/javascript" src="/wechatass/Business/common/common.js"></script>
<script type="text/javascript" src="/wechatass/Business/RSQuestionAndAnswer/init.js"></script>
<script type="text/javascript" src="/wechatass/Business/RSQuestionAndAnswer/list.js"></script>

</html>