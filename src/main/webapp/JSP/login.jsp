<!DOCTYPE html>
<html lang="zh-cn">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/wechatass/Static/Bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="/wechatass/Static/login/font-awesome.min.css" rel="stylesheet">
<link href="/wechatass/Static/login/style.css" rel="stylesheet">
<link href="/wechatass/Static/login/login-v2.css" rel="stylesheet">
<title>首页</title>
</head>
<body>



    <nav class="navbar p-navbar logo-professional" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="http://z.xiaouguanjia.com/Login.aspx#"><i class="logo"></i>U聊后台管理系统</a>
            </div>
            <ul class="nav navbar-nav navbar-right">
            <li><a href="javascript:void(0)">无锡若善文化传播有限公司</a></li>
        </ul>
        </div>
    </nav>
    <div class="container-fluid login">
        <div class="left-box">
            
              <img src="/wechatass/imgs/login-bg-3.jpg" class="bg">
             
        </div>
        <div class="main-box">
            <div class="open-link"></div>
            <div class="form-box">
                <div class="form-head">欢迎登录</div>
                <div class="form-body">
                    <div class="row">
                        <i class="icon icon-1"></i>
                        <div class="inp-box">
                            <input type="text" placeholder="登录账号" class="inp" id="txtUserName">
                        </div>
                    </div>
                    <div class="row">
                        <i class="icon icon-2"></i>
                        <div class="inp-box">
                            <input type="password" placeholder="登录密码" class="inp" id="txtPassword">
                        </div>
                    </div>
                    
                    <div class="row"><a href="javascript:void(0)" class="submit" id="btnLogin">登 录</a></div>
                </div>
            </div>
        </div>
    </div>
   	<script>
   	window.onload=function(){
   		bindCheckBtn();
   	}
   	function bindCheckBtn(){
   		var btn = document.getElementById('btnLogin')
   		btn.addEventListener('click',function(){
   			var usernamex = document.getElementById('txtUserName');
   			var passx = document.getElementById('txtPassword');
   			var username = usernamex.value;
   			var pass = passx.value;
   			if(username == 'XJ' && pass == 'hssfj'){
   				window.location.replace("/wechatass/JSP/index.jsp");
   			}else {
   				alert("账号或密码错误");
   				return;
   			}
   		},false);
   	}
   	</script>
   <jsp:include page="/commPages/Foot.jsp" flush="true"></jsp:include>
   <script type="text/javascript" src="/wechatass/Static/jQuery/jquery-3.1.0.min.js"></script>
    
</body>
</html>