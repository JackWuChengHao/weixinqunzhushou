<%@page import="java.text.SimpleDateFormat"%>

<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  

	<nav class="navbar navbar-default" style="background-color:#223a5e">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#"><span id="TBNCRMainTitle">桃娃微信群管理平台1.0</span></a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a id="TXWelcomeDate"></a></li>			
					<li><a id="TXWelcome">欢迎您！<span id="currentUser"></span></a></li>
					<li id="TXExit"><a href="/webapp/logout">注销</a></li>
					<li id="currentDepartmentName" style="display: none;"></li>
				</ul>
			</div>
		</div>
	</nav>