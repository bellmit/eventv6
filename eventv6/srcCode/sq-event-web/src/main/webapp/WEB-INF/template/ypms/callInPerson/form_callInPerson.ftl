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
		<input type="hidden" id="cpId" name="cpId" value="${(bo.cpId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>姓名</span></label>
							<input type="text" id="cpName" name="cpName" value="${(bo.cpName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>联系电话</span></label>
							<input type="text" id="cpMobile" name="cpMobile" value="${(bo.cpMobile)!}" class="inp1 easyui-validatebox" data-options="validType:'mobileorphone', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>性别</span></label>
							<label class="LabName" style="width:100px;"><span><input type="radio" style="margin-bottom: 7px" <#if (bo.cpSex)?? && bo.cpSex=='02'>checked</#if> name="cpSex" value="02" class="f-type">女</label>
							<label class="LabName" style="width:120px;"><span><input type="radio" style="margin-bottom: 7px" <#if (bo.cpSex)?? && bo.cpSex=='01'>checked</#if> name="cpSex" value="01" class="f-type" />男</label>
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
				url: '${rc.getContextPath()}/zhsq/callInPerson/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					} else if (data.result == 'mobileExist') {
						$.messager.alert('错误', '保存失败！电话号码已存在!', 'error');
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
