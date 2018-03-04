<%@page import="java.text.SimpleDateFormat"%>

<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  

	<nav class="navbar navbar-default" style="background-color:#223a5e">
		<div class="container-fluid">
			
			<div class="navbar-header">
				<a class="navbar-brand" href="#"><img src="/wechatass/imgs/06.png" style="width:35px;height:35px"/></a>
				<p class="navbar-text" style="font-size:18px;">桃娃微信群管理平台1.0</p>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a id=""></a></li>			
					<li><a id="" style="font-size:15px;">欢迎您！许骏<span id="currentUser"></span></a></li>
					<li id=""><a href="/wechatass/JSP/login.jsp" style="font-size:15px;">注销</a></li>
					<li id="currentDepartmentName" style="display: none;"></li>
				</ul>
			</div>
		</div>
	</nav>