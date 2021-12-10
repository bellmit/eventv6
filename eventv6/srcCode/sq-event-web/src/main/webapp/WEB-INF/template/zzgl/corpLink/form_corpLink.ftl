<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="cluId" name="cluId" value="${(bo.cluId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>联动单位ID</span></label>
							<input type="text" id="linkageUnitId" name="linkageUnitId" value="${(bo.linkageUnitId)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>联络员</span></label>
							<input type="text" id="linkMan" name="linkMan" value="${(bo.linkMan)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>联络员联系方式</span></label>
							<input type="text" id="linkManTel" name="linkManTel" value="${(bo.linkManTel)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[30]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>分管领导</span></label>
							<input type="text" id="leaderName" name="leaderName" value="${(bo.leaderName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>分管领导联系方式</span></label>
							<input type="text" id="leaderTel" name="leaderTel" value="${(bo.leaderTel)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[30]', tipPosition:'bottom'"  />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/corpLink/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
							parent.closeMaxJqueryWindow();
						});
						parent.searchData();
					}
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
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
