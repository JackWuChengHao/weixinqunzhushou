<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="addQuestionAndAnswerModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<!-- 头部 -->
			<div class="modal-header">
				<p class="modal-title">添加</p>
			</div>
			<!-- 主体 -->
			<div class="modal-body">
				<form class="form-horizontal" id="">
					<div class="form-group">
						<label class="col-md-3">关键词</label>
						<div class="col-md-8">
							<textarea rows="2" class="form-control" placeholder="关键词" id="keywordsText" maxlength="500"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3">问题</label>
						<div class="col-md-8">
							<textarea rows="3" class="form-control" placeholder="问题" id="questionText" maxlength="500"></textarea>
						</div>
					</div>
					<div class="form-group" id="">
						<label class="TXModalLabel col-md-3" id="">答案</label>
						<div class="col-md-8">
							<textarea rows="3" class="form-control" placeholder="答案" id="answerText" maxlength="500"></textarea>
						</div>
					</div>
				</form>
			</div>
			
			<!-- 尾部 -->
			<div class="modal-footer">
				<button id="" class="btn btn-default" type="button" data-dismiss="modal" >取消</button>
				&nbsp;
				<button id="addQuestionAndAnswerModalCommitBtn" class="btn btn-success" type="button">确定</button>
			</div>		
		</div>
	</div>
</div>