<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
		.BtnList{width: 260px !important;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="abId" name="abId" value="${(bo.abId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>联动单位名称</span></label>
							<input type="text" id="ldName" name="ldName" value="${(bo.ldName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>角色</span></label>
							<select id="abRole" name="abRole" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'" >
		                            <option value="0" <#if bo.abRole?? && bo.abRole == '0'>selected</#if> >分管领导</option>
		                            <option value="1" <#if bo.abRole?? && bo.abRole == '1'>selected</#if> >联络员</option>
		                            <option value="2" <#if bo.abRole?? && bo.abRole == '2'>selected</#if> >联系人</option>
	                        <select>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>姓名</span></label>
							<input type="text" id="abName" name="abName" value="${(bo.abName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>职务</span></label>
							<input type="text" id="abDuty" name="abDuty" value="${(bo.abDuty)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>联系方式</span></label>
							<input type="text" id="abMobile" name="abMobile" value="${(bo.abMobile)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom',validType:['maxLength[15]','mobileorphone']"  />
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
				url: '${rc.getContextPath()}/zhsq/addressBook/save.json',
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
