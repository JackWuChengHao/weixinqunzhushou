<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="RSLawFAQModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
			  <h4 class="modal-title" id="roomName">法律咨询</h4>
			</div>
			<div class="modal-body">
				<form>
				<div class="form-group">
				  	<label class="control-label">问题描述</label>
				    <textarea class="form-control" id="lawquestions"></textarea>
				  </div>
				 <div class="form-group">
				 	<label class="control-label">姓名</label>
				   <input class="form-control" id="name"/>
				 </div>
				 <div class="form-group">
				 	<label class="control-label">证件类型</label>
				   <select class="form-control">
					<option value ="1">身份证</option>
				</select>
				 </div>
				 <div class="form-group">
				 	<label class="control-label">证件号码</label>
				   <input class="form-control" id="cardnum"/>
				 </div>
				 <div class="form-group">
				 	<label class="control-label">性别</label>
				   <select class="form-control" id="sex">
					<option value ="1">男</option>
					<option value ="1">女</option>
				</select>
				 </div>
				 <div class="form-group">
				 	<label class="control-label">联系电话</label>
				   <input class="form-control" id="phone"/>
				 </div>
				</form>
			</div>
			<div class="modal-footer">
			  <button type="button" class="btn btn-default" id="cancelQuestion">取消</button>
			  <button type="button" class="btn btn-primary" id="commitQuestion">发送</button>
			</div>
		</div>
	</div>
</div>