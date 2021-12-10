<!DOCTYPE html>
<html>
<head>
	<title>新增/编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:250px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="id" name="id" value="${(bo.id_)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span><font color="red">*</font>词云名称:</span></label>
							<input type="text" id="topicName" name="topicName" value="${(bo.topicName)!}" class="inp1 easyui-validatebox" data-options="required:true"/>
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
		var topicName = $.trim($("#topicName").val()); //词云
		
		if(topicName == null || topicName == ""){
			$.messager.alert('提示', '词云名称不能为空！', 'info');
			return;
		}
		
		if(topicName.length > 8){
			$.messager.alert('提示', '词云长度不能大于8！', 'info');
			return;
		}
		modleopen(); //打开遮罩层
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/eventTopic/save.json',
			data: {"topicName":topicName,"bizType":parent.$("#bizType").val(),"id_":$("#id").val()},
			dataType: 'json',
			success: function(data) {
				if (data.result == 'fail') {
					$.messager.alert('提示', '网络异常！', 'info');
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
	
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
