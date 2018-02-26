<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<div class="modal fade" id="addNormalModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
       <!--   <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> -->
        <h4 class="modal-title" id="exampleModalLabel">发送群消息</h4>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <label for="message-text" class="control-label">内容</label>
            <textarea class="form-control" id="normal_message_text"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" id="close_add_normal_msg" data-dismiss="modal">取消</button>
        <button type="button" id="add_normal_msg" class="btn btn-primary" data-dismiss="modal">发送</button>
      </div>
    </div>
  </div>
</div>