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
		<input type="hidden" id="labelId" name="labelId" value="${(eventLabel.labelId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>标签名称</span></label>
							<input type="text" id="labelName" name="labelName" value="${(eventLabel.labelName)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[100]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>所属模块</span></label>
							<input class="hide queryParam" id="labelModel" name="labelModel" />
	        				<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="labelModelStr" />
						</td>
					</tr>
					
					<tr>
						<td>
							<label class="LabName"><span>所属地域</span></label>
							<input type="text" class="hide" id="gridId"/>
							<input type="text" class="hide queryParam" id="infoOrgCode" name="regionCode" value="${(eventLabel.regionCode)!}" />
	        				<input type="text" class="inp1 easyui-validatebox" id="gridName" value="${gridName!}" />
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
	
	$(function(){
	
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		}, {
			OnCleared : function() {
				$("#infoOrgCode").val("");
			},
			ShowOptions : {
				EnableToolbar : true
			},
			
		});
		
		
		AnoleApi.initListComboBox("labelModelStr", "labelModel", "B596", null, <#if eventLabel.labelModel??>["${eventLabel.labelModel}"]<#else>null</#if>, {
            ShowOptions:{
                EnableToolbar : true
            }
        });
		
		
	});
	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/event/eventLabelController/saveLabel.json',
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
