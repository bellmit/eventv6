<!DOCTYPE html>
<html>
<head>
	<title>驳回</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<style type="text/css">
		.Asterik{
			color: red;
		}
	</style>
</head>
<body>
<form id="submitForm">
	<input type="hidden" id="sendId" name="sendId" value="${(bo.sendId)!}">
	<div id="content-d" class="MC_con content light">
		<div name="tab" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>办理意见：</span></label>
						<textarea style="height:150px;width: 82%" name="advice" id="advice" value="" class="inp1 InpDisable easyui-validatebox" data-options="required:true,validType:'maxLength[500]', tipPosition:'bottom'" ></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="BigTool">
		<div class="BtnList">
			<a href="javascript:;" class="BigNorToolBtn RejectBtn" onClick="reject();">驳回</a>
			<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
		</div>
	</div>
</form>
</body>



<script type="text/javascript">
	//驳回
	function reject() {
		var isValid = $('#submitForm').form('validate');

		if (isValid) {
			var subMig = '驳回成功！';
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/gdPersionSendFlow/rejectHandleInfo.json',
				data:  $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.success) {
						layer.msg(subMig,{icon: 1,title:'提示'});
						setTimeout(function(){
							parent.parent.closeMaxJqueryWindow();
							parent.closeMaxJqueryWindow();
						},1500);
						parent.parent.searchData();
					} else {
						let type = data.type;
						let message = "驳回失败！"
						switch (type) {
							case '0':  message = "驳回失败！"
								break;
							case '-1':  message = "驳回异常！"
								break;
						}
						layer.msg(message, {icon:2});
					}
				},
				error: function(data) {
					//$.messager.alert('错误', '连接超时！', 'error');
					layer.alert( '连接超时！', {icon: 2,title:'错误'}, function() {
						parent.closeMaxJqueryWindow();
					});
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
		}
	}

	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
